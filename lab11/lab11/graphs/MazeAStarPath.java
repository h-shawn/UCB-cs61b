package lab11.graphs;

import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    private class Node {
        private int v;
        private int priority;
        public Node (int v) {
            this.v = v;
            this.priority = h(v);
        }
    }

    private class NodeComparator implements Comparator<Node> {
        public int compare(Node x, Node y) {
            return x.priority - y.priority;
        }
    }

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int x = Math.abs(maze.toX(v) - maze.toX(t));
        int y = Math.abs(maze.toY(v) - maze.toY(t));
        return x + y;
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        PriorityQueue<Node> pq = new PriorityQueue<>(new NodeComparator());
        pq.add(new Node(s));
        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            marked[cur.v] = true;
            announce();

            if (cur.v == t) {
                targetFound = true;
                break;
            }

            for (int w : maze.adj(cur.v)) {
                if (!marked[w]) {
                    edgeTo[w] = cur.v;
                    announce();
                    distTo[w] = distTo[cur.v] + 1;
                    pq.add(new Node(w));
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

