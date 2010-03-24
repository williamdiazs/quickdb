package cat.quickdb.operations;

import cat.quickdb.db.AdminBase;
import cat.quickdb.model.Address;
import cat.quickdb.model.District;
import cat.quickdb.model.Dog;
import cat.quickdb.model.Person;
import cat.quickdb.model.Prueba;
import cat.quickdb.model.Book;
import cat.quickdb.model.Page;
import cat.quickdb.model.Primitive;
import cat.quickdb.model.Prueba2;
import cat.quickdb.model.PruebaChild;
import cat.quickdb.model.Pruebas;
import cat.quickdb.model.Race;
import cat.quickdb.model.SuperPrueba;
import java.util.ArrayList;
import org.junit.*;
import junit.framework.Assert;

/**
 * This class is for Functional Tests Only.
 * For performance, stress test, etc. Create a New Class
 * @author gato
 */
public class OperationsTest {

    AdminBase admin;

    @Before
    public void configure() {
        this.admin = AdminBase.initialize(AdminBase.DATABASE.MYSQL, "localhost",
                "3306", "testQuickDB", "root", "");
        this.admin.setAutoCommit(true);
    }

    @Test
    public void testSaveSimpleObject() {
        Person person = new Person("LeeLoo", 1);
        boolean value = admin.save(person);

        Assert.assertTrue(value);
    }

    @Test
    public void testSaveGetIndexSimple() {
        Person person = new Person("LeeLoo", 1);
        int value = admin.saveGetIndex(person);

        Assert.assertTrue((value > 0));
    }

    @Test
    public void testSaveObtainModifyDeleteSimple() {
        Person person = new Person("Man", 20);
        boolean value = admin.save(person);
        Assert.assertTrue(value);

        Person per = new Person();
        admin.obtainWhere(per, "age=20");
        Assert.assertEquals("Man", per.getPersonName());

        per.setPersonName("Dr. House");
        value = admin.modify(per);
        Assert.assertTrue(value);

        value = admin.delete(per);
        Assert.assertTrue(value);
    }

    @Test
    public void testCreateTableSimple() {
        Prueba prueba = new Prueba();
        prueba.setName("test");
        boolean value = admin.save(prueba);

        Assert.assertTrue(value);
    }

    @Test
    public void testSaveObtainModifyCollectionSimple() {
        ArrayList array = new ArrayList();
        array.add(new Person("person1", 30));
        array.add(new Person("person2", 30));
        array.add(new Person("person3", 30));
        admin.saveAll(array);

        Person person = new Person();
        array = admin.obtainAll(person, "age=30");
        int number = 1;
        for(Object obj : array){
            Assert.assertEquals(30, ((Person) obj).getPersonAge());
            ((Person) obj).setPersonName("CRAZY NAME "+ number++);
        }

        ArrayList<Integer> results2 = admin.modifyAll(array);
        Assert.assertTrue( (results2.size() == 0) );
    }

    @Test
    public void testSaveObjectComposed() {
        Address a = new Address();
        District d = new District("district1");
        a.setStreet("false street 123");
        a.setIdDistrict(d);
        boolean value = admin.save(a);

        Assert.assertTrue(value);
    }

    @Test
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

