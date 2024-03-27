package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    private static class SearchNode implements Comparable<SearchNode> {
        private final WorldState state;
        private final int moves;
        private final SearchNode pre;

        public SearchNode(WorldState state, int moves, SearchNode pre) {
            this.state = state;
            this.moves = moves;
            this.pre = pre;
        }

        public WorldState getState() {
            return state;
        }

        public int getMoves() {
            return moves;
        }

        public SearchNode getPre() {
            return pre;
        }

        @Override
        public int compareTo(SearchNode o) {
            int v1 = moves + state.estimatedDistanceToGoal();
            int v2 = o.moves + o.state.estimatedDistanceToGoal();
            return v1 - v2;
        }
    }

    private MinPQ<SearchNode> pq;
    private SearchNode goal;

    public Solver(WorldState initial) {
        pq = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null));
        while (true) {
            SearchNode node = pq.delMin();
            WorldState cur = node.getState();
            if (cur.isGoal()) {
                goal = node;
                break;
            }

            SearchNode preNode = node.getPre();
            for (WorldState neighbor : cur.neighbors()) {
                if (preNode == null || !neighbor.equals(preNode.getState())) {
                    pq.insert(new SearchNode(neighbor, node.getMoves() + 1, node));
                }
            }
        }

    }
    public int moves() {
        return goal.getMoves();
    }

    public Iterable<WorldState> solution() {
        List<WorldState> reversedAns = new ArrayList<>();
        List<WorldState> ans = new ArrayList<>();
        SearchNode node = goal;
        while (node != null) {
            reversedAns.add(node.getState());
            node = node.getPre();
        }

        for (int i = reversedAns.size() - 1; i >= 0; i--) {
            ans.add(reversedAns.get(i));
        }
        return ans;
    }
}
