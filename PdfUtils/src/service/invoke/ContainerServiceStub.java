package service.invoke;

import javax.naming.NamingException;

import com.ciena.registry.jndi.Locator;
import com.ciena.rm.uinavigation.api.ContainerConfigProcessor;

import context.BaseContextSetUp;

public class ContainerServiceStub {

    public static ContainerConfigProcessor getContainerService() {
	ContainerConfigProcessor containerConfigProcessor = null;

	String jndiName = ContainerConfigProcessor.MAPPED_NAME + "#" + ContainerConfigProcessor.class.getCanonicalName();
        try {
            Locator localLocator = new Locator(BaseContextSetUp.get1cContext());
            containerConfigProcessor = (ContainerConfigProcessor)localLocator.lookup(jndiName);
        } catch (NamingException e) {
          e.printStackTrace();
        }
	return containerConfigProcessor;
    }
    
    public static void main(String args[]) {
	getContainerService();
    }

}
