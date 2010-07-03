package quickdb.query;

import quickdb.db.AdminBase;
import quickdb.query.model.AnotherClass;
import quickdb.query.model.AnotherParent;
import quickdb.query.model.CompleteQuery;
import quickdb.query.model.ObjectSubquery;
import quickdb.query.model.ObjectSummary;
import quickdb.query.model.QueryCollection;
import quickdb.query.model.QueryWithCollection;
import quickdb.query.model.QueryWithSubquery;
import quickdb.query.model.ReferenceQuery;
import quickdb.query.model.UserQuery;
import java.sql.Date;
import java.util.ArrayList;

public class QueryTest {

    private AdminBase admin;

    public QueryTest(AdminBase admin){
        this.admin = admin;
        System.out.println("--------------------------");
        System.out.println("QueryTest");
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

            this.admin.save(user);

            UserQuery user2 = new UserQuery();
            user2.setDescription("parent description2");
            user2.setName("son name2");

            ReferenceQuery reference2 = new ReferenceQuery();
            reference2.setValue("son value2");
            reference2.setValueParent("value parent2");
            user2.setReference(reference2);

            this.admin.save(user2);

            AnotherClass another = new AnotherClass();
            another.setMount(55.35);
            another.setProperty("son name");
            another.setDescription("parent description");
            this.admin.save(another);

            CompleteQuery query = new CompleteQuery();
            query.setName("diego sarmentero");
            query.setSalary(3500.50);
            query.setAge(20);
            query.setBirth(new java.sql.Date(85, 9, 2));
            query.setCond(true);

            this.admin.save(query);

            CompleteQuery query2 = new CompleteQuery();
            query2.setName("cat");
            query2.setAge(24);
            query2.setSalary(1500.50);
            query2.setBirth(new java.sql.Date(104, 4, 20));
            query2.setCond(false);

            this.admin.save(query2);
        }
        
