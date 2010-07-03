package quickdb.date200912;

import quickdb.date200912.model.Bind;
import quickdb.date200912.model.Bind2;
import quickdb.date200912.model.Collection1;
import quickdb.date200912.model.Collection2;
import quickdb.date200912.model.Delete1;
import quickdb.date200912.model.Delete2;
import quickdb.date200912.model.Name2;
import quickdb.date200912.model.Names;
import quickdb.date200912.model.Obj1;
import quickdb.date200912.model.Obj2;
import quickdb.date200912.model.Obj3;
import quickdb.date200912.model.Obj4;
import quickdb.date200912.model.Object1;
import quickdb.date200912.model.Object2;
import quickdb.date200912.model.Object3;
import quickdb.date200912.model.Reference1;
import quickdb.date200912.model.Reference2;
import quickdb.date200912.model.Son1;
import quickdb.date200912.model.Son2;
import quickdb.db.AdminBase;
import java.util.ArrayList;

public class TestsOperations {

    private AdminBase admin;

    public TestsOperations(AdminBase admin){
        this.admin = admin;
        System.out.println("------------------------");
        System.out.println("TestsOperations");
        System.out.println("completeDelete");
        this.completeDelete();
        System.out.println("testAtomicBlock");
        this.testAtomicBlock();
        System.out.println("testBinding");
        this.testBinding();
        System.out.println("testCollectionDelete");
        this.testCollectionDelete();
        System.out.println("testCollectionModify");
        this.testCollectionModify();
        System.out.println("testDeleteParent");
        this.testDeleteParent();
        System.out.println("testInheritanceQuery");
        this.testInheritanceQuery();
        System.out.println("testLazyLoad");
        this.testLazyLoad();
        System.out.println("testModifyCollection");
        this.testModifyCollection();
        System.out.println("testNames");
        this.testNames();
        System.out.println("testQuerySystemWithInheritance");
        this.testQuerySystemWithInheritance();
        System.out.println("testReferenceNull");
        this.testReferenceNull();
    }

    public void testBinding(){
        Bind b = new Bind();
        Bind2 b2 = new Bind2();

        b.setDescription("description");
        b2.setName("name binding2");

        System.out.println(b.save());
        System.out.println(b2.save());

        Bind bind = new Bind();
        admin.obtain(bind, "description = 'description'");
        System.out.println("description".equalsIgnoreCase(bind.getDescription()));
        Bind2 bind2 = new Bind2();
        admin.obtain(bind2, "name = 'name binding2'");
        System.out.println("name binding2".equalsIgnoreCase(bind2.getName()));
    }

    public void testDeleteParent(){
        Son1 s = new Son1();
        s.setData("data che delete");
        s.setDescription("description of son delete");
        s.setSonName("son name delete");

        System.out.println(admin.save(s));

        Son1 son = new Son1();
        admin.obtain(son, "data = 'data che delete'");
        System.out.println("data che delete".equalsIgnoreCase(son.getData()));

        System.out.println(admin.delete(son));
    }

    public void testQuerySystemWithInheritance(){
        Reference1 ref = new Reference1();
        ref.setValue("house");

        Son1 son = new Son1();
        son.setData("data from son");
        son.setSonName("child");
        son.setDescription("parent description");
        son.setReference1(ref);

        boolean value = admin.save(son);
        System.out.println(value);

        Son1 s = new Son1();
        System.out.println(admin.obtain(s, "reference1.value = 'house'"));
        System.out.println("house".equalsIgnoreCase(s.getReference1().getValue()));
    }

    public void testNames(){
        Name2 test2 = new Name2();
        test2.setMyName("name test2-otro");

        Names t = new Names();
        t.setOtro("mi otro nombre");
        t.setPruebaIgnore("pruebaaaaaaaaa");
        t.setTesta(test2);

        System.out.println(admin.save(t));

        Names t2 = new Names();
        System.out.println(admin.obtain(t2, "otro = 'mi otro nombre'"));
        System.out.println("mi otro nombre".equalsIgnoreCase(t2.getOtro()));
        System.out.println(null == t2.getPruebaIgnore());
    }

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

        System.out.println(admin.save(test));

        System.out.println(admin.obtain(test, "name = 'name'"));

        Collection2 t = new Collection2();
        t.setDescription("description added");
        test.getCollection2().add(t);

        System.out.println(admin.modify(test));

