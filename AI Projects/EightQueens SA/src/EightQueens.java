import java.util.*;

public class EightQueens {

    public EightQueens() {
    }

    public Queen[] generateBoardRandomly() {
        Queen[] start = new Queen[8];
        Random gen = new Random();

        for (int i = 0; i < 8; i++) {
            start[i] = new Queen(gen.nextInt(8), i);
        }
        return start;
    }

    public static void main(String[] args) {
        int annealNodes = 0;
        EightQueens board =new EightQueens();
        Queen[] startBoard ;
        startBoard = board.generateBoardRandomly();

        SimulatedAnnealing anneal = new SimulatedAnnealing(startBoard);

        Node annealSolved = anneal.simulatedAnneal(30, 0.001);

        annealNodes += anneal.getNodesGenerated();

        System.out.println("heuristic: " + annealSolved.getHeuristic());
        System.out.println(annealSolved);
        System.out.println("Nodes: " + annealNodes);

    }

}

