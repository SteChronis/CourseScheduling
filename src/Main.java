public class Main {

    //the population size
    public static final int POPULATION_SIZE = 30;
    //the number of the generations
    private static final int GENERATIONS = 100000;

    public static void main(String[] args) {
        Data data = new Data();
        Genetics genetics = new Genetics(data);
        Population population = new Population(POPULATION_SIZE, data, true);
        System.out.println("Starting making evolutions for " + GENERATIONS + " generations");
        for (int generations = 1; generations <= GENERATIONS; generations++) {
            population = genetics.evolve(population);
            data.getFitnessPerGeneration().put(generations, population.getFittest().getFitness());
            data.getConflictsPerGeneration().put(generations, population.getFittest().getNumberOfConflicts());
            if(population.getFittest().getNumberOfConflicts() == 0){
                break;
            }
        }
        System.out.println("Solution found with " + population.getFittest().getNumberOfConflicts() + " conflicts");
        ProcessOutput.generateScheduleOutput(population.getFittest(), data);
        System.out.println("Results saved at " + ProcessOutput.FILE_NAME);
    }
}
