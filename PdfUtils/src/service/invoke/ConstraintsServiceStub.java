package service.invoke;

import javax.naming.NamingException;

import com.ciena.model.ObjectKey;
import com.ciena.registry.jndi.Locator;
import com.ciena.rm.constraints.api.ConstraintManagerIntf;



import com.ciena.rm.constraints.api.ConstraintService;
import com.ciena.rm.constraints.impl.ConstraintManager;

import context.BaseContextSetUp;

public class ConstraintsServiceStub {

    public static ConstraintService getConstraintManagerService() {
	ConstraintService constraintService= null;

	String jndiName = ConstraintService.MAPPED_NAME + "#" + ConstraintService.class.getCanonicalName();
        try {
            Locator localLocator = new Locator(BaseContextSetUp.get1cContext());
            constraintService = (ConstraintService)localLocator.lookup(jndiName);
        } catch (NamingException e) {
          e.printStackTrace();
        }
	return constraintService;
    }
    
    public static void main(String args[]) {
	getConstraintManagerService();
    }

}
