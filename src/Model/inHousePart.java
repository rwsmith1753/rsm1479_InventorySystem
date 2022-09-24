package Model;
/**Extension of Part, added machineId attribute*/
public class inHousePart extends Part {
    private int machineId;
    /**inHousePart constructor
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param machineId
     * */
    public inHousePart(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }
    /**Retrieve Machine ID
     * @return machineId
     * */
    public int getMachineId() {
        return machineId;
    }
    /**Set part's Machine ID
     * @param machineId
     * */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
