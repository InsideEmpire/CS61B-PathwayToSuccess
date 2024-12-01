package lab11.graphs;

import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;

/**
 * @author Josh Hug
 * @author Garrison
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    private class MinPQComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            try {
                int v1 = (java.lang.Integer) o1;
                int v2 = (java.lang.Integer) o2;
                return Math.abs(maze.toX(v1) - maze.toX(t)) + Math.abs(maze.toY(v1) - maze.toY(t))
                        - Math.abs(maze.toX(v2) - maze.toX(t)) + Math.abs(maze.toY(v2) - maze.toY(t));
            } catch (ClassCastException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Performs an A star search from vertex start.
     */
    private void astar(int start) {
        MinPQ<Integer> pq = new MinPQ<>(new MinPQComparator());
        pq.insert(start);

        int curr;

        while (!pq.isEmpty()) {

            curr = pq.delMin();
            marked[curr] = true;

            for (int next : maze.adj(curr)) {
                if (!marked[next]) {
                    edgeTo[next] = curr;
                    distTo[next] = distTo[curr] + 1;
                    announce();
                    pq.insert(next);
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
        astar(s);
    }

}

