package cat.quickdb.invalid;

import cat.quickdb.db.AdminBase;
import cat.quickdb.invalid.model.Invalid2Object;
import cat.quickdb.invalid.model.InvalidObject;
import org.junit.*;

public class InvalidTest {

    AdminBase admin;

    @Before
    public void configure() {
        this.admin = AdminBase.initialize(AdminBase.DATABASE.MYSQL, "localhost",
                "3306", "testQuickDB", "root", "");
    }

    @Test
    public void testSaveInvalidLongString(){
        InvalidObject inv = new InvalidObject();
        inv.setName("quickdb");

        Assert.assertTrue(admin.save(inv));

        InvalidObject inv2 = new InvalidObject();
        inv2.setName("testingquickdb-testingquickdb-testingquickdb-testingquickdb-" +
                "testingquickdb-testingquickdb-testingquickdb-testingquickdb-testingquickdb-" +
                "testingquickdb-testingquickdb-testingquickdb-testingquickdb-testingquickdb-" +
                "testingquickdb-testingquickdb-testingquickdb-testingquickdb-");

        Assert.assertFalse(admin.save(inv2));
    }

    @Test
    public void testInvalidObtain(){
        InvalidObject inv = new InvalidObject();

        Assert.assertFalse(admin.obtain(inv, "name = 'diego'"));

        Assert.assertEquals(0, inv.getId());

        Assert.assertFalse(admin.obtain(inv).If("name").equal("diego").find());
    }

    @Test
    public void testInvalidModify(){
        InvalidObject inv = new InvalidObject();

        Assert.assertFalse(admin.obtain(inv, "name = 'diego'"));

        inv.setName("diego");
        Assert.assertFalse(admin.modify(inv));
    }

    @Test
    public void testInvalidDelete(){
        InvalidObject inv = new InvalidObject();

        Assert.assertFalse(admin.obtain(inv, "name = 'diego'"));

        Assert.assertTrue(admin.delete(inv));

        Invalid2Object inv2 = new Invalid2Object();
        Assert.assertFalse(admin.obtain(inv2, "name = 'diego'"));

        Assert.assertFalse(admin.delete(inv2));
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(InvalidTest.class);
    }

}
