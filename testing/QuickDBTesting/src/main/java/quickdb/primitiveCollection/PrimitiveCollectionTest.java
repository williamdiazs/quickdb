package quickdb.primitiveCollection;

import quickdb.db.AdminBase;
import quickdb.primitiveCollection.model.BooleanPrimitive;
import quickdb.primitiveCollection.model.DatePrimitive;
import quickdb.primitiveCollection.model.DoublePrimitive;
import quickdb.primitiveCollection.model.FloatPrimitive;
import quickdb.primitiveCollection.model.IntegerPrimitive;
import quickdb.primitiveCollection.model.StringPrimitive;
import java.util.ArrayList;

public class PrimitiveCollectionTest {

    private AdminBase admin;

    public PrimitiveCollectionTest(AdminBase admin){
        this.admin = admin;
        System.out.println("----------------------");
        System.out.println("PrimitiveCollectionTest");
        System.out.println("testBooleanCollection");
        this.testBooleanCollection();
        System.out.println("testDateCollection");
        this.testDateCollection();
        System.out.println("testDoubleCollection");
        this.testDoubleCollection();
        System.out.println("testFloatCollection");
        this.testFloatCollection();
        System.out.println("testIntegerCollection");
        this.testIntegerCollection();
        System.out.println("testStringCollection");
        this.testStringCollection();
    }

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

        System.out.println(7 == prim.getInteger().size());
    }

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

        System.out.println(7 == prim.getDoubles().size());
    }

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

        System.out.println(7 == prim.getFloats().size());
    }

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

        System.out.println(7 == prim.getStrings().size());
    }

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

        System.out.println(6 == prim.getDates().size());
    }

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

        System.out.println(6 == prim.getBooleans().size());
        boolean sec = true;
        for(Object o : prim.getBooleans()){
            if(sec){
                System.out.println(((Boolean)o));
                sec = false;
            }else{
                System.out.println(!((Boolean)o));
                sec = true;
            }
        }
    }

}
