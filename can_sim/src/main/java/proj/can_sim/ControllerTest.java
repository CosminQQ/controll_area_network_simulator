package proj.can_sim;

import model.message.CanBuss;
import model.message.Instructions;
import model.message.InternalRequest;
import model.message.MessageOp;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ControllerTest {
    private CanBuss canBuss;
    private ArrayList<Integer> addressesT1, addressesT2;
    private ArrayList<Instructions> instructionT1, instructionT2;
    private BlockingQueue<InternalRequest> pipeToController1 = new LinkedBlockingQueue<>();
    private BlockingQueue<InternalRequest> pipeToController2 = new LinkedBlockingQueue<>();
    private BlockingQueue<InternalRequest> pipeFROMcontroller1 = new LinkedBlockingQueue<>();
    private BlockingQueue<InternalRequest> pipeFROMcontroller2 = new LinkedBlockingQueue<>();

    public ControllerTest(CanBuss canBuss) {
        this.canBuss = canBuss;
    }

    public void setup() {
        addressesT1 = generateRandomAddresses(30);
        addressesT2 = generateRandomAddresses(12);

        instructionT1 = getRandomInstructions(2);
        instructionT2 = getRandomInstructions(1);

        Controller c1 = new Controller(pipeToController1, addressesT1, pipeFROMcontroller1, canBuss);
        Controller c2 = new Controller(pipeToController2, addressesT2, pipeFROMcontroller2, canBuss);

        c1.setup(1000);
        c2.setup(1000);

        Thread c11 = new Thread(c1);
        Thread c22 = new Thread(c2);

        c11.start();
        c22.start();

        for (Integer i : addressesT1) {
            pipeToController2.add(generateInstructionLists(i, instructionT2));
        }

        for (Integer i : addressesT2) {
            pipeToController1.add(generateInstructionLists(i, instructionT1));
        }

        System.out.println("info sent/go to sleep");
        try {
            Thread.sleep(1000 * 4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("woke up from sleep delete threads\n");
        c1.deactivateTheTransceivers();
        c2.deactivateTheTransceivers();

        System.out.println("print pipe from controller 1");
        for (InternalRequest i : pipeFROMcontroller1)
            MessageOp.printInternalRequestDetails(i);

        System.out.println("print pipe from controller 2");
        for (InternalRequest i : pipeFROMcontroller2)
            MessageOp.printInternalRequestDetails(i);

        System.out.println("woke up from sleep delete threads\n");
        c1.deactivateTheTransceivers();
        c2.deactivateTheTransceivers();
    }

    public static ArrayList<Instructions> getRandomInstructions(int instructionSetSize) {
        Random random = new Random();
        Instructions[] instructions = Instructions.values();
        ArrayList<Instructions> randomInstructions = new ArrayList<>();

        for (int i = 0; i < instructionSetSize; i++) {
            int randomIndex = random.nextInt(instructions.length);
            randomInstructions.add(instructions[randomIndex]);
        }

        return randomInstructions;
    }

    public static ArrayList<Integer> generateRandomAddresses(int arraySize) {
        ArrayList<Integer> addresses = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < arraySize; i++) {
            addresses.add(random.nextInt());
        }

        return addresses;
    }

    public static InternalRequest generateInstructionLists(Integer adress, ArrayList<Instructions> instructionSet) {
        return new InternalRequest(adress, instructionSet, false);
    }
}
