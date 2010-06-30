package quickdb.db.connection;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import quickdb.exception.ConnectionException;
import quickdb.exception.LoggerException;

/**
 *
 * @author Diego Sarmentero
 */
public class ProxyConnectionDB implements InvocationHandler {

    private Object obj;
    private static Logger logger = Logger.getLogger("ConnectionLog");
    private static FileHandler file;

    public static Object newInstance(Object obj) {
        return java.lang.reflect.Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new ProxyConnectionDB(obj));
    }

    public static void configureLogger(String path) {
        try{
            file = new FileHandler(path, true);
            logger.addHandler(file);
            logger.setLevel(Level.ALL);
            SimpleFormatter formatter = new SimpleFormatter();
            file.setFormatter(formatter);
        }catch(IOException ioe){
            throw new LoggerException();
        }
    }

    private ProxyConnectionDB(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args)
            throws Throwable {
        Object result;
        try {
            logger.log(Level.FINE, "execute: " + m.getName());
            result = m.invoke(obj, args);
        } catch (InvocationTargetException e) {
            if(args == null){
                if(m.getName().equalsIgnoreCase("openBlock")){
                    logger.log(Level.SEVERE, "could not execute: " + m.getName() +
                            ", for: " + args[0].toString());
                }else{
                    logger.log(Level.SEVERE, "could not execute: " + m.getName());
                }
            }else if(args.length == 1){
                logger.log(Level.SEVERE, args[0].toString());
            }else if(args.length == 2){
                logger.log(Level.SEVERE, "Column: " + args[0].toString() +
                        " - " + "Value: " + args[1].toString());
            }
            throw new SQLException();
        } catch (Exception e) {
            throw e;
        } finally {
            logger.log(Level.FINE, "end execution: " + m.getName());
        }
        
        return result;
    }
}
