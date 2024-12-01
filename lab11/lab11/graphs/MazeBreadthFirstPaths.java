package lab11.graphs;

import java.util.ArrayDeque;

/**
 *  @author Josh Hug
 * @author Garrison
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        this.s = maze.xyTo1D(sourceX, sourceY);
        this.t = maze.xyTo1D(targetX, targetY);
        this.distTo[s] = 0;
        this.edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        ArrayDeque<Integer> bfsQueue = new ArrayDeque<>();
        bfsQueue.addLast(s);

        int curr;

        while (!bfsQueue.isEmpty()) {

            curr = bfsQueue.removeFirst();
            marked[curr] = true;

            for (int next : maze.adj(curr)) {
                if (!marked[next]) {
                    edgeTo[next] = curr;
                    distTo[next] = distTo[curr] + 1;
                    announce();
                    bfsQueue.addLast(next);
                    if (targetFound) {
                        return;
                    }
                }
            }

            if (curr == t) {
                targetFound = true;
            }
            if (targetFound) {
                return;
            }

        }
    }


    @Override
    public void solve() {
        bfs();
    }
}

