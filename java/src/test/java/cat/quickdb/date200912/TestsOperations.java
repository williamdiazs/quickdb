package cat.quickdb.date200912;

import cat.quickdb.date200912.model.Bind;
import cat.quickdb.date200912.model.Bind2;
import cat.quickdb.date200912.model.Collection1;
import cat.quickdb.date200912.model.Collection2;
import cat.quickdb.date200912.model.Delete1;
import cat.quickdb.date200912.model.Delete2;
import cat.quickdb.date200912.model.Name2;
import cat.quickdb.date200912.model.Names;
import cat.quickdb.date200912.model.Obj1;
import cat.quickdb.date200912.model.Obj2;
import cat.quickdb.date200912.model.Obj3;
import cat.quickdb.date200912.model.Obj4;
import cat.quickdb.date200912.model.Object1;
import cat.quickdb.date200912.model.Object2;
import cat.quickdb.date200912.model.Object3;
import cat.quickdb.date200912.model.Reference1;
import cat.quickdb.date200912.model.Reference2;
import cat.quickdb.date200912.model.Son1;
import cat.quickdb.date200912.model.Son2;
import cat.quickdb.db.AdminBase;
import cat.quickdb.tests.QuickDBTests;
import java.util.ArrayList;
import org.junit.*;
import junit.framework.Assert;

public class TestsOperations {

    private AdminBase admin;

    @Before
    public void configure() {
        this.admin = AdminBase.initialize(QuickDBTests.db, QuickDBTests.host,
                QuickDBTests.port, QuickDBTests.instanceDB,
                QuickDBTests.user, QuickDBTests.pass);
        AdminBase.initializeAdminBinding(QuickDBTests.db, QuickDBTests.host,
                QuickDBTests.port, QuickDBTests.instanceDB,
                QuickDBTests.user, QuickDBTests.pass);
    }

    @Test
    public void testBinding(){
        Bind b = new Bind();
        Bind2 b2 = new Bind2();

        b.setDescription("description");
        b2.setName("name binding2");

        Assert.assertTrue(b.save());
        Assert.assertTrue(b2.save());

        Bind bind = new Bind();
        admin.obtain(bind, "description = 'description'");
        Assert.assertEquals("description", bind.getDescription());
        Bind2 bind2 = new Bind2();
        admin.obtain(bind2, "name = 'name binding2'");
        Assert.assertEquals("name binding2", bind2.getName());
    }

    @Test
    public void testDeleteParent(){
        Son1 s = new Son1();
        s.setData("data che delete");
        s.setDescription("description of son delete");
        s.setSonName("son name delete");

        Assert.assertTrue(admin.save(s));

        Son1 son = new Son1();
        admin.obtain(son, "data = 'data che delete'");
        Assert.assertEquals("data che delete", son.getData());

        Assert.assertTrue(admin.delete(son));
    }

    @Test
    public void testQuerySystemWithInheritance(){
        Reference1 ref = new Reference1();
        ref.setValue("house");

        Son1 son = new Son1();
        son.setData("data from son");
        son.setSonName("child");
        son.setDescription("parent description");
        son.setReference1(ref);

        boolean value = admin.save(son);
        Assert.assertTrue(value);

        Son1 s = new Son1();
        Assert.assertTrue(admin.obtain(s, "reference1.value = 'house'"));
        Assert.assertEquals("house", s.getReference1().getValue());
    }

    @Test
    public void testNames(){
        Name2 test2 = new Name2();
        test2.setMyName("name test2-otro");

        Names t = new Names();
        t.setOtro("mi otro nombre");
        t.setPruebaIgnore("pruebaaaaaaaaa");
        t.setTesta(test2);

        Assert.assertTrue(admin.save(t));

        Names t2 = new Names();
        Assert.assertTrue(admin.obtain(t2, "otro = 'mi otro nombre'"));
        Assert.assertEquals("mi otro nombre", t2.getOtro());
        Assert.assertEquals(null, t2.getPruebaIgnore());
    }

    @Test
    public void testModifyCollection(){
        Collection1 test = new Collection1();
        test.setName("name");

        Collection2 t1 = new Collection2();
        t1.setDescription("description1");
        Collection2 t2 = new Collection2();
        t2.setDescription("description2");
        Collection2 t3 = new Collection2();
        t3.setDescription("description3");
        ArrayList array = new ArrayList();
        array.add(t1);
        array.add(t2);
        array.add(t3);

        test.setCollection2(array);

        Assert.assertTrue(admin.save(test));

        Assert.assertTrue(admin.obtain(test, "name = 'name'"));

        Collection2 t = new Collection2();
        t.setDescription("description added");
        test.getCollection2().add(t);

        Assert.assertTrue(admin.modify(test));

        Collection1 tt = new Collection1();
        Assert.assertTrue(admin.obtain(tt, "name = 'name'"));
        Assert.assertEquals("description added", 
                ((Collection2)tt.getCollection2().get(tt.getCollection2().size()-1)).getDescription());
        Assert.assertTrue(admin.delete(test));
    }

