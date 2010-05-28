package quickdb.exception;

/**
 *
 * @author Diego Sarmentero
 */
public class SubQueryCloseException extends RuntimeException{

    public SubQueryCloseException() {
        super("SubQuery For closed without being initialized");
    }

    public SubQueryCloseException(Throwable t) {
        super("SubQuery For closed without being initialized", t);
    }

}
