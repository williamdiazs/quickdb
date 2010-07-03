package quickdb.complexDataStructure;

import quickdb.complexDataStructure.model.AddressComplex;
import quickdb.complexDataStructure.model.Buy;
import quickdb.complexDataStructure.model.Customer;
import quickdb.complexDataStructure.model.IndividualObject1;
import quickdb.complexDataStructure.model.IndividualObject2;
import quickdb.complexDataStructure.model.MultipleCollections;
import quickdb.complexDataStructure.model.MultipleCollectionsPrimitive;
import quickdb.complexDataStructure.model.ParentCollection;
import quickdb.complexDataStructure.model.SamePrimitiveCollection;
import quickdb.complexDataStructure.model.SonData;
import quickdb.complexDataStructure.model.SonReference;
import java.util.ArrayList;
import quickdb.db.AdminBase;

/**
 *
 * @author Diego Sarmentero
 */
public class DataStructureTest {

    private AdminBase admin;

    public DataStructureTest(AdminBase admin){
        this.admin = admin;
        System.out.println("---------------------");
        System.out.println("DataStructureTest");
        System.out.println("completeExampleTest");
        this.completeExampleTest();
        System.out.println("complexDataStructureMainTest");
        this.complexDataStructureMainTest();
        System.out.println("multipleCollections");
        this.multipleCollections();
        System.out.println("multipleCollectionsPrimitive");
        this.multipleCollectionsPrimitive();
        System.out.println("samePrimitiveCollectionTest");
        this.samePrimitiveCollectionTest();
    }

    public void multipleCollections(){
        IndividualObject1 ind1a = new IndividualObject1();
        ind1a.setDescription("individual object 1a");
        IndividualObject1 ind1b = new IndividualObject1();
        ind1b.setDescription("individual object 1b");
        IndividualObject1 ind1c = new IndividualObject1();
        ind1c.setDescription("individual object 1c");
        ArrayList individual1 = new ArrayList();
        individual1.add(ind1a);
        individual1.add(ind1b);
        individual1.add(ind1c);

        IndividualObject2 ind2a = new IndividualObject2();
        ind2a.setDescription("individual object 2a");
        IndividualObject2 ind2b = new IndividualObject2();
        ind2b.setDescription("individual object 2b");
        IndividualObject2 ind2c = new IndividualObject2();
        ind2c.setDescription("individual object 2c");
        ArrayList individual2 = new ArrayList();
        individual2.add(ind2a);
        individual2.add(ind2b);
        individual2.add(ind2c);

        MultipleCollections mul = new MultipleCollections();
        mul.setName("test collections");
        mul.setIndividualObject1(individual1);
        mul.setIndividualObject2(individual2);

        System.out.println(admin.save(mul));

        MultipleCollections mul2 = new MultipleCollections();
        admin.obtain(mul2).If("name").equal("test collections").find();

        System.out.println(mul2.getIndividualObject1().size() > 2);
        System.out.println(mul2.getIndividualObject2().size() > 2);
    }

    public void multipleCollectionsPrimitive(){
        MultipleCollectionsPrimitive prim = new MultipleCollectionsPrimitive();

        ArrayList numbers = new ArrayList();
        numbers.add(345453);
        numbers.add(67657567);
        numbers.add(2343);
        numbers.add(8714753);

        ArrayList words = new ArrayList();
        words.add("word1");
        words.add("word2");
        words.add("word3");

        prim.setName("multiple collections primitive");
        prim.setNumbers(numbers);
        prim.setWords(words);

        System.out.println(admin.save(prim));

        MultipleCollectionsPrimitive prim2 = new MultipleCollectionsPrimitive();
        admin.obtain(prim2).If("name").equal("multiple collections primitive").find();

        System.out.println(prim2.getNumbers().size() > 3);
        System.out.println(prim2.getWords().size() > 2);
    }

    public void completeExampleTest(){
        ArrayList codes1 = new ArrayList();
        codes1.add(4357673);
        codes1.add(5457334);
        ArrayList codes2 = new ArrayList();
        codes2.add(4567);
        codes2.add(3345);

        Buy buy1 = new Buy("cat food", 16.5, codes1);
        Buy buy2 = new Buy("citric juice", 7, codes2);
        Buy buy3 = new Buy("space ship", 599.99, null);
        ArrayList buys = new ArrayList();
        buys.add(buy1);
        buys.add(buy2);
        buys.add(buy3);

        AddressComplex address = new AddressComplex("unnamed street", 123);

        Customer customer = new Customer("Diego", "Sarmentero",
                "diego.sarmentero@gmail.com", address, buys);

        System.out.println(admin.save(customer));

        Customer customer2 = new Customer();
        admin.obtain(customer2).If("name").equal("Diego").find();

        System.out.println(customer2.getEmail().equals("diego.sarmentero@gmail.com"));
        System.out.println(customer2.getBuy().size() > 2);
        System.out.println(((Buy)customer2.getBuy().get(0)).getCodes().size() > 1);
    }

    public void complexDataStructureMainTest(){
        SonData son = new SonData();
        son.setName("son name");
        son.setSalary(5000.50);
        son.setParentValue("parent value");

        SonReference sonRef = new SonReference();
        sonRef.setDescription("description of son reference");
        sonRef.setValueReferenceParent("value from son reference parent");
        son.setReference(sonRef);

        ArrayList<Integer> phones = new ArrayList<Integer>();
        phones.add(2345543);
        phones.add(342356);
        phones.add(7564332);
        phones.add(546542168);
        son.setPhones(phones);

        ParentCollection p1 = new ParentCollection();
        p1.setNames("name1");
        ParentCollection p2 = new ParentCollection();
        p2.setNames("name2");
        ParentCollection p3 = new ParentCollection();
        p3.setNames("name3");
        ArrayList pcoll = new ArrayList();
        pcoll.add(p1);
        pcoll.add(p2);
        pcoll.add(p3);
        son.setParentCollection(pcoll);

        System.out.println(admin.save(son));

        SonData son2 = new SonData();
        admin.obtain(son2).If("name").equal("son name").find();

        System.out.println(son2.getName().equals("son name"));
        System.out.println(son2.getParentValue().equalsIgnoreCase("parent value"));
        System.out.println(son2.getParentCollection().size() == 3);
        System.out.println(son2.getPhones().size() == 4);
        System.out.println(son2.getReference().getValueReferenceParent().equals("value from son reference parent"));
    }

    public void samePrimitiveCollectionTest(){
        SamePrimitiveCollection same = new SamePrimitiveCollection();
        same.setName("same name");

        ArrayList array1 = new ArrayList();
        array1.add(45345);
        array1.add(64556);
        array1.add(23112);

        ArrayList array2 = new ArrayList();
        array2.add(12);
        array2.add(54);
        array2.add(68);

        same.setNumbers1(array1);
        same.setNumbers2(array2);

        System.out.println(admin.save(same));

        SamePrimitiveCollection same2 = new SamePrimitiveCollection();
        admin.obtain(same2).If("name").equal("same name").find();

        System.out.println(same2.getNumbers1().size() == 3);
        System.out.println(same2.getNumbers2().size() == 3);
        System.out.println(same2.getNumbers1().get(0) == 45345);
        System.out.println(same2.getNumbers2().get(1) == 54);
    }

}
