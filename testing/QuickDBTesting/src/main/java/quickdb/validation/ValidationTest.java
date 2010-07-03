package quickdb.validation;

import quickdb.db.AdminBase;
import quickdb.validation.model.ValidComplexDate;
import quickdb.validation.model.ValidComplexNumeric;
import quickdb.validation.model.ValidUser;
import java.util.Date;

public class ValidationTest {

    private AdminBase admin;

    public ValidationTest(AdminBase admin){
        this.admin = admin;
        System.out.println("---------------------------");
        System.out.println("ValidationTest");
        System.out.println("testInvalidComplexDate");
        this.testInvalidComplexDate();
        System.out.println("testInvalidComplexNumeric1");
        this.testInvalidComplexNumeric1();
        System.out.println("testInvalidComplexNumeric2");
        this.testInvalidComplexNumeric2();
        System.out.println("testInvalidComplexNumeric3");
        this.testInvalidComplexNumeric3();
        System.out.println("testInvalidComplexNumericAll");
        this.testInvalidComplexNumericAll();
        System.out.println("testInvalidDate");
        this.testInvalidDate();
        System.out.println("testInvalidMail");
        this.testInvalidMail();
        System.out.println("testInvalidNumeric");
        this.testInvalidNumeric();
        System.out.println("testInvalidPassword");
        this.testInvalidPassword();
        System.out.println("testInvalidUrl");
        this.testInvalidURL();
        System.out.println("testInvalidUser");
        this.testInvalidUser();
        System.out.println("testValidComplexDate");
        this.testValidComplexDate();
        System.out.println("testValidComplexNumeric");
        this.testValidComplexNumeric();
        System.out.println("testValidUser");
        this.testValidUser();
    }

    public void testValidUser(){
        ValidUser user = new ValidUser();
        user.setAge(24);
        user.setBirthDate(new java.sql.Date(105, 9, 20));
        user.setMail("diego.sarmentero@gmail.com");
        user.setPass("sfdfsgDFSasda423!##$");
        user.setWeb("www.google.com/p/quicdb");
        user.setName("diego sarmentero");

        boolean value = admin.save(user);
        System.out.println(value);
    }

    public void testInvalidUser(){
        ValidUser user = new ValidUser();
        user.setAge(24);
        user.setBirthDate(new java.sql.Date(105, 9, 20));
        user.setMail("diego.sarmentero@gmail.com");
        user.setPass("sfdfsgDFSasda423!##$");
        user.setWeb("www.google.com/p/quicdb");
        user.setName(""); //empty

        boolean value = admin.save(user);
        System.out.println(!value);

        user.setName("diego leonardo sarmentero"); //exceed max length
        value = admin.save(user);
        System.out.println(!value);
    }

    public void testInvalidPassword(){
        ValidUser user = new ValidUser();
        user.setAge(24);
        user.setBirthDate(new java.sql.Date(105, 9, 20));
        user.setMail("diego.sarmentero@gmail.com");
        user.setPass("password");
        user.setWeb("www.google.com/p/quicdb");
        user.setName("diego sarmentero");

        boolean value = admin.save(user);
        System.out.println(!value);
    }

    public void testInvalidMail(){
        ValidUser user = new ValidUser();
        user.setAge(24);
        user.setBirthDate(new java.sql.Date(105, 9, 20));
        user.setMail("diego.sarmentero@gmail");
        user.setPass("sfdfsgDFSasda423!##$");
        user.setWeb("www.google.com/p/quicdb");
        user.setName("diego sarmentero");

        boolean value = admin.save(user);
        System.out.println(!value);
    }

    public void testInvalidURL(){
        ValidUser user = new ValidUser();
        user.setAge(24);
        user.setBirthDate(new java.sql.Date(105, 9, 20));
        user.setMail("diego.sarmentero@gmail.COM");
        user.setPass("sfdfsgDFSasda423!##$");
        user.setWeb("www.webpage");
        user.setName("diego sarmentero");

        boolean value = admin.save(user);
        System.out.println(!value);
    }

