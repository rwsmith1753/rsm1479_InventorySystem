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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import static Model.Inventory.addProduct;
import static Model.Inventory.getAllParts;
import static java.util.Collections.max;

public class AddProductController implements Initializable {

//    @FXML
//    private ResourceBundle resources;
//    @FXML
//    private URL location;
//    @FXML
//    private TextField id;
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

    @Override
    public void initialize (URL url, ResourceBundle resourcebundle) {
        partTable.setItems(Inventory.getAllParts());
        partColId.setCellValueFactory(new PropertyValueFactory<Part,Integer>("id"));
        /** RUNTIME ERROR:
         * "Caused by: java.lang.NullPointerException: Cannot invoke "javafx.scene.control.TableColumn.setCellValueFactory(javafx.util.Callback)" because "this.partColId" is null"
         *               at View_Controller.AddProductController.initialize(AddProductController.java:158)   ####--need to describe solution--#####
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
         * error occured at "partColName.setCellValueFactory(new PropertyValueFactory<Part,String>("name"));"
         */
        partColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partColInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partColPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedTable.setItems(partsToAdd);
        associatedColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedColInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedColPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    @FXML
    public static int nextId() {
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ArrayList<Integer> IDs = new ArrayList<Integer>();
        int nextId = 1;
        for (Product product : allProducts) {
            int ID = product.getId();
            IDs.add(ID);
        }
        int lastId = max(IDs);
        return lastId + 1;
    }
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
    @FXML
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
    @FXML
    private Part searchPartId(int id) {
        ObservableList<Part> allParts = getAllParts();
        for (Part part : allParts) {
            if (part.getId() == id) {
                return part;
            }
        }
        return null;
    }
    public void addPart(ActionEvent event) {
        Part selected = partTable.getSelectionModel().getSelectedItem();
        partsToAdd.add(selected);
        associatedTable.setItems(partsToAdd);
    }
    public void removePart(ActionEvent event) {
        Part selected = associatedTable.getSelectionModel().getSelectedItem();
        partsToAdd.remove(selected);
        associatedTable.setItems(partsToAdd);
    }
    @FXML
    public void cancel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void save(ActionEvent event) throws IOException {
        double productPrice = Double.valueOf(price.getText());
        int productStock = Integer.valueOf(stock.getText());
        int productMin = Integer.valueOf(min.getText());
        int productMax = Integer.valueOf(max.getText());

        Product product = new Product(nextId(),name.getText(),productPrice,productStock,productMin,productMax);
        addProduct(product);
        for (Part part : partsToAdd) {
            product.addAssociatedPart(part);
        }
        cancel(event);
    }
}
