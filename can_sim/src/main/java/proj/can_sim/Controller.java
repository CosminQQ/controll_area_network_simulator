package proj.can_sim;

import model.message.*;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Controller implements Runnable {
    /*
        What is the controller doing?
            -via a pipe sends instructions to the microcontroller
            -microcontroller signals anomalies to controller via a pipe
            -based on the instruction set chooses the receiver's address
            -sends requests to transceiver
            -receives the messages from the transceiver via pipe
     */
    private Instructions currentInstruction;
    //pipe to communicate with the microcontroller;
    private BlockingQueue<InternalRequest> flagPipe;
    //pipe to fetch instructions to the microprocessor
    private BlockingQueue<InternalRequest> instructionList;
    //pipe to communicate with the transceiver
    private BlockingQueue<MessageWord> requestPipe;
    private Transceiver[] transceiversList;
    private CanBuss myCanbuss;
    private ArrayList<Integer> addresses;
    private char delimiter = ',';
    private AtomicBoolean active = new AtomicBoolean(true);
    private Thread[] runningTransceivers;

    public Controller(BlockingQueue<InternalRequest> instructionList, ArrayList<Integer> addresses, BlockingQueue<InternalRequest> flagPipe, CanBuss myCanbuss) {
        this.currentInstruction = Instructions.NO_OP;
        this.instructionList = instructionList;
        this.requestPipe = new LinkedBlockingQueue<>();
        this.addresses = addresses;
        this.transceiversList = new Transceiver[addresses.size()];
        this.myCanbuss = myCanbuss;
        this.flagPipe = flagPipe;
        this.runningTransceivers = new Thread[addresses.size()];
    }

    public void setup(int responseTime) {
        int i = 0;
        for (Integer address : addresses) {
            transceiversList[i] = new Transceiver(MessageOp.getByteAddress(address), this.myCanbuss, this.requestPipe, responseTime);
            runningTransceivers[i] = new Thread(transceiversList[i]);
            runningTransceivers[i].start();
            i++;
        }

    }

    /*
    fetch data from transceiver
    converts it to an understandable way for the microprocessor
    sends data to the microprocessor
     */

    private void decodeMessageAndSend() {
        if (!requestPipe.isEmpty()) {
            MessageWord currentRequest = requestPipe.poll();
            NewMessage decodedRequest = MessageOp.byteToProgram(currentRequest);

            ArrayList<Instructions> targetInstructions = MessageOp.parseData(decodedRequest.getTargetInstruction(), delimiter);
            flagPipe.add(new InternalRequest(decodedRequest.getTargetAddress(), targetInstructions, decodedRequest.getTypeMessage()));
        }
    }

    /*
        receives data from the microprocessor
        transforms it into messageWorld format
        sends the data to the transceiver;
     */
    private void decodeInstructionAndSend() {
        if (!instructionList.isEmpty()) {
            InternalRequest request = instructionList.poll();
            String instructionStream = MessageOp.encodeData(request.getCurrentInstruction(), delimiter);
            NewMessage newMessage = new NewMessage(request.getTargetAddress(), instructionStream.length(), instructionStream, request.isResponse());
            MessageWord encodedMessage = MessageOp.programToByte(newMessage);

            Transceiver sender = new Transceiver(myCanbuss);

            if (!sender.sendMessage(encodedMessage))
                System.out.println("error in controller decodeInstructionSend");
        }

    }

    public void deactivateTheTransceivers() {
        this.active.set(false);
        for (int i = 0; i < addresses.size(); i++) {
            if (transceiversList[i] != null)
                transceiversList[i].killThread();
            else System.out.println("null thread!\n");
        }
    }

    @Override
    public void run() {
        System.out.println("Controller thread running");
        while (active.get()) {
//            System.out.print(".");
            if (requestPipe.isEmpty() && instructionList.isEmpty()) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    System.out.println("controller thread was sleeping");
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                decodeMessageAndSend();
                decodeInstructionAndSend();
            }
        }
    }
}
