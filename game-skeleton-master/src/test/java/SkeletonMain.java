import com.codingame.gameengine.runner.SoloGameRunner;
import com.codingame.game.Graph;

public class SkeletonMain {
    public static void main(String[] args) {

        // Uncomment this section and comment the other one to create a Solo Game
        /* Solo Game */
        SoloGameRunner gameRunner = new SoloGameRunner();

        // Sets the player
        gameRunner.setAgent(Agent.class);

        // Sets a test case
        gameRunner.setTestCase("test1.json");

        // Adds as many player as you need to test your game
        gameRunner.setAgent(Agent.class);
        /* Multiplayer Game */
        //MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        // Adds as many player as you need to test your game
        //gameRunner.addAgent(Agent1.class);
        //gameRunner.addAgent(Agent2.class);

        // Another way to add a player
        // gameRunner.addAgent("python3 /home/user/player.py");
        

        //gameRunner.start();
        Graph g = new Graph(5,2,"1 0 5 5 5","1 0 2 1 2 5");
        g.print_graph();
    }
}
