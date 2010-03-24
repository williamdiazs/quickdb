package cat.quickdb.view.model;

import cat.quickdb.db.View;
import java.sql.Date;

public class ViewObjectString extends View{

    private String name;
    private String description;
    private String account;
    private Date dateView;

    @Override
    public Object query(){
        return "SELECT ObjectViewTest1.name 'name', ObjectViewTest2.description " +
                "'description', ObjectViewTest1.account 'account', " +
                "ObjectViewTest2.date 'dateView' " +
                "FROM ObjectViewTest1 " +
                "JOIN ObjectViewTest2 ON ObjectViewTest1.obj2 = ObjectViewTest2.id " +
                "WHERE  ObjectViewTest1.account = 'accountTest'";
    }

    public String getAccount() {
        return account;
    }

    public Date getDateView() {
        return dateView;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setDateView(Date dateView) {
        this.dateView = dateView;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

}
