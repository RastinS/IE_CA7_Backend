package Services;

import DataManagers.DataManager;
import DataManagers.ProjectData.ProjectDataHandler;
import Models.Project;
import Models.User;
import Repositories.ProjectRepository;

import java.util.ArrayList;
import java.util.List;

public class ProjectService {
	public static Project getProject (String id) {
		return ProjectDataHandler.getProject(id);
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
