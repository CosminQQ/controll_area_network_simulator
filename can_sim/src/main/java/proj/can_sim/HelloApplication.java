package proj.can_sim;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.message.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 750);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();

//        Initializer mySimulation = new Initializer(6);
//        try {
//            System.out.println("hahaha");
//            mySimulation.runSimulation();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//
//        }
//        System.out.println("mahaha");


//        CanBuss b = new CanBuss();
//        ControllerTest sim = new ControllerTest(b);
//        sim.setup();
//

//        NetworkCAN n = new NetworkCAN();
//        n.runSimulation();
//            CanBuss  canBuss = new CanBuss();
//        ArrayList<CreateNode> a = new ArrayList<>();
//        a.add(new CreateNode(NodeType.LEVEL_CHECK, Priority.LEVEL_3, new Bounds(10,100),"fueltank.txt","Lester"));
//        a.add(new CreateNode(NodeType.MASTER_PROGRAM, Priority.LEVEL_1, new Bounds(0,100),"main.txt","Sefu"));
//
//        CreateNode.bondNodes(a);
//        CreateNode.startNodes(canBuss, a,4);
    }
}