import com.codingame.game.Graph;
import com.codingame.gameengine.runner.SoloGameRunner;

public class SkeletonMain {
    public static void main(String[] args) {

        SoloGameRunner gameRunner = new SoloGameRunner();

        // Sets a test case
        gameRunner.setTestCase("test2.json");
        gameRunner.setTestCase("test1.json");


        // Adds as many player as you need to test your game
        gameRunner.setAgent(Agent.class);


        gameRunner.start();

    }
}