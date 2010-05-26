package cat.quickdb.operations;

import cat.quickdb.complexmodel.Many1;
import cat.quickdb.complexmodel.Many2;
import cat.quickdb.complexmodel.Parent;
import cat.quickdb.complexmodel.Reference;
import cat.quickdb.complexmodel.Single;
import cat.quickdb.complexmodel.Son;
import org.junit.*;
import junit.framework.Assert;
import quickdb.db.AdminBase;
import cat.quickdb.model.Alter1;
import cat.quickdb.model.Alter2;
import cat.quickdb.tests.QuickDBTests;
import java.util.ArrayList;

public class ComplexOperationsTest {

    private AdminBase admin;

    @Before
    public void configure() {
        this.admin = AdminBase.initialize(QuickDBTests.db, QuickDBTests.host,
                QuickDBTests.port, QuickDBTests.instanceDB,
                QuickDBTests.user, QuickDBTests.pass);
        this.admin.setAutoCommit(true);
    }

    @Test
    public void testSingleObject(){
        Single single = new Single();
        single.setName("testing single");
        single.setNumber(345908);
        single.setDate(new java.sql.Date(new java.util.Date().getTime()));

        boolean value = admin.save(single);
        Assert.assertTrue(value);
    }

    @Test
    public void testQuerySystemSimpleCondition(){
        Reference ref = new Reference();
        ref.setValue("housemd");

        Parent parent = new Parent();
        parent.setDescription("this is a test for parent");
        parent.setReference(ref);

        boolean value = admin.save(parent);
        Assert.assertTrue(value);

        Parent p = new Parent();
        admin.obtain(p, "reference.value = 'housemd'");

        Assert.assertEquals(p.getReference().getValue(), "housemd");
        Assert.assertEquals(p.getDescription(), "this is a test for parent");
    }

    @Test
    public void testQuerySystemComplexCondition(){
        Reference ref = new Reference();
        ref.setValue("house md");

        Parent parent = new Parent();
        parent.setDescription("testing5");
        parent.setReference(ref);

        boolean value = admin.save(parent);
        Assert.assertTrue(value);

        Parent p = new Parent();
        admin.obtain(p, "reference.value = 'house md' && description = 'testing5'");

        Assert.assertEquals(p.getReference().getValue(), "house md");
        Assert.assertEquals(p.getDescription(), "testing5");
    }

    @Test
    public void testQuerySystemWithInheritance(){
        Reference ref = new Reference();
        ref.setValue("house");

        Son son = new Son();
        son.setData("data from son");
        son.setSonName("child");
        son.setDescription("parent description");
        son.setReference(ref);

        boolean value = admin.save(son);
        Assert.assertTrue(value);

        Son s = new Son();
        admin.obtain(s, "reference.value = 'house'");

        Assert.assertEquals(s.getReference().getValue(), "house");
        Assert.assertEquals(s.getDescription(), "parent description");
    }

    @Test
    public void testObtainJoin(){
        String sql = "SELECT Son.data, Parent.description, Reference.value " +
                "FROM Son " +
                "JOIN Parent ON Parent.id = Son.parent_id " +
                "JOIN Reference ON Reference.id = Parent.reference " +
                "WHERE Reference.value = 'house'";

        Object[] join = admin.obtainTable(sql, 3);
        for(int i = 0; i < join.length; i++){
            String[] s = (String[]) join[i];
            Assert.assertEquals("data from son", s[0]);
            Assert.assertEquals("parent description", s[1]);
            Assert.assertEquals("house", s[2]);
        }
    }

    @Test
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

        boolean value = admin.save(many);
        Assert.assertTrue(value);

        Many1 many1 = new Many1();

        value = admin.obtainWhere(many1, "description = 'description of principal many'");
        Assert.assertTrue(value);
    }

    @Test
    public void testAlterTable(){
        admin.executeQuery("DROP TABLE AlterTable");

        Alter1 alter1 = new Alter1();
        alter1.setName("alter table1");

        Assert.assertTrue(admin.save(alter1));

        Alter2 alter2 = new Alter2();
        alter2.setName("alter table2");
        alter2.setExtend(55.45);

        Assert.assertTrue(admin.save(alter2));
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(ComplexOperationsTest.class);
    }

}
