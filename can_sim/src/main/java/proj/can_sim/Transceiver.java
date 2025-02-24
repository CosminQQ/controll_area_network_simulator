package proj.can_sim;

import model.message.CanBuss;
import model.message.MessageWord;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static model.message.MessageOp.getIntAddress;

public class Transceiver implements Runnable {
    private BlockingQueue<MessageWord> pipe;
    private MessageWord receivedMessage;
    CanBuss canBuss;
    private byte[] id;
    AtomicBoolean acceptMessages = new AtomicBoolean();
    private int runTimer;

    public Transceiver(byte[] id, CanBuss canBuss, BlockingQueue<MessageWord> pipe, int runTimer) {
        // id the address on which node receive messages
        this.id = id;
        this.receivedMessage = null;
        this.acceptMessages.set(true);
        this.canBuss = canBuss;
        this.pipe = pipe;
        this.runTimer = runTimer;
    }

    public Transceiver(CanBuss canBuss) {this.canBuss = canBuss;}

    //the request sent incorporates already the receiver's id
    public boolean sendMessage(MessageWord request) {
        if (request == null)
            return false;
        canBuss.sendRequest(request);
        return true;
    }

    public void killThread(){
        acceptMessages.set(false);
    }

    @Override
    public void run() {
        receivedMessage = null;
        while (acceptMessages.get()) {
            receivedMessage = canBuss.pullRequest(id);
            if (receivedMessage != null) {
                pipe.add(receivedMessage);
            } else {

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("transceiver was sleeping!");
                }

            }
            receivedMessage = null;
        }
    }
}