        Collection1 tt = new Collection1();
        System.out.println(admin.obtain(tt, "name = 'name'"));
        System.out.println("description added".equalsIgnoreCase(((Collection2)tt.getCollection2().get(tt.getCollection2().size()-1)).getDescription()));
        System.out.println(admin.delete(test));
    }

    public void testInheritanceQuery(){
        Son2 son = new Son2();
        son.setDescriptionSon("description - son");
        son.setDescription2("description2");
        son.setDescription1("description1");
        Reference2 test = new Reference2();
        test.setValue("value test reference");
        son.setReferenceTest(test);

        System.out.println(admin.save(son));

        System.out.println(admin.obtain(son, "referenceTest.value = 'value test reference'"));
        System.out.println("description - son".equalsIgnoreCase(son.getDescriptionSon()));
    }

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

        System.out.println(admin.save(obj));

        Object1 o = new Object1();
        System.out.println(admin.lazyLoad(o, "name1 = 'name1'"));
        System.out.println(null == o.getObject2());
        System.out.println(admin.lazyLoad(o));
        System.out.println(null == o.getObject2().getObject3());
        System.out.println(admin.lazyLoad(o));

        System.out.println(admin.obtain(o, "id = '1'"));
        System.out.println("name1".equalsIgnoreCase(o.getName1()));
    }

    public void testCollectionModify(){
        admin.executeQuery("DROP TABLE Delete1");
        admin.executeQuery("DROP TABLE Delete2");
        admin.executeQuery("DROP TABLE Delete1Delete2Delete2");
        admin.executeQuery("DROP TABLE Delete2Delete1Delete1");

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

        System.out.println(admin.save(d3));

        System.out.println(admin.obtain(d3, "description = 'description delete1'"));

        ((Delete1) d3.getDelete2().get(0).getDelete1().get(0)).setDescription("new description2");
        ((Delete1) d3.getDelete2().get(1).getDelete1().get(0)).setDescription("description modify2");
        d3.getDelete2().get(0).setName("name modify2");

        Delete2 d2q = new Delete2();
        d2q.setName("delete2 - prueba - added");
        d3.getDelete2().add(d2q);

        System.out.println(admin.modify(d3));
        Delete1 d4 = new Delete1();
        System.out.println(admin.obtain(d4, "description = 'description delete1'"));
        System.out.println("new description2".equalsIgnoreCase(((Delete1)d4.getDelete2().get(0).getDelete1().get(0)).getDescription()));
    }

    public void testCollectionDelete(){
        admin.executeQuery("DROP TABLE Delete1");
        admin.executeQuery("DROP TABLE Delete2");
        admin.executeQuery("DROP TABLE Delete1Delete2Delete2");
        admin.executeQuery("DROP TABLE Delete2Delete1Delete1");

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

        System.out.println(admin.save(d));

        Delete1 d1 = new Delete1();
        System.out.println(admin.obtain(d1, "description = 'description delete1'"));

        System.out.println(admin.delete(d1));
        System.out.println(!admin.obtain(d1, "description = 'description delete1'"));
    }

    public void completeDelete(){
        admin.executeQuery("DROP TABLE Delete1");
        admin.executeQuery("DROP TABLE Delete2");
        admin.executeQuery("DROP TABLE Delete1Delete2Delete2");
        admin.executeQuery("DROP TABLE Delete2Delete1Delete1");
        
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

        System.out.println(admin.save(d));

        System.out.println(admin.obtain(d, "description = 'description delete1'"));
        System.out.println(admin.delete(d));
    }

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
        System.out.println(admin.obtain(oo1, "name = 'diego'"));
        System.out.println(admin.obtain(oo2, "description = 'description'"));
        System.out.println(admin.obtain(oo3, "address = 'puey 600'"));

        System.out.println("diego".equalsIgnoreCase(oo1.getName()));
        System.out.println("description".equalsIgnoreCase(oo2.getDescription()));
        System.out.println("puey 600".equalsIgnoreCase(oo3.getAddress()));

        admin.openAtomicBlock();
        admin.save(o2);
        admin.save(o3);
        admin.cancelAtomicBlock();

        ArrayList array1 = admin.obtainAll(Obj2.class, "id > 0");
        ArrayList array2 = admin.obtainAll(Obj3.class, "id > 0");
        System.out.println(1 == array1.size());
        System.out.println(1 == array2.size());

        admin.openAtomicBlock();
        admin.save(o1);
        admin.closeAtomicBlock();

        ArrayList array3 = admin.obtainAll(Obj1.class, "id > 0");
        System.out.println(2 == array3.size());
        Obj4 oo4 = new Obj4();
        ArrayList array4 = admin.obtainAll(Obj4.class, "id > 0");
        System.out.println(2 == array4.size());
    }

    public void testReferenceNull(){
        Son1 son = new Son1();
        son.setData("son yes");
        son.setSonName("son name yes");
        son.setDescription("parent yes");

        boolean value = admin.save(son);
        System.out.println(value);

        Son1 s = new Son1();
        System.out.println(admin.obtain(s, "description = 'parent yes'"));
        System.out.println("parent yes".equalsIgnoreCase(s.getDescription()));
        System.out.println(null == s.getReference1());
    }

}
