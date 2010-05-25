package cat.quickdb.validation;

import quickdb.db.AdminBase;
import cat.quickdb.tests.QuickDBTests;
import cat.quickdb.validation.model.ValidComplexDate;
import cat.quickdb.validation.model.ValidComplexNumeric;
import cat.quickdb.validation.model.ValidUser;
import java.util.Date;
import org.junit.*;
import junit.framework.Assert;

public class ValidationTest {

    private AdminBase admin;

    @Before
    public void configure() {
        this.admin = AdminBase.initialize(QuickDBTests.db, QuickDBTests.host,
                QuickDBTests.port, QuickDBTests.instanceDB,
                QuickDBTests.user, QuickDBTests.pass);
    }

    @Test
    public void testValidUser(){
        ValidUser user = new ValidUser();
        user.setAge(24);
        user.setBirthDate(new java.sql.Date(105, 9, 20));
        user.setMail("diego.sarmentero@gmail.com");
        user.setPass("sfdfsgDFSasda423!##$");
        user.setWeb("www.google.com/p/quicdb");
        user.setName("diego sarmentero");

        boolean value = admin.save(user);
        Assert.assertTrue(value);
    }

    @Test
    public void testInvalidUser(){
        ValidUser user = new ValidUser();
        user.setAge(24);
        user.setBirthDate(new java.sql.Date(105, 9, 20));
        user.setMail("diego.sarmentero@gmail.com");
        user.setPass("sfdfsgDFSasda423!##$");
        user.setWeb("www.google.com/p/quicdb");
        user.setName(""); //empty

        boolean value = admin.save(user);
        Assert.assertTrue(!value);

        user.setName("diego leonardo sarmentero"); //exceed max length
        value = admin.save(user);
        Assert.assertTrue(!value);
    }

    @Test
    public void testInvalidPassword(){
        ValidUser user = new ValidUser();
        user.setAge(24);
        user.setBirthDate(new java.sql.Date(105, 9, 20));
        user.setMail("diego.sarmentero@gmail.com");
        user.setPass("password");
        user.setWeb("www.google.com/p/quicdb");
        user.setName("diego sarmentero");

        boolean value = admin.save(user);
        Assert.assertTrue(!value);
    }

    @Test
    public void testInvalidMail(){
        ValidUser user = new ValidUser();
        user.setAge(24);
        user.setBirthDate(new java.sql.Date(105, 9, 20));
        user.setMail("diego.sarmentero@gmail");
        user.setPass("sfdfsgDFSasda423!##$");
        user.setWeb("www.google.com/p/quicdb");
        user.setName("diego sarmentero");

        boolean value = admin.save(user);
        Assert.assertTrue(!value);
    }

    @Test
    public void testInvalidURL(){
        ValidUser user = new ValidUser();
        user.setAge(24);
        user.setBirthDate(new java.sql.Date(105, 9, 20));
        user.setMail("diego.sarmentero@gmail.COM");
        user.setPass("sfdfsgDFSasda423!##$");
        user.setWeb("www.webpage");
        user.setName("diego sarmentero");

        boolean value = admin.save(user);
        Assert.assertTrue(!value);
    }

    @Test
    public void testInvalidDate(){
        ValidUser user = new ValidUser();
        user.setAge(24);
        user.setBirthDate(new java.sql.Date(99, 9, 20));
        user.setMail("diego.sarmentero@gmail.com");
        user.setPass("sfdfsgDFSasda423!##$");
        user.setWeb("www.google.com/p/quicdb");
        user.setName("diego sarmentero");

        boolean value = admin.save(user);
        Assert.assertTrue(!value);
    }

    @Test
    public void testInvalidNumeric(){
        ValidUser user = new ValidUser();
        user.setAge(18);
        user.setBirthDate(new java.sql.Date(105, 9, 20));
        user.setMail("diego.sarmentero@gmail.com");
        user.setPass("sfdfsgDFSasda423!##$");
        user.setWeb("www.google.com/p/quicdb");
        user.setName("diego sarmentero");

        boolean value = admin.save(user);
        Assert.assertTrue(!value);
    }

    @Test
    public void testValidComplexDate(){
        ValidComplexDate complex = new ValidComplexDate();
        java.util.Date date = new Date(100, 4, 7);
        complex.setDate(new java.sql.Date(date.getTime()));

        boolean value = admin.save(complex);
        Assert.assertTrue(value);
    }

    @Test
    public void testInvalidComplexDate(){
        ValidComplexDate complex = new ValidComplexDate();
        java.util.Date date = new Date(100, 4, 1);
        complex.setDate(new java.sql.Date(date.getTime()));

        boolean value = admin.save(complex);
        Assert.assertTrue(!value);

        date = new Date(100, 8, 7);
        complex.setDate(new java.sql.Date(date.getTime()));

        value = admin.save(complex);
        Assert.assertTrue(!value);

        date = new Date(111, 4, 7);
        complex.setDate(new java.sql.Date(date.getTime()));

        value = admin.save(complex);
        Assert.assertTrue(!value);

        date = new Date(111, 8, 1);
        complex.setDate(new java.sql.Date(date.getTime()));

        value = admin.save(complex);
        Assert.assertTrue(!value);
    }

    @Test
    public void testValidComplexNumeric(){
        ValidComplexNumeric numeric = new ValidComplexNumeric();
        numeric.setNumber1(5);
        numeric.setNumber2(5);
        numeric.setNumber3(2);

        boolean value = admin.save(numeric);
        Assert.assertTrue(value);

        numeric.setNumber2(2);
        value = admin.save(numeric);
        Assert.assertTrue(value);
        numeric.setNumber2(9);
        value = admin.save(numeric);
        Assert.assertTrue(value);
    }

    @Test
    public void testInvalidComplexNumeric1(){
        ValidComplexNumeric numeric = new ValidComplexNumeric();
        numeric.setNumber1(4);
        numeric.setNumber2(5);
        numeric.setNumber3(2);

        boolean value = admin.save(numeric);
        Assert.assertTrue(!value);
    }

    @Test
    public void testInvalidComplexNumeric2(){
        ValidComplexNumeric numeric = new ValidComplexNumeric();
        numeric.setNumber1(5);
        numeric.setNumber2(1);
        numeric.setNumber3(2);

        boolean value = admin.save(numeric);
        Assert.assertTrue(!value);

        numeric.setNumber2(10);
        value = admin.save(numeric);
        Assert.assertTrue(!value);
    }

    @Test
    public void testInvalidComplexNumeric3(){
        ValidComplexNumeric numeric = new ValidComplexNumeric();
        numeric.setNumber1(5);
        numeric.setNumber2(4);
        numeric.setNumber3(1);

        boolean value = admin.save(numeric);
        Assert.assertTrue(!value);

        numeric.setNumber2(3);
        value = admin.save(numeric);
        Assert.assertTrue(!value);

        numeric.setNumber2(5);
        value = admin.save(numeric);
        Assert.assertTrue(!value);
    }

    @Test
    public void testInvalidComplexNumericAll(){
        ValidComplexNumeric numeric = new ValidComplexNumeric();
        numeric.setNumber1(3);
        numeric.setNumber2(10);
        numeric.setNumber3(1);

        boolean value = admin.save(numeric);
        Assert.assertTrue(!value);
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(ValidationTest.class);
    }

}
