import java.time.DayOfWeek;
import java.util.*;

/**
 * This class hold the rooms, the teachers and the lesson
 */
public class Data {
    //all lessons
    private final List<Lesson> lessons;
    //all teachers
    private final List<Teacher> teachers;
    //all rooms
    private final List<Room> rooms;
    //the fitness per generation; it is used for statistics
    private Map<Integer, Double> fitnessPerGeneration;
    //the conflicts per generation; it is used for statistics
    private Map<Integer, Integer> conflictsPerGeneration;

    /**
     * the constructor of the class
     * call the parser the get the info
     */
    public Data() {
        this.rooms = Parser.parseRooms();
        this.teachers = Parser.parseTeachers();
        this.lessons = Parser.parseLessons(teachers, rooms);
        fitnessPerGeneration = new HashMap<>();
        conflictsPerGeneration = new HashMap<>();
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Map<Integer, Double> getFitnessPerGeneration() {
        return fitnessPerGeneration;
    }

    public void setFitnessPerGeneration(Map<Integer, Double> fitnessPerGeneration) {
        this.fitnessPerGeneration = fitnessPerGeneration;
    }

    public Map<Integer, Integer> getConflictsPerGeneration() {
        return conflictsPerGeneration;
    }

    public void setConflictsPerGeneration(Map<Integer, Integer> conflictsPerGeneration) {
        this.conflictsPerGeneration = conflictsPerGeneration;
    }

    /**
     * this method gets all the lessons for a room and the available hour,
     * for knowing exactly the size of the lessons for each room
     * <p>
     * shuffles the generated list to achieve random data
     *
     * @param room the room
     * @return
     */
    public List<Lesson> getShuffledLessonsForRoom(Room room) {
        List<Lesson> shuffledLessons = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (!lesson.getTeachers().isEmpty() && lesson.getAvailableHoursPerRoom().containsKey(room)) {
                for (int i = 0; i < lesson.getAvailableHoursPerRoom().get(room); i++) {
                    shuffledLessons.add(lesson);
                }
            }
        }
        Collections.shuffle(shuffledLessons);
        return shuffledLessons;
    }

    /**
     * this method gets all the days in random order
     * for achieving randomized schedule
     */
    public List<String> getShuffledDays() {
        List<String> shuffledDays = new ArrayList<>();
        for (int i = 1; i <= Schedule.DAYS; i++) {
            shuffledDays.add(DayOfWeek.of(i).name());
        }
        return shuffledDays;
    }

    /**
     * this method method finds how many hours should each day have
     * for not having remaining hours for a lesson
     *
     * @param lessonsList all lessons for a room
     * @return
     */
    public List<Integer> getHoursPerDay(List<Lesson> lessonsList) {
        List<Integer> hoursPerDay = new ArrayList<>();
        int averageHours = lessonsList.size() / Schedule.DAYS;
        int remainingHours = lessonsList.size() % Schedule.DAYS;
        for (int i = 1; i <= Schedule.DAYS; i++) {
            if (remainingHours > 0) {
                hoursPerDay.add(averageHours + 1);
                remainingHours--;
            } else {
                hoursPerDay.add(averageHours);
            }
        }
        return hoursPerDay;
    }

    @Override
    public String toString() {
        return "Data{" +
                "lessons=" + lessons +
                ", teachers=" + teachers +
                ", rooms=" + rooms +
                '}';
    }

    /**
     * this method prints the schedule
     */
    public void print() {
        System.out.println("Rooms");
        for (Room room : rooms) {
            System.out.println(room + " ");
        }
        System.out.println();
        System.out.println("------------------------");
        System.out.println();
        System.out.println("Teachers");
        for (Teacher teacher : teachers) {
            System.out.println("ID:" + teacher.getId() + " Name:" + teacher.getName() + " AvailableHoursPerDay:" + teacher.getAvailableHoursPerDay() + " AvailableHoursPerWeek:" + teacher.getAvailableHoursPerWeek());
        }
        System.out.println();
        System.out.println("------------------------");
        System.out.println();
        System.out.println("Lessons");
        for (Lesson lesson : lessons) {
            System.out.print("ID:" + lesson.getId() + " Name:" + lesson.getName());
            System.out.print(" Teachers:");
            for (Teacher teacher : lesson.getTeachers()) {
                System.out.print(teacher + " ");
            }
            System.out.println();
        }
    }
}
