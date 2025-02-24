package proj.can_sim;

import model.message.Bounds;
import model.message.Instructions;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;

public class AverageCheck implements NodeTypeInterface {
    private String nodeName;
    private ArrayList<Instructions> noResponse;
    private ArrayDeque<Double> last10Numbers = new ArrayDeque<>(10);  // Fixed-size deque for last 10 numbers
    private double runningSum;                // Running sum for efficient averaging
    private double average;
    private double low,  high;

    public AverageCheck(String name, double low, double high) {
        this.nodeName = name;
        this.runningSum = 0.0;
        this.average = 0.0;
        this.low = low;
        this.high = high;
    }

    @Override
    public ArrayList<Instructions> processSensorData(Double sensorData, boolean sendStatus) {
        noResponse = new ArrayList<>(1);


        if (this.last10Numbers.isEmpty()) {
            noResponse.add(Instructions.NO_OP);
            this.last10Numbers.add(sensorData);
            runningSum += sensorData;
//            System.out.println(!sendStatus);
        } else {

            if (this.last10Numbers.size() == 10) {
                double oldestValue = this.last10Numbers.pollFirst();
                this.runningSum -= oldestValue;
            }
            this.last10Numbers.add(sensorData);
            this.runningSum += sensorData;

            this.average = this.runningSum / this.last10Numbers.size();
            //System.out.println(sendStatus);
            if(sendStatus){
                if(average > this.high)
                    noResponse.add(Instructions.FUMES_LEVEL_HIGH);
                else if (average <= this.low)
                    noResponse.add(Instructions.FUMES_LEVEL_LOW);
                else
                    noResponse.add(Instructions.FUMES_LEVEL_MEDIUM);

            }else{
                if(average > this.high)
                    noResponse.add(Instructions.FUMES_LEVEL_HIGH);
                else if (average <= this.low)
                    noResponse.add(Instructions.FUMES_LEVEL_LOW);
                else
                    noResponse.add(Instructions.NO_OP);
            }
        }
        return noResponse;
    }
}
