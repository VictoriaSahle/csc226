/* NetworkFlow.java
   CSC 226 - Fall 2014
   Assignment 4 - Max. Flow Template
   
   This template includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
	java NetworkFlow
	
   To conveniently test the algorithm with a large input, create a text file
   containing one or more test graphs (in the format described below) and run
   the program with
	java NetworkFlow file.txt
   where file.txt is replaced by the name of the text file.
   
   The input consists of a series of directed graphs in the following format:
   
    <number of vertices>
	<adjacency matrix row 1>
	...
	<adjacency matrix row n>
	
   Entry A[i][j] of the adjacency matrix gives the capacity of the edge from 
   vertex i to vertex j (if A[i][j] is 0, then the edge does not exist).
   For network flow computation, the 'source' vertex will always be vertex 0 
   and the 'sink' vertex will always be vertex 1.
	
   An input file can contain an unlimited number of graphs; each will be 
   processed separately.


   B. Bird - 07/05/2014
*/

/*
***************************************************************************************************
*** ID                  : xxxxxxxxx
*** Name                : Victoria Sahle
*** Date                : November 3, 2015
*** Program Name        : NetworkFlow.java
*** Program Description : Assignment 4: Implement an algorithm which takes a flow network G and 
***                       returns a feasible flow with the maximum possible total value.
*** Program Input       : An n × n adjacency matrix representing a flow network G with n vertices.
*** Program Output      : An n x n matrix containing a feasible flow on G from source vertex 0 to
***                       sink vertex 1 with the maximum possible value.
***************************************************************************************************
*/


import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.io.File;
import java.util.Queue;
import java.util.LinkedList;

//Do not change the name of the NetworkFlow class
public class NetworkFlow{

	static boolean breadthFirst(int[][] Graph, int s, int t, int[] path){
    	    
		int n = Graph.length;
		boolean[] visited = new boolean[n];
		for(int i = 0; i < n; i++){
			visited[i] = false;
		}
		
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(s);
		visited[s] = true;
		path[s] = -1;
		
		while(!q.isEmpty()){
			int u = q.remove();
			
			for(int v = 0; v < n; v++){
				if((visited[v]==false) && (Graph[u][v] > 0 )){
					q.add(v);
					path[v] = u;
					visited[v] = true;
				}
			}
		}

		return (visited[t] == true);
	}	
	
	/* MaxFlow(G)
	   Given an adjacency matrix describing the structure of a graph and the 
	   capacities of its edges, return a matrix containing a maximum flow from
	   vertex 0 to vertex 1 of G.
	   In the returned matrix, the value of entry i,j should be the total flow
	   across the edge (i,j).
	*/
	
        static int[][] MaxFlow(int[][] G){
		int numVerts = G.length;

        int a = 0;
		int b = 0;
		int s = 0;
		int t = 1;
				
		int[][] Graph = new int[numVerts][numVerts];
		
		for(a = 0; a < numVerts; a++){
			for(b = 0; b < numVerts; b++){
				Graph[a][b] = G[a][b];
				
			}
		}
		
		int[] path = new int[numVerts];
		int maximumFlow = 0;
		
		while(breadthFirst(Graph, s, t, path)){
			int flow = Integer.MAX_VALUE;
			
			for(b = t; b != s; b = path[b]){
				a = path[b];
				
				if(flow > Graph[a][b]){
					flow = Graph[a][b];
				}else{
					flow = flow;
				}
			}
			
			for(b = t; b != s; b = path[b]){
				a = path[b];
				Graph[a][b] -= flow;
				Graph[b][a] += flow;
			}
			
			maximumFlow += flow;
			
		}
		
                int[][] Graph2 = new int[numVerts][numVerts];
                int x;
                
                for (int i = 0; i < Graph.length; i++){
                    for (int j = 0; j < Graph[i].length; j++){
                         x = G[i][j] - Graph[i][j];
                         if(x > 0){
                         	 Graph2[i][j] = x;
                         }
                         else{
                         	 Graph2[i][j] = 0;
                         }
                    }
                }
                
		return Graph2;
	}
	
	public static boolean verifyFlow(int[][] G, int[][] flow){
		
		int n = G.length;
		
		//Test that the flow on each edge is less than its capacity.
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				if (flow[i][j] < 0 || flow[i][j] > G[i][j]){
					System.err.printf("ERROR: Flow from vertex %d to %d is out of bounds.\n",i,j);
					return false;
				}
			}
		}
		
		//Test that flow is conserved.
		int sourceOutput = 0;
		int sinkInput = 0;
		for (int j = 0; j < n; j++)
			sourceOutput += flow[0][j];
		for (int i = 0; i < n; i++)
			sinkInput += flow[i][1];
		
		if (sourceOutput != sinkInput){
			System.err.printf("ERROR: Flow leaving vertex 0 (%d) does not match flow entering vertex 1 (%d).\n",sourceOutput,sinkInput);
			return false;
		}
		
		for (int i = 2; i < n; i++){
			int totalIn = 0, totalOut = 0;
			for (int j = 0; j < n; j++){
				totalIn += flow[j][i];
				totalOut += flow[i][j];
			}
			if (totalOut != totalIn){
				System.err.printf("ERROR: Flow is not conserved for vertex %d (input = %d, output = %d).\n",i,totalIn,totalOut);
				return false;
			}
		}
		return true;
	}
	
	public static int totalFlowValue(int[][] flow){
		int n = flow.length;
		int sourceOutput = 0;
		for (int j = 0; j < n; j++)
			sourceOutput += flow[0][j];
		return sourceOutput;
	}
	
	/* main()
	   Contains code to test the MaxFlow function. You may modify the
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
			
			int[][] G2 = new int[n][n];
			for (int i = 0; i < n; i++)
				for (int j = 0; j < n; j++)
					G2[i][j] = G[i][j];
			int[][] flow = MaxFlow(G2);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			if (flow == null || !verifyFlow(G,flow)){
				System.out.printf("Graph %d: Flow is invalid.\n",graphNum);
			}else{
				int value = totalFlowValue(flow);
				System.out.printf("Graph %d: Max Flow Value is %d\n",graphNum,value);
			}
				
		}
		graphNum--;
		System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
	}
}

