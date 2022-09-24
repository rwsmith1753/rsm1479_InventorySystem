package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
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
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.Inventory.addProduct;
import static Model.Inventory.getAllParts;
import static View_Controller.MainWindowController.*;
import static java.util.Collections.max;

/**Controller class for AddProduct view*/
public class AddProductController implements Initializable {
    @FXML
    private TextField price;
    @FXML
    private TextField stock;
    @FXML
    private TextField max;
    @FXML
    private TextField min;
    @FXML
    private TextField name;
    @FXML
    private TextField searchPartField;
    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableView<Part> associatedTable;
    @FXML
    private TableColumn<Part, Integer> partColId;
    @FXML
    private TableColumn<Part, String> partColName;
    @FXML
    private TableColumn<Part, Integer> partColInv;
    @FXML
    private TableColumn<Part, Double> partColPrice;
    @FXML
    private TableColumn<Part, Integer> associatedColId;
    @FXML
    private TableColumn<Part, String> associatedColName;
    @FXML
    private TableColumn<Part, Integer> associatedColInv;
    @FXML
    private TableColumn<Part, Double> associatedColPrice;
    @FXML
    private ObservableList<Part> partsToAdd = FXCollections.observableArrayList();

    /**Populate table views on view open
    * @param url not used
    * @param resourcebundle not used
    *
    *
    * RUNTIME ERROR:
    * "Caused by: java.lang.NullPointerException: Cannot invoke "javafx.scene.control.TableColumn.setCellValueFactory(javafx.util.Callback)" because "this.partColId" is null"
    *               at View_Controller.AddProductController.initialize(AddProductController.java:158)
    *
    * EFFECT:
    * prevented AddProduct window from opening
    *
    * CAUSE:
    * "@FXML" annotation was missing on "Private TableColumn..." declarations
    *
    * SOLUTION:
    * added "@FXML" to each declaration
    *
    * NOTE:
    * error occurred at "partColName.setCellValueFactory(new PropertyValueFactory<Part,String>("name"));"
    */
    @Override
    public void initialize (URL url, ResourceBundle resourcebundle) {
        partTable.setItems(Inventory.getAllParts());
        partColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partColInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partColPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedTable.setItems(partsToAdd);
        associatedColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedColInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedColPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    /**Get next available ID
     * @return nextId unique ID number
     * */
    @FXML
    public int nextId() {
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ArrayList<Integer> IDs = new ArrayList<Integer>();
        for (Product product : allProducts) {
            int ID = product.getId();
            IDs.add(ID);
        }
        int nextId = max(IDs) + 1;
        return nextId;
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
        partsToAdd.add(selected);
        associatedTable.setItems(partsToAdd);
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
            partsToAdd.remove(selected);
            associatedTable.setItems(partsToAdd);
        } else {
            //ignore
        }
    }
    @FXML
    /**Void action and return to MainWindow
     * @param event
     * @throws IOException
     * */
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
            double productPrice = Double.valueOf(price.getText());
            int productStock = Integer.valueOf(stock.getText());
            int productMin = Integer.valueOf(min.getText());
            int productMax = Integer.valueOf(max.getText());
            if (productMin <= productMax && productMin <= productStock && productStock <= productMax) {
                Product product = new Product(nextId(), name.getText(), productPrice, productStock, productMin, productMax);
                addProduct(product);
                for (Part part : partsToAdd) {
                    product.addAssociatedPart(part);
                }
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
