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
		return ProjectDataHandler.getValidProjects(ID);
	}

	public static void setValidBidders(Project project, List<User> users) {
		for(User user : users) {
			if(BidService.isUserSkillValidForProject(user, project))
				project.addValidBidder(user.getId());
		}
	}
}
