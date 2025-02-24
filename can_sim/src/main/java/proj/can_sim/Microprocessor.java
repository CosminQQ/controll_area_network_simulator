package proj.can_sim;

import model.message.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Microprocessor implements Runnable {
    /*
        What is Controller class doing?
            -executing a predefined instruction set as collecting data from sensors(a database) and detects
        anomalies.
            -when an anomaly is detected it sends a request to the main controller and to other nodes in the
        network.
            -it can receive requests as well from other nodes to modify its parameters or to fetch some kind
        of needed data.
            -the execution of the predefined instruction set is happening on a custom tread
     */
    private CanBuss networkBuss;
    //the addresses the microprocessor listens to
    private ArrayList<Integer> nodeAddresses, targetAddresses;
    //pipes to communicate both ways with the controller
    private BlockingQueue<InternalRequest> getResponsesPipe, sendRequestsPipe;
    //target addresses for independent requests
    private ArrayList<Instructions> targetInstructions = new ArrayList<>();
    private Controller myController;
    private String sensorData;
    private BufferedReader values;
    private Thread controllerThread;
    private InternalRequest currentInstruction = null;
    private NodeTypeInterface specializedMethod;
    private ArrayList<String> logOfEvents;
    private final ArrayList<Instructions> responseToRequest = new ArrayList<>(1);
    private int awaitingResponses;
    private ArrayList<Instructions> logOfExecutedInstructions = new ArrayList<>();
    private boolean sendStatus = false;
    private String nodeName;
    private double upperBound, lowerBound;
    private NodeType habibi;
    private AtomicBoolean active = new AtomicBoolean();
    private NodeType nodeType;


    /*
        if the node you want to create does not have some parameters you can use a random value
     */

    public Microprocessor(String nodeName, double lowerBound, double upperBound, CanBuss networkBuss, ArrayList<Integer> nodeAddresses, ArrayList<Integer> targetAddresses, ArrayList<String> logOfEvents) {
        this.networkBuss = networkBuss;
        this.nodeAddresses = nodeAddresses;
        this.targetAddresses = targetAddresses;
        this.getResponsesPipe = new LinkedBlockingQueue<>();
        this.sendRequestsPipe = new LinkedBlockingQueue<>();
        this.myController = null;
        this.values = null;
        this.controllerThread = null;
        this.logOfEvents = logOfEvents;
        this.nodeName = nodeName;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.active.set(true);
    }

    public void setup(NodeType nodeType, String fileSensorData) {
        this.myController = new Controller(this.sendRequestsPipe, this.nodeAddresses, this.getResponsesPipe, networkBuss);
        this.myController.setup(100);
        this.sensorData = fileSensorData;
        this.nodeType = nodeType;

        if (!sensorData.isEmpty()) {
            try {
                FileReader data = new FileReader(sensorData);
                this.values = new BufferedReader(data);

            } catch (FileNotFoundException e) {
                System.out.println("Microprocessor cannot open file: " + this.sensorData + "!!!\n");
                e.printStackTrace();
            }
        }
        this.controllerThread = new Thread(this.myController);
        this.controllerThread.start();

        switch (nodeType) {
            case AVERAGE_CHECK:
                this.specializedMethod = new AverageCheck(this.nodeName, this.lowerBound, this.upperBound);
                break;
            case MASTER_PROGRAM:
                this.specializedMethod = new MasterNode();
                this.awaitingResponses = 1000000;
                break;
            case LEVEL_CHECK:
                this.specializedMethod = new LevelCheck(this.nodeName, this.lowerBound);
                break;
            case BOUNDS_CHECK:
                this.specializedMethod = new BoundsCheck(this.nodeName, this.upperBound, this.lowerBound);
                break;
            case NONE:
                this.specializedMethod = null;
                System.out.println("Node unused!\n");
                break;
        }
        habibi = nodeType;
        responseToRequest.add(Instructions.NO_OP);
        awaitingResponses = 0;
    }

    public void endExecution() {

        if (this.values != null) {
            try {
                values.close();
            } catch (IOException e) {
                System.out.println("Controller cannot close reader for file: " + this.sensorData + "!!!\n");
                e.printStackTrace();
            }
        }
        this.myController.deactivateTheTransceivers();
        this.active.set(false);

    }

    //flags errors with Double.MAX_VALUE
    private double readValue() {
        String currentData = null;
        try {
            currentData = values.readLine();
            if (currentData == null) {
                System.out.println("File " + currentData + " contents read!\n");
                return Double.MAX_VALUE;
            }

            return Double.valueOf(currentData);

        } catch (IOException e) {
            System.out.println("Microprocessor thread reading line error!\n");
            e.printStackTrace();
            return Double.MAX_VALUE;
        }
    }

    private void sendRequest() {

        if(sendStatus) {
            for (Integer address : targetAddresses) {
                sendRequestsPipe.add(new InternalRequest(address, this.targetInstructions, true));
            }
            logOfEvents.add("ANSWER SENT: " + this.targetInstructions + "\n");
        }else {
            for (Integer address : targetAddresses) {
                sendRequestsPipe.add(new InternalRequest(address, this.targetInstructions, false));
                if(nodeType == NodeType.MASTER_PROGRAM)
                    awaitingResponses++;
            }
            logOfEvents.add("REQUEST SENT: " + this.targetInstructions + "\n");

        }

        sendStatus = false;
    }

    private void handleRequest() {
        this.currentInstruction = this.getResponsesPipe.poll();
        if (!this.currentInstruction.isResponse()) {
            if(nodeType == NodeType.MASTER_PROGRAM){
                logOfEvents.add("REQUEST RECEIVED: " + this.currentInstruction.getCurrentInstruction() + "\n");
                logOfExecutedInstructions.addAll(this.currentInstruction.getCurrentInstruction());
                sendStatus = true;
            }
            for (Instructions i : this.currentInstruction.getCurrentInstruction()) {
                if (i == Instructions.STATUS) {
                    sendStatus = true;
                    logOfEvents.add("REQUEST RECEIVED: " + this.currentInstruction.getCurrentInstruction() + "\n");
                    logOfExecutedInstructions.addAll(this.currentInstruction.getCurrentInstruction());
                }
                if (i == Instructions.STOP)
                    this.endExecution();
            }

//            for (Integer address : targetAddresses) {
//                sendRequestsPipe.add(new InternalRequest(address, responseToRequest, true));
//            }

            //logOfEvents.add("RESPONSE TO REQUEST SENT!\n");
        } else if (awaitingResponses > 0) {
            logOfEvents.add("RESPONSE TO REQUEST RECEIVED:" + this.currentInstruction.getCurrentInstruction() + "\n");
            awaitingResponses--;
        }
    }

    /*
        The specialized method just apply a different based algorithm on the data from the files
        each specialized method does the following:
            data_processing: takes data from sensors in the car and detects anomalies: exhaust smoke =>
                             returns % increase in petrol consumption due to EGR
            level_check: takes data from the sensors and compare it to a threshold (gas/oil/windshield fluid level)
                          => returns %
            engine_check: data_received = temperature if overheated/underheated limits acceleration => returns temp
            wheel_check: data received = speed => checks if safe regarding outside conditions => returns full stop
                        distance approximation
            main_method: the main node in the network that can communicate with every other node
     */
    @Override
    public void run() {

        double currentData = this.readValue();
        while (active.get()) {
            if (currentData != Double.MAX_VALUE) {

                //check for responses first, if nothing is there sleep a bit
                if (this.getResponsesPipe.isEmpty()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        System.out.println("Microprocessor thread was sleeping!");
                    }
                } else {
                    //do the given instruction and return a response
                    this.handleRequest();
                }

                //apply the read value on the specific method assigned by the node
                this.targetInstructions = this.specializedMethod.processSensorData(currentData, this.sendStatus);
                if (!this.targetInstructions.contains(Instructions.NO_OP ) || (nodeType == NodeType.MASTER_PROGRAM && this.sendStatus == true)) {
                    this.sendRequest();
                }

                currentData = this.readValue();
            }else if (!this.getResponsesPipe.isEmpty()){
                this.handleRequest();
            }
        }
    }

}