    @Test
    public void testInheritanceQuery(){
        Son2 son = new Son2();
        son.setDescriptionSon("description - son");
        son.setDescription2("description2");
        son.setDescription1("description1");
        Reference2 test = new Reference2();
        test.setValue("value test reference");
        son.setReferenceTest(test);

        Assert.assertTrue(admin.save(son));

        Assert.assertTrue(admin.obtain(son, "referenceTest.value = 'value test reference'"));
        Assert.assertEquals("description - son", son.getDescriptionSon());
    }

    @Test
    public void testLazyLoad(){
        Object3 obj3 = new Object3();
        obj3.setDescription("description");
        obj3.setName3("name3");

        Object2 obj2 = new Object2();
        obj2.setName2("name2");
        obj2.setObject3(obj3);

        Object1 obj = new Object1();
        obj.setName1("name1");
        obj.setObject2(obj2);

        Assert.assertTrue(admin.save(obj));

        Object1 o = new Object1();
        Assert.assertTrue(admin.lazyLoad(o, "name1 = 'name1'"));
        Assert.assertEquals(null, o.getObject2());
        Assert.assertTrue(admin.lazyLoad(o));
        Assert.assertEquals(null, o.getObject2().getObject3());
        Assert.assertTrue(admin.lazyLoad(o));

        Assert.assertTrue(admin.obtain(o, "id = '1'"));
        Assert.assertEquals("name1", o.getName1());
    }

    @Test
    public void testCollectionModify(){

        admin.executeQuery("DROP TABLE Delete1");
        admin.executeQuery("DROP TABLE Delete2");
        admin.executeQuery("DROP TABLE Delete1Delete2");
        admin.executeQuery("DROP TABLE Delete2Delete1");

        Delete2 d2a2 = new Delete2();
        d2a2.setName("delete2 - prueba1");
        Delete1 d1a2 = new Delete1();
        d1a2.setDescription("delete2-delete1-desc1");
        Delete1 d1b2 = new Delete1();
        d1b2.setDescription("delete2-delete1-desc2");
        ArrayList a1 = new ArrayList();
        a1.add(d1a2);
        a1.add(d1b2);
        d2a2.setDelete1(a1);

        Delete2 d2b2 = new Delete2();
        d2b2.setName("delete2 - prueba2");
        Delete1 d1c2 = new Delete1();
        d1c2.setDescription("delete2-delete1-desc3");
        Delete1 d1d2 = new Delete1();
        d1d2.setDescription("delete2-delete1-desc4");
        ArrayList a2 = new ArrayList();
        a2.add(d1c2);
        a2.add(d1d2);
        d2b2.setDelete1(a2);

        ArrayList array2 = new ArrayList();
        array2.add(d2a2);
        array2.add(d2b2);

        Delete1 d3 = new Delete1();
        d3.setDescription("description delete1");
        d3.setDelete2(array2);

        Assert.assertTrue(admin.save(d3));

        Assert.assertTrue(admin.obtain(d3, "description = 'description delete1'"));

        ((Delete1) d3.getDelete2().get(0).getDelete1().get(0)).setDescription("new description2");
        ((Delete1) d3.getDelete2().get(1).getDelete1().get(0)).setDescription("description modify2");
        d3.getDelete2().get(0).setName("name modify2");

        Delete2 d2q = new Delete2();
        d2q.setName("delete2 - prueba - added");
        d3.getDelete2().add(d2q);

        Assert.assertTrue(admin.modify(d3));
        Delete1 d4 = new Delete1();
        Assert.assertTrue(admin.obtain(d4, "description = 'description delete1'"));
        Assert.assertEquals("new description2",
                ((Delete1)d4.getDelete2().get(0).getDelete1().get(0)).getDescription());
    }

    @Test
    public void testCollectionDelete(){
        admin.executeQuery("DROP TABLE Delete1");
        admin.executeQuery("DROP TABLE Delete2");
        admin.executeQuery("DROP TABLE Delete1Delete2");
        admin.executeQuery("DROP TABLE Delete2Delete1");

        Delete2 d2a = new Delete2();
        d2a.setName("delete2 - prueba1");
        Delete2 d2b = new Delete2();
        d2b.setName("delete2 - prueba2");

        ArrayList array = new ArrayList();
        array.add(d2a);
        array.add(d2b);

        Delete1 d = new Delete1();
        d.setDescription("description delete1");
        d.setDelete2(array);

        Assert.assertTrue(admin.save(d));

        Delete1 d1 = new Delete1();
        Assert.assertTrue(admin.obtain(d1, "description = 'description delete1'"));

        Assert.assertTrue(admin.delete(d1));
        Assert.assertFalse(admin.obtain(d1, "description = 'description delete1'"));
    }

