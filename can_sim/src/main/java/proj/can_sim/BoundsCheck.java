package proj.can_sim;

import model.message.Instructions;

import java.util.ArrayList;

public class BoundsCheck implements NodeTypeInterface {
    private ArrayList<Instructions> noResponse;
    private String nodeName;
    private double upperBound, lowerBound;

    public BoundsCheck(String name, double upperBound, double lowerBound) {
        this.nodeName = name;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
    }

    @Override
    public ArrayList<Instructions> processSensorData(Double sensorData, boolean sendStatus) {
        noResponse = new ArrayList<>(1);

        if(sendStatus){
            if(sensorData > upperBound)
                noResponse.add(Instructions.MOTOR_OVERHEATED);
            else if(sensorData < lowerBound)
                noResponse.add(Instructions.MOTOR_COLD);
            else
                noResponse.add(Instructions.MOTOR_IN_PARAMETERS);

        }else {
            if(sensorData > upperBound)
                noResponse.add(Instructions.MOTOR_OVERHEATED);
            else if(sensorData < lowerBound)
                noResponse.add(Instructions.MOTOR_COLD);
            else
                noResponse.add(Instructions.NO_OP);
        }

        return noResponse;
    }
}
