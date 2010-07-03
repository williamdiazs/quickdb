package cat.quickdb.features20100702;

import cat.quickdb.features20100702.model.CacheCollection;
import cat.quickdb.features20100702.model.CacheUpdateCollection;
import cat.quickdb.features20100702.model.Execution;
import cat.quickdb.features20100702.model.ExecutionOtherClass;
import cat.quickdb.features20100702.model.Optimistic;
import cat.quickdb.tests.QuickDBTests;
import java.util.ArrayList;
import org.junit.*;
import quickdb.db.AdminBase;
import quickdb.exception.OptimisticLockException;

/**
 *
 * @author Diego Sarmentero
 */
public class FeaturesTest {

    private AdminBase admin;

    @Before
    public void configure() {
        this.admin = AdminBase.initialize(QuickDBTests.db, QuickDBTests.host,
                QuickDBTests.port, QuickDBTests.instanceDB,
                QuickDBTests.user, QuickDBTests.pass, QuickDBTests.scheme);
    }

    @Test
    public void testBeforeAfterSameClass(){
        Execution e = new Execution();
        e.setDescription("description");
        e.setValue(5);

        Assert.assertTrue(admin.save(e));

        Assert.assertEquals(22, e.getValue());

        admin.obtain(e).If("description").equal("description").find();
        Assert.assertEquals(999, e.getValue());
    }

    @Test
    public void testBeforeAfterFromAnotherClass(){
        if(admin.checkTableExist("ExecutionOtherClass")){
            Assert.assertTrue(admin.executeQuery("DROP TABLE ExecutionOtherClass"));
        }
        ExecutionOtherClass ex = new ExecutionOtherClass();
        ex.setName("name");
        ex.setValue(25);

        Assert.assertTrue(admin.save(ex));

        Assert.assertEquals(40.0, ex.getValue());

        admin.obtain(ex).If("name").match("name").find();
        Assert.assertEquals(30.0, ex.getValue());
    }

    @Test
    public void testOptimisticLock(){
        Optimistic o = new Optimistic();
        o.setName("diego");
        o.setValue(5);

        Assert.assertTrue(admin.save(o));

        Optimistic o2 = new Optimistic();
        admin.obtain(o2).If("name").equal("diego").find();

        Optimistic o3 = new Optimistic();
        admin.obtain(o3).If("name").equal("diego").find();

        o3.setName("leonardo");
        Assert.assertTrue(admin.modify(o3));

        o2.setName("gato");
        boolean value = false;
        try{
            admin.modify(o2);
        }catch(OptimisticLockException e){
            value = true;
        }

        Assert.assertTrue(value);
    }

    @Test
    public void testCacheCollection(){
        for(int i = 0; i < 20; i++){
            CacheCollection cache = new CacheCollection();
            cache.setName("name" + i);
            cache.setValue(i);
            admin.save(cache);
        }

        CacheCollection cache = new CacheCollection();
        long n1 = System.currentTimeMillis();
        ArrayList array = admin.obtain(cache).findAll();
        long n2 = System.currentTimeMillis();
        Assert.assertTrue((n2-n1) > 5);

        ArrayList array2 = admin.obtain(cache).findAll();
        long n3 = System.currentTimeMillis();
        Assert.assertTrue((n3-n2) < 5);
    }

    @Test
    public void testCacheUpdateCollection(){
        for(int i = 0; i < 80; i++){
            CacheUpdateCollection cache = new CacheUpdateCollection();
            cache.setName("name" + i);
            cache.setValue(i);
            admin.save(cache);
        }

        CacheUpdateCollection cache = new CacheUpdateCollection();
        long n1 = System.currentTimeMillis();
        ArrayList array = admin.obtain(cache).findAll();
        long n2 = System.currentTimeMillis();
        Assert.assertTrue((n2-n1) > 5);

        ArrayList array2 = admin.obtain(cache).findAll();
        long n3 = System.currentTimeMillis();
        Assert.assertTrue((n3-n2) < 5);

        CacheUpdateCollection cache2 = new CacheUpdateCollection();
        cache2.setName("name-update");
        cache2.setValue(9595);
        admin.save(cache2);

        long n4 = System.currentTimeMillis();
        ArrayList array3 = admin.obtain(cache).findAll();
        long n5 = System.currentTimeMillis();
        Assert.assertTrue((n5-n4) > 5);
    }

}
