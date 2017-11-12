/**
 * This class is the genetics algorithm which performs the evolution by crossovers and mutations
 */
public class Genetics {
    //the number of schedules for selecting from the best schedule
    public static final int TOURNAMENT_SIZE = 5;
    //the mutation rate
    public static final double MUTATION_RATE = 0.015;
    //enables the elitism, this means that we only the best from the initial population
    private static final boolean ELITISM = true;
    //the data
    private Data data;

    public Genetics(Data data) {
        this.data = data;
    }

    /**
     * this method performs the evolution of the population
     */
    public Population evolve(Population population) {
        Population newPopulation = new Population(population.getPopulationSize(), data, false);
        // Keep our best individual if elitism is enabled
        int elitismOffset = 0;
        if (ELITISM) {
            newPopulation.addSchedule(population.getFittest());
            elitismOffset = 1;
        }
        // Crossover population
        // Loop over the new population's size and create individuals from
        // Current population
        for (int i = elitismOffset; i < population.getPopulationSize(); i++) {
            // Select parents
            Schedule parent1 = selectTournamentPopulation(population);
            Schedule parent2 = selectTournamentPopulation(population);
            // Crossover parents
            Schedule child = crossoverSchedule(parent1, parent2);
            // Add child to new population
            newPopulation.addSchedule(child);
        }

        // Mutate the new population a bit to add some new genetic material
        for (int i = elitismOffset; i < newPopulation.getPopulationSize(); i++) {
            mutateSchedule(newPopulation.getSchedules().get(i));
        }
        return newPopulation;
    }

    /**
     * this method gets crossover two schedules
     * gets random genes(classes) and generates a child
     */
    private Schedule crossoverSchedule(Schedule parent1, Schedule parent2) {
        // Create new child schedule
        Schedule child = new Schedule(data);
        // Get start and end sub tour positions for parent1's schedule
        int start = (int) (Math.random() * parent1.getClasses().size());
        int end = (int) (Math.random() * parent1.getClasses().size());

        // Loop and add the sub tour from parent1 to our child
        for (int i = 0; i < child.getClasses().size(); i++) {
            // If our start position is less than the end position
            if (start < end && i > start && i < end) {
                child.getClasses().set(i, parent1.getClasses().get(i));
            } // If our start position is larger
            else if (start > end) {
                if (!(i < start && i > end)) {
                    child.getClasses().set(i, parent1.getClasses().get(i));
                }
            }
        }
        // Loop through parent2's class schedule
        for (int i = 0; i < parent2.getClasses().size(); i++) {
            // If child doesn't have the class and its a valid child; add it
            if (!child.getClasses().contains(parent2.getClasses().get(i))) {
                // Loop to find a spare position in the child's schedule
                for (int j = 0; j < child.getClasses().size(); j++) {
                    // Spare position found, add city
                    if (child.getClasses().get(j) == null) {
                        child.getClasses().set(j, parent2.getClasses().get(i));
                        break;
                    }
                }
            }
        }
        return child;
    }

    /**
     * this method mutates a schedule
     * puts the classes to different positions
     *
     * @param mutateSchedule
     */
    private static void mutateSchedule(Schedule mutateSchedule) {
        // Loop through classes
        for (int i = 0; i < mutateSchedule.getClasses().size(); i++) {
            // Apply mutation rate
            if (Math.random() < MUTATION_RATE) {
                // Get a second random position in the class
                int j = (int) (mutateSchedule.getClasses().size() * Math.random());

                // Get the classes at target position in schedule
                Class class1 = mutateSchedule.getClasses().get(i);
                Class class2 = mutateSchedule.getClasses().get(j);

                // Swap them around
                mutateSchedule.getClasses().set(j, class1);
                mutateSchedule.getClasses().set(i, class2);
            }
        }
    }

    /**
     * this method creates a population from random schedules of the initial population and finds the fittest
     *
     * @param population
     * @return
     */
    public Schedule selectTournamentPopulation(Population population) {
        // Create a tournament population
        Population tournament = new Population(TOURNAMENT_SIZE, data, false);
        // For each place in the tournament get a random candidate tour and
        // add it
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            int randomId = (int) (Math.random() * population.getPopulationSize());
            tournament.addSchedule(population.getSchedules().get(randomId));
        }
        // Get the fittest schedule
        return tournament.getFittest();
    }
}
