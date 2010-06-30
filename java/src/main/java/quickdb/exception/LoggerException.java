package quickdb.exception;

/**
 *
 * @author Diego Sarmentero
 */
public class LoggerException extends RuntimeException{

    public LoggerException() {
        super("Logger exception, Logger could not be initialized");
    }

    public LoggerException(Throwable t) {
        super("Logger exception, Logger could not be initialized", t);
    }

}
