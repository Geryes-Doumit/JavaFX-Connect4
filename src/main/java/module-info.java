module ensisa.puissance4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ensisa.puissance4 to javafx.fxml;
    exports ensisa.puissance4;
}