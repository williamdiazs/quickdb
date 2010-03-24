package cat.quickdb.annotations;

import cat.quickdb.annotations.model.CollectionAnnotation;
import cat.quickdb.annotations.model.ModelAnnotation;
import cat.quickdb.db.AdminBase;
import java.util.ArrayList;
import org.junit.*;

public class AnnotationTest {

    AdminBase admin;

    @Before
    public void configure() {
        this.admin = AdminBase.initialize(AdminBase.DATABASE.MYSQL, "localhost",
                "3306", "testQuickDB", "root", "");
    }

    @Test
    public void testSave(){
        ModelAnnotation model = new ModelAnnotation();

        model.setAge(20);
        model.setBirth(new java.sql.Date(104, 4, 20));
        model.setName("model name");
        model.setSalary(3000.5);
        model.setDescription("parent description");

        CollectionAnnotation c = new CollectionAnnotation();
        c.setItemName("item name");

        ArrayList ar = new ArrayList();
        ar.add(new CollectionAnnotation("item1"));
        ar.add(new CollectionAnnotation("item2"));
        ar.add(new CollectionAnnotation("item3"));

        model.setArray(ar);
        model.setCollec(c);

        Assert.assertTrue(admin.save(model));
    }

    @Test
    public void testSaveGetIndex(){
        ModelAnnotation model = new ModelAnnotation();

        model.setAge(20);
        model.setBirth(new java.sql.Date(104, 4, 20));
        model.setName("model name");
        model.setSalary(3000.5);
        model.setDescription("parent description");

        CollectionAnnotation c = new CollectionAnnotation();
        c.setItemName("item name");

        ArrayList ar = new ArrayList();
        ar.add(new CollectionAnnotation("item1"));
        ar.add(new CollectionAnnotation("item2"));
        ar.add(new CollectionAnnotation("item3"));

        model.setArray(ar);
        model.setCollec(c);

        Assert.assertTrue((admin.saveGetIndex(model) > 0));
    }

    @Test
    public void testObtain(){
        ModelAnnotation model = new ModelAnnotation();

        Assert.assertTrue(admin.obtain(model).where("name").equal("model name").find());

        Assert.assertEquals(3000.5, model.getterSalary());
        Assert.assertEquals(3, model.getArray().size());
    }

    @Test
    public void testModify(){
        ModelAnnotation model = new ModelAnnotation();

        Assert.assertTrue(admin.obtain(model).where("name").equal("model name").find());

        model.setAge(15);
        Assert.assertFalse(admin.modify(model));

        model.setAge(25);
        Assert.assertTrue(admin.modify(model));
    }

    @Test
    public void testDelete(){
        ModelAnnotation model = new ModelAnnotation();

        Assert.assertTrue(admin.obtain(model).where("name").equal("model name").find());

        Assert.assertEquals(3000.5, model.getterSalary());
        Assert.assertTrue(admin.delete(model));
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(AnnotationTest.class);
    }

}
