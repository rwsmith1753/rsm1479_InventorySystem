import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import Model.Inventory;
import Model.inHousePart;
import Model.outsourcePart;
import Model.Product;

/**Ryan Smith's Inventory Management System -- A graphical user interface application for managing and maintaining an inventory of parts and products
*
*Initional default inventory items are load at program launch
*/
public class Main extends Application {
    @Override
    public void start(Stage mainStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("View_Controller/MainWindow.fxml"));
        Scene scene = new Scene(root, 1000, 600);
        mainStage.setTitle("Inventory Management System");
        mainStage.setScene(scene);
        mainStage.show();
    }
    public static void main(String[] args) {
        ///prelaoded parts
        //In House Parts
        inHousePart comp1 = new inHousePart(1,"Component 1", 99.99,20,10,30, 1);
        inHousePart comp2 = new inHousePart(2,"Component 2", 87.99,5,2,10, 1);
        inHousePart comp3 = new inHousePart(3,"Component 3", 124.99,45,40,50, 1);
        inHousePart comp4 = new inHousePart(4,"Component 4", 34.99,36,20,44, 1);

        //Outsource Parts
        outsourcePart comp5 = new outsourcePart(5,"Component 5", 9.99,90,50,300, "Company 1");
        outsourcePart comp6 = new outsourcePart(6,"Component 6", 49.99,64,10,100, "Company 2");
        outsourcePart comp7 = new outsourcePart(7,"Component 7", 179.99,3,1,5, "Company 3");

        //preloaded products
        Product prod1 = new Product(1, "Primary Bundle", 1990.99, 7, 5, 12);
        Product prod2 = new Product(2, "Secondary Bundle", 1599.99, 7, 5, 12);
        Product prod3 = new Product(3, "Tertiary Bundle", 999.99, 7, 5, 12);

        //add parts to inventory
        Inventory.addPart(comp1);
        Inventory.addPart(comp2);
        Inventory.addPart(comp3);
        Inventory.addPart(comp4);
        Inventory.addPart(comp5);
        Inventory.addPart(comp6);
        Inventory.addPart(comp7);

        //add products to inventory
        Inventory.addProduct(prod1);
        Inventory.addProduct(prod2);
        Inventory.addProduct(prod3);

        prod1.addAssociatedPart(comp1);
        prod2.addAssociatedPart(comp4);
        prod3.addAssociatedPart(comp7);

        launch();
    }
}
