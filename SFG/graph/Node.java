package graph;

import java.awt.Color;
import java.awt.Point;

public class Node {
	private Integer node;
	private Point position;
	private int indegree = 0;
	private int outdegree = 0;
	private Color color;
	private Color fillColor;
	public Node(Integer node ,Point position){
		this.node = node;
		this.position = position;
		color = Color.black;
		fillColor = Color.pink;
	}
	public Integer getNode() {
		return node;
	}
	public void setNode(Integer node) {
		this.node = node;
	}
	public Point getPosition() {
		return position;
	}
	public void setPosition(Point position) {
		this.position = position;
	}
	public int getIndegree() {
		return indegree;
	}
	public void setIndegree(int indegree) {
		this.indegree = indegree;
	}
	public int getOutdegree() {
		return outdegree;
	}
	public void setOutdegree(int outdegree) {
		this.outdegree = outdegree;
	}
	
	public Color getColor() {
    return this.color;
  }
	
	public Color getFillColor() {
    return this.fillColor;
  }
}
