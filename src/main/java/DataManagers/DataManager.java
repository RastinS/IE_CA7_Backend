package DataManagers;

import DataManagers.ProjectData.ProjectDataHandler;
import DataManagers.SkillData.SkillDataHandler;
import Extras.IOReader;
import Models.Project;
import Models.Skill;
import Models.User;
import Repositories.ProjectRepository;
import Repositories.SkillRepository;
import Repositories.UserRepository;
import Static.Configs;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
	private static List<Project> projects = new ArrayList<Project>();
	private static List<User>    users    = new ArrayList<User>();

	public static void init () throws Exception {
		DataManager.addProjects(IOReader.getHTML(Configs.SERVICE_URL + "/project"));
		DataManager.addUsers();
		DataManager.addSkills(IOReader.getHTML(Configs.SERVICE_URL + "/skill"));
		users.get(0).setLoggedIn(true);
	}

	private static void addProjects (String data) throws JSONException {
		//ProjectDataHandler.addProjectsToDB(ProjectRepository.setProjects(data));
		projects = ProjectRepository.setProjects(data);
	}

	private static void addSkills (String data) throws JSONException {
		SkillDataHandler.init();
		SkillDataHandler.addSkills(SkillRepository.setSkills(data, "FROM_IO"));
	}

	private static void addUsers () throws JSONException {
		users = UserRepository.setUsers(Configs.USER_DATA);
		UserRepository.setLoggedInUser(users.get(0));
	}

	public static List<User> getUsers () {
		return users;
	}

	public static List<Project> getProjects () {
		return projects;
	}

	public static List<Skill> getSkills () {
		return SkillDataHandler.getSkills();
	}
}
