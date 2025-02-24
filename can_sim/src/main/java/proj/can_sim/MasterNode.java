package proj.can_sim;

import model.message.Instructions;

import java.util.ArrayList;
import java.util.SortedMap;

public class MasterNode implements NodeTypeInterface{
    private final String nodeName = "Master Node";
    private ArrayList<Instructions> noResponse;

    /*
        the master will mostly sleep, and he will send status questions after each sleep to each node
     */

    @Override
    public ArrayList<Instructions> processSensorData(Double sensorData, boolean sendStatus){
        noResponse =new ArrayList<>(1);

        if(!sendStatus){
            if(sensorData >= 0)
                noResponse.add(Instructions.STATUS);
            else
                noResponse.add(Instructions.NO_OP);
        }else
            noResponse.add(Instructions.STATUS);

        return noResponse;
    }
}
