package com.codingame.game;
import java.util.List;
import org.javatuples.Pair;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.SoloGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.entities.Line;
import com.google.inject.Inject;

import java.util.Arrays;
import java.util.ArrayList;

public class Referee extends AbstractReferee {
    @Inject public SoloGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;
    public int actualRoom;
    Graph graph;
    public int hp;
    public int stamina;
    public ArrayList<Pair<Integer,Integer>> cords_list;
    public ArrayList<Integer> cords_valid_list;

    @Override
    public void init() {
        // Initialize your game here
        hp = 10;
        stamina = 100;
        // creating graph
        String[] graphConstructor = gameManager.getTestCaseInput().get(0).split(";");
        String testInput="";
        for (int i=0;i<5;i++)
        {
            testInput=testInput+graphConstructor[i]+";";
        }
        gameManager.getPlayer().sendInputLine(testInput);
        int vertices = Integer.parseInt(graphConstructor[0]);
        int lines = Integer.parseInt(graphConstructor[1]);
        String weights = graphConstructor[2];
        String connections = graphConstructor[3];
        int start = Integer.parseInt(graphConstructor[4]);
        int exit = Integer.parseInt(graphConstructor[5]);
        String[] cords = graphConstructor[6].split(" ");
        cords_list = new ArrayList<Pair<Integer,Integer>>();
        cords_valid_list = new ArrayList<Integer>();

        gameManager.getPlayer().sendInputLine(String.valueOf(exit));

        graph=new Graph(vertices,lines,weights,connections,start,exit);
        System.out.println(graph.print_graph());
        actualRoom = graph.getStartVertice();

        for(int i = 0; i < vertices; i += 1) {
            cords_list.add(Pair.with(Integer.parseInt(cords[i*2]), Integer.parseInt(cords[i*2+1])));
            if (i == actualRoom) {
                cords_valid_list.add(0);
            }
            cords_valid_list.add(1);
        }
        //graph.print_graph();

        //Draw background, hp_text, stamina_text (in next version replace text on bars)
        graphicEntityModule.createSprite()
                .setImage(Constants.BACKGROUND_SPRITE)
                .setAnchor(0)
                .setBaseWidth(1920)
                .setBaseHeight(1080);
        graphicEntityModule.createText("HP:")
                .setFontSize(60)
                .setFillColor(0xFFFFFF)
                .setX(100)
                .setY(100);
        graphicEntityModule.createText("Stamina:")
                .setFontSize(60)
                .setFillColor(0xFFFFFF)
                .setX(100)
                .setY(220);

        int offset = 30;
        for(int i = 0; i < vertices; i++) {
            for (Pair<Integer,Integer> p : graph.list[i]) {
                graphicEntityModule.createLine()
                        .setLineWidth(20)
                        .setFillColor(0x454545)
                        .setLineColor(0x454545)
                        .setX(cords_list.get(p.getValue0()).getValue0()+offset)
                        .setY(cords_list.get(p.getValue0()).getValue1()+offset)
                        .setX2(cords_list.get(i).getValue0()+offset)
                        .setY2(cords_list.get(i).getValue1()+offset);
            }
        }
        int i = 0;
        for (Pair<Integer,Integer> p : cords_list) {
            if(i == actualRoom) {
                graphicEntityModule.createSprite()
                        .setImage(Constants.PLAYER_SPRITE)
                        .setX(p.getValue0())
                        .setY(p.getValue1());
                i++;
                continue;
            }
            if(cords_valid_list.get(i) == 1) {
                graphicEntityModule.createSprite()
                        .setImage(Constants.VERTICLE_SPRITE)
                        .setX(p.getValue0())
                        .setY(p.getValue1());
                i++;
            }
        }

    }

    @Override
    public void gameTurn(int turn) {

        gameManager.getPlayer().sendInputLine(String.format(String.valueOf(actualRoom)));
        gameManager.getPlayer().execute();
        try {
            List<String> outputs =gameManager.getPlayer().getOutputs();
            //String output = "2";
            String output = checkOutput(outputs);

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
            }
        } catch (TimeoutException e) {
            gameManager.loseGame("Timeout!");
            return;
        }
        checkLose();
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
        if (actualRoom==graph.getExitVertice())
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