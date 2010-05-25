package cat.quickdb.complexDataStructure;

import cat.quickdb.complexDataStructure.model.SonReference;
import cat.quickdb.tests.QuickDBTests;
import org.junit.*;
import junit.framework.Assert;
import quickdb.db.AdminBase;

/**
 *
 * @author Diego Sarmentero
 */
public class DataStructureTest {

    private AdminBase admin;

    @Before
    public void configure() {
        admin = AdminBase.initialize(QuickDBTests.db, QuickDBTests.host,
                QuickDBTests.port, QuickDBTests.instanceDB,
                QuickDBTests.user, QuickDBTests.pass);
    }

    @Test
    public void simpletest(){
        SonReference sonRef = new SonReference();
        sonRef.setDescription("test description");

        Assert.assertTrue(admin.save(sonRef));
    }

}
