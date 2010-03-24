package cat.quickdb.binding;

import cat.quickdb.binding.model.BindingObject;
import cat.quickdb.db.AdminBase;
import cat.quickdb.db.AdminBase.DATABASE;
import org.junit.*;
import junit.framework.Assert;

public class BindingTest {

    @Before
    public void configure() {
        AdminBase.initializeAdminBinding(DATABASE.MYSQL, "localhost",
                "3306", "testQuickDB", "root", "");
    }

    @Test
    public void testSave(){
        BindingObject bind = new BindingObject();
        bind.setBirth(new java.sql.Date(104, 4, 20));
        bind.setName("quickdb");
        bind.setSalary(3000.50);

        Assert.assertTrue(bind.save());
    }

    @Test
    public void testSaveGetIndex(){
        BindingObject bind = new BindingObject();
        bind.setBirth(new java.sql.Date(104, 4, 20));
        bind.setName("quickdb");
        bind.setSalary(3000.50);

        Assert.assertTrue((bind.saveGetIndex() > 0));
    }

    @Test
    public void testObtain(){
        BindingObject bind = new BindingObject();

        bind.obtain().where("name").equal("quickdb").find();
        Assert.assertEquals("quickdb", bind.getName());
        Assert.assertEquals(3000.50, bind.getSalary());
    }

    @Test
    public void testObtainWhere(){
        BindingObject bind = new BindingObject();

        bind.obtainWhere("name = 'quickdb'");
        Assert.assertEquals("quickdb", bind.getName());
        Assert.assertEquals(3000.50, bind.getSalary());
    }

    @Test
    public void testObtainString(){
        BindingObject bind = new BindingObject();

        bind.obtain("name = 'quickdb'");
        Assert.assertEquals("quickdb", bind.getName());
        Assert.assertEquals(3000.50, bind.getSalary());
    }

    @Test
    public void testObtainSelect(){
        BindingObject bind = new BindingObject();

        bind.obtainSelect("SELECT * FROM BindingObject WHERE name = 'quickdb'");
        Assert.assertEquals("quickdb", bind.getName());
        Assert.assertEquals(3000.50, bind.getSalary());
    }

    @Test
    public void testDelete(){
        BindingObject bind = new BindingObject();
        bind.setBirth(new java.sql.Date(104, 4, 20));
        bind.setName("quickdb2");
        bind.setSalary(3000.50);

        int index = bind.saveGetIndex();
        Assert.assertTrue( (index > 0) );
        Assert.assertTrue(bind.obtainWhere("id = "+index));

        Assert.assertTrue(bind.delete());
    }

    @Test
    public void testModify(){
        BindingObject bind = new BindingObject();
        bind.setBirth(new java.sql.Date(104, 4, 20));
        bind.setName("quickdb3");
        bind.setSalary(3000.50);

        bind.save();
        Assert.assertTrue(bind.obtainWhere("name = 'quickdb3'"));
        bind.setName("quickdb4");

        Assert.assertTrue(bind.modify());

        BindingObject b = new BindingObject();
        Assert.assertTrue(b.obtainWhere("name = 'quickdb4'"));
        Assert.assertEquals("quickdb4", b.getName());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(BindingTest.class);
    }

}
