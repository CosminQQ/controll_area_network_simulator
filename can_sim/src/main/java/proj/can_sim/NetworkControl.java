package proj.can_sim;

import javafx.scene.control.ListView;
import model.message.Bounds;
import model.message.CanBuss;
import model.message.NodeType;
import model.message.Priority;

import java.util.ArrayList;

public class NetworkControl {
    ArrayList<CreateNode> nodeList;
    CanBuss dataBuss;

    public NetworkControl(CanBuss buss){
        this.nodeList = new ArrayList<>();
        this.dataBuss = buss;
    }

    public void addNodeToList(CreateNode newNode, ListView<CreateNode> quiListUpdate){
        this.nodeList.add(newNode);
        quiListUpdate.getItems().add(newNode);
    }

    public void removeNodeFromList(ListView<CreateNode> quiListUpdate){
        int selectedIndex = quiListUpdate.getSelectionModel().getSelectedIndex();
        if(selectedIndex >= 0){
            quiListUpdate.getItems().remove(selectedIndex);
        }

        this.nodeList.remove(selectedIndex);
        for(CreateNode i : nodeList)
            System.out.println(i);
    }

    public void runSimulation(int runningTime){
        CreateNode.bondNodes(this.nodeList);
        CreateNode.startNodes(this.nodeList, runningTime);
    }

}
