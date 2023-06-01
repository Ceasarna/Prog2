// PROG2 VT2021, Inlämningsuppgift, del 1
// Grupp 375
// Tommy Ekberg toek3476

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class ListGraph<T> implements Graph<T>{
	
	private Map<T, Set<Edge<T>>> nodes = new HashMap<>();
	
	public void add(T x) {
		nodes.putIfAbsent(x, new HashSet<>());
	} // Add
	
	public void remove(T x) {
		if (!nodes.containsKey(x)) {
			throw new NoSuchElementException("Vid remove");
		}
		Set<Edge<T>> tempNodes =  new HashSet<>();
		for(T t : nodes.keySet()) {
			for(Edge<T> e : nodes.get(t)) {		
				if(e.getDestination().equals(x)) {
					tempNodes.add(e);
				}	
			}
			nodes.get(t).removeAll(tempNodes);
		}	
		nodes.remove(x);
	} // Remove
	
	public void connect(T x, T y, String name, int weight){
		if (!nodes.containsKey(x) || !nodes.containsKey(y)) {
			throw new NoSuchElementException("Minst en av dessa Object finns inte!");
		}
		if(weight < 0) {
			throw new IllegalArgumentException("Vid connect");
		}
		
		if(getEdgeBetween(x, y) != null) {
			throw new IllegalStateException("Det finns redan en förbindelse mellan dessa två noder!");
		}
		
		Set<Edge<T>> se1 = nodes.get(x);
		Edge<T> e1 = new Edge<T>(y, name, weight);
		se1.add(e1);
			
		Set<Edge<T>> se2 = nodes.get(y);
		Edge<T> e2 = new Edge<T>(x, name, weight);
		se2.add(e2);
	} // Connect
	
	public void disconnect(T x, T y) {
		if (!nodes.containsKey(x) || !nodes.containsKey(y)) {
			throw new NoSuchElementException("Vid disconnect");
		}
		
		if(getEdgeBetween(x, y) == null) {
			throw new IllegalStateException("Vid disconnect");

		}			
		nodes.get(x).remove(getEdgeBetween(x, y));
		nodes.get(y).remove(getEdgeBetween(y, x));		
	}
	
	public void setConnectionWeight(T x, T y, int newWeight) {
		if (!nodes.containsKey(x) || !nodes.containsKey(y)) {
			throw new NoSuchElementException("Vid setConnectionWeight");
		}
		
		if(newWeight < 0 ) {
			throw new IllegalArgumentException("Vid setConncetionWeight");
				
		}
		
		getEdgeBetween(x, y).setWeight(newWeight);
		getEdgeBetween(y, x).setWeight(newWeight);
	}
	
	public Set<T> getNodes() {
		return nodes.keySet();
	}
	
	public Set<Edge<T>> getEdgesFrom(T x){
	    if (!nodes.containsKey(x)) {
			throw new NoSuchElementException("Vid setConnectionWeight");
		}
		return nodes.get(x);
	}
	
	public Edge<T> getEdgeBetween(T x, T y){
	    if (!nodes.containsKey(x) || !nodes.containsKey(y)) {
			throw new NoSuchElementException("Vid setConnectionWeight");
		}
		for(Edge<T> e : nodes.get(x)) {
			if(e.getDestination().equals(y)) {
				return e;
			}
		}
		
		for(Edge<T> e : nodes.get(y)) {
			if(e.getDestination().equals(x)) {
				return e;
			}
		}
		return null;
	}
	
	public String toString() {
		String str = "";
		for(T t : nodes.keySet()) {
			str += t + ":";
			for(Edge<T> e : nodes.get(t)) {
				str += e.toString();
			}
			str += "\n";
		}
		return str;
	}
	
	public boolean pathExists(T x, T y) {
		if (!nodes.containsKey(x) || !nodes.containsKey(y)) {
			return false;
		}
		
		Set<T> visited = new HashSet<>();
		depthFirstSearch(x, visited);
		return visited.contains(y);
	}
	
	private void depthFirstSearch(T x, Set<T> visited) {
		visited.add(x);
		for(Edge<T> e : nodes.get(x)) {
			if(!visited.contains(e.getDestination())) {
				depthFirstSearch(e.getDestination(), visited);
			}
		}
	}
	
	private void depthFirstSearch(T x, T y, Set<T> visited, Map<T, T> through) {
		visited.add(x);
		through.put(x, y);
		for(Edge<T> e : nodes.get(x)) {
			if(!visited.contains(e.getDestination())) {
				depthFirstSearch(e.getDestination(), x, visited, through);
			}
		}
	}
	
	public List<Edge<T>> getPath(T x, T y){
		Set<T> visited = new HashSet<>();
		Map<T, T> through = new HashMap<>();
		depthFirstSearch(x, null, visited, through);
		List<Edge<T>> path = new ArrayList<>();
		if(!visited.contains(y)) {
			return null;
		}
		
		T where = y;
		while(!where.equals(x)) {
			T node = through.get(where);
			Edge<T> e = getEdgeBetween(node, where);
			path.add(e);
			where = node;
		}
		Collections.reverse(path);
		return path;
		
	}
	
	
} // ListGraph