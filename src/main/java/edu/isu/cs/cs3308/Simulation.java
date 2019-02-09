package edu.isu.cs.cs3308;

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

    private int numQueue = 0;
    private int dailyMinutes = 720;
    private int currentTimer = 0;
    private int countPeopleDone = 0;
    private int queueWaitedTime = 0;
    private int iterateWaitTime = 0;
    private LinkedQueue<Integer>[] allLines;
    private LinkedQueue<Integer> avgWaitTimeList;

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
     * 1: Store the current time (currentTimer) in the queue as that persons enter time.
     * 2: When they leave get the: currentTimer - that persons time.
     * 3: Add that number to the total amount of wait time (queueWaitedTime)
     * 4: Add to person counter to know how many have gone through (countPeopleDone)
     * 5: Once minuteTimer = dailyMinutes: queueWaitedTime / countPeopleDone
     * 6: Add that to a the average wait time per iteration (iterateWaitTime)
     * 7: Repeat the steps 1-6 (numIterations) more times
     * 8: Get final average wait time as: iterateWaitTime / numIterations
     */
    public void runSimulation() {
        avgWaitTimeList = new LinkedQueue<>();

        for (numQueue = 1; numQueue <= maxNumQueues; numQueue++) {
            for (int numLoop = 0; numLoop < numIterations; numLoop++) {
                createQueueAmount();

                for (currentTimer = 1; currentTimer <= dailyMinutes; currentTimer++) {
                    addPeopleToQueues();
                    removeTwoFromEach();
                }

                iterateWaitTime += (queueWaitedTime / countPeopleDone);
            }

            avgWaitTimeList.offer(iterateWaitTime / numIterations);
            countPeopleDone = 0;
            queueWaitedTime = 0;
            iterateWaitTime = 0;
        }

        avgWaitTimeList.printQueue();
    }

    private void createQueueAmount() {
        allLines = new LinkedQueue[numQueue];

        for (int makeLanes = 0; makeLanes < numQueue; makeLanes++) {
            allLines[makeLanes] = new LinkedQueue<>();
        }
    }

    private void addPeopleToQueues() {
        int minutePeople = getPeoplePerMinute();

        for (int person = 0; person < minutePeople; person++) {
            addPersonToShortest();
        }
    }

    private int getPeoplePerMinute() {
//        int numPeople = getRandomNumPeople(r.nextDouble());
//        return (numPeople > arrivalRate) ? arrivalRate : numPeople;
        return getRandomNumPeople(arrivalRate);
    }

    private void addPersonToShortest() {
        int minSize = allLines[0].size();
        int minIndex = 0;

        if (numQueue > 1) {
            for (int checkIndex = 1; checkIndex < numQueue; checkIndex++) {
                int tempSize = allLines[checkIndex].size();

                if (tempSize < minSize) {
                    minSize = tempSize;
                    minIndex = checkIndex;
                }
            }
        }

        allLines[minIndex].offer(currentTimer-1);
    }

    private void removeTwoFromEach() {
        for (int whichQueue = 0; whichQueue < numQueue; whichQueue++) {
            int queueSize = allLines[whichQueue].size();

            if (queueSize >= 2) {
                queueWaitedTime += currentTimer - allLines[whichQueue].poll();
                queueWaitedTime += currentTimer - allLines[whichQueue].poll();
                countPeopleDone += 2;
            }
            else if (queueSize == 1) {
                queueWaitedTime += currentTimer - allLines[whichQueue].poll();
                countPeopleDone += 1;
            }
        }
    }

    public void tempTESTPRINT() {
        avgWaitTimeList.printQueue();
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
