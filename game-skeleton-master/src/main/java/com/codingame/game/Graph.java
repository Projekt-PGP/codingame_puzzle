package com.codingame.game;
import java.util.*;

import org.javatuples.Pair;

public class Graph {
	private int vertices;
	private int[] weights;
	ArrayList<Pair<Integer,Integer>>[] list; 
	
	
	public Graph(int vertices, int lines, String weights, String connections) {
		this.vertices = vertices;
		list = new ArrayList[vertices];
		this.weights = new int[vertices];
		
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
	
	public void print_graph() {
		for(int i = 0; i < vertices; i++) {
			System.out.print("Vertice number : ");
			System.out.print(i);
			System.out.print(" Weight : ");
			System.out.println(weights[i]);
			for (Pair<Integer,Integer> p : list[i]) {
				System.out.print(p.getValue0());
				System.out.print(" ");
				System.out.print(p.getValue1());
				System.out.println();
			}
		}
	}
	
	
}
