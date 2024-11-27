package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.List;

public class Solver {

    int minimumMoves;
    List<WorldState> solutionRoute;
    private MinPQ<SearchNode> queue;

    private static class SearchNode implements Comparable<SearchNode> {
        WorldState node;
        int movesFromInitial;
        SearchNode previousNode;

        SearchNode(WorldState initial) {
            this.node = initial;
            this.previousNode = null;
            this.movesFromInitial = 0;
        }

        SearchNode(WorldState insert, SearchNode previous) {
            this.node = insert;
            this.previousNode = previous;
            this.movesFromInitial = previousNode.movesFromInitial + 1;
        }

        @Override
        public int compareTo(SearchNode other) {
            return Integer.compare(this.movesFromInitial + this.node.estimatedDistanceToGoal(), other.movesFromInitial + other.node.estimatedDistanceToGoal());
        }
    }

    /**
     * Constructor which solves the puzzle, computing
     * everything necessary for moves() and solution() to
     * not have to solve the problem again. Solves the
     * puzzle using the A* algorithm. Assumes a solution exists.
     */
    public Solver(WorldState initial) {
        queue = new MinPQ<>();
        solutionRoute = new ArrayList<>();
        SearchNode initialNode = new SearchNode(initial);
        queue.insert(initialNode);
        while (!queue.isEmpty()) {
            SearchNode BMS = queue.delMin();
            if (BMS.node.isGoal()) {
                getMovesAndSolution(BMS);
                return;
            } else {
                for (WorldState neighbor : BMS.node.neighbors()) {
                    if (BMS.previousNode != null && neighbor.equals(BMS.previousNode.node)) {
                        continue;
                    }
                    queue.insert(new SearchNode(neighbor, BMS));
                }
            }
        }
    }

    private void getMovesAndSolution(SearchNode goalNode) {
        minimumMoves = goalNode.movesFromInitial;
        SearchNode searchNode = goalNode;
        while (searchNode != null) {
            solutionRoute.add(searchNode.node);
            searchNode = searchNode.previousNode;
        }
        solutionRoute = solutionRoute.reversed();
    }

    /**
     * Returns the minimum number of moves to solve the puzzle starting
     * at the initial WorldState.
     */
    public int moves() {
        return minimumMoves;
    }

    /**
     * Returns a sequence of WorldStates from the initial WorldState
     * to the solution.
     */
    public Iterable<WorldState> solution() {
        return solutionRoute;
    }
}
