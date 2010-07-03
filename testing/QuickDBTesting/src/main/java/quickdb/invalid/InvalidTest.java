package quickdb.invalid;

import quickdb.db.AdminBase;
import quickdb.invalid.model.Invalid2Object;
import quickdb.invalid.model.InvalidObject;

public class InvalidTest {

    private AdminBase admin;

    public InvalidTest(AdminBase admin){
        this.admin = admin;
        System.out.println("-----------------------");
        System.out.println("InvalidTest");
        System.out.println("testInvalidDelete");
        this.testInvalidDelete();
        System.out.println("testInvalidModify");
        this.testInvalidModify();
        System.out.println("testInvalidObtain");
        this.testInvalidObtain();
        System.out.println("testSaveInvalidLongString");
        this.testSaveInvalidLongString();
    }

    public void testSaveInvalidLongString(){
        InvalidObject inv = new InvalidObject();
        inv.setName("quickdb");

        System.out.println(admin.save(inv));

        InvalidObject inv2 = new InvalidObject();
        inv2.setName("testingquickdb-testingquickdb-testingquickdb-testingquickdb-" +
                "testingquickdb-testingquickdb-testingquickdb-testingquickdb-testingquickdb-" +
                "testingquickdb-testingquickdb-testingquickdb-testingquickdb-testingquickdb-" +
                "testingquickdb-testingquickdb-testingquickdb-testingquickdb-");

        System.out.println(!admin.save(inv2));
    }

    public void testInvalidObtain(){
        InvalidObject inv = new InvalidObject();

        System.out.println(!admin.obtain(inv, "name = 'diego'"));

        System.out.println(0 == inv.getId());

        System.out.println(!admin.obtain(inv).If("name").equal("diego").find());
    }

    public void testInvalidModify(){
        InvalidObject inv = new InvalidObject();

        System.out.println(!admin.obtain(inv, "name = 'diego'"));

        inv.setName("diego");
        System.out.println(!admin.modify(inv));
    }

    public void testInvalidDelete(){
        InvalidObject inv = new InvalidObject();

        System.out.println(!admin.obtain(inv, "name = 'diego'"));

        System.out.println(admin.delete(inv));

        Invalid2Object inv2 = new Invalid2Object();
        System.out.println(!admin.obtain(inv2, "name = 'diego'"));

        System.out.println(!admin.delete(inv2));
    }

}
