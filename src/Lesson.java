import java.util.HashMap;
import java.util.List;

/**
 * This class holds all the lesson information
 */
public class Lesson {

    //the lesson id
    private int id;
    //the lesson name
    private String name;
    //the available hours of the lesson for each room
    private HashMap<Room, Integer> availableHoursPerRoom;
    //the teachers that can teach this lesson
    private List<Teacher> teachers;

    public Lesson() {
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

    public HashMap<Room, Integer> getAvailableHoursPerRoom() {
        return availableHoursPerRoom;
    }

    public void setAvailableHoursPerRoom(HashMap<Room, Integer> availableHoursPerRoom) {
        this.availableHoursPerRoom = availableHoursPerRoom;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lesson = (Lesson) o;

        return id == lesson.id;
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
