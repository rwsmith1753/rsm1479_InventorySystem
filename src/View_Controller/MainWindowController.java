package View_Controller;

import Model.*;
import static Model.Inventory.getAllParts;
import static Model.Inventory.getAllProducts;
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

/**Controller class for MainWindow view*/
public class MainWindowController implements Initializable {
    public TextField searchPartField;
    public TextField searchProductField;
    public TableView<Part> partTable;
    public TableColumn<Part, Integer> partColId;
    public TableColumn<Part, String> partColName;
    public TableColumn<Part, Integer> partColInv;
    public TableColumn<Part, Double> partColPrice;
    public TableView<Product> productTable;
    public TableColumn<Product, Integer> productColId;
    public TableColumn<Product, String> productColName;
    public TableColumn<Product, Integer> productColInv;
    public TableColumn<Product, Double> productColPrice;
    public Button addPartBtn;
    public Button modifyPartBtn;

    private static Part partToModify;
    private static Product productToModify;
    private static int partIndex;
    private static int productIndex;

    /**Retrieve part index
     * $return int partIndex
     * */
    public static int getPartIndex() {
        return partIndex;
    }
    /**Retrieve product index
     * @return int productIndex
     * */

    public static int getProductIndex() {
        return productIndex;
    }

    /**Retrieve selected part to modify
     * @return Part partToModify
     * */
    public static Part getPartToModify() {
        return partToModify;
    }

    /**Retrieve selected product to modify
     * @return Product productToModify
     * */
    public Product getProductToModify() {
        return productToModify;
    }

    /**Populate table data on view load
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

        productTable.setItems(getAllProducts());
        productColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productColInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productColPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
    /**Handler for part search
     * View search result in table
     * @param event
     * */
    public void searchPart(ActionEvent event) {

        String search = searchPartField.getText().toLowerCase(Locale.ROOT);
        ObservableList<Part> parts = searchPartName(search);
        if (parts.size() == 0) {
            try {
                int id = Integer.parseInt(search);
                Part part = searchPartId(id);
                if (part != null) {
                    parts.add(part);
                }
            } catch (NumberFormatException e) {
                //ignore
            }
        }
        partTable.setItems(parts);
    }
    /**Search for part by name
     * @param name
     * @return parts found
     * */
    private ObservableList<Part> searchPartName(String name) {
        return Inventory.lookupPart(name);
    }
    /**Search for part by ID
     * @return part if match found
     * @return null if no match found
     * */
    private Part searchPartId(int id) {
        return Inventory.lookupPart(id);
    }
    /**Handler for product search
     * View results in table
     * @param event
     * */
    public void searchProduct(ActionEvent event) {
        String search = searchProductField.getText().toLowerCase(Locale.ROOT);
        ObservableList<Product> products = searchProductName(search);
        if (products.size() == 0) {
            try {
                int id = Integer.parseInt(search);
                Product product = searchProductId(id);
                if (product != null) {
                    products.add(product);
                }
            } catch (NumberFormatException e) {
                //ignore
            }
        }
        productTable.setItems(products);
    }
    /**Search for product by name
     * @param name
     * @return products if match found
     * @return null if no match found
     * */
    private ObservableList<Product> searchProductName(String name) {
        return Inventory.lookupProduct(name);
    }
    /**Search for product by ID
     * @return product if match found
     * @return null if no match found
     * */
    private Product searchProductId(int id) {
        return Inventory.lookupProduct(id);
    }
    /**Open AddPart view
     * @param event
     * @throws IOException
     * */
    @FXML
    public void addPart(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1000,600);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }
    /**Open AddProduct view
     * @param event
     * @throws IOException
     * */
    @FXML
    public void addProduct(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1000,600);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }
    /**Delete selected part
     * @param event
     * */
    @FXML
    public void deletePart(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("WARNING");
        alert.setHeaderText("Confirm Delete");
        alert.setContentText("Are you sure?");
        Optional<ButtonType> confirm = alert.showAndWait();
        if (confirm.isPresent() && confirm.get() == ButtonType.OK) {
            Part selected = partTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                selectedWarning();
                return;
            }
            Inventory.deletePart(selected);
        }
    }
    /**Delete selected product
     * @param event
     * */
    @FXML
    public void deleteProduct(ActionEvent event) {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            selectedWarning();
            return;
        }
        if (selected.getAssociatedParts().size() > 0) {
            activeProductWarning();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("WARNING");
            alert.setHeaderText("Confirm Delete");
            alert.setContentText("Are you sure?");
            Optional<ButtonType> confirm = alert.showAndWait();
            if (confirm.isPresent() && confirm.get() == ButtonType.OK) {
                selected = productTable.getSelectionModel().getSelectedItem();
                Inventory.deleteProduct(selected);
            }
        }
    }
    /**Open ModifyPart view to modify data for selected part
     * @param event
     * @throws IOException
     * */
    @FXML
    public void modifyPart(ActionEvent event) throws IOException {
        Part partToModify = partTable.getSelectionModel().getSelectedItem();
        if (partToModify == null) {
            selectedWarning();
            return;
        }
        partIndex = getAllParts().indexOf(partToModify);
        Parent root = FXMLLoader.load(getClass().getResource("ModifyPart.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("Modify Part");
        stage.setScene(scene);
        stage.show();
    }

    /**Open ModifyProduct view to modify data for selected product
     * @param event
     * @throws IOException
     * */
    @FXML
    public void modifyProduct(ActionEvent event) throws IOException {
        Product productToModify = productTable.getSelectionModel().getSelectedItem();
        if (productToModify == null) {
            selectedWarning();
            return;
        }
        productIndex = getAllProducts().indexOf(productToModify);
        Parent root = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("Modify Product");
        stage.setScene(scene);
        stage.show();
    }
    /**Void action and return to MainWindow
     * @param event
     * @throws IOException
     * */
    @FXML
    public void exit(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1000,600);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.close();
    }
    /**Warning! stock values violate Min/Max logic*/
    public static void stockWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("CHECK VALUES");
        alert.setHeaderText("Incorrect Stock Values");
        alert.setContentText("Minimum stock must be less than or equal to Maximum stock\nStock must be within Min/Max limits");
        alert.showAndWait();
    }
    /**Warning! Incorrect data typ*/
    public static void inputWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("INCORRECT INPUT");
        alert.setHeaderText("Incorrect data type");
        alert.setContentText("Please enter correct data types\nName: String\nPrice: Number\nInv: Integer (must be between Min and Max)\nMax: Integer\nMin: Integer");
        alert.showAndWait();
    }
    /**Warning! Products with associated parts cannot be deleted*/
    public static void activeProductWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("ACTIVE PRODUCT");
        alert.setHeaderText("Product may still be in use");
        alert.setContentText("Please remove all associated parts before deleting this product");
        alert.showAndWait();
    }
    /**Warning! Searched item was not found*/
    public static void selectedWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("ITEM NOT SELECTED");
        alert.setHeaderText("This action requires a selection");
        alert.setContentText("Please select an item from the table to continue");
        alert.showAndWait();
    }
}
