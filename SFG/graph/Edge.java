package graph;

public class Edge {
	private Node from, to;
	private Double signal;
	public Edge(Node node1, Node node2 ,Double signal){
		this.from = node1;
		this.to = node2;
		this.signal = signal;
	}
	public Node getFrom(){
		return from;
	}
	public Node getTo(){
		return to;
	}
	public Double getSignal(){
		return signal;
	}
}
