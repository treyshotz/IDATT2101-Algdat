class Last{
	private int distance;
	private int fullDist;
	private int estimatedDistToEnd;
	private Node last;
	private static int inf = 1000000000;
	public int getDistance(){return distance;}
	Last(){
		this.distance = inf;
		this.fullDist = inf;
		this.estimatedDistToEnd = -1;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public int getFullDist() {
		return fullDist;
	}
	
	public void setFullDist(int fullDist) {
		this.fullDist = fullDist;
	}
	
	public int getEstimatedDistToEnd() {
		return estimatedDistToEnd;
	}
	
	public void setEstimatedDistToEnd(int estimatedDistToEnd) {
		this.estimatedDistToEnd = estimatedDistToEnd;
	}
	
	public Node getLast() {
		return last;
	}
	
	public void setLast(Node last) {
		this.last = last;
	}
	
	public static int getInf() {
		return inf;
	}
	
	public static void setInf(int inf) {
		Last.inf = inf;
	}
}
