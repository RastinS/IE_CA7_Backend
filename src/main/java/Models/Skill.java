package Models;

import java.util.ArrayList;
import java.util.List;

public class Skill {
    private String name;
    private int    point;
    private List<String> endorsedBy = new ArrayList<String>();

    public Skill () {}

    public Skill (String name) {
        this.name = name;
        point = 0;
    }

    public Skill (String name, int point) {
        this.name = name;
        this.point = point;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getName () {
        return name;
    }

    public int getPoint () {
        return point;
    }

    public void addPoint() {this.point += 1;}

    public void addEndorser(String ID) {
        endorsedBy.add(ID);
    }

    public List<String> getEndorsers() {return endorsedBy;}
}
