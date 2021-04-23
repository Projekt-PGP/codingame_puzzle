package com.codingame.game;
import java.util.List;
import java.util.Random;

import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;

import org.javatuples.Pair;

import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.SoloGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.*;
import com.google.inject.Inject;


import java.util.ArrayList;

public class Referee extends AbstractReferee {
	
    @Inject public SoloGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;
    @Inject TooltipModule tooltips;
    
    private int actualRoom;
    private int hp;
    private int stamina;
    private int vertices;
    private int edges;
    
    private int start;
    private int exit;
    private int spriteIdx[];
    private int xOffPlayer = 35;
    private int yOffPlayer = 30;
    private int xOffPlanet = 64;
    private int yOffPlanet = 64;
    
    
    private Sprite spritesPlanets[];
    private Sprite spritesAliens[];
    private Sprite playerSprite;
    private Text hpText;
    private Text staminaText;
    
    private ArrayList<Pair<Integer,Integer>> cordsList;
    private ArrayList<Integer> cordsValidList;

    private Graph graph;

    
    private void drawLines() {
        for(int i = 0; i < vertices; i++) {
            for (Pair<Integer,Integer> p : graph.list[i]) {
            	
                graphicEntityModule.createLine()
                        .setLineWidth(10)
                        .setFillColor(0x454545)
                        .setLineColor(0x454545)
                        .setX(cordsList.get(p.getValue0()).getValue0()+xOffPlanet)
                        .setY(cordsList.get(p.getValue0()).getValue1()+xOffPlanet)
                        .setX2(cordsList.get(i).getValue0()+xOffPlanet)
                        .setY2(cordsList.get(i).getValue1()+yOffPlanet);
                
            }
        }
    }
    
    private void drawPlanets() {
    	int i = 0;
        for (Pair<Integer,Integer> p : cordsList) {
        	if(i != exit) {
                spritesPlanets[i] = graphicEntityModule.createSprite()
                        .setImage(Constants.VERTICLE_SPRITE[spriteIdx[i]])
                        .setX(p.getValue0())
                        .setY(p.getValue1());
            }
        	else{
                spritesPlanets[i] = graphicEntityModule.createSprite()
                        .setImage(Constants.END_VERTEX)
                        .setX(p.getValue0())
                        .setY(p.getValue1());
            }

            if (i == actualRoom) {
            	playerSprite = graphicEntityModule.createSprite()
                        .setImage(Constants.PLAYER_SPRITE)
                        .setX(p.getValue0()+xOffPlayer)
                        .setY(p.getValue1()+yOffPlayer);
                tooltips.setTooltipText(playerSprite, "Player");
            }
            else {
            	spritesAliens[i] = graphicEntityModule.createSprite()
                        .setImage(Constants.VERTICLE_ENEMY)
                        .setX(p.getValue0()+xOffPlayer)
                        .setY(p.getValue1()+yOffPlayer);
            }
            
            i++;
        }
        
    }
    
    private void drawBgDesc() {
    	
    	graphicEntityModule.createSprite()
        .setImage(Constants.BACKGROUND_SPRITE)
        .setAnchor(0)
        .setBaseWidth(1920)
        .setBaseHeight(1080);
    	
		hpText = graphicEntityModule.createText("HP: " + String.valueOf(hp))
		        .setFontSize(50)
		        .setFillColor(0xFFFFFF)
		        .setX(100)
		        .setY(100);
		
		staminaText = graphicEntityModule.createText("Stamina:" + String.valueOf(stamina))
		        .setFontSize(50)
		        .setFillColor(0xFFFFFF)
		        .setX(100)
		        .setY(220);
		
    }

    private void update(int dest) {

    	playerSprite.setX(cordsList.get(dest).getValue0()+35).setY(cordsList.get(dest).getValue1()+28);
    	spritesAliens[dest].setVisible(false);
    	hpText.setText("HP: " + String.valueOf(hp));
    	staminaText.setText("Stamina: " + String.valueOf(stamina));
    	
    }    
    @Override
    public void init() {
        // Creating graph
        String[] graphConstructor = gameManager.getTestCaseInput().get(0).split(";");

        gameManager.getPlayer().sendInputLine(graphConstructor[0]);
        gameManager.getPlayer().sendInputLine(graphConstructor[1]);
        gameManager.getPlayer().sendInputLine(graphConstructor[2]);
        gameManager.getPlayer().sendInputLine(graphConstructor[3]);
        gameManager.getPlayer().sendInputLine(graphConstructor[6]);
        gameManager.getPlayer().sendInputLine(graphConstructor[7]);
        gameManager.getPlayer().sendInputLine(graphConstructor[5]);


        vertices = Integer.parseInt(graphConstructor[0]);
        edges = Integer.parseInt(graphConstructor[1]);
        start = Integer.parseInt(graphConstructor[4]);
        exit = Integer.parseInt(graphConstructor[5]);
        hp = Integer.parseInt(graphConstructor[6]);
        stamina= Integer.parseInt(graphConstructor[7]);
        actualRoom = start;

        cordsList = new ArrayList<Pair<Integer,Integer>>();
        cordsValidList = new ArrayList<Integer>();

        spriteIdx = new int[vertices];
        spritesPlanets = new Sprite[vertices];
        spritesAliens = new Sprite[vertices];
        
        String weights = graphConstructor[2];
        String connections = graphConstructor[3];
        String[] cords = graphConstructor[8].split(" ");

        String testInput="";
        /*for (int i=0;i<=7;i++)
        {
            testInput=testInput+graphConstructor[i]+";";
        }
        testInput=testInput.substring(0,testInput.length()-1);*/
        //gameManager.getPlayer().sendInputLine(testInput);
        gameManager.getPlayer().sendInputLine(String.valueOf(exit));


        graph = new Graph(vertices,edges,weights,connections,start,exit);
        
        for(int i = 0; i < vertices; i += 1) {
        	spriteIdx[i] = new Random().nextInt(4);
            cordsList.add(Pair.with(Integer.parseInt(cords[i*2]), Integer.parseInt(cords[i*2+1])));
            
            if (i == actualRoom) {
                cordsValidList.add(0);
            }
            cordsValidList.add(1);
        }
        drawBgDesc();
        drawLines();
        drawPlanets();
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