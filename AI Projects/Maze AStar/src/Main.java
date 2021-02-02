import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    private String[] options = {"Start", "Goal", "Wall", "Eraser"};
    private int xStart = -1;
    private int yStart = -1;
    private int xGoal = -1;
    private int yGoal = -1;
    private int dimension = 10;
    private double totalDensity = (dimension * dimension) * 0.3;
    private int option = 0;
    private int noOfCheckedNodes = 0;
    private int pathLength = 0;
    private int WIDTH = 850;
    private final int HEIGHT = 650;
    private final int Mazesize = 600;
    private int cellSize = Mazesize / dimension;
    Algorithm Alg = new Algorithm();
    Random rand = new Random();
    private boolean isSearchStarted = false;
    Node goal;
    JFrame frame;
    Node map[][];
    JPanel panelBox = new JPanel();
    Maze canvas;

    JLabel optionList = new JLabel("Options");
    JLabel noOfChecksLabel = new JLabel("Checked:" + noOfCheckedNodes);
    JLabel pathLengthLabel = new JLabel("Path Length: " + pathLength);

    JButton searchPath = new JButton("Search Path");
    JButton newMaze = new JButton("Random Maze");
    JButton eraseAll = new JButton("Erase All");

    JComboBox optionsBox = new JComboBox(options);

    private void initialize() {
        frame = new JFrame();
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setSize(WIDTH, HEIGHT);
        frame.setTitle("Solve Maze with A* Algorithm");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        int small = 25;
        int large = 45;

        panelBox.setLayout(null);
        panelBox.setBounds(10, 10, 210, 600);

        searchPath.setBounds(40, small, 120, 25);
        panelBox.add(searchPath);
        small += large;

        newMaze.setBounds(40, small, 120, 25);
        panelBox.add(newMaze);
        small += large;

        eraseAll.setBounds(40, small, 120, 25);
        panelBox.add(eraseAll);
        small += 40;

        optionList.setBounds(40, small, 120, 25);
        panelBox.add(optionList);
        small += 25;

        optionsBox.setBounds(40, small, 120, 25);
        panelBox.add(optionsBox);
        small += large;

        noOfChecksLabel.setBounds(15, small, 100, 25);
        panelBox.add(noOfChecksLabel);
        small += large;

        pathLengthLabel.setBounds(15, small, 100, 25);
        panelBox.add(pathLengthLabel);
        small += large;

        frame.getContentPane().add(panelBox);

        canvas = new Maze();
        canvas.setBounds(230, 10, Mazesize + 1, Mazesize + 1);
        frame.getContentPane().add(canvas);

        //ACTION LISTENERS
        searchPath.addActionListener(e -> {
            resetVar();
            if ((xStart > -1 && yStart > -1) && (xGoal > -1 && yGoal > -1))
                isSearchStarted = true;
        });
        newMaze.addActionListener(e -> {
            createNewMaze();
            Update();
        });
        eraseAll.addActionListener(e -> {
            clearAllPaths();
            Update();
        });

        optionsBox.addItemListener(e -> option = optionsBox.getSelectedIndex());

        startSearch();
    }


    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        clearAllPaths();
        initialize();
    }

    public void clearAllPaths() {
        xStart = -1;
        yStart = -1;
        xGoal = -1;
        yGoal = -1;
        map = new Node[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                map[i][j] = new Node(3, i, j);    //SET ALL NODES TO EMPTY
            }
        }
        resetVar();
    }

    public void resetVar() {
        isSearchStarted = false;
        pathLength = 0;
        noOfCheckedNodes = 0;
    }

    public void createNewMaze() {
        clearAllPaths();
        for (int i = 0; i < totalDensity; i++) {
            Node current;
            do {
                int x = rand.nextInt(dimension);
                int y = rand.nextInt(dimension);
                current = map[x][y];
            } while (current.getStateType() == 2);    //IF IT IS ALREADY A WALL, FIND A NEW ONE
            current.setStateType(2);
        }
    } //create new random maze

    public void Update() {
        cellSize = Mazesize / dimension;
        canvas.repaint();
        pathLengthLabel.setText("Path Length: " + pathLength);
        noOfChecksLabel.setText("Checked:" + noOfCheckedNodes);
    }

    public void startSearch() {
        if (isSearchStarted) {
            Alg.AStar();
        }
        pause();
    }

    public void pause() {
        int i = 0;
        while (!isSearchStarted) {
            i++;
            if (i > 500)
                i = 0;

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        startSearch();
    }


    class Maze extends JPanel implements MouseListener {

        public Maze() {
            addMouseListener(this);
        }

        public void paintComponent(Graphics gr) {
            super.paintComponent(gr);
            for (int x = 0; x < dimension; x++) {
                for (int y = 0; y < dimension; y++) {
                    switch (map[x][y].getStateType()) {
                        case 0:
                            gr.setColor(Color.MAGENTA);
                            break;
                        case 1:{
                            gr.setColor(Color.YELLOW);
                            goal = map[x][y];
                        }
                            break;
                        case 2:
                            gr.setColor(Color.BLACK);
                        break;
                        case 3:
                            gr.setColor(Color.WHITE);
                            break;
                        case 4:
                            gr.setColor(Color.WHITE);
                            break;
                        case 5:
                            gr.setColor(Color.BLUE);
                            break;
                    }
                    gr.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    gr.setColor(Color.BLACK); //border color
                    gr.drawRect(x * cellSize, y * cellSize, cellSize, cellSize); //border
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent evt) {
            int x = evt.getX() / cellSize;
            int y = evt.getY() / cellSize;
            Node current = map[x][y];
            switch (option) {
                case 0: {    //START NODE
                    if (current.getStateType() != 2) {    //IF NOT WALL
                        if (xStart > -1 && yStart > -1) {    //IF START ALREADY EXISTS SET IT TO EMPTY
                            map[xStart][yStart].setStateType(3);
                            map[xStart][yStart].setPathLength(-1);
                        }
                        current.setPathLength(0);
                        xStart = x;
                        yStart = y;
                        current.setStateType(0);
                    }
                    break;
                }
                case 1: {     //FINISH NODE
                    if (current.getStateType() != 2) {    //IF NOT WALL
                        if (xGoal > -1 && yGoal > -1)    //IF FINISH EXISTS SET IT TO EMPTY
                            map[xGoal][yGoal].setStateType(3);
                        xGoal = x;
                        yGoal = y;
                        current.setStateType(1);
                    }
                    break;
                }
                default:
                    if (current.getStateType() != 0 && current.getStateType() != 1)
                        current.setStateType(option);
                    break;
            }
            Update();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }


    class Algorithm {

        public void AStar() {
            ArrayList<Node> pq = new ArrayList<Node>();
            pq.add(map[xStart][yStart]);
            while (isSearchStarted) {
                if (pq.size() <= 0) {
                    isSearchStarted = false;
                    break;
                }
                int cost = pq.get(0).getPathLength() + 1;
                ArrayList<Node> exploredList = exploreSuccessor(pq.get(0), cost);
                if (exploredList.size() > 0) {
                    pq.remove(0);
                    pq.addAll(exploredList);
                    Update();
                } else {
                    pq.remove(0);
                }
                queuesort(pq);
            }
        }

        public ArrayList<Node> queuesort(ArrayList<Node> q) {
            int count = 0;
            while (count < q.size()) {
                int t = count;
                for (int i = count + 1; i < q.size(); i++) {
                    if (q.get(i).getManhattanDistance(goal) + q.get(i).getPathLength() < q.get(t).getManhattanDistance(goal) + q.get(t).getPathLength())
                        t = i;
                }
                if (count != t) {
                    Node temp = q.get(count);
                    q.set(count, q.get(t));
                    q.set(t, temp);
                }
                count++;
            }
            return q;
        }

        public ArrayList<Node> exploreSuccessor(Node e, int g) {
            ArrayList<Node> exploredElements = new ArrayList<Node>();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int x = e.getX() + i;
                    int y = e.getY() + j;
                    if ((x > -1 && x < dimension) && (y > -1 && y < dimension) && Math.abs(x - e.getX()) != Math.abs(y - e.getY())) {
                        Node successor = map[x][y];
                        if ((successor.getPathLength() == -1 || successor.getPathLength() > g)
                                && successor.getStateType() != 2) { //CHECKS IF THE NODE IS NOT A WALL AND THAT IT HAS NOT BEEN EXPLORED
                            lookup(successor, e.getX(), e.getY(), g);
                            exploredElements.add(successor);
                        }
                    }
                }
            }
            return exploredElements;
        }

        public void lookup(Node e, int lx, int ly, int cost) {
            if (e.getStateType() != 0 && e.getStateType() != 1)    //CHECK THAT THE NODE IS NOT THE START OR FINISH
                e.setStateType(4);
            e.setEndNode(lx, ly);    //KEEP TRACK OF THE NODE THAT THIS NODE IS EXPLORED FROM
            e.setPathLength(cost);
            noOfCheckedNodes++;
            if (e.getStateType() == 1) {    //IF THE NODE IS THE FINISH THEN BACKTRACK TO GET THE PATH
                backtrack(e.getEndX(), e.getEndY(), cost);
            }
        }

        public void backtrack(int lx, int ly, int cost) {
            pathLength = cost;
            while (cost > 1) {
                Node e = map[lx][ly];
                e.setStateType(5);
                lx = e.getEndX();
                ly = e.getEndY();
                cost--;
            }
            isSearchStarted = false;
        }
    }
}