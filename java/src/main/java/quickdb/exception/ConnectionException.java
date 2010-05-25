package quickdb.exception;

/**
 *
 * @author Diego Sarmentero
 */
public class ConnectionException extends Exception {

    public ConnectionException(String msg) {
        super(msg);
    }

    public ConnectionException(String msg, Throwable t) {
        super(msg, t);
    }
}