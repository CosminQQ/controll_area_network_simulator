package proj.can_sim;

import javafx.scene.control.ListView;
import model.message.*;

import java.util.ArrayList;

public class CreateNode implements Comparable<CreateNode>{
    private NodeType nodeType;
    private Priority nodePriority;
    private Bounds nodeBounds;
    private String inputFile;
    private CanBuss canBus;
    private String nodeName;
    private ArrayList<String> info = new ArrayList<>();
    private ArrayList<Integer> responseAddresses = new ArrayList<>();
    private ArrayList<Integer> targetAddresses = new ArrayList<>();
    private RandomAddressGenerator rad = new RandomAddressGenerator();
    private Microprocessor microprocessor = null;
    private Thread thread = null;
    private String outputFile = null;

    public CreateNode(NodeType nodeType, Priority nodePriority, Bounds nodeBounds, String inputFile, String name, CanBuss canBuss) {
        this.nodeType = nodeType;
        this.nodeBounds = nodeBounds;
        this.nodePriority = nodePriority;
        this.inputFile = inputFile;
        this.nodeName = name;
        this.responseAddresses.add(rad.generateRandomAddress(this.nodePriority));
        this.canBus = canBuss;
    }


    public ArrayList<Integer> getResponseAddress(){
        return this.responseAddresses;
    }

    public void addTargetAddress(ArrayList<Integer> targetAddresses){
        this.targetAddresses.addAll(targetAddresses);
    }

    public void initialize(){
        this.microprocessor = new Microprocessor(this.nodeName, this.nodeBounds.getLowerBound(), this.nodeBounds.getUpperBound(),this.canBus,this.responseAddresses, this.targetAddresses,this.info);
        this.microprocessor.setup(this.nodeType,this.inputFile);
    }



    public void run(){
        this.thread = new Thread(this.microprocessor);
        this.thread.start();
    }

    public void endExecution(){
        this.microprocessor.endExecution();

        outputFile = inputFile.replaceFirst("\\.txt$", "");
        outputFile = outputFile+"_log.txt";

        MessageOp.writeListToFile(this.info, outputFile);
    }

    /**
    Returns 0 if equal else 1
     */
    @Override
    public int compareTo(CreateNode t){
        for(Integer i : t.getResponseAddress()){
            for(Integer j : this.getResponseAddress()){
                if(i.compareTo(j) == 0)
                    return 0;
            }
        }
        return 1;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Node at address ");
        sb.append(this.responseAddresses);
        sb.append(" is an ");
        sb.append(this.nodeType);
        sb.append(" with priority ");
        sb.append(this.nodePriority);
//        sb.append("\nCommunicates to: ");
//        for(Integer i : targetAddresses){
//            sb.append(i);
//            sb.append(", ");
//        }
//        sb.append("\n");

        return sb.toString();
    }

    public static void bondNodes(ArrayList<CreateNode> unboundArrayOfNodes) {
        if(!unboundArrayOfNodes.isEmpty()){
            for(CreateNode current : unboundArrayOfNodes)
                for(CreateNode other : unboundArrayOfNodes){
                    if(current.compareTo(other) != 0){
                        current.addTargetAddress(other.getResponseAddress());
                    }
                }
        }else System.out.println("bond nodes called with empty array!");
    }

    public static void startNodes(ArrayList<CreateNode> nodeArray, int simulationTime){
        for(CreateNode node : nodeArray){
            node.initialize();
        }

        for(CreateNode node : nodeArray){
            node.run();
        }

        try{
            Thread.sleep(simulationTime * 1000);
        } catch (InterruptedException e) {
            System.out.println("Cannot wake up the program!");
        }

        for(CreateNode node : nodeArray){
            node.endExecution();
        }
    }
}
