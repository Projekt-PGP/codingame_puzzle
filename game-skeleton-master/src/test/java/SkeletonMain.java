import com.codingame.game.Graph;
import com.codingame.gameengine.runner.SoloGameRunner;

public class SkeletonMain {
    public static void main(String[] args) {

        SoloGameRunner gameRunner = new SoloGameRunner();

        // Sets a test case
        //test1 "4;5;4 3 2 5;1 2 3 1 3 4 0 1 1 0 3 4 1 3 3;3;1;500 400 600 600 750 750 1000 900"
         gameRunner.setTestCase("test1.json");

        // Adds as many player as you need to test your game
        gameRunner.setAgent(Agent.class);


        gameRunner.start();

    }
}