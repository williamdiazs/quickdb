package quickdb.exception;

/**
 *
 * @author Diego Sarmentero
 */
public class DictionaryIncompleteException extends RuntimeException{

    public DictionaryIncompleteException() {
        super("Dictionary Incomplete exception");
    }

    public DictionaryIncompleteException(Throwable t) {
        super("Query malformed exception", t);
    }

}
