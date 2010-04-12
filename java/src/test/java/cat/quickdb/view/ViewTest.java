package cat.quickdb.view;

import cat.quickdb.db.AdminBase;
import cat.quickdb.query.Query;
import cat.quickdb.tests.QuickDBTests;
import cat.quickdb.view.model.*;
import java.util.ArrayList;
import org.junit.*;

public class ViewTest {

    private AdminBase admin;

    @Before
    public void configure() {
        this.admin = AdminBase.initialize(QuickDBTests.db, QuickDBTests.host,
                QuickDBTests.port, QuickDBTests.instanceDB,
                QuickDBTests.user, QuickDBTests.pass);

        if(!(admin.checkTableExist("ObjectViewTest1") &&
                admin.checkTableExist("ObjectViewTest2"))){
            admin.executeQuery("DROP TABLE ObjectViewTest1");
            admin.executeQuery("DROP TABLE ObjectViewTest2");

            ObjectViewTest1 o1 = new ObjectViewTest1();
            o1.setAccount("accountTest");
            o1.setName("View Test");

            ObjectViewTest2 o2 = new ObjectViewTest2();
            o2.setDescription("description from view");
            o2.setDate(new java.sql.Date(105, 4, 20));
            o1.setObj2(o2);

            admin.save(o1);
        }
    }

    @Test
    public void testViewObtain(){
        ViewObject view = new ViewObject();
        view.initializeAdminBase(admin);
        view.obtain();

        Assert.assertEquals("accountTest", view.getAccount());
        Assert.assertEquals("View Test", view.getName());
        Assert.assertEquals(4, view.getDateView().getMonth());
    }

    @Test
    public void testViewObtainString(){
        ViewObjectString view = new ViewObjectString();
        view.initializeAdminBase(admin);
        view.obtain();

        Assert.assertEquals("accountTest", view.getAccount());
        Assert.assertEquals("View Test", view.getName());
        Assert.assertEquals(4, view.getDateView().getMonth());
    }

    @Test
    public void testViewObtainWithoutRename(){
        ViewObjectWithoutRename view = new ViewObjectWithoutRename();
        view.initializeAdminBase(admin);
        view.obtain();

        Assert.assertEquals("accountTest", view.getAccount());
        Assert.assertEquals("View Test", view.getName());
        Assert.assertEquals(4, view.getDateView().getMonth());
    }

    @Test
    public void testViewDynamicObtain(){
        ObjectViewTest1 o1 = new ObjectViewTest1();
        o1.setAccount("accountTest");
        o1.setName("View Test Dynamic");

        ObjectViewTest2 o2 = new ObjectViewTest2();
        o2.setDescription("description dynamic");
        o2.setDate(new java.sql.Date(105, 4, 20));
        o1.setObj2(o2);

        admin.save(o1);

        ViewObject view = new ViewObject();
        view.initializeAdminBase(admin);
        view.dynamicQuery( ((Query)view.query()).and("name").match("Dynamic") );
        view.obtain();

        Assert.assertEquals("accountTest", view.getAccount());
        Assert.assertEquals("View Test Dynamic", view.getName());
        Assert.assertEquals("description dynamic", view.getDescription());
        Assert.assertEquals(4, view.getDateView().getMonth());
    }

    @Test
    public void testViewDynamicObtainString(){
        ObjectViewTest1 o1 = new ObjectViewTest1();
        o1.setAccount("accountTest");
        o1.setName("View Test Dynamic");

        ObjectViewTest2 o2 = new ObjectViewTest2();
        o2.setDescription("description dynamic");
        o2.setDate(new java.sql.Date(105, 4, 20));
        o1.setObj2(o2);

        admin.save(o1);

        ViewObjectString view = new ViewObjectString();
        view.initializeAdminBase(admin);
        view.dynamicQuery("AND name LIKE '%Dynamic%'");
        view.obtain();

        Assert.assertEquals("accountTest", view.getAccount());
        Assert.assertEquals("View Test Dynamic", view.getName());
        Assert.assertEquals("description dynamic", view.getDescription());
        Assert.assertEquals(4, view.getDateView().getMonth());
    }

    @Test
    public void testViewObtainAll(){
        ObjectViewTest1 o1 = new ObjectViewTest1();
        o1.setAccount("accountTest");
        o1.setName("View Test2");

        ObjectViewTest2 o2 = new ObjectViewTest2();
        o2.setDescription("description from view2");
        o2.setDate(new java.sql.Date(105, 4, 20));
        o1.setObj2(o2);

        admin.save(o1);

        ViewObject view = new ViewObject();
        view.initializeAdminBase(admin);
        ArrayList array = view.obtainAll();

        Assert.assertTrue( (array.size() > 1) );
        int size = array.size();
        for(int i = 0; i < size; i++){
            ViewObject v = (ViewObject) array.get(i);
            Assert.assertEquals("accountTest", v.getAccount());
        }
    }

    @Test
    public void testViewObtainAllString(){
        ObjectViewTest1 o1 = new ObjectViewTest1();
        o1.setAccount("accountTest");
        o1.setName("View Test2");

        ObjectViewTest2 o2 = new ObjectViewTest2();
        o2.setDescription("description from view2");
        o2.setDate(new java.sql.Date(105, 4, 20));
        o1.setObj2(o2);

        admin.save(o1);

        ViewObjectString view = new ViewObjectString();
        view.initializeAdminBase(admin);
        ArrayList array = view.obtainAll();

        Assert.assertTrue( (array.size() > 1) );
        int size = array.size();
        for(int i = 0; i < size; i++){
            ViewObjectString v = (ViewObjectString) array.get(i);
            Assert.assertEquals("accountTest", v.getAccount());
        }
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(ViewTest.class);
    }

}
