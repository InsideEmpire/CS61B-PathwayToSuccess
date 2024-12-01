package lab11.graphs;

import edu.princeton.cs.algs4.Stack;

/**
 *  @author Josh Hug
 * @author Garrison
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int[] parentOf;

    public MazeCycles(Maze m) {
        super(m);
        parentOf = new int[maze.V()];
        for (int i = 1; i < maze.V(); i += 1) {
            parentOf[i] = Integer.MAX_VALUE;
        }
    }

    @Override
    public void solve() {
        dfsHelper(0);
    }

    private void dfsHelper(int start) {

        Stack<Integer> dfsStack = new Stack<>();
        Stack<Integer> loopStack = new Stack<>();

        dfsStack.push(start);
        int curr;

        while (!dfsStack.isEmpty()) {

            curr = dfsStack.pop();
            marked[curr] = true;
            loopStack.push(curr);

            announce();

            for (int next : maze.adj(curr)) {
                if (!marked[next]) {
                    dfsStack.push(next);
                    parentOf[next] = curr;
                } else if (parentOf[curr] != next) {
                    findCycle(loopStack, next);
                    return;
                }
            }
        }
    }

    private void findCycle(Stack<Integer> loopStack, int duplicate) {

        int head = loopStack.peek();
        edgeTo[head] = duplicate;
        edgeTo[duplicate] = head;
        announce();

        while (!loopStack.isEmpty() && loopStack.peek() != duplicate) {
            int out = loopStack.pop();
            if (!loopStack.isEmpty()) {
                edgeTo[out] = loopStack.peek();
                announce();
            }
        }

    }
}

