import com.codingame.game.Graph;
import com.codingame.gameengine.runner.SoloGameRunner;

public class SkeletonMain {
    public static void main(String[] args) {

        SoloGameRunner gameRunner = new SoloGameRunner();

        // Sets a test case
         gameRunner.setTestCase("test1.json");

        // Adds as many player as you need to test your game
        gameRunner.setAgent(Agent.class);


        gameRunner.start();

        //Graph g = new Graph(5,2,"1 0 5 5 5","1 0 2 1 2 5",1,0);
        //g.print_graph();
    }
}