package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**Product class*/
public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    /**Product constructor
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }
    /**Get product ID
     * @return id
     * */
    public int getId() {
        return id;
    }
    /**Get product Name
     * @return name
     * */
    public String getName() {
        return name;
    }
    /**Get product Price
     * @return price
     * */
    public double getPrice() {
        return price;
    }
    /**Get product Stock Level
     * @return stock
     * */
    public int getStock() {
        return stock;
    }
    /**Get product Minimum Stock Level
     * @return min
     * */
    public int getMin() {
        return min;
    }
    /**Get product Maximum Stock Level
     * @return max
     * */
    public int getMax() {
        return max;
    }

    /**Set product ID
     * @param id
     * */
    public void setId(int id) {
        this.id = id;
    }
    /**Set product Name
     * @param name
     * */
    public void setName(String name) {
        this.name = name;
    }
    /**Set product Price
     * @param price
     * */
    public void setPrice(Double price) {
        this.price = price;
    }
    /**Set product Stock level
     * @param stock
     * */
    public void setStock(int stock) {
        this.stock = stock;
    }
    /**Set product Minimum Inventory Leve
     * @param min
     * */
    public void setMin(int min) {
        this.min = min;
    }
    /**Set product Maximum Inventory Level
     * @param max
     * */
    public void setMax(int max) {
        this.max = max;
    }
    /**Associate part to product
     * @param part
     * */
    public void addAssociatedPart(Part part) {
        this.associatedParts.add(part);
    }
    /**Get parts associated with this product
     * @return associatedParts
     * */
    public ObservableList getAssociatedParts() {
        return associatedParts;
    }
    /**Remove associated part from this product
     * @param part
     * */
    public void deleteAssociatedPart(Part part) {
        this.associatedParts.remove(part);
    }
}
