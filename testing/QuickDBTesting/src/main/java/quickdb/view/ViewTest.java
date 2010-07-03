package quickdb.view;

import quickdb.db.AdminBase;
import quickdb.query.Query;
import quickdb.view.model.*;
import java.util.ArrayList;

public class ViewTest {

    private AdminBase admin;

    public ViewTest(AdminBase admin) {
        this.admin = admin;
        System.out.println("-----------------------");
        System.out.println("ViewTest");

        if(!(this.admin.checkTableExist("ObjectViewTest1") &&
                this.admin.checkTableExist("ObjectViewTest2"))){
            this.admin.executeQuery("DROP TABLE ObjectViewTest1");
            this.admin.executeQuery("DROP TABLE ObjectViewTest2");

            ObjectViewTest1 o1 = new ObjectViewTest1();
            o1.setAccount("accountTest");
            o1.setName("View Test");

            ObjectViewTest2 o2 = new ObjectViewTest2();
            o2.setDescription("description from view");
            o2.setDate(new java.sql.Date(105, 4, 20));
            o1.setObj2(o2);

            this.admin.save(o1);
        }
        System.out.println("testViewDynamicObtain");
        this.testViewDynamicObtain();
        System.out.println("testViewDynamicObtainString");
        this.testViewDynamicObtainString();
        System.out.println("testViewObtain");
        this.testViewObtain();
        System.out.println("testViewObtainAll");
        this.testViewObtainAll();
        System.out.println("testViewObtainAllString");
        this.testViewObtainAllString();
        System.out.println("testViewObtainString");
        this.testViewObtainString();
        System.out.println("testViewObtainWithoutRename");
        this.testViewObtainWithoutRename();
    }

    public void testViewObtain(){
        ViewObject view = new ViewObject();
        view.initializeAdminBase(admin);
        view.obtain();

        System.out.println("accountTest".equalsIgnoreCase(view.getAccount()));
        System.out.println("View Test".equalsIgnoreCase(view.getName()));
        System.out.println(4 == view.getDateView().getMonth());
    }

    public void testViewObtainString(){
        ViewObjectString view = new ViewObjectString();
        view.initializeAdminBase(admin);
        view.obtain();

        System.out.println("accountTest".equalsIgnoreCase(view.getAccount()));
        System.out.println("View Test".equalsIgnoreCase(view.getName()));
        System.out.println(4 == view.getDateView().getMonth());
    }

    public void testViewObtainWithoutRename(){
        ViewObjectWithoutRename view = new ViewObjectWithoutRename();
        view.initializeAdminBase(admin);
        view.obtain();

        System.out.println("accountTest".equalsIgnoreCase(view.getAccount()));
        System.out.println("View Test".equalsIgnoreCase(view.getName()));
        System.out.println(4 == view.getDateView().getMonth());
    }

    public void testViewDynamicObtain(){
        ObjectViewTest1 o1 = new ObjectViewTest1();
        o1.setAccount("accountTest");
        o1.setName("View Test Dynamic");

        ObjectViewTest2 o2 = new ObjectViewTest2();
        o2.setDescription("description dynamic");
        o2.setDate(new java.sql.Date(105, 4, 20));
        o1.setObj2(o2);

        System.out.println(admin.save(o1));

        ViewObject view = new ViewObject();
        view.initializeAdminBase(admin);
        view.dynamicQuery( ((Query)view.query()).and("name").match("Dynamic") );
        view.obtain();

        System.out.println("accountTest".equalsIgnoreCase(view.getAccount()));
        System.out.println("View Test Dynamic".equalsIgnoreCase(view.getName()));
        System.out.println("description dynamic".equalsIgnoreCase(view.getDescription()));
        System.out.println(4 == view.getDateView().getMonth());
    }

    public void testViewDynamicObtainString(){
        ObjectViewTest1 o1 = new ObjectViewTest1();
        o1.setAccount("accountTest");
        o1.setName("View Test Dynamic");

        ObjectViewTest2 o2 = new ObjectViewTest2();
        o2.setDescription("description dynamic");
        o2.setDate(new java.sql.Date(105, 4, 20));
        o1.setObj2(o2);

        System.out.println(admin.save(o1));

        ViewObjectString view = new ViewObjectString();
        view.initializeAdminBase(admin);
        view.dynamicQuery("AND name LIKE '%Dynamic%'");
        view.obtain();

        System.out.println("accountTest".equalsIgnoreCase(view.getAccount()));
        System.out.println("View Test Dynamic".equalsIgnoreCase(view.getName()));
        System.out.println("description dynamic".equalsIgnoreCase(view.getDescription()));
        System.out.println(4 == view.getDateView().getMonth());
    }

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

        System.out.println( (array.size() > 0) );
        int size = array.size();
        for(int i = 0; i < size; i++){
            ViewObject v = (ViewObject) array.get(i);
            System.out.println("accountTest".equalsIgnoreCase(v.getAccount()));
        }
    }

    public void testViewObtainAllString(){
        ObjectViewTest1 o1 = new ObjectViewTest1();
        o1.setAccount("accountTest");
        o1.setName("View Test2");

        ObjectViewTest2 o2 = new ObjectViewTest2();
        o2.setDescription("description from view2");
        o2.setDate(new java.sql.Date(105, 4, 20));
        o1.setObj2(o2);

        System.out.println(admin.save(o1));

        ViewObjectString view = new ViewObjectString();
        view.initializeAdminBase(admin);
        ArrayList array = view.obtainAll();

        System.out.println( (array.size() > 0) );
        int size = array.size();
        for(int i = 0; i < size; i++){
            ViewObjectString v = (ViewObjectString) array.get(i);
            System.out.println("accountTest".equalsIgnoreCase(v.getAccount()));
        }
    }

}
