package cat.quickdb.query;

import quickdb.db.AdminBase;
import cat.quickdb.query.model.AnotherClass;
import cat.quickdb.query.model.AnotherParent;
import cat.quickdb.query.model.CompleteQuery;
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

}
