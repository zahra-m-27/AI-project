import java.util.ArrayList;

public class State {
    private String[][] board ;
    private Action executedMove;	// move to be executed to get to this state
    private String move;
    private int type;	// 1 for maximizer, 2 for minimizer
    private int utility;
    private ArrayList<State> children = new ArrayList<State>();

    public State(String[][] board, Action moveToBeExecuted, String move, int type) {
        this.board = board;
        this.executedMove = moveToBeExecuted;
        this.move = move;
        this.type = type;
    }

    private int max(int a, int b) {
        return a > b ? a : b;
    }

    private int min(int a, int b) {
        return a < b ? a : b;
    }

    public void printBoard() {
        System.out.println("**********");
        if (this.executedMove != null) System.out.println("MOVE TO BE EXECUTED: " + this.executedMove.getX() + ", " + this.executedMove.getY());

        for (int i = 0; i < Main.ROW; i++) {
            for (int j = 0; j < Main.COL; j++) {
                System.out.print(this.board[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("**********");
    }

    public boolean isTerminal() {
        if (this.isWinner() || this.isDraw()) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isDraw() {
        for (int i = 0; i < Main.ROW; i++) {
            for (int j = 0; j < Main.COL; j++) {
                if (this.board[i][j] == "-") {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isWinner() {
        // check the rows
        for (int i = 0; i < Main.ROW; i++) {
            if (this.board[i][0] == this.board[i][1] &&
                    this.board[i][0] == this.board[i][2] &&
                    this.board[i][0] != "-") {
                return true;
            }
        }

        // check the columns
        for (int j = 0; j < Main.COL; j++) {
            if (this.board[0][j] == this.board[1][j] &&
                    this.board[0][j] == this.board[2][j] &&
                    this.board[0][j] != "-") {
                return true;
            }
        }

        // check diagonal
        if (this.board[0][0] == this.board[1][1] &&
                this.board[0][0] == this.board[2][2] &&
                this.board[0][0] != "-") {

            return true;
        }

        if (this.board[0][2] == this.board[1][1] &&
                this.board[0][2] == this.board[2][0] &&
                this.board[0][2] != "-") {

            return true;
        }

        return false;
    }

    public ArrayList<Action> actions() {
        ArrayList<Action> actionList = new ArrayList<Action>();

        for (int i = 0; i < Main.ROW; i++) {
            for (int j = 0; j < Main.COL; j++) {
                if (this.board[i][j] == "-") {
                    actionList.add(new Action(i, j));
                }
            }
        }

        return actionList;
    }

    public State value(int alpha, int beta) {
        if (this.isTerminal()) {
            this.utility = this.utility();
            return this;
        }
        else if (this.type == 1) {
            this.utility = this.maxValue(alpha, beta);
            return this;
        }
        else {
            this.utility = this.minValue(alpha, beta);
            return this;
        }
    }

    public int maxValue(int alpha, int beta) {
        int m = Integer.MIN_VALUE;

        ArrayList<State> states = new ArrayList<State>();

        for (Action a : this.actions()) {
            states.add(this.result(a));
        }

        for (State s : states) {
            this.children.add(s);
            State v = s.value(alpha, beta);

            m = s.max(m, v.utility);

            if (m >= beta) {
                System.out.println("PRUNING (MAX)");
                return m;
            }

            alpha = s.max(alpha, m);
        }

        return m;
    }

    public int minValue(int alpha, int beta) {
        int m = Integer.MAX_VALUE;

        ArrayList<State> states = new ArrayList<State>();

        for (Action a : this.actions()) {
            states.add(this.result(a));
        }

        for (State s : states) {
            this.children.add(s);
            State v = s.value(alpha, beta);

            m = s.min(m, v.utility);

            if (m <= alpha) {
                System.out.println("PRUNING (MIN)");
                return m;
            }

            beta = s.min(beta, m);
        }

        return m;
    }

    public State result(Action action) {
        String[][] newBoard = new String[Main.ROW][Main.COL];

        for (int i = 0; i < Main.ROW; i++) {
            for (int j = 0; j < Main.COL; j++) {
                newBoard[i][j] = this.getBoard()[i][j];
            }
        }

        newBoard[action.getX()][action.getY()] = this.move;

        return new State(newBoard, action, this.move == "X" ? "O" : "X", this.type == 1 ? 2 : 1);
    }

    public int utility() {
        if (this.type == 1 && this.isWinner()) {
            return -1;
        }
        else if (this.type == 2 && this.isWinner()) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public String[][] getBoard() {
        return this.board;
    }

    public ArrayList<State> getChildren() {
        return this.children;
    }

    public int getUtility() {
        return this.utility;
    }

    public Action getExecutedMove() {
        return this.executedMove;
    }

    public String getMove() {
        return this.move;
    }

}