package quickdb.operations;

import quickdb.db.AdminBase;
import quickdb.model.Address;
import quickdb.model.District;
import quickdb.model.Dog;
import quickdb.model.Person;
import quickdb.model.Prueba;
import quickdb.model.Book;
import quickdb.model.Page;
import quickdb.model.Primitive;
import quickdb.model.Prueba2;
import quickdb.model.PruebaChild;
import quickdb.model.Pruebas;
import quickdb.model.Race;
import quickdb.model.SuperPrueba;
import java.util.ArrayList;

/**
 * This class is for Functional Tests Only.
 * For performance, stress test, etc. Create a New Class
 * @author gato
 */
public class OperationsTest {

    private AdminBase admin;

    public OperationsTest(AdminBase admin){
        this.admin = admin;
        System.out.println("------------------------");
        System.out.println("OperationsTest");
        System.out.println("testCreateTableComposed");
        this.testCreateTableComposed();
        System.out.println("testCreateTableSimple");
        this.testCreateTableSimple();
        System.out.println("testInheritanceWithAnnotation");
        this.testInheritanceWithAnnotation();
        System.out.println("testMany2ManyFunction");
        this.testMany2ManyFunction();
        System.out.println("testObjectsWithCollectionIntuitive");
        this.testObjectsWithCollectionIntuitive();
        System.out.println("testObtainAll");
        this.testObtainAll();
        System.out.println("testPrimitives");
        this.testPrimitives();
        System.out.println("testSaveCollectionComposed");
        this.testSaveCollectionComposed();
        System.out.println("testSaveDeleteObjectSimpleIntuitive");
        this.testSaveDeleteObjectSimpleIntuitive();
        System.out.println("testSaveGetIndexComposed");
        this.testSaveGetIndexComposed();
        System.out.println("testSaveGetIndexSimple");
        this.testSaveGetIndexSimple();
        System.out.println("testSaveModifyObjectComposedIntuitive");
        this.testSaveModifyObjectComposedIntuitive();
        System.out.println("testSaveObjectComposed");
        this.testSaveObjectComposed();
        System.out.println("testSaveObjectComposedCollectionIntuitive");
        this.testSaveObjectComposedCollectionIntuitive();
        System.out.println("testSaveObjectComposedIntuitive");
        this.testSaveObjectComposedIntuitive();
        System.out.println("testSaveObjectSimpleIntuitive");
        this.testSaveObjectSimpleIntuitive();
        System.out.println("testSaveObtainModifyCollectionSimple");
        this.testSaveObtainModifyCollectionSimple();
        System.out.println("testSaveObtainModifyCollectionSimpleIntuitive");
        this.testSaveObtainModifyCollectionSimpleIntuitive();
        System.out.println("testSaveObtainModifyCompound");
        this.testSaveObtainModifyCompound();
        System.out.println("testSaveObtainModifyCompoundCollection");
        this.testSaveObtainModifyCompoundCollection();
        System.out.println("testSaveObtainModifyDeleteComposed");
        this.testSaveObtainModifyDeleteComposed();
        System.out.println("testSaveObtainModifyDeleteSimple");
        this.testSaveObtainModifyDeleteSimple();
        System.out.println("testSaveSimpleObject");
        this.testSaveSimpleObject();
    }

    public void testSaveSimpleObject() {
        Person person = new Person("LeeLoo", 1);
        boolean value = admin.save(person);

        System.out.println(value);
    }

    public void testSaveGetIndexSimple() {
        Person person = new Person("LeeLoo", 1);
        int value = admin.saveGetIndex(person);

        System.out.println((value > 0));
    }

    public void testSaveObtainModifyDeleteSimple() {
        Person person = new Person("Man", 20);
        boolean value = admin.save(person);
        System.out.println(value);

        Person per = new Person();
        admin.obtainWhere(per, "age=20");
        System.out.println("Man".equalsIgnoreCase(per.getPersonName()));

        per.setPersonName("Dr. House");
        value = admin.modify(per);
        System.out.println(value);

        value = admin.delete(per);
        System.out.println(value);
    }

    public void testCreateTableSimple() {
        Prueba prueba = new Prueba();
        prueba.setName("test");
        boolean value = admin.save(prueba);

        System.out.println(value);
    }

