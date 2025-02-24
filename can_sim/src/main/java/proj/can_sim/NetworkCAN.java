package proj.can_sim;

import model.message.CanBuss;
import model.message.MessageOp;
import model.message.NodeType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static model.message.MessageOp.writeListToFile;

public class NetworkCAN {
    /*
        1 - main node -> 1 node address
                      -> 3 - target addresses (to motor, exhaust, fuel tank)
                      -> MASTER_PROGRAM type

        1 - motor -> 1 node address
                  -> 1 target address (to main node)
                  -> BOUNDS_CHECK type

        1 - exhaust -> 1 node address
                    -> 1 target address (to main node)
                    -> AVERAGE_CHECK type

        1 - fuel tank -> 1 node address
                      -> 1 target address (to main node)
                      -> LEVEL_CHECK type
     */
    CanBuss controlAreaNetwork = new CanBuss();
    ArrayList<Integer> mainNodeAddress, motorNodeAddress, exhaustNodeAddress, fuelTankNodeAddress;
    ArrayList<Integer> mainNodeTargetAddress, motorTargetAddress, exhaustTargetAddress, fuelTankTargetAddress;
    ArrayList<String> mainLog, motorLog, tankLog, exhaustLog;

    public void runSimulation(){

        //main node setup
        mainNodeAddress = new ArrayList<>(1);
        mainNodeAddress.add(-1);

        mainNodeTargetAddress = new ArrayList<>(4);
        mainNodeTargetAddress.add(-10); //motor address
        mainNodeTargetAddress.add(10); //fuel tank address
        mainNodeTargetAddress.add(100); //exhaust address

        mainLog = new ArrayList<>();

        Microprocessor mainNode = new Microprocessor("", 0, 0,controlAreaNetwork,mainNodeAddress,mainNodeTargetAddress,mainLog);


        //motor node setup
        motorNodeAddress = new ArrayList<>(1);
        motorNodeAddress.add(-10);

        motorTargetAddress = new ArrayList<>(1);
        motorTargetAddress.add(-1); // main node

        motorLog = new ArrayList<>();

        Microprocessor motorNode = new Microprocessor("motor1", 90, 105, controlAreaNetwork, motorNodeAddress, motorTargetAddress, motorLog);

        //exhaust node setup
        exhaustNodeAddress = new ArrayList<>(1);
        exhaustNodeAddress.add(100);

        exhaustTargetAddress = new ArrayList<>(1);
        exhaustTargetAddress.add(-1); // main node

        exhaustLog = new ArrayList<>();

        Microprocessor exhaustNode = new Microprocessor("exhaust1", 0, 0, controlAreaNetwork, exhaustNodeAddress, exhaustTargetAddress, exhaustLog);

        //fuel tank node setup
        fuelTankNodeAddress = new ArrayList<>(1);
        fuelTankNodeAddress.add(-10);

        fuelTankTargetAddress = new ArrayList<>(1);
        fuelTankTargetAddress.add(-1); // main node

        tankLog = new ArrayList<>();

        Microprocessor fuelTankNode = new Microprocessor("fuel_tank1", 10, 0, controlAreaNetwork, fuelTankNodeAddress, fuelTankTargetAddress, tankLog);

        //set nodes up
        mainNode.setup(NodeType.MASTER_PROGRAM,"motor.txt");
        motorNode.setup(NodeType.BOUNDS_CHECK, "main.txt");
        fuelTankNode.setup(NodeType.LEVEL_CHECK, "fueltank.txt");
        exhaustNode.setup(NodeType.AVERAGE_CHECK, "exhaust.txt");


        //start the nodes
        ArrayList<Thread> runningNodes = new ArrayList<>(4);
        runningNodes.add(new Thread(mainNode));
        runningNodes.add(new Thread(motorNode));
        runningNodes.add(new Thread(exhaustNode));
        runningNodes.add(new Thread(fuelTankNode));

        for(Thread t: runningNodes){
            t.start();
        }

        try{
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            System.out.println("Cannot sleep");
        }



        writeListToFile(mainLog, "main_log.txt");
        writeListToFile(motorLog, "motor_log.txt");
        writeListToFile(tankLog, "fuel_tank_log.txt");
        writeListToFile(exhaustLog, "exhaust_log.txt");

        mainNode.endExecution();
        motorNode.endExecution();
        exhaustNode.endExecution();
        fuelTankNode.endExecution();

    }




}
