package cat.quickdb.primitiveCollection;

import cat.quickdb.db.AdminBase;
import cat.quickdb.primitiveCollection.model.BooleanPrimitive;
import cat.quickdb.primitiveCollection.model.DatePrimitive;
import cat.quickdb.primitiveCollection.model.DoublePrimitive;
import cat.quickdb.primitiveCollection.model.FloatPrimitive;
import cat.quickdb.primitiveCollection.model.IntegerPrimitive;
import cat.quickdb.primitiveCollection.model.StringPrimitive;
import java.util.ArrayList;
import org.junit.*;

public class PrimitiveCollectionTest {

    AdminBase admin;

    @Before
    public void configure() {
        this.admin = AdminBase.initialize(AdminBase.DATABASE.MYSQL, "localhost",
                "3306", "testQuickDB", "root", "");
    }

    @Test
    public void testIntegerCollection(){
        IntegerPrimitive primitive = new IntegerPrimitive();
        primitive.setType("integer");
        ArrayList integer = new ArrayList();
        integer.add(1);
        integer.add(2);
        integer.add(3);
        integer.add(4);
        integer.add(5);
        integer.add(6);
        integer.add(7);
        primitive.setInteger(integer);

        admin.save(primitive);

        IntegerPrimitive prim = new IntegerPrimitive();
        admin.obtain(prim, "type = 'integer'");

        Assert.assertEquals(7, prim.getInteger().size());
    }

    @Test
    public void testDoubleCollection(){
        DoublePrimitive primitive = new DoublePrimitive();
        primitive.setType("double");
        ArrayList doubles = new ArrayList();
        doubles.add(1.55);
        doubles.add(2.55);
        doubles.add(3.54);
        doubles.add(4.43);
        doubles.add(5.23);
        doubles.add(6.678);
        doubles.add(7.7657);
        primitive.setDoubles(doubles);

        admin.save(primitive);

        DoublePrimitive prim = new DoublePrimitive();
        admin.obtain(prim, "type = 'double'");

        Assert.assertEquals(7, prim.getDoubles().size());
    }

    @Test
    public void testFloatCollection(){
        FloatPrimitive primitive = new FloatPrimitive();
        primitive.setType("float");
        ArrayList floats = new ArrayList();
        floats.add(1.55);
        floats.add(2.55);
        floats.add(3.54);
        floats.add(4.43);
        floats.add(5.23);
        floats.add(6.678);
        floats.add(7.765);
        primitive.setFloats(floats);

        admin.save(primitive);

        FloatPrimitive prim = new FloatPrimitive();
        admin.obtain(prim, "type = 'float'");

        Assert.assertEquals(7, prim.getFloats().size());
    }

    @Test
    public void testStringCollection(){
        StringPrimitive primitive = new StringPrimitive();
        primitive.setType("string");
        ArrayList strings = new ArrayList();
        strings.add("strings1");
        strings.add("strings2");
        strings.add("strings3");
        strings.add("strings4");
        strings.add("strings5");
        strings.add("strings6");
        strings.add("strings7");
        primitive.setStrings(strings);

        admin.save(primitive);

        StringPrimitive prim = new StringPrimitive();
        admin.obtain(prim, "type = 'string'");

        Assert.assertEquals(7, prim.getStrings().size());
    }

    @Test
    public void testDateCollection(){
        DatePrimitive primitive = new DatePrimitive();
        primitive.setType("date");
        ArrayList dates = new ArrayList();
        dates.add(new java.sql.Date(105, 4, 20));
        dates.add(new java.sql.Date(105, 4, 21));
        dates.add(new java.sql.Date(105, 4, 22));
        dates.add(new java.sql.Date(105, 4, 23));
        dates.add(new java.sql.Date(105, 4, 24));
        dates.add(new java.sql.Date(105, 4, 25));
        primitive.setDates(dates);

        admin.save(primitive);

        DatePrimitive prim = new DatePrimitive();
        admin.obtain(prim, "type = 'date'");

        Assert.assertEquals(6, prim.getDates().size());
    }

    @Test
    public void testBooleanCollection(){
        BooleanPrimitive primitive = new BooleanPrimitive();
        primitive.setType("boolean");
        ArrayList booleans = new ArrayList();
        booleans.add(true);
        booleans.add(false);
        booleans.add(true);
        booleans.add(false);
        booleans.add(true);
        booleans.add(false);
        primitive.setBooleans(booleans);

        admin.save(primitive);

        BooleanPrimitive prim = new BooleanPrimitive();
        admin.obtain(prim, "type = 'boolean'");

        Assert.assertEquals(6, prim.getBooleans().size());
        boolean sec = true;
        for(Object o : prim.getBooleans()){
            if(sec){
                Assert.assertTrue(((Boolean)o));
                sec = false;
            }else{
                Assert.assertFalse(((Boolean)o));
                sec = true;
            }
        }
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(PrimitiveCollectionTest.class);
    }

}
