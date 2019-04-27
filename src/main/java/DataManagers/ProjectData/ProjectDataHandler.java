package DataManagers.ProjectData;

import DataManagers.DataBaseConnector;
import DataManagers.DataManager;
import DataManagers.SkillData.SkillDataMapper;
import Models.Project;
import Models.Skill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDataHandler {
	private static final String COLUMNS = "(id, title, budget, description, imageUrl, deadline)";
	private static final String SKILL_COLUMNS = "(projectID, skillName, point)";
	private static Connection con = null;

	public static void init() {
		try {
			DataManager.dropExistingTable("project");
			DataManager.dropExistingTable("projectSkill");
			con = DataBaseConnector.getConnection();
			Statement st = con.createStatement();

			String sql = "CREATE TABLE " +
					"project " +
					"(id TEXT PRIMARY KEY, " +
					"title TEXT, " +
					"budget INTEGER, " +
					"description TEXT, " +
					"imageUrl TEXT, " +
					"deadline INTEGER)";
			st.executeUpdate(sql);

			sql = "CREATE TABLE " +
					"projectSkill " +
					"(projectID TEXT, " +
					"skillName TEXT, " +
					"point INTEGER, " +
					"FOREIGN KEY (projectID) REFERENCES project(id)," +
					"FOREIGN KEY (skillName) REFERENCES skill(name))";
			st.executeUpdate(sql);

			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addProjects(List<Project> projects) {
		String projectSql = "INSERT INTO project " + COLUMNS + " VALUES (?, ?, ?, ?, ?, ?)";
		String skillSql = "INSERT INTO projectSkill " + SKILL_COLUMNS + " VALUES (?, ?, ?)";

		try {
			con = DataBaseConnector.getConnection();
			PreparedStatement pst = con.prepareStatement(projectSql);
			PreparedStatement sst = con. prepareStatement(skillSql);
			for(Project project : projects) {
				ProjectDataMapper.projectDomainToDB(project, pst);
				pst.executeUpdate();
				for(Skill skill : project.getSkills()) {
					SkillDataMapper.skillDomainToDB(skill, project.getId(), sst);
					sst.executeUpdate();
				}
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<Project> getProjects() {
		Statement stmt;
		List<Project> projects = new ArrayList<>();
		try{
			con = DataBaseConnector.getConnection();
			stmt = con.createStatement();

			String sql = "SELECT * FROM project";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
				projects.add(ProjectDataMapper.projectDBtoDomain(rs));
			rs.close();
			stmt.close();

			sql = "SELECT skillName, point FROM projectSkill WHERE projectID = ?";
			PreparedStatement st = con.prepareStatement(sql);
			for(Project project : projects) {
				st.setString(1, project.getId());
				ResultSet rss = st.executeQuery();
				while(rss.next())
					project.addSkill(SkillDataMapper.skillDBtoDomain(rss));
				rss.close();
			}

			st.close();
			con.close();
		}catch(SQLException se){
			se.printStackTrace();
		}

		return projects;
	}

}
