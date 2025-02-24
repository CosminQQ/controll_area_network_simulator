package proj.can_sim;

import model.message.CanBuss;
import model.message.MessageOp;
import model.message.MessageWord;
import model.message.NewMessage;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static model.message.MessageOp.*;

public class Initializer {

    private int nbTransceivers;
    private CanBuss canBus;
    private BlockingQueue<MessageWord> pipe;

    private Transceiver[] tArray;

    Initializer(int nbTransceivers) {
        this.nbTransceivers = nbTransceivers;
        this.canBus = new CanBuss();
        this.pipe = new LinkedBlockingQueue<>();
        this.tArray = new Transceiver[nbTransceivers];
    }

    public void runSimulation() throws InterruptedException {
        byte[][] idArray = new byte[nbTransceivers][4];
        Random randomIdGen = new Random();

        //generate random ids for the transceivers
        for (int i = 0; i < nbTransceivers; i++) {
            randomIdGen.nextBytes(idArray[i]);
            System.out.println(getIntAddress(idArray[i]));
        }
        System.out.println("nr0");
        //initialize the transceivers
        for (int i = 0; i < nbTransceivers; i++) {
            tArray[i] = new Transceiver(idArray[i], canBus, pipe, 1000);
        }
        System.out.println("nr1");
        //each transceiver sends a welcoming message to the other in the network
        MessageWord[][] messageWords = new MessageWord[nbTransceivers][nbTransceivers];

        for (int i = 0; i < nbTransceivers; i++) {
            for (int j = 0; j < nbTransceivers; j++) {
                if (j != i) {
                    System.out.println("+++ " + getIntAddress(idArray[j]) + " +++");
                    messageWords[i][j] = programToByte(new NewMessage(MessageOp.getIntAddress(idArray[j]), 10, "Salut de la " + i, true));
                } else messageWords[i][j] = null;

            }
        }
        System.out.println("nr2");
        //create threads to run transceiver code
        Thread[] newThreads = new Thread[nbTransceivers];

        for (int i = 0; i < nbTransceivers; i++) {
            newThreads[i] = new Thread(tArray[i]);
        }
        System.out.println("nr3");
        for (int i = 0; i < nbTransceivers; i++) {
            newThreads[i].start();
        }
        System.out.println("nr4");
        for (int i = 0; i < nbTransceivers; i++) {
            for (int j = 0; j < nbTransceivers; j++) {
                //System.out.println("imi bag pula!");
                if (messageWords[i][j] != null){

                    tArray[i].sendMessage(messageWords[i][j]);
                    //System.out.println("muie");
                }
            }
        }

        System.out.println("nr5");
        try {
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e){
            System.out.println("initializer dont wanna sleep");
        }


        for (int i = 0; i < nbTransceivers; i++) {
            tArray[i].killThread();
            System.out.println("nr5." + i);
//            try {
//                newThreads[i].join();
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();  // Preserve the interruption status
//            }
        }
        System.out.println("nr6");
        for (MessageWord pipeMessage : pipe) {
            testMessageTransmission(pipeMessage);
        }
    }
}
