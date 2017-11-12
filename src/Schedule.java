import java.time.DayOfWeek;
import java.util.*;

/**
 * This class represents a created schedule
 */
public class Schedule implements Comparable {

    //the number of the days of the week that lessons are teached
    public final static int DAYS = 5;
    //the maximum hours for classes for a day
    public final static int MAX_HOURS = 7;
    //the data(rooms,lessons, teachers)
    private Data data;
    //the classes as a list, represents the schedule
    private LinkedList<Class> classes;
    //the fitness of the schedule
    private double fitness = -1;
    //indicates if fitness of the schedule has changed, used for not computing fitness again and again
    private boolean hasFitnessChanged = true;
    //the number of conflicts of the schedule
    private int numberOfConflicts;

    /**
     * the constructor of the class
     *
     * @param data the data
     */
    public Schedule(Data data) {
        this.data = data;
        initializeSchedule();
    }

    public LinkedList<Class> getClasses() {
        return classes;
    }

    /**
     * this method calculates and return the fitness
     *
     * return the fitness of the schedule
     */
    public double getFitness() {
        if (hasFitnessChanged) {
            fitness = calculateFitness();
            hasFitnessChanged = false;
        }
        return fitness;
    }

    /**
     * this method gets the number of the conflicts
     *
     * @return the number of the conflicts
     */
    public int getNumberOfConflicts() {
        return numberOfConflicts;
    }

    /**
     * this method calculates the fitness of the schedule
     *
     * @return the fitness of the schedule
     */
    public double calculateFitness() {
        HashMap<Teacher,HashMap<String, Integer>> hoursForTeacherPerDay = new HashMap<>();
        HashMap<Teacher,Integer> hoursForTeacherPerWeek = new HashMap<>();
        HashMap<Lesson,HashMap<Room, Integer>> hoursForLessonPerRoom = new HashMap<>();
        for (Class class1 : classes) {
            for (Class class2 : classes) {
                 if (!class1.equals(class2)) {
                     //teacher teaches for two hours
                    if ((class1.getHour() == (class2.getHour() - 1)) &&
                            class1.getDay().equals(class2.getDay()) &&
                            class1.getTeacher().equals(class2.getTeacher())) {
                        numberOfConflicts++;
                    }
                     //schedule has two lessons at same hour, same day and same room
                    if (class1.getHour() == class2.getHour() &&
                            class1.getDay().equals(class2.getDay()) &&
                            class1.getRoom().equals(class2.getRoom())) {
                        numberOfConflicts++;
                    }
                    //teacher teaches at two rooms at the same day and hour
                    if (class1.getHour() == class2.getHour() &&
                            class1.getDay().equals(class2.getDay()) &&
                            !class1.getRoom().equals(class2.getRoom()) &&
                            class1.getTeacher().equals(class2.getTeacher())) {
                        numberOfConflicts++;
                    }
                }
            }
            addLessonHoursForRoom(class1, hoursForLessonPerRoom);
            addTeacherHoursForDay(class1, hoursForTeacherPerDay);
            addTeacherHoursForWeek(class1, hoursForTeacherPerWeek);
        }
        for(Teacher teacher : data.getTeachers()){
            //teacher teaches more than the available hours per week
            if(hoursForTeacherPerWeek.containsKey(teacher) && teacher.getAvailableHoursPerWeek() < hoursForTeacherPerWeek.get(teacher)){
                numberOfConflicts++;
            }
            //teacher teaches more than the available hours per day
            for(int day = 1; day<=DAYS; day++){
                String currentDate = DayOfWeek.of(day).name();
                if(hoursForTeacherPerDay.containsKey(teacher) && hoursForTeacherPerDay.get(teacher).containsKey(currentDate)) {
                    if (teacher.getAvailableHoursPerDay().get(currentDate) < hoursForTeacherPerDay.get(teacher).get(currentDate)) {
                        numberOfConflicts++;
                    }
                }
            }
        }
        //lesson does not have as many teaching hours as it should
        for(Lesson lesson : data.getLessons()){
            for(Room room : data.getRooms()){
                if(hoursForLessonPerRoom.containsKey(lesson) && hoursForLessonPerRoom.get(lesson).containsKey(room)) {
                    if (lesson.getAvailableHoursPerRoom().get(room) != hoursForLessonPerRoom.get(lesson).get(room)) {
                        numberOfConflicts++;
                    }
                }
            }
        }
        //add the conflicts from the schedule breaks
        numberOfConflicts += findScheduleBreaks();
        return 1 / (double) (numberOfConflicts + 1);
    }

