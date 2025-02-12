package creatures;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

public class TestClorus {

    /* Replace with the magic word given in lab.
     * If you are submitting early, just put in "early" */
    public static final String MAGIC_WORD = "";

    @Test
    public void testBasics() {
        // Initialize a Clorus with some energy (e.g., 2 units)
        Clorus c = new Clorus(2);

        // Test the initial energy and color
        assertEquals(2, c.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), c.color());

        // Test MOVE action (should lose 0.03 energy)
        c.move();
        assertEquals(1.97, c.energy(), 0.01);

        // Test STAY action (should lose 0.01 energy)
        c.stay();
        assertEquals(1.96, c.energy(), 0.01);
    }

    @Test
    public void testAttack() {
        // Initialize a Clorus and a Plip
        Clorus c = new Clorus(2);
        Plip p = new Plip(1);

        // The Clorus attacks the Plip and gains energy
        c.attack(p);

        // Clorus should gain energy equal to the Plip's energy (1 unit in this case)
        assertEquals(3, c.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        // Initialize a Clorus with some energy
        Clorus c = new Clorus(2);

        // Clorus replicates, keeping 50% of its energy
        Clorus offspring = (Clorus) c.replicate();

        // Check if offspring has half the energy of the original
        assertNotSame(c, offspring);

        // The original should have lost half of its energy
        assertEquals(c.energy(), c.energy(), 0.01);
    }

    @Test
    public void testChooseActionStay() {
        // Create a Clorus with energy 0.5
        Clorus c = new Clorus(0.5);

        // Create a map of directions with all impassible objects
        HashMap<Direction, Occupant> surrounded = new HashMap<>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        // The Clorus should STAY because there are no empty spaces
        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);
        assertEquals(expected, actual);
    }

    @Test
    public void testChooseActionAttack() {
        // Create a Clorus with enough energy to attack
        Clorus c = new Clorus(2);

        // Create a map of directions with a Plip present
        HashMap<Direction, Occupant> surrounded = new HashMap<>();
        surrounded.put(Direction.TOP, new Plip(1));  // Plip can be attacked
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        // The Clorus should attack the Plip because it's a valid target
        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);
        assertEquals(expected, actual);

        // Create a Clorus with enough energy to attack
        Clorus c2 = new Clorus(2);

        // Create a map of directions with a Plip present
        HashMap<Direction, Occupant> surrounded2 = new HashMap<>();
        surrounded2.put(Direction.TOP, new Plip(1));  // Plip can be attacked
        surrounded2.put(Direction.BOTTOM, new Empty());
        surrounded2.put(Direction.LEFT, new Impassible());
        surrounded2.put(Direction.RIGHT, new Impassible());

        // The Clorus should attack the Plip because it's a valid target
        Action actual2 = c2.chooseAction(surrounded2);
        Action expected2 = new Action(Action.ActionType.ATTACK, Direction.TOP);
        assertEquals(expected2, actual2);
    }

    @Test
    public void testChooseActionReplicate() {
        // Create a Clorus with energy greater than or equal to 1 (enough for replication)
        Clorus c = new Clorus(2);

        // Create a map of directions with only empty spaces
        HashMap<Direction, Occupant> surrounded = new HashMap<>();
        surrounded.put(Direction.TOP, new Empty());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        // The Clorus should replicate to the first available empty space
        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.REPLICATE, Direction.TOP);
        assertEquals(expected, actual);
    }

    @Test
    public void testChooseActionMove() {
        // Create a Clorus with not enough energy to replicate or attack
        Clorus c = new Clorus(0.5);

        // Create a map of directions with only empty or impassible spaces
        HashMap<Direction, Occupant> surrounded = new HashMap<>();
        surrounded.put(Direction.TOP, new Empty());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        // The Clorus should move to an empty space since it can't replicate or attack
        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.MOVE, Direction.TOP);
        assertEquals(expected, actual);
    }

    public static void main(String[] args) {
        System.exit(jh61b.junit.textui.runClasses(TestClorus.class));
    }
}
