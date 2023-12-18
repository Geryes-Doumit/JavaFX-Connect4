package ensisa.puissance4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Puissance4App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Puissance4App.class.getResource("Connect4View.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 650);
        stage.setTitle("Puissance 4");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}