    @Test
    public void completeDelete(){
        admin.executeQuery("DROP TABLE Delete1");
        admin.executeQuery("DROP TABLE Delete2");
        admin.executeQuery("DROP TABLE Delete1Delete2");
        admin.executeQuery("DROP TABLE Delete2Delete1");
        
        Delete2 d2a = new Delete2();
        d2a.setName("delete2 - prueba1");
        Delete1 d1a = new Delete1();
        d1a.setDescription("delete2-delete1-desc1");
        Delete1 d1b = new Delete1();
        d1b.setDescription("delete2-delete1-desc2");
        ArrayList a1 = new ArrayList();
        a1.add(d1a);
        a1.add(d1b);
        d2a.setDelete1(a1);

        Delete2 d2b = new Delete2();
        d2b.setName("delete2 - prueba2");
        Delete1 d1c = new Delete1();
        d1c.setDescription("delete2-delete1-desc3");
        Delete1 d1d = new Delete1();
        d1d.setDescription("delete2-delete1-desc4");
        ArrayList a2 = new ArrayList();
        a2.add(d1c);
        a2.add(d1d);
        d2b.setDelete1(a2);

        ArrayList array = new ArrayList();
        array.add(d2a);
        array.add(d2b);

        Delete1 d = new Delete1();
        d.setDescription("description delete1");
        d.setDelete2(array);

        Assert.assertTrue(admin.save(d));

        Assert.assertTrue(admin.obtain(d, "description = 'description delete1'"));
        Assert.assertTrue(admin.delete(d));
    }

    @Test
    public void testAtomicBlock(){
        admin.executeQuery("DROP TABLE Obj1");
        admin.executeQuery("DROP TABLE Obj2");
        admin.executeQuery("DROP TABLE Obj3");
        admin.executeQuery("DROP TABLE Obj4");

        Obj1 o1 = new Obj1();
        o1.setName("diego");
        Obj2 o2 = new Obj2();
        o2.setDescription("description");
        Obj3 o3 = new Obj3();
        o3.setAddress("puey 600");

        Obj4 o4 = new Obj4();
        o4.setPrueba("this is a test");
        o1.setObj4(o4);

        admin.openAtomicBlock();
        admin.save(o1);
        admin.save(o2);
        admin.save(o3);
        admin.closeAtomicBlock();

        Obj1 oo1 = new Obj1();
        Obj2 oo2 = new Obj2();
        Obj3 oo3 = new Obj3();
        Assert.assertTrue(admin.obtain(oo1, "name = 'diego'"));
        Assert.assertTrue(admin.obtain(oo2, "description = 'description'"));
        Assert.assertTrue(admin.obtain(oo3, "address = 'puey 600'"));

        Assert.assertEquals("diego", oo1.getName());
        Assert.assertEquals("description", oo2.getDescription());
        Assert.assertEquals("puey 600", oo3.getAddress());

        admin.openAtomicBlock();
        admin.save(o2);
        admin.save(o3);
        admin.cancelAtomicBlock();

        ArrayList array1 = admin.obtainAll(Obj2.class, "id > 0");
        ArrayList array2 = admin.obtainAll(Obj3.class, "id > 0");
        Assert.assertEquals(1, array1.size());
        Assert.assertEquals(1, array2.size());

        admin.openAtomicBlock();
        admin.save(o1);
        admin.closeAtomicBlock();

        ArrayList array3 = admin.obtainAll(Obj1.class, "id > 0");
        Assert.assertEquals(2, array3.size());
        Obj4 oo4 = new Obj4();
        ArrayList array4 = admin.obtainAll(Obj4.class, "id > 0");
        Assert.assertEquals(2, array4.size());
    }

    @Test
    public void testReferenceNull(){
        Son1 son = new Son1();
        son.setData("son yes");
        son.setSonName("son name yes");
        son.setDescription("parent yes");

        boolean value = admin.save(son);
        Assert.assertTrue(value);

        Son1 s = new Son1();
        Assert.assertTrue(admin.obtain(s, "description = 'parent yes'"));
        Assert.assertEquals("parent yes", s.getDescription());
        Assert.assertEquals(null, s.getReference1());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestsOperations.class);
    }

}