        Assert.assertEquals(2, list.size());
    }

    @Test
    public void testSaveGetIndexComposed() {
        Address a = new Address();
        District d = new District("districtComposed1");
        a.setStreet("street 123");
        a.setIdDistrict(d);
        int value = admin.saveGetIndex(a);

        Assert.assertTrue((value > 0));
    }

    @Test
    public void testSaveObtainModifyDeleteComposed() {
        Address a = new Address();
        District d = new District("testing district");
        a.setStreet("composed 555");
        boolean value = admin.save(a);
        Assert.assertTrue(value);

        Address ad = new Address();
        admin.obtainWhere(ad, "street='composed 555'");
        Assert.assertEquals("composed 555", ad.getStreet());

        ad.setStreet("object 123");
        value = admin.modify(ad);
        Assert.assertTrue(value);

        value = admin.delete(ad);
        Assert.assertTrue(value);
    }

    @Test
    public void testCreateTableComposed() {
        SuperPrueba sup = new SuperPrueba();
        sup.setName("superPrueba");

        Prueba prueba = new Prueba();
        prueba.setName("test Prueba Composed");
        sup.setPrueba(prueba);
        boolean value = admin.save(sup);

        Assert.assertTrue(value);
    }

    @Test
    public void testSaveObjectComposedIntuitive() {
        Dog dog = new Dog();
        Race race = new Race();
        race.setName("collie");
        dog.setColor("black");
        dog.setName("sasha");
        dog.setIdRace(race);

        boolean value = admin.save(dog);

        Assert.assertTrue(value);
    }

    @Test
    public void testSaveModifyObjectComposedIntuitive() {
        Dog dog = new Dog();
        Race race = new Race();
        race.setName("collie to modify");
        dog.setColor("black to modify");
        dog.setName("sasha to modify");
        dog.setIdRace(race);

        boolean value = admin.save(dog);
        Assert.assertTrue(value);

        Dog d = new Dog();
        value = admin.obtainWhere(d, "name like '%modify'");
        Assert.assertTrue(value);

        Race r2 = new Race("new race modified");
        d.setIdRace(r2);
        d.setName("collie to modified");
        value = admin.modify(d);
        Assert.assertTrue(value);
    }

    @Test
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

        Assert.assertEquals(2, list.size());
    }

    @Test
    public void testSaveObjectSimpleIntuitive() {
        Race race = new Race();
        race.setName("Race Individual test");

        boolean value = admin.save(race);

        Assert.assertTrue(value);
    }

    @Test
    public void testSaveDeleteObjectSimpleIntuitive() {
        Race race = new Race();
        race.setName("Race to delete");

        boolean value = admin.save(race);
        Assert.assertTrue(value);

        Race race2 = new Race();
        value = admin.delete(race2);
        Assert.assertTrue(value);
    }

    @Test
    public void testSaveObtainModifyCollectionSimpleIntuitive() {
        ArrayList array = new ArrayList();
        array.add(new Race("race1-intuitive"));
        array.add(new Race("race2-intuitive"));
        array.add(new Race("race3-intuitive"));
        admin.saveAll(array);

        Race race = new Race();
        array = admin.obtainAll(race, "name like '%intuitive'");
        int cant = array.size();
        Assert.assertTrue(cant>1);

        int number = 1;
        for(Object obj : array){
            ((Race) obj).setName("ANOTHER-INTUITIVE "+ number++);
        }

        ArrayList<Integer> results2 = admin.modifyAll(array);
        Assert.assertTrue( (results2.size() == 0) );
    }

    @Test
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
        Assert.assertTrue(value);

        Book b = new Book();
        b.getPage().add(new Page());
        value = admin.obtainWhere(b, "name='foundation'");
        Assert.assertTrue(value);

        Assert.assertEquals(5, ((Page)b.getPage().get(1)).getPageNumber());
    }

    @Test
    public void testObtainAll(){
        ArrayList array = new ArrayList();
        array.add(new Prueba("obtainAll1"));
        array.add(new Prueba("obtainAll2"));
        array.add(new Prueba("obtainAll3"));
        admin.saveAll(array);

        Prueba p = new Prueba();
        ArrayList results = admin.obtainAll(p, "name like 'obtainAll%'");
        Assert.assertTrue((results.size() >= 3));
    }

    @Test
    public void testInheritanceWithAnnotation(){
        PruebaChild p = new PruebaChild();
        p.setName("prueba herencia annotation");
        p.setNameParent("nombre padre annotation");
        p.setNumber(333);
        boolean value = admin.save(p);
        Assert.assertTrue(value);

        PruebaChild pr = new PruebaChild();
        value = admin.obtainWhere(pr, "name='prueba herencia annotation'");
        Assert.assertTrue(value);

        Assert.assertEquals(333, pr.getNumber());
    }

    @Test
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
        Assert.assertEquals(q.getIdDistrict().getName(), "prueba modificando");
    }

    @Test
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

        Assert.assertTrue(admin.save(p));

        admin.obtainWhere(p, "descripcion='diego'");
        Assert.assertEquals(p.getDescripcion(), "diego");
        Assert.assertEquals(((Prueba2)p.getPrueba2().get(1)).getNombre(), "gato2");
    }

    @Test
    public void testSaveObtainModifyCompoundCollection(){
        District d1 = new District("collectionModify1");
        Address a1 = new Address("collectionModify", d1);
        District d2 = new District("collectionModify2");
        Address a2 = new Address("collectionModify", d2);
        ArrayList array = new ArrayList();
        array.add(a1);
        array.add(a2);

        admin.saveAll(array);

        ArrayList array2 = admin.obtainAll(a1, "street='collectionModify'");

        int i = 0;
        for( Object o : array2 ){
            ((Address) o).getIdDistrict().setName("prueba modificando"+i);
            i++;
        }
        admin.modifyAll(array2);

        ArrayList array3 = admin.obtainAll(a1, "street='collectionModify'");

        i = 0;
        for( Object o : array2 ){
            Assert.assertEquals(((Address) o).getIdDistrict().getName(), "prueba modificando"+i);
            i++;
        }
    }

    @Test
    public void testPrimitives(){
        Primitive primitive = new Primitive();
        primitive.setDate(new java.sql.Date(110, 0, 1));
        primitive.setDoubleNumber(55.35d);
        primitive.setFloatNumber(33.4f);
        primitive.setIntNumber(5);
        primitive.setString("string");

        Assert.assertTrue(admin.save(primitive));
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(OperationsTest.class);
    }

}