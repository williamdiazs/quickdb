package quickdb.validation.model;

import quickdb.annotation.Validation;

public class ValidComplexNumeric {

    private int id;
    @Validation(numeric={Validation.EQUAL, 5})
    private int number1;
    @Validation(numeric={Validation.EQUALORGREATER, 2, Validation.EQUALORLOWER, 9})
    private int number2;
    @Validation(numeric={Validation.GREATER, 1, Validation.LOWER, 3})
    private int number3;

    public void setId(int id) {
        this.id = id;
    }

    public void setNumber1(int number1) {
        this.number1 = number1;
    }

    public void setNumber2(int number2) {
        this.number2 = number2;
    }

    public void setNumber3(int number3) {
        this.number3 = number3;
    }

    public int getId() {
        return id;
    }

    public int getNumber1() {
        return number1;
    }

    public int getNumber2() {
        return number2;
    }

    public int getNumber3() {
        return number3;
    }

}
