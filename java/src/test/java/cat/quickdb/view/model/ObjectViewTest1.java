package cat.quickdb.view.model;

public class ObjectViewTest1 {

    private int id;
    private String name;
    private String account;
    private ObjectViewTest2 obj2;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setObj2(ObjectViewTest2 obj2) {
        this.obj2 = obj2;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ObjectViewTest2 getObj2() {
        return obj2;
    }

}
