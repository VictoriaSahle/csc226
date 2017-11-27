/*
************************************************************************************************
*** ID                  : xxxxxxxxx
*** Name                : Victoria Sahle
*** Program Name        : MWST.java
*** Date                : September 17, 2015
*** Program Description : Assignment 1: Finds a minimum weight spanning tree of an edge-weighted 
***						  graph, calculates the total weight of MST.
*** Program Input       : A n x n array G representing an edge-weighted graph.
*** Program Output      : An integer value corresponding to the total weight of a minimum weight
***                       spanning tree of G.
************************************************************************************************
*/

import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.io.File;


public class MWST{

	/* mwst(G)
		Given an adjacency matrix for graph G, return the total weight
		of all edges in a minimum weight spanning tree.
		
		If G[i][j] == 0, there is no edge between vertex i and vertex j
		If G[i][j] > 0, there is an edge between vertices i and j, and the
		value of G[i][j] gives the weight of the edge.
		No entries of G will be negative.
	*/

	static int MWST(int[][] G){
	
		//Number of vertices
		int vertices = G.length;

		/* (You may add extra functions if necessary) */
		//eW[] stores edge values to chose minimum weight edge
		int eW[] = new int [vertices];
		//Keep track of vertex in MST
		boolean MinSpan[] = new boolean[vertices];
		//Initialize each index of eW[] to MAX_VALUE/infinity
		for(int i = 0; i < vertices; i++){
			eW[i] = java.lang.Integer.MAX_VALUE;
		}
		//First vertex chosen to be in MST
		eW[0] = 0;
		
		for(int i = 0; i < vertices - 1; i++){
			//Choose smallest edge from set of vertices; not already in MST
			int s = java.lang.Integer.MAX_VALUE;
			int index = 0;
			
			for (int j = 0; j < vertices; j++){
				if (!MinSpan[j] & (eW[j] < s)){
					s = eW[j];
					index = j;
				}
			}
			
			int u = index;
			//Chosen vertex u is now in MST
			MinSpan[u] = true;
			//Update edge weight of chosen vertex
			for(int k = 0; k < vertices; k++){
					if((G[u][k] != 0) & (!MinSpan[k]) & G[u][k] < eW[k]){
						eW[k] = G[u][k];
					}
			}
			
		}
		
		/* Add the weight of each edge in the minimum weight spanning tree
		   to totalWeight, which will store the total weight of the tree.
		*/
		int totalWeight = 0;
		//Sum the edges/ values at each index in eW[], for MST total weight
		for(int i = 0; i < vertices; i++){
			totalWeight += eW[i];
		}
		
		return totalWeight;
		
	}
	
		
	/* main()
	   Contains code to test the MWST function. You may modify the
	   testing code if needed, but nothing in this function will be considered
	   during marking, and the testing process used for marking will not
	   execute any of the code below.
	*/
	public static void main(String[] args){
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int graphNum = 0;
		double totalTimeSeconds = 0;
		
		//Read graphs until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading graph %d\n",graphNum);
			int n = s.nextInt();
			int[][] G = new int[n][n];
			int valuesRead = 0;
			for (int i = 0; i < n && s.hasNextInt(); i++){
				for (int j = 0; j < n && s.hasNextInt(); j++){
					G[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < n*n){
				System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
				break;
			}
			long startTime = System.currentTimeMillis();
			
			int totalWeight = MWST(G);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			System.out.printf("Graph %d: Total weight is %d\n",graphNum,totalWeight);
		}
		graphNum--;
		System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
	}
}
