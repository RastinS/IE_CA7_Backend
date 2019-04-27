package DataManagers.UserData;

import Models.Skill;
import Models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class UserDataMapper {

	static void UserDomainToDB(User user, PreparedStatement st) {
		try {
			st.setString(1, user.getId());
			st.setString(2, user.getFirstName());
			st.setString(3, user.getLastName());
			st.setString(4, user.getJobTitle());
			st.setString(5, user.getProfilePictureURL());
			st.setString(6, user.getBio());
			if (user.isLoggedIn())
				st.setInt(7, 1);
			else
				st.setInt(7, 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static void userSkillDomainToDB(User user, Skill skill, PreparedStatement st) {
		try {
			st.setString(1, user.getId());
			st.setString(2, skill.getName());
			st.setInt(3, skill.getPoint());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static User UserDBtoDomain(ResultSet rs) {
		User user = new User();
		try {
			user.setId(rs.getString(1));
			user.setFirstName(rs.getString(2));
			user.setLastName(rs.getString(3));
			user.setJobTitle(rs.getString(4));
			user.setProfilePictureURL(rs.getString(5));
			user.setBio(rs.getString(6));
			if (rs.getInt(7) == 1)
				user.setLoggedIn(true);
			else
				user.setLoggedIn(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	static Skill userSkillDBtoDomain(ResultSet rs) {
		Skill skill = new Skill();
		try {
			skill.setName(rs.getString(1));
			skill.setPoint(rs.getInt(2));
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return skill;
	}
}
