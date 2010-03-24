package cat.quickdb.query;

import java.util.ArrayList;

public interface IQuery {

    boolean find();

    ArrayList findAll();

    void dataForViews(String fields, String names, Object obj, Class... clazz);

}
