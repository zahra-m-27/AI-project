public class Node {

    // 0 = start, 1 = finish, 2 = wall, 3 = empty, 4 = checked, 5 = finalPath
    private int stateType = 0;
    private int x;
    private int y;
    private int endX;
    private int endY;
    private int pathLength;
    private double heuristic = 0;

    public Node(int state, int x, int y) {
        this.stateType = state;
        this.x = x;
        this.y = y;
        pathLength = -1;
    }

    public double getManhattanDistance(Node e) {
        int xdifference = Math.abs(x - e.getX());
        int ydifference = Math.abs(y - e.getY());
        heuristic = xdifference + ydifference;
        return heuristic;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public int getStateType() {
        return stateType;
    }

    public int getPathLength() {
        return pathLength;
    }

    public void setStateType(int state) {
        stateType = state;
    }

    public void setEndNode(int x, int y) {
        endX = x;
        endY = y;
    }

    public void setPathLength(int pathLength) {
        this.pathLength = pathLength;
    }
}