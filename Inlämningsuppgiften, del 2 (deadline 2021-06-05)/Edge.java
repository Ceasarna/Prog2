// PROG2 VT2021, Inlämningsuppgift, del 1
// Grupp 375
// Tommy Ekberg toek3476

public class Edge<T>{
	private T destination;
	private String name;
	private int weight;
	
	public Edge(T destination, String name, int weight){
		this.destination = destination;
		this.name = name;
		this.weight = weight;
	}	
	public Edge(String name){
			this.destination = null;
			this.name = name;
			this.weight = 0;
	}	
	public T getDestination(){
		return destination;
	}
	
	public int getWeight(){
		return weight;
	}
	
	public void setWeight(int newWeight) {
		if(newWeight < 0) {
			throw new IllegalArgumentException("Vid setWeight");
		}
		weight = newWeight;
	}
	public String getName(){
		return name;
	}

	public String toString(){
		return " to " + destination + " by " + name + " takes " + weight ;
	}
}
