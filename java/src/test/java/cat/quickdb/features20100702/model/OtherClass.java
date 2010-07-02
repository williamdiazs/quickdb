package cat.quickdb.features20100702.model;

/**
 *
 * @author Diego Sarmentero
 */
public class OtherClass {

    public void modifyData(ExecutionOtherClass ex){
        ex.setValue(ex.getValue() + 5);
    }

    public void modifyData2(ExecutionOtherClass ex){
        ex.setValue(ex.getValue() + 10);
    }

}
