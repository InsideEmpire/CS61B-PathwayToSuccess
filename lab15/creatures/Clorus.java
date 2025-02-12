package creatures;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.Occupant;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Clorus extends Creature {
    public Clorus(double e) {
        super("clorus");
        this.energy = e;
    }

    @Override
    public void move() {
        this.energy -= 0.03;
    }

    @Override
    public void attack(Creature c) {
        this.energy += c.energy();
    }

    @Override
    public Creature replicate() {
        this.energy /= 2;
        return new Clorus(this.energy);
    }

    @Override
    public void stay() {
        this.energy -= 0.01;
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plip = getNeighborsOfType(neighbors, "plip");
        Random random = new Random();
        if (empties.size() == 0) {
            return new Action(Action.ActionType.STAY);
        } else if (plip.size() > 0) {
            return new Action(Action.ActionType.ATTACK, plip.get(random.nextInt(plip.size())));
        } else if (this.energy >= 1) {
            return new Action(Action.ActionType.REPLICATE, empties.get(random.nextInt(empties.size())));
        } else {
            return new Action(Action.ActionType.MOVE, empties.get(random.nextInt(empties.size())));
        }
    }

    @Override
    public Color color() {
        return color(34, 0, 231);
    }
}
