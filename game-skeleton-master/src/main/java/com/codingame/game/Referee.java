package com.codingame.game;
import java.util.List;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.SoloGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.entities.Line;
import com.google.inject.Inject;
import java.util.Arrays;

public class Referee extends AbstractReferee {
    @Inject public SoloGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;
    public int actualRoom;
    Graph graph;
    public int hp;
    public int stamina;

    @Override
    public void init() {
        // Initialize your game here
        // creating graph
        String[] graphConstructor = gameManager.getTestCaseInput().get(0).split(";");
        int vertices = Integer.parseInt(graphConstructor[0]);
        int lines = Integer.parseInt(graphConstructor[1]);
        String weights = graphConstructor[2];
        String connections = graphConstructor[3];
        int start = Integer.parseInt(graphConstructor[4]);
        int exit = Integer.parseInt(graphConstructor[5]);

        graph=new Graph(vertices,lines,weights,connections,start,exit);
        actualRoom = graph.getStartVertice();
        //graph.print_graph();

        //Draw background, hp_text, stamina_text (in next version replace text on bars)
        graphicEntityModule.createSprite()
                .setImage(Constants.BACKGROUND_SPRITE)
                .setAnchor(0);
        graphicEntityModule.createText("HP:")
                .setFontSize(60)
                .setFillColor(0x000000)
                .setX(100)
                .setY(100);
        graphicEntityModule.createText("Stamina:")
                .setFontSize(60)
                .setFillColor(0x000000)
                .setX(100)
                .setY(220);

    }

    @Override
    public void gameTurn(int turn) {

        gameManager.getPlayer().sendInputLine(String.format(graph.print_graph()));
        gameManager.getPlayer().sendInputLine("Your actual room is: " + actualRoom);

        gameManager.getPlayer().execute();
        try {
            List<String> outputs =gameManager.getPlayer().getOutputs();
            String output = "2";
            //String output = checkOutput(outputs);

            if (output != null)
            {
                Action action = new Action(actualRoom, Integer.parseInt(output), graph);
                if (action.destination!=-1)
                {
                    actualRoom=Integer.parseInt(output);
                }
                else
                {
                    gameManager.loseGame("you cant go to this room");
                }
                //System.out.println(graph.list[actualRoom]);
                //stamina= graph.list[actualRoom].get(1);
            }
        } catch (TimeoutException e) {
            gameManager.loseGame("Timeout!");
        }
        //checkLose();
        checkVictory();
    }

    public String checkOutput(List<String> outputs)
    {
        if (outputs.size()!=1)
        {
            gameManager.loseGame("You send wrong amount of outputs");
        }
        else
        {
            String output = outputs.get(0);
            if (!output.matches("^[0-9]{1,2}"))
            {
                gameManager.loseGame(String.format("Expected output: a number between 0 and %d but recived %s",
                        graph.getVertices(),output));
            }
            else if (Integer.parseInt(output)>=graph.getVertices())
            {
                gameManager.loseGame("Outputed number is greater than number of verticles");
            } else {
                return output;
            }
        }
        return null;
    }

    public void checkVictory()
    {
        if (actualRoom==graph.getExitVertice());
            gameManager.winGame("Congratz, you won!");
    }
    public void checkLose()
    {
        if ((hp<=0|| stamina<=0) && actualRoom!=graph.getExitVertice())
        {
            gameManager.loseGame("You lost!");
        }
    }
}