        System.out.println("testBetweenAndLower");
        this.testBetweenAndLower();
        System.out.println("testDateDayLower");
        this.testDateDayLower();
        System.out.println("testDateDifferenceWith");
        this.testDateDifferenceWith();
        System.out.println("testDateMonthEqual");
        this.testDateMonthEqual();
        System.out.println("testGroupHaving");
        this.testGroupHaving();
        System.out.println("testInOrMatch");
        this.testInOrMatch();
        System.out.println("testIsNotNullAndGreater");
        this.testIsNotNullAndGreater();
        System.out.println("testMatchOrNotEqual");
        this.testMatchOrNotEqual();
        System.out.println("testQueryIfGroupWithSummary");
        this.testQueryIfGroupWithSummary();
        System.out.println("testQueryWithCollection");
        this.testQueryWithCollection();
        System.out.println("testRelatedWithOtherClassInheritanceObtain");
        this.testRelatedWithOtherClassInheritanceObtain();
        System.out.println("testRelatedWithOtherClassObtain");
        this.testRelatedWithOtherClassObtain();
        System.out.println("testSimpleInheritanceObtain");
        this.testSimpleInheritanceObtain();
        System.out.println("testSimpleObtain");
        this.testSimpleObtain();
        System.out.println("testSort");
        this.testSort();
        System.out.println("testSubQuery");
        this.testSubQuery();
        System.out.println("testSummaryAttributes");
        this.testSummaryAttributes();
        System.out.println("testWithReferenceInheritanceObtain");
        this.testWithReferenceInheritanceObtain();
        System.out.println("testWithReferenceObtain");
        this.testWithReferenceObtain();
    }

    public void testSimpleObtain(){
        //single case: atribute from class
        UserQuery user = new UserQuery();
        admin.obtain(user).If("name").equal("son name").find();

        System.out.println("son name".equalsIgnoreCase(user.getName()));
    }

    public void testSimpleInheritanceObtain(){
        UserQuery user = new UserQuery();
        admin.obtain(user).If("description").equal("parent description2").find();

        System.out.println("son name2".equalsIgnoreCase(user.getName()));
    }

    public void testWithReferenceObtain(){
        UserQuery user = new UserQuery();
        admin.obtain(user).If("value", ReferenceQuery.class).equal("son value").find();

        System.out.println("parent description".equalsIgnoreCase(user.getDescription()));
    }

    public void testWithReferenceInheritanceObtain(){
        UserQuery user = new UserQuery();
        admin.obtain(user).If("valueParent", ReferenceQuery.class).equal("value Parent").find();

        System.out.println("son value".equalsIgnoreCase(user.getReference().getValue()));
    }

    public void testRelatedWithOtherClassObtain(){
        UserQuery user = new UserQuery();
        admin.obtain(user).If("property", AnotherClass.class).equal("son name").
                and("name").equal("property", AnotherClass.class).find();

        System.out.println("son value".equalsIgnoreCase(user.getReference().getValue()));

        UserQuery user3 = new UserQuery();
        admin.obtain(user3).If("name").equal("property", AnotherClass.class).
                and("property", AnotherClass.class).equal("son name").find();
        System.out.println("son value".equalsIgnoreCase(user3.getReference().getValue()));
    }

    public void testRelatedWithOtherClassInheritanceObtain(){
        UserQuery user = new UserQuery();
        admin.obtain(user).If("description").equal("description", AnotherParent.class).
                and("description", AnotherParent.class).equal("parent description").find();

        System.out.println("son value".equalsIgnoreCase(user.getReference().getValue()));
    }

    public void testMatchOrNotEqual(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).If("name").
                match("diego sarmentero").or("cond").notEqual(true).findAll();

        System.out.println(2 == array.size());
    }

    public void testBetweenAndLower(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).If("birth").inRange("1980-01-01", "2010-12-31").
                and("salary").lower(2000).findAll();

        System.out.println(1 == array.size());
    }

    public void testInOrMatch(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).If("age").in(22, 23, 24, 25).
                or("name").match("sarmentero").findAll();

        System.out.println(2 == array.size());
    }

    public void testIsNotNullAndGreater(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).If("name").isNotNull().
                and("salary").equalORgreater(2000).findAll();

        System.out.println(1 == array.size());
    }

    public void testSort(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).sort(true, "salary").findAll();

        System.out.println("cat".equalsIgnoreCase(((CompleteQuery)array.get(0)).getName()));
    }

    public void testGroupHaving(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).If("age").greater(10).
                group("salary").ifGroup("salary").greater(2000).findAll();

        System.out.println(1 == array.size());
    }

    public void testDateMonthEqual(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).If("birth").date().month().equal(5).findAll();

        System.out.println(1 == array.size());
        System.out.println("cat".equalsIgnoreCase(((CompleteQuery)array.get(0)).getName()));
    }

    public void testDateDayLower(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).If("birth").date().
                day().lower(21).findAll();

        System.out.println(2 == array.size());
    }

    public void testDateDifferenceWith(){
        CompleteQuery query = new CompleteQuery();

        ArrayList array = admin.obtain(query).If("birth").date().
                differenceWith("20040522").equal(2).findAll();

        System.out.println(1 == array.size());

        java.sql.Date date = new Date(104, 4, 22);
        array = admin.obtain(query).If("birth").date().
                differenceWith(date.toString()).equal(2).findAll();

        System.out.println(1 == array.size());
    }

    public void testQueryWithCollection(){
        QueryWithCollection with = new QueryWithCollection();
        with.setDescription("description");
        ArrayList<QueryCollection> qu = new ArrayList<QueryCollection>();
        qu.add(new QueryCollection(11));
        qu.add(new QueryCollection(22));
        qu.add(new QueryCollection(33));
        with.setQueryCollection(qu);
        System.out.println(admin.save(with));

        QueryWithCollection with2 = new QueryWithCollection();
        with2.setDescription("description2");
        ArrayList<QueryCollection> qu2 = new ArrayList<QueryCollection>();
        qu2.add(new QueryCollection(44));
        qu2.add(new QueryCollection(55));
        qu2.add(new QueryCollection(66));
        with2.setQueryCollection(qu2);
        System.out.println(admin.save(with2));

        QueryWithCollection with3 = new QueryWithCollection();
        admin.obtain(with3).If("value", QueryCollection.class, "queryCollection").equal(22).find();

        System.out.println("description".equalsIgnoreCase(with3.getDescription()));
        System.out.println(33 == with3.getQueryCollection().get(2).getValue());
    }

    public void testSubQuery(){
        QueryWithSubquery sub = new QueryWithSubquery();
        sub.setName("subQuery1");
        sub.setSubQuery(new ObjectSubquery("name1"));
        System.out.println(admin.save(sub));

        QueryWithSubquery sub2 = new QueryWithSubquery();
        sub2.setName("subQuery2");
        sub2.setSubQuery(new ObjectSubquery("subQuery2"));
        System.out.println(admin.save(sub2));

        QueryWithSubquery sub3 = new QueryWithSubquery();
        sub3.setName("subQuery3");
        sub3.setSubQuery(new ObjectSubquery("name3"));
        System.out.println(admin.save(sub3));

        QueryWithSubquery sub4 = new QueryWithSubquery();
        admin.obtain(sub4).If().For("value", ObjectSubquery.class).closeFor().in("name", QueryWithSubquery.class).find();

        System.out.println("subQuery2".equalsIgnoreCase(sub4.getName()));
        System.out.println("subQuery2".equalsIgnoreCase(sub4.getSubQuery().getValue()));

        QueryWithSubquery sub5 = new QueryWithSubquery();
        admin.obtain(sub5).If("id").greater(0).and().For("value", ObjectSubquery.class).
                closeFor().in("name", QueryWithSubquery.class).find();

        System.out.println("subQuery2".equalsIgnoreCase(sub5.getName()));
        System.out.println("subQuery2".equalsIgnoreCase(sub5.getSubQuery().getValue()));
    }

    public void testSummaryAttributes(){
        if(this.admin.checkTableExist("ObjectSummary")){
            this.admin.executeQuery("DROP TABLE ObjectSummary");
        }

        ObjectSummary summ = new ObjectSummary();
        summ.setValue(23);
        summ.setSalary(100.5);
        System.out.println(admin.save(summ));

        ObjectSummary summ2 = new ObjectSummary();
        summ2.setValue(5);
        summ2.setSalary(500.5);
        System.out.println(admin.save(summ2));

        ObjectSummary summ3 = new ObjectSummary();
        summ3.setValue(34);
        summ3.setSalary(200);
        System.out.println(admin.save(summ3));

        ObjectSummary summ4 = new ObjectSummary();
        admin.obtain(summ4).If("value").greater(2).find();
        System.out.println(62.0 == summ4.getSummmary());
        System.out.println(267.0 == summ4.getPromSalary());
        System.out.println(34.0 == summ4.getMaxValue());
        System.out.println(5.0 == summ4.getMinValue());
        System.out.println(3.0 == summ4.getCountValue());

        ObjectSummary summ5 = new ObjectSummary();
        admin.obtain(summ5, "value > 10");
        System.out.println(57.0 == summ5.getSummmary());
        System.out.println(150.25 == summ5.getPromSalary());
        System.out.println(34.0 == summ5.getMaxValue());
        System.out.println(23.0 == summ5.getMinValue());
        System.out.println(2.0 == summ5.getCountValue());
    }

    public void testQueryIfGroupWithSummary(){
        if(this.admin.checkTableExist("ObjectSummary")){
            System.out.println(this.admin.executeQuery("DROP TABLE ObjectSummary"));
        }

        ObjectSummary summ = new ObjectSummary();
        summ.setValue(23);
        summ.setSalary(100.5);
        System.out.println(admin.save(summ));

        ObjectSummary summ2 = new ObjectSummary();
        summ2.setValue(5);
        summ2.setSalary(500.5);
        System.out.println(admin.save(summ2));

        ObjectSummary summ3 = new ObjectSummary();
        summ3.setValue(34);
        summ3.setSalary(200);
        System.out.println(admin.save(summ3));

        ObjectSummary summ4 = new ObjectSummary();
        summ4.setValue(23);
        summ4.setSalary(10);
        System.out.println(admin.save(summ4));

        ObjectSummary summ5 = new ObjectSummary();
        summ5.setValue(5);
        summ5.setSalary(55);
        System.out.println(admin.save(summ5));

        ObjectSummary summ6 = new ObjectSummary();
        summ6.setValue(34);
        summ6.setSalary(800);
        System.out.println(admin.save(summ6));

        ObjectSummary summ7 = new ObjectSummary();
        admin.obtain(summ7).If("id").greater(0).group("value").ifGroup("+salary").greater(900).find();

        System.out.println(34.0 == summ7.getMaxValue());
        System.out.println(34.0 == summ7.getMinValue());
        System.out.println(2.0 == summ7.getCountValue());
        System.out.println(500.0 == summ7.getPromSalary());
        System.out.println(68.0 == summ7.getSummmary());
    }

}