    public void testSaveObtainModifyCollectionSimple() {
        ArrayList array = new ArrayList();
        array.add(new Person("person1", 30));
        array.add(new Person("person2", 30));
        array.add(new Person("person3", 30));
        admin.saveAll(array);

        array = admin.obtainAll(Person.class, "age=30");
        int number = 1;
        for(Object obj : array){
            System.out.println(30 == ((Person) obj).getPersonAge());
            ((Person) obj).setPersonName("CRAZY NAME "+ number++);
        }

        ArrayList<Integer> results2 = admin.modifyAll(array);
        System.out.println( (results2.size() == 0) );
    }

    public void testSaveObjectComposed() {
        Address a = new Address();
        District d = new District("district1");
        a.setStreet("false street 123");
        a.setIdDistrict(d);
        boolean value = admin.save(a);

        System.out.println(value);
    }

    public void testSaveCollectionComposed() {
        ArrayList array = new ArrayList();
        Address a2 = new Address();
        District d2 = new District("district2");
        a2.setStreet("address 2");
        a2.setIdDistrict(d2);
        array.add(a2);

        Address a3 = new Address();
        District d3 = new District("district3");
        a3.setStreet("address 3");
        a3.setIdDistrict(d3);
        array.add(a3);

        admin.setCollection(true);
        ArrayList list = admin.saveAll(array);
        admin.setCollection(false);

        System.out.println(2 == list.size());
    }

    public void testSaveGetIndexComposed() {
        Address a = new Address();
        District d = new District("districtComposed1");
        a.setStreet("street 123");
        a.setIdDistrict(d);
        int value = admin.saveGetIndex(a);

        System.out.println((value > 0));
    }

    public void testSaveObtainModifyDeleteComposed() {
        Address a = new Address();
        District d = new District("testing district");
        a.setStreet("composed 555");
        boolean value = admin.save(a);
        System.out.println(value);

        Address ad = new Address();
        admin.obtainWhere(ad, "street='composed 555'");
        System.out.println("composed 555".equalsIgnoreCase(ad.getStreet()));

        ad.setStreet("object 123");
        value = admin.modify(ad);
        System.out.println(value);

        value = admin.delete(ad);
        System.out.println(value);
    }

    public void testCreateTableComposed() {
        SuperPrueba sup = new SuperPrueba();
        sup.setName("superPrueba");

        Prueba prueba = new Prueba();
        prueba.setName("test Prueba Composed");
        sup.setPrueba(prueba);
        boolean value = admin.save(sup);

        System.out.println(value);
    }

    public void testSaveObjectComposedIntuitive() {
        Dog dog = new Dog();
        Race race = new Race();
        race.setName("collie");
        dog.setColor("black");
        dog.setName("sasha");
        dog.setIdRace(race);

        boolean value = admin.save(dog);

        System.out.println(value);
    }

    public void testSaveModifyObjectComposedIntuitive() {
        Dog dog = new Dog();
        Race race = new Race();
        race.setName("collie to modify");
        dog.setColor("black to modify");
        dog.setName("sasha to modify");
        dog.setIdRace(race);

        boolean value = admin.save(dog);
        System.out.println(value);

        Dog d = new Dog();
        value = admin.obtainWhere(d, "name like '%modify'");
        System.out.println(value);

        Race r2 = new Race("new race modified");
        d.setIdRace(r2);
        d.setName("collie to modified");
        value = admin.modify(d);
        System.out.println(value);
    }

    public void testSaveObjectComposedCollectionIntuitive() {
        ArrayList array = new ArrayList();
        Dog dog = new Dog();
        Race race = new Race();
        race.setName("race collection 1");
        dog.setName("dog name collection 1");
        dog.setIdRace(race);
        array.add(dog);

        Dog dog2 = new Dog();
        Race race2 = new Race();
        race2.setName("race collection 2");
        dog2.setName("dog name collection 2");
        dog2.setIdRace(race2);
        array.add(dog2);

        admin.setCollection(true);
        ArrayList list = admin.saveAll(array);
        admin.setCollection(false);

        System.out.println(2 == list.size());
    }

    public void testSaveObjectSimpleIntuitive() {
        Race race = new Race();
        race.setName("Race Individual test");

        boolean value = admin.save(race);

        System.out.println(value);
    }

    public void testSaveDeleteObjectSimpleIntuitive() {
        Race race = new Race();
        race.setName("Race to delete");

        boolean value = admin.save(race);
        System.out.println(value);

        Race race2 = new Race();
        value = admin.delete(race2);
        System.out.println(value);
    }

