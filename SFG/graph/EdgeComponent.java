package graph;

public class EdgeComponent {
	private Node node;
	private Double signal;
	private Integer edgeNum;
	public EdgeComponent(Node node ,Double signal ,Integer edgeNum){
		this.node = node;
		this.signal = signal;
		this.edgeNum = edgeNum;
	}
	public Node getNode() {
		return node;
	}
	public void setNode(Node node) {
		this.node = node;
	}
	public Double getSignal() {
		return signal;
	}
	public void setSignal(Double signal) {
		this.signal = signal;
	}
	public Integer getEdgeNum(){
		return edgeNum;
	}
}
