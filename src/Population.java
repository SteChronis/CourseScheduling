import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a population of schedules
 */
public class Population {
    //the population
    private List<Schedule> schedules;

    /**
     * the constructor of the class
     *
     * @param populationSize the number of schedules for the population
     * @param data           the data
     * @param initialize     indicates if we need to create a new population
     */
    public Population(int populationSize, Data data, boolean initialize){
        schedules = new ArrayList<>(populationSize);
        if(initialize) {
            for (int i = 0; i < populationSize; i++) {
                schedules.add(new Schedule(data));
            }
        }
    }

    public void addSchedule(Schedule schedule){
        schedules.add(schedule);
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    /**
     * this method gets the schedule with the better fitness
     * @return
     */
    public Schedule getFittest(){
        Collections.sort(schedules);
        return schedules.get(0);
    }

    public int getPopulationSize() {
        return schedules.size();
    }
}
