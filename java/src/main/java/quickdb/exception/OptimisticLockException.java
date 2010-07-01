package quickdb.exception;

/**
 *
 * @author Diego Sarmentero
 */
public class OptimisticLockException extends RuntimeException{

    public OptimisticLockException() {
        super("Optimistic Lock Exception, the object has been modified outside this session.");
    }

    public OptimisticLockException(Throwable t) {
        super("Optimistic Lock Exception, the object has been modified outside this session.", t);
    }

}
