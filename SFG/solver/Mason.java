package solver;
import java.util.ArrayList;


import java.util.HashMap;
import java.util.TreeSet;

import graph.Graph;
import graph.EdgeComponent;
import graph.Node;
public class Mason {
	private ArrayList<Loop>loops;
	private ArrayList<ForwardPath>forwardPaths;
	private ArrayList<Double>deltas;
	private Double delta = 1.0;
	private HashMap<Integer,ArrayList<ArrayList<Integer>>>combinations;
	private boolean calculated = false;
	public Mason(){
		loops = new ArrayList<Loop>();
		forwardPaths = new ArrayList<ForwardPath>();
		deltas = new ArrayList<Double>();
		setCombinations(new HashMap<Integer,ArrayList<ArrayList<Integer>>>());
	}
	public Double solve(Integer from ,Integer to){
		if(from.equals(to)){
			throw new RuntimeException("source and destination must be different");
		}
		if(!find(Graph.getGraph().getNodes().get(from),Graph.getGraph().getNodes().get(to),0)){
			throw new RuntimeException("No paths found");
		}
		Integer r = Graph.getGraph().getR();
		Integer c = Graph.getGraph().getC();
		
		if(r == -1){
			throw new RuntimeException("Graph is invalid");
		}
		loops.clear();
		for(int i = 0 ; i < Graph.getGraph().getVertices() ; i++){
			ArrayList<Integer>nodes = new ArrayList<Integer>();
			nodes.add(i);
			getCycles(Graph.getGraph().getNodes().get(i),Graph.getGraph().getNodes().get(i),1.0,1<<i,loops,new TreeSet<Integer>(),nodes);
		}
		delta = calDelta();
		calculated = true;
		System.out.println(delta);
		try{
			if(from.compareTo(r) == 0 || to.compareTo(c) == 0){
				return solver(Graph.getGraph().getNodes().get(from),Graph.getGraph().getNodes().get(to));
			}
			else{
				return solver(Graph.getGraph().getNodes().get(to),Graph.getGraph().getNodes().get(r)) / solver(Graph.getGraph().getNodes().get(from),Graph.getGraph().getNodes().get(r));
			}
		}
		catch(Exception e){
			throw new RuntimeException("No paths found");
		}
	}
	private Double calDelta() {
		Double d = 0.0;
		int tot = 1 << loops.size();
		for(int i = 0 ; i < tot ; i++){
			ArrayList<Loop>list = new ArrayList<Loop>();
			ArrayList<Integer>list1 = new ArrayList<Integer>();
			int j = 0;
			Double totalGain = 1.0;
			for(Loop loop : loops){
				if((i&(1<<j)) != 0){
					list.add(loop);
					list1.add(j);
					totalGain *= loop.getGain();
				}
				j++;
			}
			boolean flag = checkNonTouchingLoops(list);
			if(!flag){
				continue;
			}
			if(combinations.get(list.size()) == null){
				combinations.put(list.size(), new ArrayList<ArrayList<Integer>>());
			}
			combinations.get(list.size()).add(list1);
			if(list.size()%2 == 0){
				d += totalGain;
			}
			else {
				d -= totalGain;
			}
		}
		//calDeltaRecursively(0 , new ArrayList<Loop>() , 1.0);
		return d;
	}
	//initially calDeltaRecursively(0 , 0.0 , new ArrayList<Loop>() , 1.0)
	/*private void calDeltaRecursively(int i , ArrayList<Loop>list , Double gain){
		if(i == loops.size()){
			boolean flag = checkNonTouchingLoops(list);
			if(flag){
				if(list.size() % 2 == 0){
					d += gain;
				}
				else{
					d -= gain;
				}
			}
			return;
		}
		calDeltaRecursively(i+1, list, gain);     //loop number i is skipped
		
		list.add(loops.get(i));
		calDeltaRecursively(i+1, list, gain * loops.get(i).getGain());
		list.remove(list.size()-1);           //remove the loop again
	}*/
	private boolean checkNonTouchingLoops(ArrayList<Loop> list) {
		for(int i = 0 ; i < list.size() ; i++){
			for(int j = i+1 ; j < list.size() ; j++){
				if((list.get(i).getMask() & list.get(j).getMask()) != 0){
					return false;
				}
			}
		}
		return true;
	}
	private boolean find(Node node, Node node2 ,Integer mask) {
		if(node == node2){
			return true;
		}
		ArrayList<EdgeComponent>adj = Graph.getGraph().getAdjacent(node.getNode());
		for(EdgeComponent e : adj){
			Node x = e.getNode();
			if((mask&(1 << x.getNode())) != 0){
				continue;
			}
			if(find(x,node2,(mask|(1 << x.getNode())))){
				return true;
			}
		}
		return false;
	}
	private Double solver(Node from ,Node to){
		forwardPaths.clear();
		getForwardPaths(from, to, 0 ,forwardPaths ,new ArrayList<Integer>(),1.0);
		if(forwardPaths.size() == 0){
			throw new RuntimeException();
		}
		Double totalTransfer = 0.0;
		deltas.clear();
		for(ForwardPath path : forwardPaths){
			Double deltaI = 0.0;
			int tot = 1 << loops.size();
			for(int i = 0 ; i < tot ; i++){
				ArrayList<Loop>list = new ArrayList<Loop>();
				int j = 0;
				Double totalGain = 1.0;
				for(Loop loop : loops){
					if((i&(1<<j)) != 0){
						if((loop.getMask() & path.getMask()) != 0){
							break;
						}
						list.add(loop);
						totalGain *= loop.getGain();
					}
					j++;
				}
				if(j < loops.size()){
					continue;
				}
				boolean flag = checkNonTouchingLoops(list);
				if(!flag){
					continue;
				}
				if(list.size()%2 == 0){
					deltaI += totalGain;
				}
				else {
					deltaI -= totalGain;
				}
				System.out.println(deltaI + " " + totalGain + " " + list);
			}
			totalTransfer += deltaI * path.getM();
			deltas.add(deltaI);
		}
		return totalTransfer / delta;
	}
	private void getForwardPaths(Node from, Node to, int mask,
			ArrayList<ForwardPath> forwardPaths, ArrayList<Integer> list , Double gain) {
		list.add(from.getNode());
		if(from == to){
			forwardPaths.add(new ForwardPath((ArrayList<Integer>) list.clone(),gain,mask));
			list.remove(list.size()-1);
			return;
		}
		ArrayList<EdgeComponent>adj = Graph.getGraph().getAdjacent(from.getNode());
		for(EdgeComponent e : adj){
			Node x = e.getNode();
			if((mask&(1 << x.getNode())) != 0){
				continue;
			}
			getForwardPaths(x, to, (mask|(1 << x.getNode())), forwardPaths, list, gain * e.getSignal());
		}
		list.remove(list.size()-1);
	}
	private void getCycles(Node from, Node to, Double gain, int mask,
			ArrayList<Loop> loops, TreeSet<Integer> set , ArrayList<Integer>nodes) {
		ArrayList<EdgeComponent>adj = Graph.getGraph().getAdjacent(from.getNode());
		for(EdgeComponent e : adj){
			Node x = e.getNode();
			if((mask&(1 << x.getNode())) != 0 && x != to){
				continue;
			}
			else if((mask&(1 << x.getNode())) != 0){
				set.add(e.getEdgeNum());
				Loop loop = new Loop();
				loop.setEdges((TreeSet<Integer>) set.clone());
				loop.setMask(mask | (1 << x.getNode()));
				loop.setNodes((ArrayList<Integer>) nodes.clone());
				loop.setGain(gain*e.getSignal());
				boolean flag = false;
				for(Loop xx : loops){
					if(xx.equals(loop)){
						flag = true;
						break;
					}
				}
				if(!flag){
					loops.add(loop);
				}
				set.remove(e.getEdgeNum());
			}
			else{
				set.add(e.getEdgeNum());
				nodes.add(x.getNode());
				getCycles(x, to, gain * e.getSignal(), mask | (1 << x.getNode()), loops, set , nodes);
				set.remove(e.getEdgeNum());
				nodes.remove(nodes.size()-1);
			}
		}
	}
	public ArrayList<ForwardPath> getForwardPaths(){
		return forwardPaths;
	}
	public ArrayList<Loop>getLoops(){
		return loops;
	}
	public ArrayList<Double>getDeltas() {
		return deltas;
	}
	public HashMap<Integer,ArrayList<ArrayList<Integer>>> getCombinations() {
		return combinations;
	}
	public void setCombinations(HashMap<Integer,ArrayList<ArrayList<Integer>>> combinations) {
		this.combinations = combinations;
	}
	public Double getDelta(){
		if(calculated){
			return delta;
		}
		else{
			throw new RuntimeException();
		}
	}
}
