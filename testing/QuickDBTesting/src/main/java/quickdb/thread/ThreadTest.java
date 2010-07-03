package quickdb.thread;

import quickdb.db.AdminBase;
import quickdb.db.AdminThread;
import quickdb.thread.model.ThreadObject;
import quickdb.thread.model.ThreadReference2;
import java.util.ArrayList;

public class ThreadTest {

    private AdminThread admin;

    public ThreadTest(AdminThread admin){
        this.admin = admin;
    }
    
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
        System.out.println(true); //TODO
    }

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
            System.out.println(false);
        }
    }

    public void testModify(){
        AdminBase a = AdminBase.initialize(AdminBase.DATABASE.MYSQL, "localhost",
                "3306", "testQuickDB", "root", "");
        
        ThreadObject t = new ThreadObject();
        t.setBirth(new java.sql.Date(204, 4, 20));
        t.setName("test");
        t.setSalary(4000.29);
        t.setAge(20);
        t.setAlive(true);

        System.out.println(a.save(t));

        ThreadObject t2 = new ThreadObject();
        a.obtain(t2, "name = 'test'");

        t.setName("test modify");
        try{
            admin.modify(t2);
        }catch(Exception e){
            System.out.println(false);
        }
    }

    public void testObtain(){
        AdminBase a = AdminBase.initialize(AdminBase.DATABASE.MYSQL, "localhost",
                "3306", "testQuickDB", "root", "");

        ThreadObject t = new ThreadObject();
        t.setBirth(new java.sql.Date(204, 4, 20));
        t.setName("test");
        t.setSalary(4000.29);
        t.setAge(20);
        t.setAlive(true);

        System.out.println(a.save(t));

        ThreadObject t2 = new ThreadObject();

        try{
            admin.obtain(t2, "name = 'test'");
        }catch(Exception e){
            System.out.println(false);
        }
    }
    
}
