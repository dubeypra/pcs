/**
 * 
 */
package pcs.pradeep.dubey.com.service.project;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import pcs.pradeep.dubey.com.project.Project;
import pcs.pradeep.dubey.com.project.ProjectList;

/**
 * @author prdubey
 *
 */
@Path("/ProjectDataService")
public class ProjectDataService {

    private static final String SUCCESS_RESULT = "<result>success</result>";
    private static final String FAILURE_RESULT = "<result>failure</result>";

    @GET
    @Path("/projects")
    @Produces(MediaType.APPLICATION_XML)
    public ProjectList getProjects() {
	try {
	    return ProjectDao.Service.INSTANCE.getAllProjects();
	} catch (JAXBException e) {
	    e.printStackTrace();
	}
	return null;
    }

    @GET
    @Path("/projects/{projectId}")
    @Produces(MediaType.APPLICATION_XML)
    public Project getProject(@PathParam("projectId") String projectId) {
	try {
	    return ProjectDao.Service.INSTANCE.getProject(projectId);
	} catch (JAXBException e) {
	    e.printStackTrace();
	}
	return null;
    }

    @POST
    @Path("/projects/update")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public String updateProject(String incomingXML) throws IOException {
	System.out.println("Incoming Project  Details are : " + incomingXML);
	boolean isTransaction = ProjectDao.Service.INSTANCE.updateProject(incomingXML);

	if (isTransaction) {
	    return SUCCESS_RESULT;
	}
	return FAILURE_RESULT;
    }

    @POST
    @Path("/projects/create")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public String createProject(String incomingXML) throws IOException {
	System.out.println("Incoming Project Details are : " + incomingXML);
	String projectId = ProjectDao.Service.INSTANCE.createProject(incomingXML);

	if (projectId != null) {
	    return "<result>" + projectId + "</result>";
	}
	return FAILURE_RESULT;
    }

    @DELETE
    @Path("/projects/{projectId}")
    @Produces(MediaType.APPLICATION_XML)
    public String deleteProject(@PathParam("projectId") String projectId) {
	boolean isTransctionSuccessFul = ProjectDao.Service.INSTANCE.deleteProject(projectId);
	if (isTransctionSuccessFul) {
	    return SUCCESS_RESULT;
	}
	return FAILURE_RESULT;
    }

}
