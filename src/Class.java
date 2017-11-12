import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 10/11/2017.
 */
public class Class {

    private static int SEQUENCE = 0;
    private int id;
    private String day;
    private int hour;
    private Room room;
    private Lesson lesson;
    private Teacher teacher;

    public Class(Lesson lesson, Teacher teacher, String day, int hour, Room room) {
        this.id = SEQUENCE;
        this.lesson = lesson;
        this.teacher = teacher;
        this.day = day;
        this.hour = hour;
        this.room = room;
        SEQUENCE++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Room getRoom() {
        return room;
    }

    public int getHour() {
        return hour;
    }

    public static void resetSequence(){
        SEQUENCE = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Class aClass = (Class) o;

        if (hour != aClass.hour) return false;
        if (day != null ? !day.equals(aClass.day) : aClass.day != null) return false;
        if (room != null ? !room.equals(aClass.room) : aClass.room != null) return false;
        if (lesson != null ? !lesson.equals(aClass.lesson) : aClass.lesson != null) return false;
        return teacher != null ? teacher.equals(aClass.teacher) : aClass.teacher == null;
    }

    @Override
    public int hashCode() {
        int result = day != null ? day.hashCode() : 0;
        result = 31 * result + hour;
        result = 31 * result + (room != null ? room.hashCode() : 0);
        result = 31 * result + (lesson != null ? lesson.hashCode() : 0);
        result = 31 * result + (teacher != null ? teacher.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Class{" +
                "id=" + id +
                ", day='" + day + '\'' +
                ", hour=" + hour +
                ", room=" + room +
                ", lesson=" + lesson +
                ", teacher=" + teacher +
                '}';
    }
}
