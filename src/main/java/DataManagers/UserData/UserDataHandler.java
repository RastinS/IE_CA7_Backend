package DataManagers.UserData;

import DataManagers.DataBaseConnector;
import DataManagers.DataManager;
import DataManagers.SkillData.SkillDataMapper;
import Models.Skill;
import Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDataHandler {
	private static final String USER_COLUMNS = "(id, firstName, lastName, jobTitle, profilePictureUrl, bio, isLoggedIn)";
	private static final String SKILL_COLUMNS = "(userID, skillName, point)";
	private static Connection con = null;

	public static void init() {
		try {
			DataManager.dropExistingTable("user");
			DataManager.dropExistingTable("userSkill");
			con = DataBaseConnector.getConnection();
			Statement st = con.createStatement();

			String sql = "CREATE TABLE " +
					"user " +
					"(id TEXT PRIMARY KEY, " +
					"firstName TEXT, " +
					"lastName TEXT, " +
					"jobTitle TEXT, " +
					"profilePictureUrl TEXT, " +
					"bio TEXT, " +
					"isLoggedIn INTEGER)";
			st.executeUpdate(sql);

			sql = "CREATE TABLE " +
					"userSkill " +
					"(userID TEXT, " +
					"skillName TEXT, " +
					"point INTEGER, " +
					"FOREIGN KEY (userID) REFERENCES user(id)," +
					"FOREIGN KEY (skillName) REFERENCES skill(name))";
			st.executeUpdate(sql);

			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addUsers(List<User> users) {
		String userSql = "INSERT INTO user " + USER_COLUMNS + " VALUES (?, ?, ?, ?, ?, ?, ?)";
		String skillSql = "INSERT INTO userSkill " + SKILL_COLUMNS + " VALUES (?, ?, ?)";

		try {
			con = DataBaseConnector.getConnection();
			PreparedStatement ust = con.prepareStatement(userSql);
			PreparedStatement sst = con.prepareStatement(skillSql);

			for(User user : users) {
				UserDataMapper.userDomainToDB(user, ust);
				ust.executeUpdate();
				for(Skill skill : user.getSkills()) {
					SkillDataMapper.skillDomainToDB(skill, user.getId(), sst);
					sst.executeUpdate();
				}
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<User> getUsers() {
		Statement stmt;
		List<User> users = new ArrayList<>();
		try{
			con = DataBaseConnector.getConnection();
			stmt = con.createStatement();

			String sql = "SELECT * FROM user";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
				users.add(UserDataMapper.userDBtoDomain(rs));
			rs.close();
			stmt.close();

			sql = "SELECT skillName, point FROM userSkill WHERE userID = ?";
			PreparedStatement st = con.prepareStatement(sql);
			for(User user : users) {
				st.setString(1, user.getId());
				ResultSet rss = st.executeQuery();
				while(rss.next())
					user.addSkill(SkillDataMapper.skillDBtoDomain(rss));
				rss.close();
			}

			st.close();
			con.close();
		}catch(SQLException se){
			se.printStackTrace();
		}

		return users;
	}
}
