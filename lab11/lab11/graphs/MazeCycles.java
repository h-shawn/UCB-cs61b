package lab11.graphs;

import edu.princeton.cs.algs4.Stack;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Maze maze;
    private int[] nodeTo;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        nodeTo = new int[maze.N() * maze.N()];
    }

    @Override
    public void solve() {
        Stack<Integer> stk = new Stack<>();

        int s = maze.xyTo1D(1, 1);
        distTo[s] = 0;

        stk.push(s);
        while (!stk.isEmpty()) {
            int cur = stk.pop();
            marked[cur] = true;
            announce();

            for (int w : maze.adj(cur)) {
                if (!marked[w]) {
                    nodeTo[w] = cur;
                    distTo[w] = distTo[cur] + 1;
                    stk.push(w);
                } else if (w != nodeTo[cur]) {
                    findCircle(cur, w);
                    return;
                }
            }
        }
    }

    // Helper methods go here
    private void findCircle(int s, int t) {
        edgeTo[t] = s;
        announce();
        int cur = s;
        while (cur != t) {
            int pre = nodeTo[cur];
            edgeTo[cur] = pre;
            cur = pre;
            announce();
        }
    }
}