    public void testInvalidDate(){
        ValidUser user = new ValidUser();
        user.setAge(24);
        user.setBirthDate(new java.sql.Date(99, 9, 20));
        user.setMail("diego.sarmentero@gmail.com");
        user.setPass("sfdfsgDFSasda423!##$");
        user.setWeb("www.google.com/p/quicdb");
        user.setName("diego sarmentero");

        boolean value = admin.save(user);
        System.out.println(!value);
    }

    public void testInvalidNumeric(){
        ValidUser user = new ValidUser();
        user.setAge(18);
        user.setBirthDate(new java.sql.Date(105, 9, 20));
        user.setMail("diego.sarmentero@gmail.com");
        user.setPass("sfdfsgDFSasda423!##$");
        user.setWeb("www.google.com/p/quicdb");
        user.setName("diego sarmentero");

        boolean value = admin.save(user);
        System.out.println(!value);
    }

    public void testValidComplexDate(){
        ValidComplexDate complex = new ValidComplexDate();
        java.util.Date date = new Date(100, 4, 7);
        complex.setDate(new java.sql.Date(date.getTime()));

        boolean value = admin.save(complex);
        System.out.println(value);
    }

    public void testInvalidComplexDate(){
        ValidComplexDate complex = new ValidComplexDate();
        java.util.Date date = new Date(100, 4, 1);
        complex.setDate(new java.sql.Date(date.getTime()));

        boolean value = admin.save(complex);
        System.out.println(!value);

        date = new Date(100, 8, 7);
        complex.setDate(new java.sql.Date(date.getTime()));

        value = admin.save(complex);
        System.out.println(!value);

        date = new Date(111, 4, 7);
        complex.setDate(new java.sql.Date(date.getTime()));

        value = admin.save(complex);
        System.out.println(!value);

        date = new Date(111, 8, 1);
        complex.setDate(new java.sql.Date(date.getTime()));

        value = admin.save(complex);
        System.out.println(!value);
    }

    public void testValidComplexNumeric(){
        ValidComplexNumeric numeric = new ValidComplexNumeric();
        numeric.setNumber1(5);
        numeric.setNumber2(5);
        numeric.setNumber3(2);

        boolean value = admin.save(numeric);
        System.out.println(value);

        numeric.setNumber2(2);
        value = admin.save(numeric);
        System.out.println(value);
        numeric.setNumber2(9);
        value = admin.save(numeric);
        System.out.println(value);
    }

    public void testInvalidComplexNumeric1(){
        ValidComplexNumeric numeric = new ValidComplexNumeric();
        numeric.setNumber1(4);
        numeric.setNumber2(5);
        numeric.setNumber3(2);

        boolean value = admin.save(numeric);
        System.out.println(!value);
    }

    public void testInvalidComplexNumeric2(){
        ValidComplexNumeric numeric = new ValidComplexNumeric();
        numeric.setNumber1(5);
        numeric.setNumber2(1);
        numeric.setNumber3(2);

        boolean value = admin.save(numeric);
        System.out.println(!value);

        numeric.setNumber2(10);
        value = admin.save(numeric);
        System.out.println(!value);
    }

    public void testInvalidComplexNumeric3(){
        ValidComplexNumeric numeric = new ValidComplexNumeric();
        numeric.setNumber1(5);
        numeric.setNumber2(4);
        numeric.setNumber3(1);

        boolean value = admin.save(numeric);
        System.out.println(!value);

        numeric.setNumber2(3);
        value = admin.save(numeric);
        System.out.println(!value);

        numeric.setNumber2(5);
        value = admin.save(numeric);
        System.out.println(!value);
    }

    public void testInvalidComplexNumericAll(){
        ValidComplexNumeric numeric = new ValidComplexNumeric();
        numeric.setNumber1(3);
        numeric.setNumber2(10);
        numeric.setNumber3(1);

        boolean value = admin.save(numeric);
        System.out.println(!value);
    }

}
