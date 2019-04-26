package DataManagers.ProjectData;

import DataManagers.DataBaseConnector;
import Models.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ProjectDataHandler {
	private static final String COLUMNS = "(id, title, budget, description, imageUrl, deadline)";
	private static Connection con = null;

	public static void init() {
		try {
			con = DataBaseConnector.getConnection();
			Statement st = con.createStatement();

			String sql = "DROP TABLE project";
			st.executeUpdate(sql);

			sql = "CREATE TABLE " +
					"project " +
					"(id TEXT PRIMARY KEY, " +
					"title TEXT, " +
					"budget INTEGER, " +
					"description TEXT, " +
					"imageUrl TEXT, " +
					"deadline INTEGER)";
			st.executeUpdate(sql);

			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addProjectsToDB(List<Project> projects) {
		String sql = "INSERT INTO project " + COLUMNS + " VALUES (?, ?, ?, ?, ?, ?)";

		try {
			con = DataBaseConnector.getConnection();
			PreparedStatement st;
			for(Project project : projects) {
				st = con.prepareStatement(sql);
				st.setString(1, project.getId());
				st.setString(2, project.getTitle());
				st.setInt(3, project.getBudget());
				st.setString(4, project.getDescription());
				st.setString(5, project.getImageUrl());
				st.setLong(6, project.getDeadline());
				st.executeUpdate();
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
