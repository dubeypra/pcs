/**
 * 
 */
package pcs.pradeep.dubey.com.ui.facade;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import pcs.pradeep.dubey.com.project.Project;
import pcs.pradeep.dubey.com.project.ProjectList;

/**
 * This class deals with handling all the project related information which are
 * stored on the file system
 * 
 * @author prdubey
 *
 */
public class ProjectFacade extends Facade {

    public static String BASE_URL = "http://localhost:8080/services/rest/ProjectDataService/";

    public static String GET_ALL_PROJECTS = "projects";
    public static String UPDATE_PROJECT = "projects/update";
    public static String CREATE_PROJECT = "projects/create";
    public static String DELETE_PROJECT = "projects";

    public static String OPERATION_GET = "GET";
    public static String OPERATION_POST = "POST";
    public static String OPERATION_DELETE = "DELETE";

    /**
     * @return List of all the Projects
     */
    public List<Project> retrieveProjects() {
	List<Project> projectList = new ArrayList<Project>();
	try {
	    HttpURLConnection conn = connectDataService(BASE_URL + GET_ALL_PROJECTS, OPERATION_GET);

	    JAXBContext jaxbContext = JAXBContext.newInstance(ProjectList.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

	    ProjectList projectList1 = (ProjectList) (jaxbUnmarshaller.unmarshal(conn.getInputStream()));

	    isServiceCallSuccessful(conn);

	    projectList = projectList1.getProjectList();
	    System.out.println(projectList.size());

	    conn.disconnect();
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (JAXBException e) {
	    e.printStackTrace();
	}
	return projectList;
    }

    /**
     * Fetch the specific Project Id
     * 
     * @param projectId
     *            : Employee Id
     * @return: Project
     * 
     */
    public Project retreiveProject(String projectId) {
	Project project = null;
	try {
	    String urlPath = BASE_URL + GET_ALL_PROJECTS + "/" + projectId;
	    HttpURLConnection conn = connectDataService(urlPath, OPERATION_GET);

	    JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

	    project = (Project) (jaxbUnmarshaller.unmarshal(conn.getInputStream()));

	    isServiceCallSuccessful(conn);

	    System.out.println("Project is : " + project.getDescription());

	    conn.disconnect();

	} catch (IOException | RuntimeException e) {
	    e.printStackTrace();
	} catch (JAXBException e) {
	    e.printStackTrace();
	}
	return project;
    }

    /**
     * This method will update the employee data in the file system As per make
     * is more generic we have passes the complete changed Employee from UI
     * layer to Bussiness Layer so that all the data will be in sync
     * 
     * @param project
     * @return
     */
    public String createProject(Project project) {
	String projectId = "-1";

	JAXBContext jaxbContext;
	try {
	    jaxbContext = JAXBContext.newInstance(Project.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	    String urlPath = BASE_URL + CREATE_PROJECT;

	    HttpURLConnection conn = connectDataService(urlPath, OPERATION_POST);
	    conn.setRequestProperty("Content-Type", "application/xml");
	    conn.setDoOutput(true);
	    conn.setDoInput(true);
	    jaxbMarshaller.marshal(project, conn.getOutputStream());

	    String response = readResponse(conn);
	    isServiceCallSuccessful(conn);
	    System.out.println(response.toString());

	    conn.disconnect();
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (ProtocolException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (RuntimeException e) {
	    e.printStackTrace();
	} catch (JAXBException e1) {
	    e1.printStackTrace();
	}
	return projectId;
    }

    /**
     * This method will update the employee data in the file system As per make
     * is more generic we have passes the complete changed Employee from UI
     * layer to Bussiness Layer so that all the data will be in sync
     * 
     * @param project
     * @return
     */
    public boolean updateProject(Project project) {
	boolean isTransactionSuccessFul = true;

	JAXBContext jaxbContext;
	try {
	    jaxbContext = JAXBContext.newInstance(Project.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	    String urlPath = BASE_URL + UPDATE_PROJECT;

	    HttpURLConnection conn = connectDataService(urlPath, OPERATION_POST);
	    conn.setRequestProperty("Content-Type", "application/xml");
	    conn.setDoOutput(true);
	    conn.setDoInput(true);
	    jaxbMarshaller.marshal(project, conn.getOutputStream());

	    String response = readResponse(conn);
	    isServiceCallSuccessful(conn);
	    System.out.println(response.toString());

	    conn.disconnect();
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (ProtocolException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (RuntimeException e) {
	    e.printStackTrace();
	} catch (JAXBException e1) {
	    e1.printStackTrace();
	}
	return isTransactionSuccessFul;
    }

    /**
     * This method deals with delete of the Employee from the System. Clean up
     * of all the data linked with this employee Id is not implemented. Moreover
     * inspite of hard delete we can move this data to obsolete folder which
     * must have the same structure as current data structure had.
     * 
     * Orchestration of the Delete related views will be implemented from here
     * by invoking respective methods from other facades
     * 
     * @param projectId
     * @return
     */
    public boolean deleteProject(String projectId) {
	boolean isTransactionSuccesfull = true;
	String urlPath = BASE_URL + DELETE_PROJECT + "/" + projectId;
	try {
	    HttpURLConnection conn = connectDataService(urlPath, OPERATION_DELETE);

	    String response = readResponse(conn);
	    isServiceCallSuccessful(conn);
	    System.out.println(response.toString());

	    conn.disconnect();

	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (ProtocolException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (RuntimeException e) {
	    e.printStackTrace();
	}
	return isTransactionSuccesfull;
    }

    public static void main(String args[]) {
	ProjectFacade projectFacade = new ProjectFacade();
	// employeeFacade.deleteEmployee("1005");
	projectFacade.updateProject(createDummyProjectData());
	projectFacade.retreiveProject("8001");
    }

}
