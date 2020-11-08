public class Edge {
	private int from;
	private Node to;
	private int weight;
	private Edge nextEdge;
	
	public Edge() {
	}
	
	public Edge(int from, Node to, int weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
	}
	
	public int getFrom() {
		return from;
	}
	
	public Node getTo() {
		return to;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setFrom(int from) {
		this.from = from;
	}
	
	public void setTo(Node to) {
		this.to = to;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public Edge getNextEdge() {
		return nextEdge;
	}
	
	public void setNextEdge(Edge nextEdge) {
		this.nextEdge = nextEdge;
	}
}
