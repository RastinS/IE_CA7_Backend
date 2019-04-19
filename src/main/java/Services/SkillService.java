package Services;

import Database.Database;
import Models.Skill;

public class SkillService {

    public static boolean isSkillValid(String skillName) {
        for(Skill skill : Database.getSkills()) {
            if(skill.getName().equals(skillName))
                return true;
        }
        return false;
    }
}
