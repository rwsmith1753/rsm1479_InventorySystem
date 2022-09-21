package View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Model.Part;
import Model.inHousePart;
import Model.outsourcePart;
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

import static Model.Inventory.getAllParts;
import static View_Controller.MainWindowController.*;

public class ModifyPartController implements Initializable {
    @FXML
    private TextField id;
    @FXML
    private RadioButton inHouse;
    @FXML
    private TextField max;
    @FXML
    private TextField min;
    @FXML
    private TextField name;
    @FXML
    private RadioButton outsource;
    @FXML
    private TextField price;
    @FXML
    private TextField source;
    @FXML
    private Text sourceSwitch;
    @FXML
    private TextField stock;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Part part = (Part) getAllParts().get(getPartIndex());
        id.setText(String.valueOf(part.getId()));
        name.setText(String.valueOf(part.getName()));
        price.setText(String.valueOf(part.getPrice()));
        stock.setText(String.valueOf(part.getStock()));
        min.setText(String.valueOf(part.getMin()));
        max.setText(String.valueOf(part.getMax()));
        if (getAllParts().get(getPartIndex()) instanceof inHousePart) {
            inHousePart IH = (inHousePart) getAllParts().get(getPartIndex());
            source.setText(String.valueOf(IH.getMachineId()));
            sourceSwitch.setText("Machine ID");
            inHouse.setSelected(true);
        }
        else {
            outsourcePart OS = (outsourcePart) getAllParts().get(getPartIndex());
            source.setText(OS.getCompanyName());
            sourceSwitch.setText("Company");
            outsource.setSelected(true);
        }
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
    void savePart(ActionEvent event) throws IOException {
        int index = getPartIndex();
        int partId = Integer.valueOf(id.getText());
        String partName = name.getText();
        double partPrice = Double.valueOf(price.getText());
        int partStock = Integer.valueOf(stock.getText());
        int partMin = Integer.valueOf(min.getText());
        int partMax = Integer.valueOf(max.getText());
        if (inHouse.isSelected() == true) {
            int partSource = Integer.valueOf(source.getText());
            inHousePart part = new inHousePart(partId,partName,partPrice,partStock,partMin,partMax,partSource);
            getAllParts().set(index,part);
        }
        else {
            String partSource = source.getText();
            outsourcePart part = new outsourcePart(partId,partName,partPrice,partStock,partMin,partMax,partSource);
            getAllParts().set(index,part);
        }
        cancel(event);
    }
}
