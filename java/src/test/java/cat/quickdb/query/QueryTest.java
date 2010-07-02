package cat.quickdb.query;

import quickdb.db.AdminBase;
import cat.quickdb.query.model.AnotherClass;
import cat.quickdb.query.model.AnotherParent;
import cat.quickdb.query.model.CompleteQuery;
import cat.quickdb.query.model.ObjectSubquery;
import cat.quickdb.query.model.ObjectSummary;
import cat.quickdb.query.model.QueryCollection;
import cat.quickdb.query.model.QueryWithCollection;
import cat.quickdb.query.model.QueryWithSubquery;
import cat.quickdb.query.model.ReferenceQuery;
import cat.quickdb.query.model.UserQuery;
import cat.quickdb.tests.QuickDBTests;
import java.sql.Date;
import java.util.ArrayList;
import org.junit.*;

public class QueryTest {

    private AdminBase admin;

    @Before
    public void configure() {
        this.admin = AdminBase.initialize(QuickDBTests.db, QuickDBTests.host,
                QuickDBTests.port, QuickDBTests.instanceDB,
                QuickDBTests.user, QuickDBTests.pass);

        if( !(this.admin.checkTableExist("UserQuery") &&
                this.admin.checkTableExist("UserParent") &&
                this.admin.checkTableExist("AnotherClass") &&
                this.admin.checkTableExist("AnotherParent") &&
                this.admin.checkTableExist("ReferenceQuery") &&
                this.admin.checkTableExist("ReferenceParent") &&
                this.admin.checkTableExist("CompleteQuery")) ){
            this.admin.executeQuery("DROP TABLE UserQuery");
            this.admin.executeQuery("DROP TABLE UserParent");
            this.admin.executeQuery("DROP TABLE AnotherClass");
            this.admin.executeQuery("DROP TABLE AnotherParent");
            this.admin.executeQuery("DROP TABLE ReferenceQuery");
            this.admin.executeQuery("DROP TABLE ReferenceParent");
            this.admin.executeQuery("DROP TABLE CompleteQuery");

            UserQuery user = new UserQuery();
            user.setDescription("parent description");
            user.setName("son name");

            ReferenceQuery reference = new ReferenceQuery();
            reference.setValue("son value");
            reference.setValueParent("value parent");
            user.setReference(reference);

            admin.save(user);

            UserQuery user2 = new UserQuery();
            user2.setDescription("parent description2");
            user2.setName("son name2");

            ReferenceQuery reference2 = new ReferenceQuery();
            reference2.setValue("son value2");
            reference2.setValueParent("value parent2");
            user2.setReference(reference2);

            admin.save(user2);

            AnotherClass another = new AnotherClass();
            another.setMount(55.35);
            another.setProperty("son name");
            another.setDescription("parent description");
            admin.save(another);

            CompleteQuery query = new CompleteQuery();
            query.setName("diego sarmentero");
            query.setSalary(3500.50);
            query.setAge(20);
            query.setBirth(new java.sql.Date(85, 9, 2));
            query.setCond(true);

            admin.save(query);

            CompleteQuery query2 = new CompleteQuery();
            query2.setName("cat");
            query2.setAge(24);
            query2.setSalary(1500.50);
            query2.setBirth(new java.sql.Date(104, 4, 20));
            query2.setCond(false);

            admin.save(query2);
        }
    }

    @Test
    public void testSimpleObtain(){
        //single case: atribute from class
        UserQuery user = new UserQuery();
        admin.obtain(user).If("name").equal("son name").find();

        Assert.assertEquals("son name", user.getName());
    }

    @Test
    public void testSimpleInheritanceObtain(){
        UserQuery user = new UserQuery();
        admin.obtain(user).If("description").equal("parent description2").find();

        Assert.assertEquals("son name2", user.getName());
    }

    @Test
    public void testWithReferenceObtain(){
        UserQuery user = new UserQuery();
        admin.obtain(user).If("value", ReferenceQuery.class).equal("son value").find();

        Assert.assertEquals("parent description", user.getDescription());
    }

    @Test
    public void testWithReferenceInheritanceObtain(){
        UserQuery user = new UserQuery();
        admin.obtain(user).If("valueParent", ReferenceQuery.class).equal("value Parent").find();

        Assert.assertEquals("son value", user.getReference().getValue());
    }

