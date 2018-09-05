/**
 * 
 */
package service.invoke;

import javax.naming.NamingException;

import com.ciena.registry.jndi.Locator;
import com.ciena.rm.uinavigation.api.NodeConfigProcessor;

import context.BaseContextSetUp;

/**
 * @author prdubey
 *
 */
public class NodeConfigProcessorStub {
    
    public static NodeConfigProcessor getNodeConfigProcessor() {
	NodeConfigProcessor nodeConfigProcessor = null;

	String jndiName = NodeConfigProcessor.MAPPED_NAME + "#" + NodeConfigProcessor.class.getCanonicalName();
        try {
            Locator localLocator = new Locator(BaseContextSetUp.get1cContext());
            nodeConfigProcessor = (NodeConfigProcessor)localLocator.lookup(jndiName);
        } catch (NamingException e) {
          e.printStackTrace();
        }
	return nodeConfigProcessor;
    }
    
    public static void main(String args[]) {
	getNodeConfigProcessor();
       }

}
