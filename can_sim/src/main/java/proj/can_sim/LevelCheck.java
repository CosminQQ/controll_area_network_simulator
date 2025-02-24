package proj.can_sim;

import model.message.Instructions;

import java.util.ArrayList;

public class LevelCheck implements NodeTypeInterface {
    private ArrayList<Instructions> noResponse;
    private String nodeName;
    private double threshold;

    public LevelCheck(String name, double threshold) {
        this.nodeName = name;
        this.threshold = threshold;
    }

    @Override
    public ArrayList<Instructions> processSensorData(Double sensorData, boolean sendStatus) {
        noResponse = new ArrayList<>(1);

        if(sendStatus){
            if(sensorData >= threshold)
                noResponse.add(Instructions.GAS_LEVEL_HIGH);
            else
                noResponse.add(Instructions.GAS_LEVEL_LOW);

        }else{
            if(sensorData < threshold)
                noResponse.add(Instructions.GAS_LEVEL_LOW);
            else
                noResponse.add(Instructions.NO_OP);
        }

        return noResponse;
    }
}
