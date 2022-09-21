package Model;

public class inHousePart extends Part {
    private static int machineId;

    public inHousePart(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }
    public static int getMachineId() {
        return machineId;
    }
    public void setMachineId() {
        this.machineId = machineId;
    }
}
