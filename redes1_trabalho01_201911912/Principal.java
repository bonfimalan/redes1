/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 30/07/2021 17:55
 * Last update: 04/08/2021 13:00
 * Name: Principal.java
 * Function: The main class
 *******************************************************************/
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import controllers.MainController;

public class Principal extends Application{
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_view.fxml"));
    loader.setController(new MainController());
    Parent root = loader.load();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }
}