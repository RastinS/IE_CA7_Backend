package Models;

public class Endorsement {
    private User endorsingUser;
    private User endorsedUser;
    private Skill endorsedSkill;

    public Endorsement(User endorsingUser, User endorsedUser, Skill endorsedSkill) {
        this.endorsingUser = endorsingUser;
        this.endorsedUser = endorsedUser;
        this.endorsedSkill = endorsedSkill;
    }

    public Skill getEndorsedSkill() {
        return endorsedSkill;
    }

    public User getEndorsedUser() {
        return endorsedUser;
    }

    public User getEndorsingUser() {
        return endorsingUser;
    }

    public void setEndorsedSkill(Skill endorsedSkill) {
        this.endorsedSkill = endorsedSkill;
    }

    public void setEndorsedUser(User endorsedUser) {
        this.endorsedUser = endorsedUser;
    }

    public void setEndorsingUser(User endorsingUser) {
        this.endorsingUser = endorsingUser;
    }
}
