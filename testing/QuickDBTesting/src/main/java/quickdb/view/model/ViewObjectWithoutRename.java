package quickdb.view.model;

import quickdb.db.View;
import quickdb.query.Query;
import java.sql.Date;

public class ViewObjectWithoutRename extends View{

    private String name;
    private String description;
    private String account;
    private Date dateView;

    @Override
    public Object query(){
        ObjectViewTest1 o1 = new ObjectViewTest1();
        Query query = Query.create(this.getAdminBase(), o1);
        query.If("account", ObjectViewTest1.class).equal("accountTest");
        return query;
    }

    @Override
    public String columns(){
        return "name, description, account, date";
    }

    @Override
    public Class[] classes(){
        return new Class[]{ObjectViewTest1.class, ObjectViewTest2.class,
        ObjectViewTest1.class, ObjectViewTest2.class};
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
