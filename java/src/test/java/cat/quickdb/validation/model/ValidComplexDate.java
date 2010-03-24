package cat.quickdb.validation.model;

import cat.quickdb.annotation.Validation;
import java.sql.Date;

public class ValidComplexDate {

    private int id;
    @Validation(date={Validation.DAY, Validation.GREATER, 5,
    Validation.MONTH, Validation.EQUAL, 5,
    Validation.YEAR, Validation.EQUALORLOWER, 2009})
    private Date date;

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

}
