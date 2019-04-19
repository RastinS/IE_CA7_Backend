package Database;

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

public class Database {
	private static List<Project> projects = new ArrayList<Project>();
	private static List<Skill>   skills   = new ArrayList<Skill>();
	private static List<User>    users    = new ArrayList<User>();

	private static boolean didInit = false;


	public static void init () throws Exception {
		Database.addProjects(IOReader.getHTML(Configs.SERVICE_URL + "/project"));
		Database.addUsers();
		Database.addSkills(IOReader.getHTML(Configs.SERVICE_URL + "/skill"));
		users.get(0).setLoggedIn(true);
		didInit = true;
	}

	private static void addProjects (String data) throws JSONException {
		projects = ProjectRepository.setProjects(data);
	}

	private static void addSkills (String data) throws JSONException {
		skills = SkillRepository.setSkills(data, "FROM_IO");
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

	public static boolean didInit () {
		return didInit;
	}

	public static List<Skill> getSkills () {
		return skills;
	}
}
