package View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import Model.Inventory;
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

/**Controller class for ModifyPart view*/
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

    /**Populate data on view load
     * @param url not used
     * @param resourceBundle not used
     * */
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
    /**Set In-House toggle to active, part source to "Machine ID"
     * @param event
     * @throws IOException
     * */
    @FXML
    public void setInHouse(ActionEvent event) throws IOException {
        sourceSwitch.setText("Machine ID");
    }
    /**Set Outsource toggle to active, part source to "Company"
     * @param event
     * @throws IOException
     * */
    @FXML
    public void setOutsource(ActionEvent event) throws IOException {
        sourceSwitch.setText("Company");
    }
    /**Void action and return to Main Window
     * @param event
     * @throws IOException
     * */
    @FXML
    public void cancel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**Saves modified part data and return to Main Window
     * @param event
     * @throws IOException
     * */
    @FXML
    void savePart(ActionEvent event) throws IOException {
        try {
            int index = getPartIndex();
            int partId = Integer.valueOf(id.getText());
            String partName = name.getText();
            double partPrice = Double.valueOf(price.getText());
            int partStock = Integer.valueOf(stock.getText());
            int partMin = Integer.valueOf(min.getText());
            int partMax = Integer.valueOf(max.getText());
            if (partMin <= partMax && partMin <= partStock && partStock <= partMax) {
                if (inHouse.isSelected() == true) {
                    int partSource = Integer.valueOf(source.getText());
                    inHousePart part = new inHousePart(partId, partName, partPrice, partStock, partMin, partMax, partSource);
                    Inventory.updatePart(index,part);
                } else {
                    String partSource = source.getText();
                    outsourcePart part = new outsourcePart(partId, partName, partPrice, partStock, partMin, partMax, partSource);
                    Inventory.updatePart(index,part);
                }
                cancel(event);
            }
            else {
                stockWarning();
            }
        }
        catch (NumberFormatException e) {
            inputWarning();
        }
    }
}
