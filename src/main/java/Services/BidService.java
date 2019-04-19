package Services;

import ErrorClasses.*;
import Models.Bid;
import Models.Project;
import Models.Skill;
import Models.User;

public class BidService {

	private static boolean isUserSuggested (Project project, User user) {
		for (Bid bid : project.getBids()) {
			if (bid.getBiddingUser().getId().equals(user.getId())) {
				return true;
			}
		}
		return false;
	}

	private static boolean isBidGraterThanBudget (Project project, int bidAmount) {
		return bidAmount <= project.getBudget();
	}

	private static boolean isUserSkillValidForProject (User user, Project project) {
		for (Skill skill : project.getSkills()) {
			if (user.getSkillPoint(skill) < skill.getPoint()) {
				return false;
			}
		}
		return true;
	}

	public static void addBid (String userID, String projectID, int bidAmount) throws UserNotLoggedInException, DuplicateBidException, UserNotFoundException, ProjectNotFoundException, UserSkillsNotMatchWithProjectSkillException, BidGraterThanBudgetException {
		User user = UserService.findUserWithID(userID);
		if (user == null) {
			throw new UserNotFoundException();
		}

		Project project = ProjectService.getProject(projectID);
		if (project == null) {
			throw new ProjectNotFoundException();
		}

		if (user.isLoggedIn()) {
			if (!BidService.isUserSuggested(project, user)) {
				if (BidService.isBidGraterThanBudget(project, bidAmount)) {
					if (BidService.isUserSkillValidForProject(user, project)) {
						Bid bid = new Bid(user, project, bidAmount);
						project.addBid(bid);
					} else {
						throw new UserSkillsNotMatchWithProjectSkillException();
					}
				} else {
					throw new BidGraterThanBudgetException();
				}
			} else {
				throw new DuplicateBidException();
			}
		} else {
			throw new UserNotLoggedInException();
		}
	}
}
