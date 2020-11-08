public class Node {
	private int num;
	private double latitude;
	private double longitude;
	private boolean finished;
	private int type;
	private Edge edge;
	private Last prev;
	private String name;
	
	public Node() {
	}
	
	public Node(int num) {
		this.num = num;
	}
	
	public int getNum() {
		return num;
	}
	
	public void setNum(int num) {
		this.num = num;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	public Edge getEdge() {
		return edge;
	}
	
	public void setEdge(Edge edge) {
		this.edge = edge;
	}
	
	public Last getPrev() {
		return prev;
	}
	
	public void setPrev(Last prev) {
		this.prev = prev;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.getLatitude() + "," + this.getLongitude();
	}
}
