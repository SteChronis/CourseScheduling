import java.util.HashMap;
import java.util.Map;

/**
 * This class holds all the room information
 */
public class Room implements Comparable{

    public static final int INSTANCES = 3;

    private String grade;
    //the name of the room
    private String name;

    public Room(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * the name of the room is and identifier too so we use it to compare the object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        return name != null ? name.equals(room.name) : room.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Object o) {
        return name.compareTo(((Room)o).getName());
    }
}
