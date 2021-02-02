public class SimulatedAnnealing {
    private final static int N=8;
    int nodesGenerated;
    private Queen[] startState;
    private Node start;

    public SimulatedAnnealing(Queen[] s){
        nodesGenerated = 0;
        start = new Node();
        startState = new Queen[N];

        for(int i=0; i<N; i++){
            startState[i] = new Queen(s[i].getRow(), s[i].getColumn());
        }
        start.setState(startState);
        start.computeHeuristic();
    }

    public Node simulatedAnneal(double initialTemp, double step){
        Node currentNode = start;
        double temperature = initialTemp;
        double val = step;
        double probability;
        int delta;
        double determine;
        Node nextNode;

        System.out.println("initial temp: " + initialTemp);
        System.out.println("initial heuristic: " + start.getHeuristic());
        System.out.println("initial state:\n" + start);

        while(currentNode.getHeuristic()!=0 && temperature > 0){
            //select a random neighbour from currentNode
            nextNode = currentNode.getRandomNeighbour(currentNode);
            nodesGenerated++;

            if(nextNode.getHeuristic()==0)
                return nextNode;

            delta = currentNode.getHeuristic() - nextNode.getHeuristic();

            if(delta > 0){ //currentNode has a higher heuristic
                currentNode = nextNode;
            }else{
                probability = Math.exp(delta/temperature);
                determine = Math.random();
               // System.out.println(probability);

                if(determine <= probability){ //choose nextNode
                    currentNode = nextNode;
                }
            }
            temperature = temperature - val;
        }
        return currentNode;
    }

    public int getNodesGenerated(){
        return nodesGenerated;
    }

    public Node getStartNode(){
        return start;
    }
}
