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
import java.util.ResourceBundle;

import static Model.Inventory.*;
import static View_Controller.MainWindowController.*;

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
    private static Product product = getAllProducts().get(getProductIndex());
    @FXML
    private ObservableList<Part> partsToAdd = FXCollections.observableArrayList();
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
        product.addAssociatedPart(selected);
        associatedTable.setItems(product.getAssociatedParts());
    }
    public void removePart(ActionEvent event) {
        Part selected = associatedTable.getSelectionModel().getSelectedItem();
        product.getAssociatedParts().remove(selected);
        associatedTable.setItems(product.getAssociatedParts());
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
        int index = getProductIndex();
        int productId = Integer.valueOf(id.getText());
        String productName = name.getText();
        double productPrice = Double.valueOf(price.getText());
        int productStock = Integer.valueOf(stock.getText());
        int productMin = Integer.valueOf(min.getText());
        int productMax = Integer.valueOf(max.getText());
        if (productMin <= productMax && productMin <= productStock && productStock <= productMax) {
            getAllProducts().set(index,product);
            cancel(event);
        } else {
            stockWarning();
        }
    }
}
