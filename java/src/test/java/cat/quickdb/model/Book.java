package cat.quickdb.model;

import java.util.ArrayList;
import cat.quickdb.annotation.*;

@Table
public class Book{

    @Column(type=Properties.TYPES.PRIMARYKEY)
    @ColumnDefinition(type=Definition.DATATYPE.INT, length=11,
    autoIncrement=true, primary=true)
    private int id;
    @Column
    @ColumnDefinition
    private String name;
    @Column
    @ColumnDefinition(type=Definition.DATATYPE.INTEGER)
    private int isbn;
    @Column(type=Properties.TYPES.COLLECTION)
    @ColumnDefinition(type=Definition.DATATYPE.INTEGER)
    private ArrayList page;

    public Book(){
        this.name = "";
        this.page = new ArrayList();
    }

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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the isbn
     */
    public int getIsbn() {
        return isbn;
    }

    /**
     * @param isbn the isbn to set
     */
    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    /**
     * @return the pages
     */
    public ArrayList getPage() {
        return page;
    }

    /**
     * @param pages the pages to set
     */
    public void setPage(ArrayList pages) {
        this.page = pages;
    }

}