    /**
     * this method counts the breaks of the schedule
     */
    private int findScheduleBreaks(){
        int conflicts = 0;
        for (Room room : data.getRooms()) {
            for(int day = 1; day<=DAYS; day++){
                List<Integer> allHoursForDay = new ArrayList<>();
                for(int hour = 1; hour<=MAX_HOURS; hour++){
                    if(classContainsHour(room, DayOfWeek.of(day).name(), hour)){
                        allHoursForDay.add(hour);
                    }
                }
                Collections.sort(allHoursForDay);
                for(int i = 0; i<allHoursForDay.size()-1; i++){
                    if(allHoursForDay.get(i+1) - allHoursForDay.get(i) == 1){
                        conflicts++;
                    }
                }
            }
        }
        return conflicts;
    }

    /**
     * this method is used for finding the class for a room, day and hour
     */
    private boolean classContainsHour(Room room, String day, int hour) {
        for (Class class1 : classes) {
            if (class1.getRoom().equals(room) &&
                    class1.getDay().equals(day) &&
                    class1.getHour() == hour) {
                return true;
            }
        }
        return false;
    }

    /**
     * this method sums the teachers teaching hour per day
     */
    private void addTeacherHoursForDay(Class class1,HashMap<Teacher,HashMap<String, Integer>> hoursForTeacherPerDay){
        if(hoursForTeacherPerDay.containsKey(class1.getTeacher())){
            if(hoursForTeacherPerDay.get(class1.getTeacher()).containsKey(class1.getDay())){
                hoursForTeacherPerDay.get(class1.getTeacher()).
                        put(class1.getDay(),
                                hoursForTeacherPerDay.get(class1.getTeacher()).get(class1.getDay())+1);
            }else {
                hoursForTeacherPerDay.get(class1.getTeacher()).put(class1.getDay(), 1);
            }
        } else{
            HashMap<String, Integer> hoursPerDay = new HashMap<>();
            hoursPerDay.put(class1.getDay(), 1);
            hoursForTeacherPerDay.put(class1.getTeacher(),hoursPerDay);
        }
    }

    /**
     * this method sums the teachers teaching hour for the week
     */
    private void addTeacherHoursForWeek(Class class1,HashMap<Teacher,Integer> hoursForTeacherPerWeek){
        if(hoursForTeacherPerWeek.containsKey(class1.getTeacher())) {
            hoursForTeacherPerWeek.put(class1.getTeacher(), hoursForTeacherPerWeek.get(class1.getTeacher()) + 1);
        } else{
            hoursForTeacherPerWeek.put(class1.getTeacher(), 1);
        }
    }

    /**
     * this method sums the lesson teaching hours per room
     */
    private void addLessonHoursForRoom(Class class1,HashMap<Lesson,HashMap<Room, Integer>> hoursForLessonPerRoom){
        if(hoursForLessonPerRoom.containsKey(class1.getLesson())){
            if(hoursForLessonPerRoom.get(class1.getLesson()).containsKey(class1.getRoom())){
                hoursForLessonPerRoom.get(class1.getLesson()).
                        put(class1.getRoom(),
                                hoursForLessonPerRoom.get(class1.getLesson()).get(class1.getRoom())+1);
            }else {
                hoursForLessonPerRoom.get(class1.getLesson()).put(class1.getRoom(), 1);
            }
        } else{
            HashMap<Room, Integer> hoursPerRoom = new HashMap<>();
            hoursPerRoom.put(class1.getRoom(), 1);
            hoursForLessonPerRoom.put(class1.getLesson(),hoursPerRoom);
        }
    }

    /**
     * this method builds the schedule
     */
    private void initializeSchedule() {
        classes = new LinkedList<>();
        for (Room room : data.getRooms()) {
            List<Lesson> shuffledLessons = data.getShuffledLessonsForRoom(room);
            List<Integer> hoursPerDay = data.getHoursPerDay(shuffledLessons);
            for (String day : data.getShuffledDays()) {
                for (int hour = 1; hour <= hoursPerDay.get(DayOfWeek.valueOf(day).getValue() - 1); hour++) {
                    Lesson lesson = shuffledLessons.get(hour);
                    Teacher teacher = lesson.getTeachers().get((int) (Math.random() * lesson.getTeachers().size()));
                    classes.add(new Class(lesson, teacher, day, hour, room));
                }
                Collections.shuffle(shuffledLessons);
            }
        }
    }

    /**
     * this method prints all class of the schedule
     */
    public void print() {
        System.out.println();
        System.out.println();
        System.out.println();
        for (Class cl : classes) {
            System.out.println(cl);
        }
    }

    /**
     * compare schedules by the fitness
     */
    @Override
    public int compareTo(Object o) {
        if (this.getFitness() == ((Schedule) o).getFitness()) return 0;
        return this.getFitness() > ((Schedule) o).getFitness() ? -1 : 1;
    }
}