    @Test
    public void testRelatedWithOtherClassObtain(){
        UserQuery user = new UserQuery();
        admin.obtain(user).If("property", AnotherClass.class).equal("son name").
                and("name").equal("property", AnotherClass.class).find();

        Assert.assertEquals("son value", user.getReference().getValue());

        UserQuery user3 = new UserQuery();
        admin.obtain(user3).If("name").equal("property", AnotherClass.class).
                and("property", AnotherClass.class).equal("son name").find();
        Assert.assertEquals("son value", user3.getReference().getValue());
    }

    @Test
    public void testRelatedWithOtherClassInheritanceObtain(){
        UserQuery user = new UserQuery();
        admin.obtain(user).If("description").equal("description", AnotherParent.class).
                and("description", AnotherParent.class).equal("parent description").find();

        Assert.assertEquals("son value", user.getReference().getValue());
    }

    @Test
    public void testMatchOrNotEqual(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).If("name").
                match("diego sarmentero").or("cond").notEqual(true).findAll();

        Assert.assertEquals(2, array.size());
    }

    @Test
    public void testBetweenAndLower(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).If("birth").inRange("1980-01-01", "2010-12-31").
                and("salary").lower(2000).findAll();

        Assert.assertEquals(1, array.size());
    }

    @Test
    public void testInOrMatch(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).If("age").in(22, 23, 24, 25).
                or("name").match("sarmentero").findAll();

        Assert.assertEquals(2, array.size());
    }

    @Test
    public void testIsNotNullAndGreater(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).If("name").isNotNull().
                and("salary").equalORgreater(2000).findAll();

        Assert.assertEquals(1, array.size());
    }

    @Test
    public void testSort(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).sort(true, "salary").findAll();

        Assert.assertEquals("cat", ((CompleteQuery)array.get(0)).getName());
    }

    @Test
    public void testGroupHaving(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).If("age").greater(10).
                group("salary").ifGroup("salary").greater(2000).findAll();

        Assert.assertEquals(1, array.size());
    }

    @Test
    public void testDateMonthEqual(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).If("birth").date().month().equal(5).findAll();

        Assert.assertEquals(1, array.size());
        Assert.assertEquals("cat", ((CompleteQuery)array.get(0)).getName());
    }

    @Test
    public void testDateDayLower(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).If("birth").date().
                day().lower(21).findAll();

        Assert.assertEquals(2, array.size());
    }

    @Test
    public void testDateDifferenceWith(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).If("birth").date().
                differenceWith("20040522").equal(2).findAll();

        Assert.assertEquals(1, array.size());

        java.sql.Date date = new Date(104, 4, 22);
        array = admin.obtain(query).If("birth").date().
                differenceWith(date.toString()).equal(2).findAll();

        Assert.assertEquals(1, array.size());
    }

    @Test
    public void testQueryWithCollection(){
        QueryWithCollection with = new QueryWithCollection();
        with.setDescription("description");
        ArrayList<QueryCollection> qu = new ArrayList<QueryCollection>();
        qu.add(new QueryCollection(11));
        qu.add(new QueryCollection(22));
        qu.add(new QueryCollection(33));
        with.setQueryCollection(qu);
        Assert.assertTrue(admin.save(with));

        QueryWithCollection with2 = new QueryWithCollection();
        with2.setDescription("description2");
        ArrayList<QueryCollection> qu2 = new ArrayList<QueryCollection>();
        qu2.add(new QueryCollection(44));
        qu2.add(new QueryCollection(55));
        qu2.add(new QueryCollection(66));
        with2.setQueryCollection(qu2);
        Assert.assertTrue(admin.save(with2));

        QueryWithCollection with3 = new QueryWithCollection();
        admin.obtain(with3).If("value", QueryCollection.class, "queryCollection").equal(22).find();

        Assert.assertEquals("description", with3.getDescription());
        Assert.assertEquals(33, with3.getQueryCollection().get(2).getValue());
    }

    @Test
    public void testSubQuery(){
        QueryWithSubquery sub = new QueryWithSubquery();
        sub.setName("subQuery1");
        sub.setSubQuery(new ObjectSubquery("name1"));
        Assert.assertTrue(admin.save(sub));

        QueryWithSubquery sub2 = new QueryWithSubquery();
        sub2.setName("subQuery2");
        sub2.setSubQuery(new ObjectSubquery("subQuery2"));
        Assert.assertTrue(admin.save(sub2));

        QueryWithSubquery sub3 = new QueryWithSubquery();
        sub3.setName("subQuery3");
        sub3.setSubQuery(new ObjectSubquery("name3"));
        Assert.assertTrue(admin.save(sub3));

        QueryWithSubquery sub4 = new QueryWithSubquery();
        admin.obtain(sub4).If().For("value", ObjectSubquery.class).closeFor().in("name", QueryWithSubquery.class).find();

        Assert.assertEquals("subQuery2", sub4.getName());
        Assert.assertEquals("subQuery2", sub4.getSubQuery().getValue());

        QueryWithSubquery sub5 = new QueryWithSubquery();
        admin.obtain(sub5).If("id").greater(0).and().For("value", ObjectSubquery.class).
                closeFor().in("name", QueryWithSubquery.class).find();

        Assert.assertEquals("subQuery2", sub5.getName());
        Assert.assertEquals("subQuery2", sub5.getSubQuery().getValue());
    }

    @Test
    public void testSummaryAttributes(){
        if(this.admin.checkTableExist("ObjectSummary")){
            this.admin.executeQuery("DROP TABLE ObjectSummary");
        }

        ObjectSummary summ = new ObjectSummary();
        summ.setValue(23);
        summ.setSalary(100.5);
        Assert.assertTrue(admin.save(summ));

        ObjectSummary summ2 = new ObjectSummary();
        summ2.setValue(5);
        summ2.setSalary(500.5);
        Assert.assertTrue(admin.save(summ2));

        ObjectSummary summ3 = new ObjectSummary();
        summ3.setValue(34);
        summ3.setSalary(200);
        Assert.assertTrue(admin.save(summ3));

        ObjectSummary summ4 = new ObjectSummary();
        admin.obtain(summ4).If("value").greater(2).find();
        Assert.assertEquals(62.0, summ4.getSummmary());
        Assert.assertEquals(267.0, summ4.getPromSalary());
        Assert.assertEquals(34.0, summ4.getMaxValue());
        Assert.assertEquals(5.0, summ4.getMinValue());
        Assert.assertEquals(3.0, summ4.getCountValue());

        ObjectSummary summ5 = new ObjectSummary();
        admin.obtain(summ5, "value > 10");
        Assert.assertEquals(57.0, summ5.getSummmary());
        Assert.assertEquals(150.25, summ5.getPromSalary());
        Assert.assertEquals(34.0, summ5.getMaxValue());
        Assert.assertEquals(23.0, summ5.getMinValue());
        Assert.assertEquals(2.0, summ5.getCountValue());
    }

    @Test
    public void testQueryIfGroupWithSummary(){
        if(this.admin.checkTableExist("ObjectSummary")){
            Assert.assertTrue(this.admin.executeQuery("DROP TABLE ObjectSummary"));
        }

        ObjectSummary summ = new ObjectSummary();
        summ.setValue(23);
        summ.setSalary(100.5);
        Assert.assertTrue(admin.save(summ));

        ObjectSummary summ2 = new ObjectSummary();
        summ2.setValue(5);
        summ2.setSalary(500.5);
        Assert.assertTrue(admin.save(summ2));

        ObjectSummary summ3 = new ObjectSummary();
        summ3.setValue(34);
        summ3.setSalary(200);
        Assert.assertTrue(admin.save(summ3));

        ObjectSummary summ4 = new ObjectSummary();
        summ4.setValue(23);
        summ4.setSalary(10);
        Assert.assertTrue(admin.save(summ4));

        ObjectSummary summ5 = new ObjectSummary();
        summ5.setValue(5);
        summ5.setSalary(55);
        Assert.assertTrue(admin.save(summ5));

        ObjectSummary summ6 = new ObjectSummary();
        summ6.setValue(34);
        summ6.setSalary(800);
        Assert.assertTrue(admin.save(summ6));

        ObjectSummary summ7 = new ObjectSummary();
        admin.obtain(summ7).If("id").greater(0).group("value").ifGroup("+salary").greater(900).find();

        Assert.assertEquals(34.0, summ7.getMaxValue());
        Assert.assertEquals(34.0, summ7.getMinValue());
        Assert.assertEquals(2.0, summ7.getCountValue());
        Assert.assertEquals(500.0, summ7.getPromSalary());
        Assert.assertEquals(68.0, summ7.getSummmary());
    }

}
