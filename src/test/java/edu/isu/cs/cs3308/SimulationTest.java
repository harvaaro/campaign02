package edu.isu.cs.cs3308;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimulationTest {

    private Simulation fixture;

    @Before
    public void setup() {

    }

    @Test
    public void testSingleIteration() {
        fixture = new Simulation(18, 10, 1, 1024);
        testSimulation();
    }

    private void testSimulation() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        fixture.runSimulation();

        System.out.flush();
        System.setOut(old);

        String output = baos.toString();
        String[] strs = output.split(System.lineSeparator());

        String pattern = "^Average wait time using (\\d+) queue\\(s\\): (\\d+)$";
        Pattern p = Pattern.compile(pattern);
        for (int i = 1; i < strs.length; i++) {
            Matcher m = p.matcher(strs[i]);
            if (!m.find()) {
                fail("Does not match expected output format!");
            }
        }
    }

    @Test
    public void testMultipleIterations() {
        fixture = new Simulation(18, 10, 50, 1024);
        testSimulation();
    }

    @Test
    public void testResults() {
        fixture = new Simulation(18, 10, 50, 1024);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        fixture.runSimulation();

        System.out.flush();
        System.setOut(old);

        String output = baos.toString();
        int table[][] = {{1,2,3,4,5,6,7,8,9,10},
                         {320,280,240,200,160,120,80,40,4,1}};

        int results[][] = process(output);

        for (int i = 0; i < table[0].length; i++) {
            assertEquals(table[0][i], results[0][i]);
            assertTrue(inRange(results[1][i], table[1][i]));
        }
    }

    public int[][] process(String s) {
        String[] strs = s.split(System.lineSeparator());

        int[][] results = new int[2][strs.length - 1];

        String pattern = "^Average wait time using (\\d+) queue\\(s\\): (\\d+)$";
        Pattern p = Pattern.compile(pattern);
        for (int i = 1; i < strs.length; i++) {
            Matcher m = p.matcher(strs[i]);
            if (m.find()) {
                results[0][i - 1] = Integer.parseInt(m.group(1));
                results[1][i - 1] = Integer.parseInt(m.group(2));
            } else {
                fail();
            }
        }

        return results;
    }

    /**
     * Boolen test to determine if the provided result is within +/- 1 of the expected value.
     * @param result the value to check
     * @param expected the expected value (centerpoint of the range)
     * @return true if the provided result is within the range of expected, false otherwise
     */
    private boolean inRange(int result, int expected) {
        return (result > expected - 2 && result < expected + 2);
    }
}
