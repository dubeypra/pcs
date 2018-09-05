package context;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class BaseContextSetUp {
    public static String ONE_CONTROL_PROVIDER_URL=  "t3://haxv-aspandey-3:9001" ;
        
        public  static InitialContext getContext() throws NamingException{
                Hashtable h= new Hashtable();
                h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
                h.put(Context.PROVIDER_URL, "t3://haxv-ankgarg-1:7001");
                InitialContext context = new InitialContext(h);
                return context;
            }
        
        public  static InitialContext get1cContext() throws NamingException{
            Hashtable h= new Hashtable();
            h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
            h.put(Context.PROVIDER_URL,ONE_CONTROL_PROVIDER_URL);
            InitialContext context = new InitialContext(h);
            return context;
        }

}
