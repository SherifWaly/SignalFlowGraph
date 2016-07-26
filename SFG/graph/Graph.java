package graph;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph {
	private ArrayList<Edge>edges;
	private int numNodes = 0;
	private Map<Node,ArrayList<EdgeComponent>>map;
	private Map<Integer,Node>nodes;
	private static Graph graph = null;
	
	//singleton
	public static Graph getGraph(){
		if(graph == null){
			graph = new Graph();
		}
		return graph;
	}
	private Graph(){
		edges = new ArrayList<Edge>();
		map = new HashMap<Node,ArrayList<EdgeComponent>>();
		nodes = new HashMap<Integer,Node>();
	}
	public void addNode(Point pos){
		Node node = new Node(numNodes,pos);
		ArrayList<EdgeComponent>list = new ArrayList<EdgeComponent>();
		map.put(node, list);
		nodes.put(numNodes, node);
		numNodes++;
	}
	public void addEdge(int from ,int to ,Double signal){
		Node x = nodes.get(from);
		Node y = nodes.get(to);
		x.setOutdegree(x.getOutdegree()+1);
		y.setIndegree(y.getIndegree()+1);
		Edge edge = new Edge(x,y,signal);
		edges.add(edge);
		EdgeComponent e = new EdgeComponent(y,signal,edges.size());
		map.get(x).add(e);
	}
	public ArrayList<EdgeComponent>getAdjacent(int x){
		return map.get(nodes.get(x));
	}
	public int getVertices(){
		return numNodes;
	}
	public int getNumEdges(){
		return edges.size();
	}
	public Map<Integer,Node>getNodes(){
		return nodes;
	}
	public ArrayList<Edge>getEdges(){
		return edges;
	}
	public Integer getR(){
		for(Map.Entry<Integer,Node>entry : nodes.entrySet()){
			if(entry.getValue().getIndegree() == 0){
				return entry.getKey();
			}
		}
		return -1;
	}
	public Integer getC(){
		for(Map.Entry<Integer,Node>entry : nodes.entrySet()){
			if(entry.getValue().getOutdegree() == 0){
				return entry.getKey();
			}
		}
		return -1;
	}
}
