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
	 * 01: Print out the (arrivalRate) to show how many will be added to the queues
	 * 02: For each number of queues (numQueue) up to and including the (maxNumQueues) loop
	 * 		This is for doing each amount of queues, in our case 1 through 10
	 * 03: For each number of loops (numLoop) up to the total iterations (numIterations) loop
	 * 		This is for doing the test multiple times to get a good average, in our case 50
	 * 04: Create the number of queues needed (allList) based off of the (numQueue)
	 * 05: For each current minute (currentTimer) up to and including the daily amount (dailyMinutes) loop
	 * 		This is for adding and removing people to the queue, in our case through 720 minutes
	 * 06: Add a random number of people to the lineup based on the (arrivalRate)
	 * 		This also makes sure each person joins the shortest queue as the are added
	 * 		Also stores the currentTimer as the value in that queue position for later comparison
	 * 07: Remove 2 people from each queue, or 1 if there is only 1, or none if the queue is empty
     * 		When they leave get the: currentTimer - that persons time.
     * 		Add that number to the total amount of wait time (queueWaitedTime)
     * 		Add to person counter to know how many have gone through (countPeopleDone)
     * 08: Once minuteTimer = dailyMinutes: queueWaitedTime / countPeopleDone
     * 		Add that to a the average wait time per iteration (iterateWaitTime)
     * 09: Repeat the steps 3-8 (numIterations) more times
     * 10: Print final average wait time as: iterateWaitTime / numIterations
	 * 11: Reset a few variables to not carry over values into the next loop
	 * 12: Repeat the steps 2-11 (maxNumQueues) more times
     */
    public void runSimulation() {
        System.out.println("Arrival rate: " + arrivalRate);

        for (numQueue = 1; numQueue <= maxNumQueues; numQueue++) {
            for (int numLoop = 0; numLoop < numIterations; numLoop++) {
                createQueueAmount();

                for (currentTimer = 1; currentTimer <= dailyMinutes; currentTimer++) {
                    addPeopleToQueues();
                    removeTwoFromEach();
                }

                iterateWaitTime += (queueWaitedTime / countPeopleDone);
            }

            System.out.println("Average wait time using "
                    +(numQueue)+" queue(s): "+(iterateWaitTime / numIterations));

            countPeopleDone = 0;
            queueWaitedTime = 0;
            iterateWaitTime = 0;
        }
    }

	/**
	 * Creates the needed array size for the amount of queues to use
	 */
	private void createQueueAmount() {
        allLines = new LinkedQueue[numQueue];

        for (int makeLanes = 0; makeLanes < numQueue; makeLanes++) {
            allLines[makeLanes] = new LinkedQueue<>();
        }
    }

	/**
	 * Gets a random number of people that entered in that minute
	 * Inserts those people into the shortest queue per person
	 */
	private void addPeopleToQueues() {
        int minutePeople = getRandomNumPeople(arrivalRate);

        for (int person = 0; person < minutePeople; person++) {
            addPersonToShortest();
        }
    }

	/**
	 * Iterates through the array to find the shortest queue
	 * Then adds the current time to that queue, to represent
	 * A new person being added to that queue
	 */
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

	/**
	 * Iterates through the array of queues and remove 2 people form each
	 * If there is only 1 person it only removes 1, otherwise none
	 * Adds to the wait time tally, and increments a count of people
	 */
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
