package quickdb.tests.quickdbtesting;

import quickdb.annotations.AnnotationTest;
import quickdb.binding.BindingTest;
import quickdb.complexDataStructure.DataStructureTest;
import quickdb.date200912.TestsOperations;
import quickdb.db.AdminBase;
import quickdb.db.AdminBase.DATABASE;
import quickdb.features20100702.FeaturesTest;
import quickdb.invalid.InvalidTest;
import quickdb.operations.ComplexOperationsTest;
import quickdb.operations.OperationsTest;
import quickdb.primitiveCollection.PrimitiveCollectionTest;
import quickdb.query.QueryTest;
import quickdb.validation.ValidationTest;
import quickdb.view.ViewTest;

public class App 
{
    public static void main( String[] args )
    {
        DATABASE db = DATABASE.MYSQL;
        String host = "localhost";
        String port = "3306";
        String database = "testQuickDB";
        String user = "root";
        String pass = "";
        String scheme = "";
        AdminBase admin = AdminBase.initialize(db, host,
                port, database, user, pass);
        AdminBase.initializeAdminBinding(db, host,
                port, database, user, pass, scheme);

        ViewTest test13 = new ViewTest(admin);
        AnnotationTest test1 = new AnnotationTest(admin);
        BindingTest test2 = new BindingTest();
        DataStructureTest test3 = new DataStructureTest(admin);
        TestsOperations test4 = new TestsOperations(admin);
        FeaturesTest test5 = new FeaturesTest(admin);
        InvalidTest test6 = new InvalidTest(admin);
        ComplexOperationsTest test7 = new ComplexOperationsTest(admin);
        OperationsTest test8 = new OperationsTest(admin);
        PrimitiveCollectionTest test9 = new PrimitiveCollectionTest(admin);
        QueryTest test10 = new QueryTest(admin);
        ValidationTest test12 = new ValidationTest(admin);
        
    }
}
