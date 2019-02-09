package edu.isu.cs.cs3308;

import edu.isu.cs.cs3308.structures.Queue;
import edu.isu.cs.cs3308.structures.impl.LinkedQueue;
import java.util.Random;

/**
 * Class representing a wait time simulation program.
 *
 * @author Isaac Griffith
 * @author Aaron Harvey
 */
public class Simulation {

    private int arrivalRate;
    private int maxNumQueues;
    private Random r;
    private int numIterations = 50;

    private int dailyMinutes = 720;
    private int countPeopleDone = 0;
    private int queueWaitTime = 0;
    private int iterateWaitTime = 0;
    private LinkedQueue[] allLines;

    /**
     * Constructs a new simulation with the given arrival rate and maximum number of queues. The Random
     * number generator is seeded with the current time. This defaults to using 50 iterations.
     *
     * @param arrivalRate the integer rate representing the maximum number of new people to arrive each minute
     * @param maxNumQueues the maximum number of lines that are open
     */
    public Simulation(int arrivalRate, int maxNumQueues) {
        this.arrivalRate = arrivalRate;

        this.maxNumQueues = maxNumQueues;
        r = new Random();
    }

    /**
     * Constructs a new siulation with the given arrival rate and maximum number of queues. The Random
     * number generator is seeded with the provided seed value, and the number of iterations is set to
     * the provided value.
     *
     * @param arrivalRate the integer rate representing the maximum number of new people to arrive each minute
     * @param maxNumQueues the maximum number of lines that are open
     * @param numIterations the number of iterations used to improve data
     * @param seed the initial seed value for the random number generator
     */
    public Simulation(int arrivalRate, int maxNumQueues, int numIterations, int seed) {
        this(arrivalRate, maxNumQueues);
        r = new Random(seed);
        this.numIterations = numIterations;
    }

    /**
     * Executes the Simulation
     * 1: Store the current time (minuteTimer) in the queue as that persons enter time.
     * 2: When they leave get the minuteTimer - that persons time.
     * 3: Add that number to the total amount of wait time (queueWaitTime)
     * 4: Add to person counter to know how many have gone through (countPeopleDone)
     * 5: Once minuteTimer = 720: queueWaitTime / countPeopleDone
     * 6: Add that to a the average wait time per iteration (iterateWaitTime)
     * 7: Repeat the steps 1-6 (numIterations) more times
     * 8: Get final average wait time as: iterateWaitTime / numIterations
     */
    public void runSimulation() {
        for (int numQueue = 1; numQueue <= maxNumQueues; numQueue++) {
            for (int numLoop = 0; numLoop < numIterations; numLoop++) {
                createQueuesNeeded(numQueue);
                getAverageWaitTime(numQueue);
            }
        }
    }

    private void createQueuesNeeded(int numOfQueues) {
        allLines = new LinkedQueue[numOfQueues];

        for (int makeLanes = 0; makeLanes < numOfQueues; makeLanes++) {
            allLines[makeLanes] = new LinkedQueue<Integer>();
        }
    }

    private void getAverageWaitTime(int numOfQueues) {
        for (int timer = 0; timer < dailyMinutes; timer++) {

        }
    }

    private int getPeoplePerMinute() {
        int numPeople = getRandomNumPeople(r.nextDouble());

        return (numPeople > arrivalRate) ? arrivalRate : numPeople;
    }


    /**
     * returns a number of people based on the provided average
     *
     * @param avg The average number of people to generate
     * @return An integer representing the number of people generated this minute
     */
    //Don't change this method.
    private static int getRandomNumPeople(double avg) {
        Random r = new Random();
        double L = Math.exp(-avg);
        int k = 0;
        double p = 1.0;
        do {
            p = p * r.nextDouble();
            k++;
        } while (p > L);
        return k - 1;
    }
}
