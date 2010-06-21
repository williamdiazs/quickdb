package cat.quickdb.query.model;

public class ReferenceQuery extends ReferenceParent{

    private int id;
    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
