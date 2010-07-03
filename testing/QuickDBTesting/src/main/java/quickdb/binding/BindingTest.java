package quickdb.binding;

import quickdb.binding.model.BindingObject;

public class BindingTest {

    public BindingTest(){
        System.out.println("-----------------");
        System.out.println("BindingTest");
        System.out.println("testDelete");
        this.testDelete();
        System.out.println("testModify");
        this.testModify();
        System.out.println("testObtain");
        this.testObtain();
        System.out.println("testObtainSelect");
        this.testObtainSelect();
        System.out.println("testObtainString");
        this.testObtainString();
        System.out.println("testObtainWhere");
        this.testObtainWhere();
        System.out.println("testSave");
        this.testSave();
        System.out.println("testSaveGetIndex");
        this.testSaveGetIndex();
    }
    
    public void testSave(){
        BindingObject bind = new BindingObject();
        bind.setBirth(new java.sql.Date(104, 4, 20));
        bind.setName("quickdb");
        bind.setSalary(3000.50);

        System.out.println(bind.save());
    }

    public void testSaveGetIndex(){
        BindingObject bind = new BindingObject();
        bind.setBirth(new java.sql.Date(104, 4, 20));
        bind.setName("quickdb");
        bind.setSalary(3000.50);

        System.out.println((bind.saveGetIndex() > 0));
    }

    public void testObtain(){
        BindingObject bind = new BindingObject();

        bind.obtain().If("name").equal("quickdb").find();
        System.out.println("quickdb".equalsIgnoreCase(bind.getName()));
        System.out.println(3000.50 == bind.getSalary());
    }

    public void testObtainWhere(){
        BindingObject bind = new BindingObject();

        bind.obtainWhere("name = 'quickdb'");
        System.out.println("quickdb".equalsIgnoreCase(bind.getName()));
        System.out.println(3000.50 == bind.getSalary());
    }

    public void testObtainString(){
        BindingObject bind = new BindingObject();

        bind.obtain("name = 'quickdb'");
        System.out.println("quickdb".equalsIgnoreCase(bind.getName()));
        System.out.println(3000.50 == bind.getSalary());
    }

    public void testObtainSelect(){
        BindingObject bind = new BindingObject();

        bind.obtainSelect("SELECT * FROM BindingObject WHERE name = 'quickdb'");
        System.out.println("quickdb".equalsIgnoreCase(bind.getName()));
        System.out.println(3000.50 == bind.getSalary());
    }

    public void testDelete(){
        BindingObject bind = new BindingObject();
        bind.setBirth(new java.sql.Date(104, 4, 20));
        bind.setName("quickdb2");
        bind.setSalary(3000.50);

        int index = bind.saveGetIndex();
        System.out.println( (index > 0) );
        System.out.println(bind.obtainWhere("id = "+index));

        System.out.println(bind.delete());
    }

    public void testModify(){
        BindingObject bind = new BindingObject();
        bind.setBirth(new java.sql.Date(104, 4, 20));
        bind.setName("quickdb3");
        bind.setSalary(3000.50);

        bind.save();
        System.out.println(bind.obtainWhere("name = 'quickdb3'"));
        bind.setName("quickdb4");

        System.out.println(bind.modify());

        BindingObject b = new BindingObject();
        System.out.println(b.obtainWhere("name = 'quickdb4'"));
        System.out.println("quickdb4".equalsIgnoreCase(b.getName()));
    }

}
