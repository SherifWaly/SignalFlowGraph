package solver;

import java.util.ArrayList;

public class ForwardPath implements Cloneable{
	private ArrayList<Integer>path = new ArrayList<Integer>();
	private Double m;
	private Integer mask = 0;
	public ForwardPath(ArrayList<Integer>list , Double gain , Integer mask){
		path = list;
		m = gain;
		this.mask = mask;
	}
	public Double getM(){
		return m;
	}
	public ArrayList<Integer> getPath(){
		return path;
	}
	public Integer getMask() {
		return mask;
	}
	public void setMask(Integer mask) {
		this.mask = mask;
	}
	public Object clone(){
		return this;
	}
	public String toString(){
		String ans = "" ;
		for(Integer i : path){
			ans += i.toString() + " " ;
		}
		return ans;
	}
}