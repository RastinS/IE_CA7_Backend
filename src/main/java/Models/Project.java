package Models;

import Repositories.SkillRepository;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Project {
	private String      id;
	private String      title;
	private String      description;
	private String      imageUrl;
	private List<Skill> skills = new ArrayList<>();
	private List<Bid>   bids = new ArrayList<>();
	private int         budget;
	private long        deadline;
	private User        winner;

	public String getId () {
		return id;
	}

	public void setId (String id) {
		this.id = id;
	}

	public String getTitle () {
		return title;
	}

	public void setTitle (String title) {
		this.title = title;
	}

	public String getDescription () {
		return description;
	}

	public void setDescription (String description) {
		this.description = description;
	}

	public String getImageUrl () {
		return imageUrl;
	}

	public void setImageUrl (String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public List<Skill> getSkills () {
		return skills;
	}

	public void setSkills (String skills) throws JSONException {
		this.skills = SkillRepository.setSkills(skills, "");
	}

	@JsonManagedReference
	public List<Bid> getBids () {
		return bids;
	}

	public void setBids (List<Bid> bids) {
		this.bids = bids;
	}

	public int getBudget () {
		return budget;
	}

	public void setBudget (int budget) {
		this.budget = budget;
	}

	public long getDeadline () {
		return deadline;
	}

	public void setDeadline (long deadline) {
		this.deadline = deadline;
	}

	public User getWinner () {
		return winner;
	}

	public void setWinner (User winner) {
		this.winner = winner;
	}

	public String getPrintableSkillSet () {
		StringBuilder ret = new StringBuilder();
		for (Skill skill : skills) {
			ret.append(skill.getName()).append(": ").append(skill.getPoint()).append("; ");
		}
		return ret.toString();
	}

	public void addBid(Bid bid) {
		bids.add(bid);
	}

	public void addSkill(Skill skill) {skills.add(skill);}
}
