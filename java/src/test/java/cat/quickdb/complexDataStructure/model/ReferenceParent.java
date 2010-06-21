package cat.quickdb.complexDataStructure.model;

import java.sql.Date;

/**
 *
 * @author Diego Sarmentero
 */
public class ReferenceParent {

    private int id;
    private String referenceValue;
    private Date actualDate;

    public void setActualDate(Date actualDate) {
        this.actualDate = actualDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReferenceValue(String referenceValue) {
        this.referenceValue = referenceValue;
    }

    public Date getActualDate() {
        return actualDate;
    }

    public int getId() {
        return id;
    }

    public String getReferenceValue() {
        return referenceValue;
    }

}
