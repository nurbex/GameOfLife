package pckg0;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class GameOfLife extends Application {

    @Override
    public void start(Stage stage){

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/pckg0/CellLifeGame.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("CellLife Game!");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        launch();
    }
}
