package com.codingame.game;
import java.util.*;

import org.javatuples.Pair;

public class Graph {
	private int vertices;
	private int[] weights;
	ArrayList<Pair<Integer,Integer>>[] list;
	private int exitVertice;
	private int startVertice;

	
	
	public Graph(int vertices, int lines, String weights, String connections, int start,int exit) {
		this.vertices = vertices;
		list = new ArrayList[vertices];
		this.weights = new int[vertices];
		exitVertice=exit;
		startVertice=start;

		for (int i = 0; i < vertices; i++) { 
			list[i] = new ArrayList<Pair<Integer,Integer>>(); 
        } 
		
		
		String[] data = connections.split(" ");
		String[] data_weights = weights.split(" ");
		
		for(int i = 0; i < lines; i++) {
			list[Integer.parseInt(data[i*3])].add(Pair.with(Integer.parseInt(data[i*3+1]), Integer.parseInt(data[i*3+2])));
			list[Integer.parseInt(data[i*3+1])].add(Pair.with(Integer.parseInt(data[i*3]), Integer.parseInt(data[i*3+2])));
		}
		for(int i = 0; i < vertices; i++) {
			this.weights[i] = Integer.parseInt(data_weights[i]);
		}
		
	}
	
	public String print_graph() {
		String res="";
		for(int i = 0; i < vertices; i++) {
			res+="Vertice number : ";
			res+=i;
			res+=" Weight : ";
			res+=weights[i]+"\n";
			for (Pair<Integer,Integer> p : list[i]) {
				res+=p.getValue0();
				res+=" ";
				res+=p.getValue1()+"\n";
			}
		}
		res+="The exit room is room number: " + exitVertice;
		return res;
	}

	public void setStartVertice(int n)
	{
		startVertice=n;
	}

	public int getStartVertice()
	{
		return startVertice;
	}
	public int getExitVertice()
	{
		return exitVertice;
	}
	public int getVertices()
	{
		return vertices;
	}

}
