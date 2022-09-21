package View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.Part;
import Model.inHousePart;
import Model.outsourcePart;
import Model.Inventory;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static java.util.Collections.max;


public class AddPartController implements Initializable {

    public Text sourceSwitch;
    public RadioButton inHouse;
    public RadioButton outsource;
    public static TextField id;
    public TextField name;
    public TextField stock;
    public TextField price;
    public TextField min;
    public TextField max;
    public TextField source;
    public ObservableList<Part> parts;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    @FXML
    public static int nextId() {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ArrayList<Integer> IDs = new ArrayList<Integer>();
        int nextId = 1;
        for (Part part: allParts) {
            int ID = part.getId();
            IDs.add(ID);
        }
        int lastId = max(IDs);
        return lastId + 1;
    }
    @FXML
    public void setInHouse(ActionEvent event) throws IOException {
        sourceSwitch.setText("Machine ID");
    }
    @FXML
    public void setOutsource(ActionEvent event) throws IOException {
        sourceSwitch.setText("Company");
    }
    @FXML
    public void cancel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void savePart(ActionEvent event) throws IOException {
        Part part;
        double partPrice = Double.valueOf(price.getText());
        int partStock = Integer.valueOf(stock.getText());
        int partMin = Integer.valueOf(min.getText());
        int partMax = Integer.valueOf(max.getText());
        if (inHouse.isSelected()) {
            int partSource = Integer.valueOf(source.getText());
            part = new inHousePart(nextId(),name.getText(),partPrice,partStock,partMin,partMax,partSource);
        } else {
            part = new outsourcePart(nextId(),name.getText(),partPrice,partStock,partMin,partMax,source.getText());
        }
        Inventory.getAllParts().add(part);
        cancel(event);
    }
}
