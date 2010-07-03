package quickdb.operations;

import quickdb.complexmodel.Many1;
import quickdb.complexmodel.Many2;
import quickdb.complexmodel.Parent;
import quickdb.complexmodel.Reference;
import quickdb.complexmodel.Single;
import quickdb.complexmodel.Son;
import quickdb.db.AdminBase;
import quickdb.model.Alter1;
import quickdb.model.Alter2;
import java.util.ArrayList;

public class ComplexOperationsTest {

    private AdminBase admin;

    public ComplexOperationsTest(AdminBase admin){
        this.admin = admin;
        System.out.println("-------------------------");
        System.out.println("ComplexOperationsTest");
        System.out.println("testAlterTable");
        this.testAlterTable();
        System.out.println("testManyToMany");
        this.testManyToMany();
        System.out.println("testObtainJoin");
        this.testObtainJoin();
        System.out.println("testQuerySystemComplexCondition");
        this.testQuerySystemComplexCondition();
        System.out.println("testQuerySystemSimpleCondition");
        this.testQuerySystemSimpleCondition();
        System.out.println("testQuerySystemWithInheritance");
        this.testQuerySystemWithInheritance();
        System.out.println("testSingleObject");
        this.testSingleObject();
    }

    public void testSingleObject(){
        Single single = new Single();
        single.setName("testing single");
        single.setNumber(345908);
        single.setDate(new java.sql.Date(new java.util.Date().getTime()));

        boolean value = admin.save(single);
        System.out.println(value);
    }

    public void testQuerySystemSimpleCondition(){
        Reference ref = new Reference();
        ref.setValue("housemd");

        Parent parent = new Parent();
        parent.setDescription("this is a test for parent");
        parent.setReference(ref);

        boolean value = admin.save(parent);
        System.out.println(value);

        Parent p = new Parent();
        admin.obtain(p, "reference.value = 'housemd'");

        System.out.println(p.getReference().getValue().equalsIgnoreCase("housemd"));
        System.out.println(p.getDescription().equalsIgnoreCase("this is a test for parent"));
    }

    public void testQuerySystemComplexCondition(){
        Reference ref = new Reference();
        ref.setValue("house md");

        Parent parent = new Parent();
        parent.setDescription("testing5");
        parent.setReference(ref);

        boolean value = admin.save(parent);
        System.out.println(value);

        Parent p = new Parent();
        admin.obtain(p, "reference.value = 'house md' && description = 'testing5'");

        System.out.println(p.getReference().getValue().equalsIgnoreCase("house md"));
        System.out.println(p.getDescription().equalsIgnoreCase("testing5"));
    }

    public void testQuerySystemWithInheritance(){
        Reference ref = new Reference();
        ref.setValue("house");

        Son son = new Son();
        son.setData("data from son");
        son.setSonName("child");
        son.setDescription("parent description");
        son.setReference(ref);

        boolean value = admin.save(son);
        System.out.println(value);

        Son s = new Son();
        admin.obtain(s, "reference.value = 'house'");

        System.out.println(s.getReference().getValue().equalsIgnoreCase("house"));
        System.out.println(s.getDescription().equalsIgnoreCase("parent description"));
    }

    public void testObtainJoin(){
        Reference ref = new Reference();
        ref.setValue("house");

        Son son = new Son();
        son.setData("data from son");
        son.setSonName("child");
        son.setDescription("parent description");
        son.setReference(ref);

        boolean value = admin.save(son);
        System.out.println(value);
        
        String sql = "SELECT Son.data, Parent.description, Reference.value " +
                "FROM Son " +
                "JOIN Parent ON Parent.id = Son.parent_id " +
                "JOIN Reference ON Reference.id = Parent.reference " +
                "WHERE Reference.value = 'house'";

        Object[] join = admin.obtainTable(sql, 3);
        for(int i = 0; i < join.length; i++){
            String[] s = (String[]) join[i];
            System.out.println("data from son".equalsIgnoreCase(s[0]));
            System.out.println("parent description".equalsIgnoreCase(s[1]));
            System.out.println("house".equalsIgnoreCase(s[2]));
        }
    }

    public void testManyToMany(){
        Many2 m2a = new Many2();
        m2a.setName("many2 - a");
        Many1 m1 = new Many1();
        m1.setDescription("description1 m2a");
        Many1 m2 = new Many1();
        m2.setDescription("description2 m2a");
        ArrayList<Many1> manys1 = new ArrayList<Many1>();
        manys1.add(m1);
        manys1.add(m2);
        m2a.setMany1(manys1);

        Many2 m2b = new Many2();
        m2b.setName("many2 - b");
        Many1 m3 = new Many1();
        m3.setDescription("description3 m2b");
        Many1 m4 = new Many1();
        m4.setDescription("description4 m2b");
        ArrayList<Many1> manys2 = new ArrayList<Many1>();
        manys2.add(m3);
        manys2.add(m4);
        m2b.setMany1(manys2);

        Many1 many = new Many1();
        many.setDescription("description of principal many");
        ArrayList<Many2> m = new ArrayList<Many2>();
        m.add(m2a);
        m.add(m2b);
        many.setMany2(m);

        System.out.println(admin.save(many));

        Many1 many1 = new Many1();
        admin.obtainWhere(many1, "description = 'description of principal many'");
        System.out.println("description4 m2b".equalsIgnoreCase(((Many1)((Many2)many1.getMany2().get(1)).getMany1().get(1)).getDescription()));
    }

    public void testAlterTable(){
        admin.executeQuery("DROP TABLE AlterTable");

        Alter1 alter1 = new Alter1();
        alter1.setName("alter table1");

        System.out.println(admin.save(alter1));

        Alter2 alter2 = new Alter2();
        alter2.setName("alter table2");
        alter2.setExtend(55.45);

        System.out.println(admin.save(alter2));
    }

}
