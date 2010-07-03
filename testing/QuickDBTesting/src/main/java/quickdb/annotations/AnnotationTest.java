package quickdb.annotations;

import quickdb.annotations.model.CollectionAnnotation;
import quickdb.annotations.model.ModelAnnotation;
import quickdb.db.AdminBase;
import java.util.ArrayList;

public class AnnotationTest {

    private AdminBase admin;

    public AnnotationTest(AdminBase admin){
        this.admin = admin;
        System.out.println("----------------------------");
        System.out.println("AnnotationTest");
        System.out.println("testSave");
        this.testSave();
        System.out.println("testSaveGetIndex");
        this.testSaveGetIndex();
        System.out.println("testObtain");
        this.testObtain();
        System.out.println("testModify");
        this.testModify();
        System.out.println("testDelete");
        this.testDelete();
    }

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

        System.out.println(admin.save(model));
    }

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

        System.out.println((admin.saveGetIndex(model) > 0));
    }

    public void testObtain(){
        ModelAnnotation model = new ModelAnnotation();

        System.out.println(admin.obtain(model).If("name").equal("model name").find());

        System.out.println(3000.5 == model.getterSalary());
        System.out.println(3 == model.getArray().size());
    }

    public void testModify(){
        ModelAnnotation model = new ModelAnnotation();

        System.out.println(admin.obtain(model).If("name").equal("model name").find());

        model.setAge(15);
        System.out.println(!admin.modify(model));

        model.setAge(25);
        System.out.println(admin.modify(model));
    }

    public void testDelete(){
        ModelAnnotation model = new ModelAnnotation();

        System.out.println(admin.obtain(model).If("name").equal("model name").find());

        System.out.println(3000.5 == model.getterSalary());
        System.out.println(admin.delete(model));
    }

}
