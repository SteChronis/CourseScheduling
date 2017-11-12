import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class holds all the teacher information
 */
public class Teacher{
    //the teacher id
    private int id;
    //the teacher name
    private String name;
    //the lesson ids the this teacher can teach
    private List<Integer> lessons = new ArrayList<Integer>();
    //the available that this teacher can teach for a day
    private HashMap<String, Integer> availableHoursPerDay;
    //the available hours that this teacher can teach for a week
    private int availableHoursPerWeek;

    public Teacher() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getLessons() {
        return lessons;
    }

    public void setLessons(List<Integer> lessons) {
        this.lessons = lessons;
    }

    public HashMap<String, Integer> getAvailableHoursPerDay() {
        return availableHoursPerDay;
    }

    public void setAvailableHoursPerDay(HashMap<String, Integer> availableHoursPerDay) {
        this.availableHoursPerDay = availableHoursPerDay;
    }

    public int getAvailableHoursPerWeek() {
        return availableHoursPerWeek;
    }

    public void setAvailableHoursPerWeek(int availableHoursPerWeek) {
        this.availableHoursPerWeek = availableHoursPerWeek;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Teacher teacher = (Teacher) o;

        return id == teacher.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
