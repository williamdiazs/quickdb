package cat.quickdb.thread;

import quickdb.db.AdminBase;
import quickdb.db.AdminThread;
import cat.quickdb.tests.QuickDBTests;
import cat.quickdb.thread.model.ThreadObject;
import cat.quickdb.thread.model.ThreadReference2;
import java.util.ArrayList;
import org.junit.*;

public class ThreadTest {

    private AdminThread admin;

    @Before
    public void configure() {
        this.admin = new AdminThread(QuickDBTests.db, QuickDBTests.host,
                QuickDBTests.port, QuickDBTests.instanceDB,
                QuickDBTests.user, QuickDBTests.pass);
    }
    
    @Test
    public void testSaveAll(){
        ArrayList array = new ArrayList();
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));
        array.add(new ThreadReference2("prueba"));

        this.admin.saveAll(array);
        Assert.assertTrue(true); //TODO
    }

    @Test
    public void testSave(){
        ThreadObject t = new ThreadObject();
        t.setBirth(new java.sql.Date(204, 4, 20));
        t.setName("test");
        t.setSalary(4000.29);
        t.setAge(20);
        t.setAlive(true);

        try{
            admin.save(t);
        }catch(Exception e){
            Assert.fail();
        }
    }

    @Test
    public void testModify(){
        AdminBase a = AdminBase.initialize(AdminBase.DATABASE.MYSQL, "localhost",
                "3306", "testQuickDB", "root", "");
        
        ThreadObject t = new ThreadObject();
        t.setBirth(new java.sql.Date(204, 4, 20));
        t.setName("test");
        t.setSalary(4000.29);
        t.setAge(20);
        t.setAlive(true);

        Assert.assertTrue(a.save(t));

        ThreadObject t2 = new ThreadObject();
        a.obtain(t2, "name = 'test'");

        t.setName("test modify");
        try{
            admin.modify(t2);
        }catch(Exception e){
            Assert.fail();
        }
    }

    @Test
    public void testObtain(){
        AdminBase a = AdminBase.initialize(AdminBase.DATABASE.MYSQL, "localhost",
                "3306", "testQuickDB", "root", "");

        ThreadObject t = new ThreadObject();
        t.setBirth(new java.sql.Date(204, 4, 20));
        t.setName("test");
        t.setSalary(4000.29);
        t.setAge(20);
        t.setAlive(true);

        Assert.assertTrue(a.save(t));

        ThreadObject t2 = new ThreadObject();

        try{
            admin.obtain(t2, "name = 'test'");
        }catch(Exception e){
            Assert.fail();
        }
    }
    
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(ThreadTest.class);
    }

}
