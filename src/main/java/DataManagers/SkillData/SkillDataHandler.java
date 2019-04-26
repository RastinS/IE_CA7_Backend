package DataManagers.SkillData;

import DataManagers.DataBaseConnector;
import Models.Skill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SkillDataHandler {
	private static final String COLUMNS = "(name)";
	private static Connection con = null;

	public static void init() {
		try {
			con = DataBaseConnector.getConnection();
			Statement st = con.createStatement();

			String sql;

			Statement stmt = con.createStatement();
			sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='skill'";
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()) {
				if("skill".equals(rs.getString("name"))) {
					stmt.close();
					sql = "DROP TABLE skill";
					st.executeUpdate(sql);
				}
			}
			rs.close();

			sql = "CREATE TABLE " +
					"skill " +
					"(name TEXT PRIMARY KEY)";
			st.executeUpdate(sql);

			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addSkills(List<Skill> skills) {
		String sql = "INSERT INTO skill " + COLUMNS + " VALUES (?)";

		try {
			con = DataBaseConnector.getConnection();
			PreparedStatement st;
			for(Skill skill : skills) {
				st = con.prepareStatement(sql);
				st.setString(1, skill.getName());
				st.executeUpdate();
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<Skill> getSkills() {
		Statement stmt;
		List<Skill> skills = new ArrayList<>();
		try{
			con = DataBaseConnector.getConnection();
			stmt = con.createStatement();

			String sql = "SELECT * FROM skill";
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next())
				skills.add(new Skill(rs.getString("name")));

			rs.close();
			con.close();
		}catch(SQLException se){
			se.printStackTrace();
		}

		return skills;
	}
}
