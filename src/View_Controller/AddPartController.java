package View_Controller;

import Model.Part;
import Model.inHousePart;
import Model.outsourcePart;
import Model.Inventory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static View_Controller.MainWindowController.inputWarning;
import static View_Controller.MainWindowController.stockWarning;
import static java.util.Collections.max;

/**Controller class for AddPart view*/
public class AddPartController implements Initializable {

    public Text sourceSwitch;
    public RadioButton inHouse;
    public RadioButton outsource;
    public TextField id;
    public TextField name;
    public TextField stock;
    public TextField price;
    public TextField min;
    public TextField max;
    public TextField source;
    public ObservableList<Part> parts;

    /**Open view with empty text fields
     * @param url not used
     * @param resourceBundle not used
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**Generate next available ID
     * @return nextId unique ID number
     * */
    @FXML
    public static int nextId() {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ArrayList<Integer> IDs = new ArrayList<>();
        for (Part part: allParts) {
            int ID = part.getId();
            IDs.add(ID);
        }
        int nextId = max(IDs) + 1;
        return nextId;
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
    /**Saves new part and return to Main Window
     * @param event
     * @throws IOException
     * */
    @FXML
    public void savePart(ActionEvent event) throws IOException {
        try {
            double partPrice = Double.valueOf(price.getText());
            int partStock = Integer.valueOf(stock.getText());
            int partMin = Integer.valueOf(min.getText());
            int partMax = Integer.valueOf(max.getText());

            if (partMin <= partMax && partMin <= partStock && partStock <= partMax) {
                if (inHouse.isSelected()) {
                    int partSource = Integer.valueOf(source.getText());
                    inHousePart part = new inHousePart(0, "null", 0, 0, 0, 0, 0);
                    part.setId(nextId());
                    part.setName(name.getText());
                    part.setPrice(partPrice);
                    part.setStock(partStock);
                    part.setMin(partMin);
                    part.setMax(partMax);
                    part.setMachineId(partSource);
                    Inventory.getAllParts().add(part);
                    cancel(event);
                }
                else {
                    String partSource = source.getText();
                    outsourcePart part = new outsourcePart(0, "null", 0, 0, 0, 0, "null");
                    part.setId(nextId());
                    part.setName(name.getText());
                    part.setPrice(partPrice);
                    part.setStock(partStock);
                    part.setMin(partMin);
                    part.setMax(partMax);
                    part.setCompanyName(partSource);
                    Inventory.getAllParts().add(part);
                    cancel(event);
                }
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
