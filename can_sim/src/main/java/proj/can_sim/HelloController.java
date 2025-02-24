package proj.can_sim;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.message.*;

public class HelloController {

    @FXML
    public Button removeButton;
    public Button startSimButton;
    public TextField simTime;
    public ListView<Integer> listCanBus;
    public Button resetListView;
    @FXML
    private TextField nodeNameField;

    @FXML
    private ComboBox<NodeType> nodeTypeComboBox;

    @FXML
    private TextField lowerBoundField,inputTextFieldFile;
    @FXML
    private ListView<CreateNode> listView;

    @FXML
    private TextField upperBoundField;

    @FXML
    private ComboBox<Priority> priorityComboBox;
    @FXML
    private Label errorName, errorNodeType, errorPriority, errorLowerBound, errorUpperBound, inputTextFileError, validNodeAddedLabel;
    private NetworkControl simulator;
    private int simRunningTime;
    private CanBuss canBuss;

    public void resetFields(){
        errorName.setText("");
        errorLowerBound.setText("");
        errorNodeType.setText("");
        errorPriority.setText("");
        errorUpperBound.setText("");
        inputTextFileError.setText("");
        validNodeAddedLabel.setText("");
    }
    @FXML
    public void initialize() {
        resetFields();
        nodeTypeComboBox.getItems().setAll(NodeType.values());
        priorityComboBox.getItems().setAll(Priority.values());
        canBuss = new CanBuss();
        simulator = new NetworkControl(canBuss);
        canBuss.setL(listCanBus);
    }

    @FXML
    private void deleteSelectedItem() {
        simulator.removeNodeFromList(listView);
    }

    @FXML
    private void startSimulationOnAction(){
        simRunningTime = Integer.parseInt(simTime.getText());
        if(simRunningTime <= 0){
            printError(InputError.BOUND_ERROR);
            return;
        }try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("cannot wait to change menu!");
        }
        simulator.runSimulation(this.simRunningTime);
    }

    public void printError(InputError inputError){
        switch(inputError){
            case NULL_NAME:
                errorName.setText("NULL_NAME");
                break;
            case BOUND_ERROR:
                errorUpperBound.setText("BOUND_ERROR");
                break;
            case INVALID_PRIORITY:
                errorPriority.setText("INVALID_PRIORITY");
                break;
            case INVALID_NODE_TYPE:
                errorNodeType.setText("INVALID_NODE_TYPE");
                break;
            case INPUT_FILE_ERROR:
                inputTextFileError.setText("INPUT_FILE_ERROR");
                break;
            default:
                errorName.setText("UNKNOWN_ERROR");
                errorLowerBound.setText("UNKNOWN_ERROR");
                errorNodeType.setText("UNKNOWN_ERROR");
                errorPriority.setText("UNKNOWN_ERROR");
                errorUpperBound.setText("UNKNOWN_ERROR");
                inputTextFileError.setText("UNKNOWN_ERROR");
                validNodeAddedLabel.setText("UNKNOWN_ERROR");

        }
    }

    @FXML
    protected void handleCreateNode() {
        try {
            resetFields();

            String nodeName = nodeNameField.getText();
            NodeType nodeType = nodeTypeComboBox.getValue();
            Double lowerBound = Double.parseDouble(lowerBoundField.getText());
            Double upperBound = Double.parseDouble(upperBoundField.getText());
            String inputFile = inputTextFieldFile.getText();
            Priority priority = priorityComboBox.getValue();

            if(priority == null){
                printError(InputError.INVALID_PRIORITY);
                return;
            }

            if(inputFile==null || !inputFile.endsWith(".txt")){
                printError(InputError.INPUT_FILE_ERROR);
                return;
            }

            if(nodeType == null || nodeType == NodeType.NONE){
                printError(InputError.INVALID_NODE_TYPE);
                return;
            }

            if (lowerBound > upperBound) {
                printError(InputError.BOUND_ERROR);
                return;
            }

            if(nodeName.isEmpty()){
                printError(InputError.NULL_NAME);
                return;
            }

            simulator.addNodeToList(new CreateNode(nodeType,priority, new Bounds(lowerBound,upperBound),inputFile,nodeName, canBuss), listView);
            validNodeAddedLabel.setText("Node successfully added!");

        } catch (NumberFormatException e) {
            printError(InputError.BOUND_ERROR);
        } catch (NullPointerException e) {
            printError(InputError.NULL_NAME);
        }
    }

}