package pcs.pradeep.dubey.com.ui.project;

import java.util.List;

import pcs.pradeep.dubey.com.project.Project;
import pcs.pradeep.dubey.com.ui.facade.ProjectFacade;

public class ProjectAction {

	private List<Project> projectList;

	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

	public String execute() {
		try {
			
			ProjectFacade projectFacade = new ProjectFacade();
			projectList = projectFacade.retrieveProjects();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return "success";
	}
}
