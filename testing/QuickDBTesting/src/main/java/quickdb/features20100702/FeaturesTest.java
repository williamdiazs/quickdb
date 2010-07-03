package quickdb.features20100702;

import quickdb.features20100702.model.CacheCollection;
import quickdb.features20100702.model.CacheUpdateCollection;
import quickdb.features20100702.model.Execution;
import quickdb.features20100702.model.ExecutionOtherClass;
import quickdb.features20100702.model.Optimistic;
import java.util.ArrayList;
import quickdb.db.AdminBase;
import quickdb.exception.OptimisticLockException;

/**
 *
 * @author Diego Sarmentero
 */
public class FeaturesTest {

    private AdminBase admin;

    public FeaturesTest(AdminBase admin){
        this.admin = admin;
        System.out.println("FeaturesTest");
        System.out.println("testBeforeAfterFromAnotherClass");
        this.testBeforeAfterFromAnotherClass();
        System.out.println("testBeforeAfterSameClass");
        this.testBeforeAfterSameClass();
        System.out.println("testCacheCollection");
        this.testCacheCollection();
        System.out.println("testCacheUpdateCollection");
        this.testCacheUpdateCollection();
        System.out.println("testOptimisticLock");
        this.testOptimisticLock();
    }

    public void testBeforeAfterSameClass(){
        Execution e = new Execution();
        e.setDescription("description");
        e.setValue(5);

        System.out.println(admin.save(e));

        System.out.println(22 == e.getValue());

        admin.obtain(e).If("description").equal("description").find();
        System.out.println(999 == e.getValue());
    }

    public void testBeforeAfterFromAnotherClass(){
        if(admin.checkTableExist("ExecutionOtherClass")){
            System.out.println(admin.executeQuery("DROP TABLE ExecutionOtherClass"));
        }
        ExecutionOtherClass ex = new ExecutionOtherClass();
        ex.setName("name");
        ex.setValue(25);

        System.out.println(admin.save(ex));

        System.out.println(40.0 == ex.getValue());

        admin.obtain(ex).If("name").match("name").find();
        System.out.println(30.0 == ex.getValue());
    }

    public void testOptimisticLock(){
        Optimistic o = new Optimistic();
        o.setName("diego");
        o.setValue(5);

        System.out.println(admin.save(o));

        Optimistic o2 = new Optimistic();
        admin.obtain(o2).If("name").equal("diego").find();

        Optimistic o3 = new Optimistic();
        admin.obtain(o3).If("name").equal("diego").find();

        o3.setName("leonardo");
        System.out.println(admin.modify(o3));

        o2.setName("gato");
        boolean value = false;
        try{
            admin.modify(o2);
        }catch(OptimisticLockException e){
            value = true;
        }

        System.out.println(value);
    }

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
        System.out.println((n2-n1) > 5);

        ArrayList array2 = admin.obtain(cache).findAll();
        long n3 = System.currentTimeMillis();
        System.out.println((n3-n2) < 5);
    }

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
        System.out.println((n2-n1) > 5);

        ArrayList array2 = admin.obtain(cache).findAll();
        long n3 = System.currentTimeMillis();
        System.out.println((n3-n2) < 5);

        CacheUpdateCollection cache2 = new CacheUpdateCollection();
        cache2.setName("name-update");
        cache2.setValue(9595);
        admin.save(cache2);

        long n4 = System.currentTimeMillis();
        ArrayList array3 = admin.obtain(cache).findAll();
        long n5 = System.currentTimeMillis();
        System.out.println((n5-n4) > 5);
    }

}
