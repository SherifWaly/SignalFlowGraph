package solver;

import graph.Graph;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	public static void main(String []args){
		Point pos = new Point(0,0);
		for(int i = 0 ; i < 6 ; i++){
			Graph.getGraph().addNode(pos);
		}
		Graph.getGraph().addEdge(0, 1, 5.0);
		Graph.getGraph().addEdge(1, 2, 5.0);
		Graph.getGraph().addEdge(2, 3, 5.0);
		Graph.getGraph().addEdge(3, 4, 5.0);
		Graph.getGraph().addEdge(4, 5, 5.0);
		Graph.getGraph().addEdge(4, 3, -5.0);
		Graph.getGraph().addEdge(2, 1, -5.0);
		Mason mason = new Mason();
		System.out.println(mason.solve(0, 5));
		System.out.println(mason.getForwardPaths() + "\n" + mason.getLoops());
		HashMap<Integer,ArrayList<ArrayList<Integer>>>map = mason.getCombinations();
		int i = 1 ;
		while(map.get(i) != null){
			ArrayList<ArrayList<Integer>>list = map.get(i);
			System.out.println(i + ":");
			for(ArrayList<Integer>z : list){
				System.out.println(z);
			}
			i++;
		}
	}
}
