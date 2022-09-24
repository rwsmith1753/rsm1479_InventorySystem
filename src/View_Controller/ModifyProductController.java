package View_Controller;

import Model.*;
import static Model.Inventory.getAllParts;
import static Model.Inventory.getAllProducts;
import static View_Controller.MainWindowController.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**Controller class for ModifyProduct view*/
public class ModifyProductController implements Initializable {
    @FXML
    private TextField id;
    @FXML
    private TextField max;
    @FXML
    private TextField min;
    @FXML
    private TextField name;
    @FXML
    private TextField price;
    @FXML
    private TextField stock;
    @FXML
    private TextField searchPartField;
    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableColumn<Part, Integer> partColId;
    @FXML
    private TableColumn<Part, String> partColName;
    @FXML
    private TableColumn<Part, Integer> partColInv;
    @FXML
    private TableColumn<Part, Double> partColPrice;
    @FXML
    private TableView<Part> associatedTable;
    @FXML
    private TableColumn<Part, Integer> associatedColId;
    @FXML
    private TableColumn<Part, String> associatedColName;
    @FXML
    private TableColumn<Part, Integer> associatedColInv;
    @FXML
    private TableColumn<Part, Double> associatedColPrice;
    @FXML
    private Product product = getAllProducts().get(getProductIndex());
    @FXML
    private ObservableList<Part> partsToAdd = FXCollections.observableArrayList();

    /**Populate table views on view load
     * @param url not used
     * @param resourceBundle not used
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partTable.setItems(getAllParts());
        partColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partColInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partColPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        id.setText(String.valueOf(product.getId()));
        name.setText(String.valueOf(product.getName()));
        price.setText(String.valueOf(product.getPrice()));
        stock.setText(String.valueOf(product.getStock()));
        min.setText(String.valueOf(product.getMin()));
        max.setText(String.valueOf(product.getMax()));

        associatedTable.setItems(product.getAssociatedParts());
        associatedColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedColInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedColPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


    }
    /**Handler for part search
     * View results in part table
     * @param event
     * */
    @FXML
    public void search(ActionEvent event) {
        String search = searchPartField.getText().toLowerCase(Locale.ROOT);
        ObservableList<Part> parts = searchPartName(search);
        if (parts.size() == 0) {
            try {
                int id = Integer.parseInt(search);
                Part part = searchPartId(id);

                if (part != null) {
                    parts.add(part);
                }
            }
            catch(NumberFormatException e){
                //ignore
            }
        }
        partTable.setItems(parts);
    }
    /**Search for part by name
     * @param name
     * @return parts if match found
     * @return null if no match found
     * */
    @FXML
    private ObservableList<Part> searchPartName(String name) {
        return Inventory.lookupPart(name);
    }
    /**Search for part by ID
     * @param id
     * @return part if match found
     * @return null if no match found
     * */
    @FXML
    private Part searchPartId(int id) {
        return Inventory.lookupPart(id);
    }
    /**Associate selected part to active product
     * Add selected part to associatedParts table
     * @param event
     * */
    public void addPart(ActionEvent event) {
        Part selected = partTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            selectedWarning();
            return;
        }
        product.addAssociatedPart(selected);
        associatedTable.setItems(product.getAssociatedParts());
    }

    /**Remove selected associated part from product
     * Remove selected part from associatedParts table
     * @param event
     * */
    public void removePart(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("WARNING");
        alert.setHeaderText("Confirm Remove");
        alert.setContentText("Are you sure?");
        Optional<ButtonType> confirm = alert.showAndWait();
        if (confirm.isPresent() && confirm.get() == ButtonType.OK) {
            Part selected = associatedTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                selectedWarning();
                return;
            }
            product.getAssociatedParts().remove(selected);
            associatedTable.setItems(product.getAssociatedParts());
        } else {
            //ignore
        }
    }

    /**Void action and return to MainWindow
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
    /**Save new product to inventory and return to MainWindow
     * @param event
     * @throws IOException
     * */
    public void save(ActionEvent event) throws IOException {
        try {
            int index = getProductIndex();
            int productId = Integer.valueOf(id.getText());
            String productName = name.getText();
            double productPrice = Double.valueOf(price.getText());
            int productStock = Integer.valueOf(stock.getText());
            int productMin = Integer.valueOf(min.getText());
            int productMax = Integer.valueOf(max.getText());
            product.setId(productId);
            product.setName(productName);
            product.setPrice(productPrice);
            product.setStock(productStock);
            product.setMin(productMin);
            product.setMax(productMax);
            if (productMin <= productMax && productMin <= productStock && productStock <= productMax) {
                Inventory.updateProduct(index,product);
                cancel(event);
            } else {
                stockWarning();
            }
        }
        catch (NumberFormatException e) {
            inputWarning();
        }
    }
}
