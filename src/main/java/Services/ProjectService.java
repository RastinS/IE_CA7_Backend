package Services;

import DataManagers.DataManager;
import Models.Project;
import Models.User;
import Repositories.ProjectRepository;

import java.util.ArrayList;
import java.util.List;

public class ProjectService {
	public static Project getProject (String id) {
		for (Project project : DataManager.getProjects()) {
			if (project.getId().equals(id)) {
				return project;
			}
		}
		return null;
	}

	public static List<Project> getProjects () {
		return ProjectRepository.getProjects();
	}

	public static List<Project> getProjects (String ID) {
		User user = UserService.findUserWithID(ID);
		List<Project> projects = new ArrayList<Project>();
		for(Project project : ProjectRepository.getProjects()) {
			if(UserService.canBid(project, user))
				projects.add(project);
		}
		return projects;
	}

}
