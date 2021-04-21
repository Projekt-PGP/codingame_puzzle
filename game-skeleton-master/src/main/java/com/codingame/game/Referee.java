package com.codingame.game;
import java.util.List;
import java.util.Random;
import org.javatuples.Pair;


import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.SoloGameManager;
import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.*;
import com.google.inject.Inject;


import java.util.ArrayList;

public class Referee extends AbstractReferee {
    @Inject public SoloGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;
    @Inject TooltipModule tooltips;
    public int actualRoom;
    Graph graph;
    public int hp;
    public int stamina;
    public ArrayList<Pair<Integer,Integer>> cords_list;
    public ArrayList<Integer> cords_valid_list;
    public int sprite_idx[];
    public Sprite sprites[];
    int player_idx;

    @Override
    public void init() {
        // Initialize your game here

        // creating graph
        String[] graphConstructor = gameManager.getTestCaseInput().get(0).split(";");
        String testInput="";
        for (int i=0;i<=7;i++)
        {
            testInput=testInput+graphConstructor[i]+";";
        }
        testInput=testInput.substring(0,testInput.length()-1);
        gameManager.getPlayer().sendInputLine(testInput);
        int vertices = Integer.parseInt(graphConstructor[0]);
        int lines = Integer.parseInt(graphConstructor[1]);
        String weights = graphConstructor[2];
        String connections = graphConstructor[3];
        int start = Integer.parseInt(graphConstructor[4]);
        int exit = Integer.parseInt(graphConstructor[5]);
        hp = Integer.parseInt(graphConstructor[6]);
        stamina= Integer.parseInt(graphConstructor[7]);
        String[] cords = graphConstructor[8].split(" ");
        cords_list = new ArrayList<Pair<Integer,Integer>>();
        cords_valid_list = new ArrayList<Integer>();
        sprite_idx = new int[vertices];
        sprites = new Sprite[vertices];
        

        gameManager.getPlayer().sendInputLine(String.valueOf(exit));

        graph=new Graph(vertices,lines,weights,connections,start,exit);
        actualRoom = graph.getStartVertice();

        for(int i = 0; i < vertices; i += 1) {
        	sprite_idx[i] = new Random().nextInt(4);
            cords_list.add(Pair.with(Integer.parseInt(cords[i*2]), Integer.parseInt(cords[i*2+1])));
            if (i == actualRoom) {
                cords_valid_list.add(0);
            }
            cords_valid_list.add(1);
        }

        
        
        //Draw background, hp_text, stamina_text (in next version replace text on bars)
        graphicEntityModule.createSprite()
                .setImage(Constants.BACKGROUND_SPRITE)
                .setAnchor(0)
                .setBaseWidth(1920)
                .setBaseHeight(1080);
        graphicEntityModule.createText("HP: " + String.valueOf(hp))
                .setFontSize(60)
                .setFillColor(0xFFFFFF)
                .setX(100)
                .setY(100);
        graphicEntityModule.createText("Stamina:" + String.valueOf(stamina))
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
            	player_idx = i;
            	sprites[i] = graphicEntityModule.createSprite()
                        .setImage(Constants.PLAYER_SPRITE)
                        .setX(p.getValue0())
                        .setY(p.getValue1());
                i++;
                continue;
            }
            if(cords_valid_list.get(i) == 1) {
                sprites[i] = graphicEntityModule.createSprite()
                        .setImage(Constants.VERTICLE_SPRITE[sprite_idx[i]])
                        .setX(p.getValue0())
                        .setY(p.getValue1());
                i++;
            }
        }

    }
    
    
    private double get_angle(double px, double py, double dx, double dy){
    	double h = Math.abs(py-dy);
    	double a = Math.abs(px-dx);
    	return Math.atan(h/a);
    }
    
    private void update(int dest) {
    	double ang = get_angle(cords_list.get(player_idx).getValue0(), cords_list.get(player_idx).getValue1(),
				  cords_list.get(dest).getValue0(), cords_list.get(dest).getValue1());
    	sprites[player_idx].setRotation(ang+Math.PI+Math.PI/4);
    	graphicEntityModule.commitEntityState(0.2, sprites[player_idx]);
    	sprites[player_idx].setX(cords_list.get(dest).getValue0()).setY(cords_list.get(dest).getValue1());
    }

    @Override
    public void gameTurn(int turn) {
        gameManager.getPlayer().sendInputLine(String.format(String.valueOf(actualRoom)));
        gameManager.getPlayer().execute();
        try {
            List<String> outputs =gameManager.getPlayer().getOutputs();
            String output = checkOutput(outputs);

            if (output != null)
            {
                Action action = new Action(actualRoom, Integer.parseInt(output), graph);
                if (action.destination!=-1)
                {
                    if (hp-graph.getWeights()[action.destination]>=0 && stamina-staminaLose(actualRoom,action.destination,graph)>=0)
                    {
                        hp-=graph.getWeights()[action.destination];
                        stamina-=staminaLose(actualRoom,action.destination,graph);
                        actualRoom=action.destination;
                        update(action.destination);
                        
                    }
                    else
                    {
                        gameManager.loseGame("You lost whole HP or Stamina");
                    }
                }
                else
                {
                    gameManager.loseGame("you cant go to this room");
                }
            }
        } catch (Exception e) {
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
    public int staminaLose(int actualRoom,int destinationRoom,Graph g)
    {
        for (Pair<Integer,Integer> p:g.list[actualRoom]) {
            if (p.getValue0()==destinationRoom)
            {
                return p.getValue1();
            }
        }
        return 0;
    }
}