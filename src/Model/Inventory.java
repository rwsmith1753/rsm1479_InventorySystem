package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    //declare parts and products lists
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    //show all parts
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    //show all products
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    public static void addPart(Part part) {
        allParts.add(part);
    }
    public static void addProduct(Product product) {
        allProducts.add(product);
    }
    public static void modifyPart(int index, Part part) {
        allParts.set(index,part);
    }
}
