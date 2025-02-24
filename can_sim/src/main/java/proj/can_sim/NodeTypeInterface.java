package proj.can_sim;

import model.message.Instructions;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface NodeTypeInterface {
    ArrayList<Instructions> processSensorData(Double sensorData, boolean sendStatus);
}
