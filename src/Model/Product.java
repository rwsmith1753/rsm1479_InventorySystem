package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }
    public int getId() {return id;}
    public String getName() {return name;}
    public double getPrice() {return price;}
    public int getStock() {return stock;}
    public int getMin() {return min;}
    public int getMax() {return max;}

    public void setId() {this.id = id;}
    public void setName() {this.name = name;}
    public void setPrice() {this.price = price;}
    public void setStock() {this.stock = stock;}
    public void setMin() {this.min = min;}
    public void setMax() {this.max = max;}

    public void addAssociatedPart(Part part) {
        this.associatedParts.add(part);
    }
    public ObservableList getAssociatedParts() {
        return associatedParts;
    }
    public void deleteAssociatedPart(Part part) {
        this.associatedParts.remove(part);
    }
}
