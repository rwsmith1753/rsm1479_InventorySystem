package View_Controller;

import Model.*;
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

import static Model.Inventory.getAllParts;
import static Model.Inventory.getAllProducts;


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

    public static int getPartIndex() {
        return partIndex;
    }
    public static int getProductIndex() {
        return productIndex;
    }
    public static Part getPartToModify() {
        return partToModify;
    }
    public static Product getProductToModify() {
        return productToModify;
    }

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
            }
            catch(NumberFormatException e){
                //ignore
            }
        }
        partTable.setItems(parts);
    }
    private ObservableList<Part> searchPartName(String name) {
        ObservableList<Part> parts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = getAllParts();
        for(Part part : allParts) {
            if(part.getName().toLowerCase().contains(name)) {
                parts.add(part);
            }
        }
        return parts;
    }
    private Part searchPartId(int id) {
        ObservableList<Part> allParts = getAllParts();
        for(Part part : allParts) {
            if(part.getId() == id) {
                return part;
            }
        }
        return null;
    }
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
            }
            catch(NumberFormatException e){
                //ignore
            }
        }
        productTable.setItems(products);
    }
    private ObservableList<Product> searchProductName(String name) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = getAllProducts();

        for(Product product : allProducts) {
            if(product.getName().toLowerCase().contains(name)) {
                products.add(product);
            }
        }

        return products;
    }
    private Product searchProductId(int id) {
        ObservableList<Product> allProducts = getAllProducts();

        for(Product product : allProducts) {
            if(product.getId() == id) {
                return product;
            }
        }
        return null;
    }
    @FXML
    public void addPart(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1000,600);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void addProduct(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1000,600);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void deletePart(ActionEvent event) {
        Part selected = partTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        getAllParts().remove(selected);
    }
    @FXML
    public void deleteProduct(ActionEvent event) {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            noneSelectedWarning();
        }
        if (selected.getAssociatedParts() == null) {
            activeProductWarning();
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("WARNING");
        alert.setHeaderText("Confirm Delete");
        alert.setContentText("Are you sure?");
        Optional<ButtonType> confirm = alert.showAndWait();
        if (confirm.isPresent() && confirm.get() == ButtonType.OK) {
            selected = productTable.getSelectionModel().getSelectedItem();
            getAllProducts().remove(selected);
        }
    }
    @FXML
    public void modifyPart(ActionEvent event) throws IOException {
        Part partToModify = partTable.getSelectionModel().getSelectedItem();
        partIndex = getAllParts().indexOf(partToModify);
        Parent root = FXMLLoader.load(getClass().getResource("ModifyPart.fxml"));
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1000,600);
        stage.setTitle("Modify Part");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void modifyProduct(ActionEvent event) throws IOException {
        Product productToModify = productTable.getSelectionModel().getSelectedItem();
        productIndex = getAllProducts().indexOf(productToModify);
        Parent root = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1000,600);
        stage.setTitle("Modify Part");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void exit(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1000,600);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.close();
    }
    public static void stockWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("CHECK VALUES");
        alert.setHeaderText("Incorrect Stock Values");
        alert.setContentText("Minimum stock must be less than or equal to Maximum stock\nStock must be within Min/Max limits");
        alert.showAndWait();
    }
    public static void stringWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("INPUT TYPE");
        alert.setHeaderText("Incorrect Input");
        alert.setContentText("String type expected");
        alert.showAndWait();
    }
    public static void intWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("INPUT TYPE");
        alert.setHeaderText("Incorrect Input");
        alert.setContentText("Integer type expected");
        alert.showAndWait();
    }
    public static void doubleWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("INPUT TYPE");
        alert.setHeaderText("Incorrect Input");
        alert.setContentText("Number type expected");
        alert.showAndWait();
    }
    public static void noneSelectedWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("NONE SELECTED");
        alert.setHeaderText("Nothing Selected");
        alert.setContentText("Please select an item");
        alert.showAndWait();
    }
    public static void activeProductWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("ACTIVE PRODUCT");
        alert.setHeaderText("Product may still be in use");
        alert.setContentText("Please remove the associated parts before deleting this product");
        alert.showAndWait();
    }
}
