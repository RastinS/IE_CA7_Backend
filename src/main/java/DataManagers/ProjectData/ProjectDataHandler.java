package DataManagers.ProjectData;

import DataManagers.DataBaseConnector;
import DataManagers.DataManager;
import DataManagers.SkillData.SkillDataMapper;
import Models.Bid;
import Models.Project;
import Models.Skill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDataHandler {
	private static final String COLUMNS = "(id, title, budget, description, imageUrl, deadline)";
	private static final String SKILL_COLUMNS = "(projectID, skillName, point)";
	private static final String BID_COLLUMNS = "(userID, projectID, amount)";
	private static Connection con = null;

	public static void init() {
		try {
			DataManager.dropExistingTable("project");
			DataManager.dropExistingTable("projectSkill");
			DataManager.dropExistingTable("bid");
			DataManager.dropExistingTable("bidWinner");
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

			sql = "CREATE TABLE " +
					"bid " +
					"(userID TEXT, " +
					"projectID TEXT, " +
					"amount INTEGER, " +
					"FOREIGN KEY (userID) REFERENCES user(id)," +
					"FOREIGN KEY (projectID) REFERENCES project(id))";
			st.executeUpdate(sql);

			sql = "CREATE TABLE " +
					"bidWinner " +
					"(userID TEXT, " +
					"projectID TEXT PRIMARY KEY, " +
					"amount INTEGER, " +
					"FOREIGN KEY (userID) REFERENCES user(id)," +
					"FOREIGN KEY (projectID) REFERENCES project(id))";
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
				project.setSkills(getProjectSkills(project.getId()));
				setProjectBids(project);
			}


			st.close();
			con.close();
		}catch(SQLException se){
			se.printStackTrace();
		}

		return projects;
	}

	public static Project getProject(String id) {
		String sql = "SELECT * FROM project WHERE id = " + id;
		try {
			Project project = null;
			con = DataBaseConnector.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				if(rs.getString(1).equals(id))
					project = ProjectDataMapper.projectDBtoDomain(rs);
			}
			if(project == null)
				return null;

			project.setSkills(getProjectSkills(project.getId()));
			setProjectBids(project);
			return project;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static List<Skill> getProjectSkills(String projectID) {
		List<Skill> skills = new ArrayList<>();
		String sql = "SELECT skillName, point FROM projectSkill WHERE projectID = ?";

		try {
			con = DataBaseConnector.getConnection();
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, projectID);
			ResultSet rss = st.executeQuery();
			while (rss.next())
				skills.add(SkillDataMapper.skillDBtoDomain(rss));

			rss.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return skills;
	}

	private static void setProjectBids(Project project) {
		String sql = "SELECT userID, amount FROM bid WHERE projectID = ?";
		try {
			con = DataBaseConnector.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, project.getId());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				project.addBid(new Bid(rs.getString(1), project.getId(), rs.getInt(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addBidToDB(Bid bid) {
		String sql = "INSERT INTO bid " + BID_COLLUMNS + " VALUES (?, ?, ?)";

		try {
			con = DataBaseConnector.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, bid.getBiddingUserID());
			stmt.setString(2, bid.getProjectID());
			stmt.setInt(3, bid.getBidAmount());
			stmt.executeUpdate();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
