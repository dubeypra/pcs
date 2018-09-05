import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import com.ciena.core.util.lookup.LookupException;
import com.ciena.core.util.lookup.LookupService;
import com.ciena.ems.EmsException;
import com.ciena.ems.dataModel.EmsChangedObject;
import com.ciena.ems.server.system.dataManager.csmConnection.CSMConnectionManagerProxy;
import com.ciena.ems.server.system.dataManager.csmConnection.CSMConnectionManagerProxyHome;
import com.ciena.registry.ServiceNotFoundException;
import com.ciena.rm.fre.impl.FreExtServiceFinder;
import com.ciena.rm.fre.impl.FreExtServiceFinder.OCServiceEnum;

import context.BaseContextSetUp;


public class CSMMangaerTest {
        public static CSMConnectionManagerProxy getCSMConnectionManager() throws ServiceNotFoundException, RemoteException, CreateException {
                CSMConnectionManagerProxyHome connectionManagerProxyHome = null;
                try {
                        connectionManagerProxyHome = (CSMConnectionManagerProxyHome)LookupService.
                                        getEJBHome("csmConnection.CSMConnectionManagerProxyHome",BaseContextSetUp.getContext());
                } catch (LookupException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (NamingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                return connectionManagerProxyHome.create();
            }
        
        public static void main(String args[]) {
                try {
                        CSMConnectionManagerProxy  connectionManagerProxy =CSMMangaerTest.getCSMConnectionManager();
                        List<String> fresToCreate = new ArrayList<String>();
                        fresToCreate.add("Hello"); // Name of Connection
                        String neName="GSIM-HAX16-033";
                        List<EmsChangedObject> changesobjectList ;
                        System.out.println( connectionManagerProxy.getAllTopLevelConnectionsName(neName));
                       
                        System.out.println("##############################################");
                        changesobjectList =
                                        
                                        connectionManagerProxy.getAllConnectionObjectsByName(fresToCreate);
                        
                        System.out.println("Changed Objects are "+changesobjectList);
                } catch (ServiceNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (CreateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (EmsException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }
}
