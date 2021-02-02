import java.util.*;

public class Node implements Comparable<Node>{
    private static final int N=8;
    public Queen[] state; //the node's state
    private int h; //heuristic score

    public Node(){
        state = new Queen[N];
    }

    public Node(Node n){
        state = new Queen[N];
        for(int i=0; i<N; i++)
            state[i] = new Queen(n.state[i].getRow(), n.state[i].getColumn());
        h=0;
    }

    public Node getRandomNeighbour(Node startState){
        Random gen = new Random();

        int col = gen.nextInt(N);
        int d = gen.nextInt(N-1)+1;

        Node neighbour = new Node(startState);
        neighbour.state[col].moveDown(d);
        neighbour.computeHeuristic();

        return neighbour;
    }

    public int computeHeuristic(){

        for(int i=0; i<N-1; i++){
            for(int j=i+1; j<N; j++){
                if(state[i].canAttack(state[j])){
                    h++;
                }
            }
        }
        return h;
    }

    public int getHeuristic(){
        return h;
    }

    public int compareTo(Node n){
        if(this.h < n.getHeuristic())
            return -1;
        else if(this.h > n.getHeuristic())
            return 1;
        else
            return 0;
    }

    public void setState(Queen[] s){
        for(int i=0; i<N; i++){
            state[i]= new Queen(s[i].getRow(), s[i].getColumn());
        }
    }

    public Queen[] getState(){
        return state;
    }

    public String toString(){
        String result="";
        String[][] board = new String[N][N];

        //initialise board with x's to indicate empty spaces
        for(int i=0; i<N; i++)
            for(int j=0; j<N; j++)
                board[i][j]="x  ";

        //place the queens on the board
        for(int i=0; i<N; i++){
            board[state[i].getRow()][state[i].getColumn()]="Q  ";
        }

        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                result+=board[i][j];
            }
            result+="\n";
        }

        return result;
    }
}
