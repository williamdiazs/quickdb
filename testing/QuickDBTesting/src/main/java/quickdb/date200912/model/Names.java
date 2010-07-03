package quickdb.date200912.model;

import quickdb.annotation.*;

@Table
public class Names {

    @Column(type=Properties.TYPES.PRIMARYKEY)
    @ColumnDefinition(type=Definition.DATATYPE.INT, autoIncrement=true,
    length=11, primary=true)
    private int idName;
    private String otro;
    private Name2 testa;
    @Column(ignore=true)
    private String pruebaIgnore;

    /**
     * @return the idName
     */
    public int getIdName() {
        return idName;
    }

    /**
     * @param idName the idName to set
     */
    public void setIdName(int idName) {
        this.idName = idName;
    }

    /**
     * @return the otro
     */
    public String getOtro() {
        return otro;
    }

    /**
     * @param otro the otro to set
     */
    public void setOtro(String otro) {
        this.otro = otro;
    }

    /**
     * @return the testa
     */
    public Name2 getTesta() {
        return testa;
    }

    /**
     * @param testa the testa to set
     */
    public void setTesta(Name2 testa) {
        this.testa = testa;
    }

    /**
     * @return the pruebaIgnore
     */
    public String getPruebaIgnore() {
        return pruebaIgnore;
    }

    /**
     * @param pruebaIgnore the pruebaIgnore to set
     */
    public void setPruebaIgnore(String pruebaIgnore) {
        this.pruebaIgnore = pruebaIgnore;
    }

}
