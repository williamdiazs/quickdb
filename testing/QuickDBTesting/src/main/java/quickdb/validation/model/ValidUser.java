package quickdb.validation.model;

import quickdb.annotation.Validation;
import java.sql.Date;

public class ValidUser {

    private int id;
    @Validation(maxLength=20, conditionMatch={Validation.conditionNoEmpty})
    private String name;
    @Validation(conditionMatch={Validation.conditionSecurePassword})
    private String pass;
    @Validation(conditionMatch={Validation.conditionMail})
    private String mail;
    @Validation(conditionMatch={Validation.conditionURL})
    private String web;
    @Validation(date={Validation.YEAR, Validation.EQUALORGREATER, 2000})
    private Date birthDate;
    @Validation(numeric={Validation.GREATER, 18})
    private int age;

    public void setAge(int age) {
        this.age = age;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public int getAge() {
        return age;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public int getId() {
        return id;
    }

    public String getMail() {
        return mail;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public String getWeb() {
        return web;
    }

}
