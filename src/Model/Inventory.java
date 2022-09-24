package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**Inventory class controls inventory item actions*/
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**Add part to inventory
     * @param part
     * */
    public static void addPart(Part part) {
        allParts.add(part);
    }
    /**Add product to inventory
     * @param product
     * */
    public static void addProduct(Product product) {
        allProducts.add(product);
    }
    /**Search for part by ID
     * @param partId
     * @return part if match found
     * @return null if no match found
     * */
    public static Part lookupPart(int partId) {
        ObservableList<Part> allParts = getAllParts();
        for(Part part : allParts) {
            if(part.getId() == partId) {
                return part;
            }
        }
        return null;
    }
    /**Search for product by ID
     * @param productId
     * @return product if match found
     * @return null if no match found
     * */
    public static Product lookupProduct(int productId) {
        ObservableList<Product> allProducts = getAllProducts();

        for(Product product : allProducts) {
            if(product.getId() == productId) {
                return product;
            }
        }
        return null;
    }
    /**Search for part by name
     * @param partName
     * @return parts if match found
     * @return null if no match found
     * */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> parts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = getAllParts();
        for(Part part : allParts) {
            if(part.getName().toLowerCase().contains(partName)) {
                parts.add(part);
            }
        }
        return parts;
    }
    /**Search for product by name
     * @param productName
     * @return products if match found
     * @return null if no match found
     * */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = getAllProducts();
        for(Product product : allProducts) {
            if(product.getName().toLowerCase().contains(productName)) {
                products.add(product);
            }
        }
        return products;
    }
//        else {
//            return null;
//        }

    /**Update selected part with modified data
     * @param index
     * @param selectedPart
     * */
    public static void updatePart(int index, Part selectedPart) {
        getAllParts().set(index, selectedPart);
    }
    /**Update selected product with modified data
     * @param index
     * @param selectedProduct
     * */
    public static void updateProduct(int index, Product selectedProduct) {
        getAllProducts().set(index, selectedProduct);
    }
    /**Delete selected part
     * @param selectedPart
     * @return true if present
     * @return false if not present
     * */
    public static boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        }
        else {return false;}
    }
    /**Delete selected product
     * @param selectedProduct
     * @return true if present
     * @return false if not present
     * */
    public static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        }
        else {return false;}
    }
    /**Get all parts
     * @return allParts
     * */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    /**Get all products
     * @return allProducts
     * */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
