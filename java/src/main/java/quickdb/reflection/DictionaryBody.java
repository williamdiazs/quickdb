package quickdb.reflection;

import java.lang.reflect.Method;
import quickdb.annotation.Properties;
import quickdb.annotation.Properties.TYPES;
import quickdb.annotation.Validation;

/**
 *
 * @author Diego Sarmentero
 */
public class DictionaryBody {

    private String fieldName;
    private String colName;
    private Method set;
    private Method get;
    private Properties.TYPES dataType;
    private Class collectionClass;
    private Validation validation;
    private String summary;

    DictionaryBody() {
        this.validation = null;
    }

    public String colName() {
        return colName;
    }

    public Class collectionClass() {
        return collectionClass;
    }

    public TYPES dataType() {
        return dataType;
    }

    public String fieldName() {
        return fieldName;
    }

    public Method get() {
        return get;
    }

    public Method set() {
        return set;
    }

    public Validation validation() {
        return validation;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public void setCollectionClass(Class collectionClass) {
        this.collectionClass = collectionClass;
    }

    public void setDataType(TYPES dataType) {
        this.dataType = dataType;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setGet(Method get) {
        this.get = get;
    }

    public void setSet(Method set) {
        this.set = set;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    public String summary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
