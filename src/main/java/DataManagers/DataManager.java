package DataManagers;

import DataManagers.ProjectData.ProjectDataHandler;
import DataManagers.SkillData.SkillDataHandler;
import DataManagers.UserData.UserDataHandler;
import Extras.IOReader;
import Models.Project;
import Models.Skill;
import Models.User;
import Repositories.ProjectRepository;
import Repositories.SkillRepository;
import Repositories.UserRepository;
import Static.Configs;
import org.json.JSONException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

	public static void init () throws Exception {
		DataManager.addSkills(IOReader.getHTML(Configs.SERVICE_URL + "/skill"));
		DataManager.addProjects(IOReader.getHTML(Configs.SERVICE_URL + "/project"));
		DataManager.addUsers();
	}

	private static void addProjects (String data) throws JSONException {
		ProjectDataHandler.init();
		ProjectDataHandler.addProjects(ProjectRepository.setProjects(data));
	}

	private static void addSkills (String data) throws JSONException {
		SkillDataHandler.init();
		SkillDataHandler.addSkills(SkillRepository.setSkills(data, "FROM_IO"));
	}

	private static void addUsers () throws JSONException {
		UserDataHandler.init();
		List<User> users = UserRepository.setUsers(Configs.USER_DATA);
		UserRepository.setLoggedInUser(users.get(0));
		UserDataHandler.addUsers(users);
	}

	public static List<User> getUsers () {
		return UserDataHandler.getUsers();
	}

	public static List<Project> getProjects () {
		return ProjectDataHandler.getProjects();
	}

	public static List<Skill> getSkills () {
		return SkillDataHandler.getSkills();
	}

	public static void dropExistingTable(String tableName) {
		try {
			Connection con = DataBaseConnector.getConnection();

			Statement stmt = con.createStatement();
			String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'";
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()) {
				if(tableName.equals(rs.getString("name"))) {
					sql = "DROP TABLE " + tableName;
					stmt.executeUpdate(sql);
				}
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static User findUserWithID(String ID) {
		return UserDataHandler.getUser(ID);
	}
}
