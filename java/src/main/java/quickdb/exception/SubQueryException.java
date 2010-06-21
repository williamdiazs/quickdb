package quickdb.exception;

/**
 *
 * @author Diego Sarmentero
 */
public class SubQueryException extends RuntimeException{

    public SubQueryException() {
        super("For was expected");
    }

    public SubQueryException(String s){
        super(s);
    }

    public SubQueryException(Throwable t) {
        super("For was expected", t);
    }

}
