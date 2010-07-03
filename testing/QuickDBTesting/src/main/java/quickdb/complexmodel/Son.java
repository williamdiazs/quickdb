package quickdb.complexmodel;

public class Son extends Parent{

    private int id;
    private String sonName;
    private String data;

    public Son(){}

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the sonName
     */
    public String getSonName() {
        return sonName;
    }

    /**
     * @param sonName the sonName to set
     */
    public void setSonName(String sonName) {
        this.sonName = sonName;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

}
