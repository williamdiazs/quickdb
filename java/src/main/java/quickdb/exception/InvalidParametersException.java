package quickdb.exception;

/**
 *
 * @author Diego Sarmentero
 */
public class InvalidParametersException  extends RuntimeException{

    public InvalidParametersException() {
        super("Invalid Parameters exception");
    }

    public InvalidParametersException(Throwable t) {
        super("Invalid Parameters exception", t);
    }

}
