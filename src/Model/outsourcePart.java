package Model;
/**Extension of Part, adds companyName attribute*/
public class outsourcePart extends Part {
    private String companyName;
    /**outsourcePart constructor
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param companyName
     * */
    public outsourcePart(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
    /**Retrieve Company Name
     * @return companyName
     * */
    public String getCompanyName() {
        return companyName;
    }
    /**Set part's Company Name
     * @param companyName
     * */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