    public void testSaveObtainModifyCollectionSimpleIntuitive() {
        ArrayList array = new ArrayList();
        array.add(new Race("race1-intuitive"));
        array.add(new Race("race2-intuitive"));
        array.add(new Race("race3-intuitive"));
        admin.saveAll(array);

        array = admin.obtainAll(Race.class, "name like '%intuitive'");
        int cant = array.size();
        System.out.println(cant>1);

        int number = 1;
        for(Object obj : array){
            ((Race) obj).setName("ANOTHER-INTUITIVE "+ number++);
        }

        ArrayList<Integer> results2 = admin.modifyAll(array);
        System.out.println( (results2.size() == 0) );
    }

    public void testMany2ManyFunction(){
        ArrayList pages = new ArrayList();
        pages.add(new Page(2));
        pages.add(new Page(5));
        pages.add(new Page(7));
        Book book = new Book();
        book.setIsbn(234553);
        book.setName("foundation");
        book.setPage(pages);

        boolean value = admin.save(book);
        System.out.println(value);

        Book b = new Book();
        b.getPage().add(new Page());
        value = admin.obtainWhere(b, "name='foundation'");
        System.out.println(value);

        System.out.println(5 == ((Page)b.getPage().get(1)).getPageNumber());
    }

    public void testObtainAll(){
        ArrayList array = new ArrayList();
        array.add(new Prueba("obtainAll1"));
        array.add(new Prueba("obtainAll2"));
        array.add(new Prueba("obtainAll3"));
        admin.saveAll(array);

        ArrayList results = admin.obtainAll(Prueba.class, "name like 'obtainAll%'");
        System.out.println((results.size() >= 3));
    }

    public void testInheritanceWithAnnotation(){
        PruebaChild p = new PruebaChild();
        p.setName("prueba herencia annotation");
        p.setNameParent("nombre padre annotation");
        p.setNumber(333);
        boolean value = admin.save(p);
        System.out.println(value);

        PruebaChild pr = new PruebaChild();
        value = admin.obtainWhere(pr, "name='prueba herencia annotation'");
        System.out.println(value);

        System.out.println(333 == pr.getNumber());
    }

    public void testSaveObtainModifyCompound(){
        District d = new District("prueba district modify");
        Address a = new Address("modifyAddress", d);

        admin.save(a);

        Address w = new Address();
        admin.obtainWhere(w, "street='modifyAddress'");

        w.getIdDistrict().setName("prueba modificando");
        admin.modify(w);

        Address q = new Address();
        admin.obtainWhere(q, "street='modifyAddress'");
        System.out.println(q.getIdDistrict().getName().equalsIgnoreCase("prueba modificando"));
    }

    public void testObjectsWithCollectionIntuitive(){
        Pruebas p = new Pruebas();
        p.setDescripcion("diego");
        ArrayList<Prueba2> arrayP = new ArrayList<Prueba2>();
        Prueba2 p1 = new Prueba2();
        p1.setNombre("gato1");
        Prueba2 p2 = new Prueba2();
        p2.setNombre("gato2");
        arrayP.add(p1);
        arrayP.add(p2);

        p.setPrueba2(arrayP);

        System.out.println(admin.save(p));

        admin.obtainWhere(p, "descripcion='diego'");
        System.out.println(p.getDescripcion().equalsIgnoreCase("diego"));
        System.out.println(((Prueba2)p.getPrueba2().get(1)).getNombre().equalsIgnoreCase("gato2"));
    }

    public void testSaveObtainModifyCompoundCollection(){
        District d1 = new District("collectionModify1");
        Address a1 = new Address("collectionModify", d1);
        District d2 = new District("collectionModify2");
        Address a2 = new Address("collectionModify", d2);
        ArrayList array = new ArrayList();
        array.add(a1);
        array.add(a2);

        admin.saveAll(array);

        ArrayList array2 = admin.obtainAll(Address.class, "street='collectionModify'");

        int i = 0;
        for( Object o : array2 ){
            ((Address) o).getIdDistrict().setName("prueba modificando"+i);
            i++;
        }
        admin.modifyAll(array2);

        ArrayList array3 = admin.obtainAll(Address.class, "street='collectionModify'");

        i = 0;
        for( Object o : array2 ){
            System.out.println(((Address) o).getIdDistrict().getName().equalsIgnoreCase("prueba modificando"+i));
            i++;
        }
    }

    public void testPrimitives(){
        Primitive primitive = new Primitive();
        primitive.setDate(new java.sql.Date(110, 0, 1));
        primitive.setDoubleNumber(55.35d);
        primitive.setFloatNumber(33.4f);
        primitive.setIntNumber(5);
        primitive.setString("string");

        System.out.println(admin.save(primitive));
    }

}