import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Graph {
	
	
	private ArrayList<Node> nodes;
	
	
	int size;
	
	
	private ArrayList<Edge> edges;
	
	
	public Graph(ArrayList<Node> nodes) {
		this.nodes = nodes;
		this.size = nodes.size();
	}
	
	
	public Graph() {
		this.nodes = new ArrayList<>();
		this.edges = new ArrayList<>();
	}
	
	
	/**
	 * Inserts information into edge
	 * The edge documents uses tabs as a separator
	 * Using index of to shorten the time usage, discussed here; https://stackoverflow.com/questions/5965767/performance-of-stringtokenizer-class-vs-string-split-method-in-java
	 *
	 * @param text to split and paste
	 * @param e    edge to set values to
	 */
	void insertToEdge(String text, Edge e) {
		List<String> list = new ArrayList<String>();
		int pos = 0, end, count = 0;
		//Method finds all text part between spaces, but ignores the last text since there is no space, just newline
		while ((end = text.indexOf("\t", pos)) >= 0) {
			count++;
			list.add(text.substring(pos, end));
			pos = end + 1;
			//For saving time it only uses the 3 first text parts and breaks after those are found
			if (count == 3) break;
		}
		int from = Integer.parseInt(list.get(0).trim());
		e.setFrom(from);
		int to = Integer.parseInt(list.get(1).trim());
		e.setTo(this.getNodes().get(to));
		e.setWeight(Integer.parseInt(list.get(2).trim()));
		e.setNextEdge(this.getNodes().get(from).getEdge());
		nodes.get(from).setEdge(e);
		
	}
	
	
	/**
	 * Inserts information into node
	 * The edge documents uses space as a separator
	 * Using index of to shorten the time usage, discussed here; https://stackoverflow.com/questions/5965767/performance-of-stringtokenizer-class-vs-string-split-method-in-java
	 *
	 * @param text to split and paste
	 * @param n    node to set values to
	 */
	void insertToNode(String text, Node n) {
		List<String> list = new ArrayList<String>();
		int pos = 0, end;
		//Method finds all text part between spaces, but ignores the last text since there is no space, just newline
		while ((end = text.indexOf(" ", pos)) >= 0) {
			list.add(text.substring(pos, end));
			pos = end + 1;
		}
		//Adds the last text since it else wise is ignored
		list.add(2, text.substring(pos, text.length() - 1));
		
		n.setNum(Integer.parseInt(list.get(0).trim()));
		n.setLatitude(Double.parseDouble(list.get(1).trim()));
		n.setLongitude(Double.parseDouble(list.get(2).trim()));
		n.setPrev(new Last());
	}
	
	
	/**
	 * Inserts type and name to nodes
	 *
	 * @param text to split and paste
	 */
	void insertTypeToNode(String text) {
		List<String> list = new ArrayList<String>();
		int pos = 0, end;
		while ((end = text.indexOf("\t", pos)) >= 0) {
			list.add(text.substring(pos, end));
			pos = end + 1;
		}
		list.add(2, text.substring(pos, text.length() - 1));
		int nodeNum = Integer.parseInt(list.get(0).trim());
		Node n = this.nodes.get(nodeNum);
		n.setType(Integer.parseInt(list.get(1)));
		n.setName(list.get(2));
	}
	
	
	void resetNodes() {
		for (Node n : nodes) {
			n.setPrev(new Last());
			n.setFinished(false);
			n.setFinished(false);
		}
	}
	
	
	int astar(Node src, Node last) {
		//Makes a priority queue and method for comparing based on previous total distance
		PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> (a.getPrev().getFullDist()) - (b.getPrev().getFullDist()));
		src.getPrev().setDistance(0);
		src.getPrev().setEstimatedDistToEnd(distance(src, last));
		last.setFinished(true);
		pq.add(src);
		
		int count = 0;
		while (!pq.isEmpty()) {
			Node n = pq.poll();
			count++;
			if (n.isFinished()) return count;
			for (Edge edge = n.getEdge(); edge != null; edge = edge.getNextEdge()) {
				Last edgeCheck = edge.getTo().getPrev();
				Last nodeCheck = n.getPrev();
				
				if (edgeCheck.getDistance() > nodeCheck.getDistance() + edge.getWeight()) {
					if (edgeCheck.getEstimatedDistToEnd() == -1)
						edgeCheck.setEstimatedDistToEnd(distance(edge.getTo(), last));
					edgeCheck.setDistance(nodeCheck.getDistance() + edge.getWeight());
					edgeCheck.setLast(n);
					edgeCheck.setFullDist(edgeCheck.getDistance() + edgeCheck.getEstimatedDistToEnd());
					pq.add(edge.getTo());
				}
			}
		}
		return count;
	}
	
	
	ArrayList<Node> astarPoints(Node start, int pointType) {
		ArrayList<Node> nearestNodes = new ArrayList<>();
		
		PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> (a.getPrev().getDistance()) - (b.getPrev().getDistance()));
		start.getPrev().setDistance(0);
		pq.add(start);
		int count = 0;
		
		while (!pq.isEmpty()) {
			Node n = pq.poll();
			if(n.getType() == pointType) {
				nearestNodes.add(n);
				count++;
				n.setFinished(true);
			}
			if(count == 10) break;
			for(Edge edge = n.getEdge(); edge != null; edge = edge.getNextEdge()) {
				//A bit waste to use extra variables here but makes it much easier to understand and debug
				Last edgeCheck = edge.getTo().getPrev();
				Last nodeCheck = n.getPrev();
				if(edgeCheck.getDistance() > nodeCheck.getDistance() + edge.getWeight()) {
					edgeCheck.setDistance(nodeCheck.getDistance() + edge.getWeight());
					edge.getTo().getPrev().setLast(n);
					pq.add(edge.getTo());
				}
			}
		}
		return nearestNodes;
	}
	
	
	int distance(Node n1, Node n2) {
		double sin_width = Math.sin((n1.getLatitude() - n2.getLatitude()) / 2.0);
		double sin_length = Math.sin((n1.getLongitude() - n2.getLongitude()) / 2.0);
		//TODO: This will make the runtime a bit longer... Consider doing this in the insertMethod
		//double cos_width1 = Math.cos(n1.getLatitude());
		//double cos_width2 = Math.cos(n2.getLatitude());
		return (int) (35285538.46153846153846153846 * Math.asin(Math.sqrt(
				sin_width * sin_width + n1.getCosWidth() * n1.getCosWidth() * sin_length * sin_length)));
	}
	
	
	/**
	 * Dijkstra's algorithm for finding quickest path from start node to end node
	 *
	 * @param src  node to start the search from
	 * @param last node to find the quickest path to
	 * @return the amount of nodes checked
	 */
	int dijkstras(Node src, Node last) {
		PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> (a.getPrev().getDistance()) - (b.getPrev().getDistance()));
		src.getPrev().setDistance(0);
		last.setFinished(true);
		pq.add(src);
		int count = 0;
		
		while (!pq.isEmpty()) {
			Node n = pq.poll();
			count++;
			if (n.isFinished()) return count;
			for (Edge edge = n.getEdge(); edge != null; edge = edge.getNextEdge()) {
				
				//Checks to see if a path is shorter than the current one
				if (edge.getTo().getPrev().getDistance() > n.getPrev().getDistance() + edge.getWeight()) {
					edge.getTo().getPrev().setDistance(n.getPrev().getDistance() + edge.getWeight());
					edge.getTo().getPrev().setLast(n);
					pq.add(edge.getTo());
				}
			}
		}
		return count;
	}
	
	
	/**
	 * Dijkstra's algorithm for finding the 10 nearest point of a type (Gas station, Charging spot or Both)
	 *
	 * @param start     node start the search from
	 * @param pointType what kind of type it will search for
	 * @return a list of all the nearest places
	 */
	ArrayList<Node> dijkstraPoints(Node start, int pointType) {
		ArrayList<Node> nearestNodes = new ArrayList<>();
		
		//Sets up priority queue with method for comparing
		PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> (a.getPrev().getDistance()) - (b.getPrev().getDistance()));
		start.getPrev().setDistance(0);
		pq.add(start);
		int count = 0;
		
		while (!pq.isEmpty()) {
			Node n = pq.poll();
			if (n.getType() == pointType) {
				nearestNodes.add(n);
				count++;
				n.setFinished(true);
			}
			if (count == 10) break;
			for (Edge edge = n.getEdge(); edge != null; edge = edge.getNextEdge()) {
				//Checks to see if a path is shorter than the current one
				if (edge.getTo().getPrev().getDistance() > n.getPrev().getDistance() + edge.getWeight()) {
					edge.getTo().getPrev().setDistance(n.getPrev().getDistance() + edge.getWeight());
					edge.getTo().getPrev().setLast(n);
					pq.add(edge.getTo());
				}
			}
		}
		return nearestNodes;
	}
	
	
	void setNodesToDegrees() {
		for (Node n : nodes) {
			n.setLatitude(n.getLatitude() * (180 / Math.PI));
			n.setLongitude(n.getLongitude() * (180 / Math.PI));
			n.setCosWidth(Math.cos(n.getLatitude()));
		}
	}
	
	
	void setNodesToRad() {
		for (Node n : nodes) {
			n.setLatitude(n.getLatitude() * (Math.PI / 180));
			n.setLongitude(n.getLongitude() * (Math.PI / 180));
		}
	}
	
	
	void runAstar(int start, int end) throws IOException {
		Node startNode = this.getNodes().get(start);
		Node endNode = this.getNodes().get(end);
		
		this.setNodesToRad();
		long startTime = System.nanoTime();
		int num = astar(startNode, endNode);
		long endTime = System.nanoTime();
		long resultTime = endTime - startTime;
		this.setNodesToDegrees();
		
		System.out.println("\nA* path time: " + (double) resultTime / 1000000000 + "s");
		FileWriter fileWriter = new FileWriter("/Users/madslun/Documents/Programmering/AlgDat/Oblig8/Java/A*Result.txt");
		System.out.println("Start node: " + startNode.getName() + "| End node: " + endNode.getName());
		System.out.println("Number of nodes checked: " + num);
		System.out.println("Time from start to finish in s: " + (double) endNode.getPrev().getDistance() / 100);
		System.out.println("Time from start to finish in min: " + (double) endNode.getPrev().getDistance() / 6000);
		System.out.println("Time from start to finish in h: " + (double) endNode.getPrev().getDistance() / 360000);
		
		while (endNode != null) {
			fileWriter.write(endNode.toString() + "\n");
			endNode = endNode.getPrev().getLast();
		}
		fileWriter.close();
	}
	
	
	void runAstarPoints(int start, int pointType) throws IOException {
		Node startNode = this.getNodes().get(start);
		long startTime = System.nanoTime();
		ArrayList<Node> result = this.astarPoints(startNode, pointType);
		long endTime = System.nanoTime();
		long resultTime = endTime - startTime;
		
		System.out.println("\nA* point time: " + (double) resultTime / 1000000000 + "s");
		System.out.println("A* point: " + startNode.getName());
		FileWriter fileWriter = new FileWriter("/Users/madslun/Documents/Programmering/AlgDat/Oblig8/Java/A*PointResult.txt");
		for (Node n : result) {
			fileWriter.write(n.toString() + "\n");
		}
		fileWriter.close();
	}
	
	
	/**
	 * A setup method for finding the nearest places of type.
	 * Writes the result to file
	 *
	 * @param start     number, converted to node
	 * @param pointType to search for
	 * @throws IOException
	 */
	void dijkstraRunPoints(int start, int pointType) throws IOException {
		Node startNode = this.nodes.get(start);
		long startTime = System.nanoTime();
		ArrayList<Node> result = this.dijkstraPoints(startNode, pointType);
		long endTime = System.nanoTime();
		long resultTime = endTime - startTime;
		
		System.out.println("\nDijkstra point time: " + (double) resultTime / 1000000000 + "s");
		System.out.println("Dijkstra point: " + startNode.getName());
		FileWriter fileWriter = new FileWriter("/Users/madslun/Documents/Programmering/AlgDat/Oblig8/Java/dijkstraPointResult.txt");
		for (Node n : result) {
			fileWriter.write(n.toString() + "\n");
		}
		fileWriter.close();
	}
	
	
	/**
	 * Setup method for dijkstra's algorithm for finding quickest path from start point to end point
	 * Follows the path back to start node and writes it to file
	 *
	 * @param start number, converted to node
	 * @param end   number, converted to node
	 * @throws IOException
	 */
	void dijkstraRun(int start, int end) throws IOException {
		Node startNode = this.nodes.get(start);
		Node endNode = this.nodes.get(end);
		long startTime = System.nanoTime();
		int num = dijkstras(startNode, endNode);
		long endTime = System.nanoTime();
		long total = endTime - startTime;
		FileWriter fileWriter = new FileWriter("/Users/madslun/Documents/Programmering/AlgDat/Oblig8/Java/dijkstraResult.txt");
		System.out.println("Djikstra time: " + (double) total / 1000000000 + "s");
		System.out.println("Start node: " + startNode.getName() + " | End node: " + endNode.getName());
		System.out.println("Nodes visited " + num);
		System.out.println("Time from start to finish in s: " + (double) endNode.getPrev().getDistance() / 100);
		System.out.println("Time from start to finish in min: " + (double) endNode.getPrev().getDistance() / 6000);
		System.out.println("Time from start to finish in h: " + (double) endNode.getPrev().getDistance() / 360000);
		while (endNode != null) {
			fileWriter.write(endNode.toString() + "\n");
			endNode = endNode.getPrev().getLast();
		}
		fileWriter.close();
	}
	
	
	public ArrayList<Node> getNodes() {
		return nodes;
	}
	
	
	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}
	
	
	public int getSize() {
		return size;
	}
	
	
	public void setSize(int size) {
		this.size = size;
	}
	
	
	public void addNode(Node n) {
		this.nodes.add(n);
	}
	
	
	public void addEdge(Edge edge) {
		this.edges.add(edge);
	}
	
	
	public static void main(String[] args) throws IOException {
		//Path for files
		String nodePath = "/Users/madslun/Documents/Programmering/AlgDat/Oblig8/Java/noder.txt";
		String edgePath = "/Users/madslun/Documents/Programmering/AlgDat/Oblig8/Java/kanter.txt";
		String pointPath = "/Users/madslun/Documents/Programmering/AlgDat/Oblig8/Java/interessepkt.txt";
		BufferedReader bufferedReader;
		Graph g = new Graph();
		
		//Sets up reader for nodes
		bufferedReader = new BufferedReader(new FileReader(nodePath));
		String nodeNumString = bufferedReader.readLine();
		int nodeNum = Integer.parseInt(nodeNumString.trim());
		g.setSize(nodeNum);
		
		//Adds nodes to graph and takes time of adding nodes
		long startTime = System.nanoTime();
		String nodeString;
		while ((nodeString = bufferedReader.readLine()) != null) {
			Node n = new Node();
			g.insertToNode(nodeString, n);
			g.addNode(n);
		}
		long endTime = System.nanoTime();
		long total = endTime - startTime;
		System.out.println("Adding nodes time: " + (double) total / 1000000000 + "s");
		
		
		bufferedReader = new BufferedReader(new FileReader(edgePath));
		//First line contains amount of edges but it is more safe to read while there is some text instead of depending on the amount
		bufferedReader.readLine();
		
		
		//Adds the edges and takes the time of adding the edges
		startTime = System.nanoTime();
		String edgeString;
		while ((edgeString = bufferedReader.readLine()) != null) {
			Edge e = new Edge();
			g.insertToEdge(edgeString, e);
			g.addEdge(e);
		}
		endTime = System.nanoTime();
		total = endTime - startTime;
		System.out.println("Adding edges time: " + (double) total / 1000000000 + "s");
		
		
		//Sets up reader for interest points
		bufferedReader = new BufferedReader(new FileReader(pointPath));
		bufferedReader.readLine();
		
		
		//Add the interest points and takes the time of adding points
		startTime = System.nanoTime();
		String pointString;
		while ((pointString = bufferedReader.readLine()) != null) {
			g.insertTypeToNode(pointString);
		}
		endTime = System.nanoTime();
		total = endTime - startTime;
		System.out.println("Adding type time: " + (double) total / 1000000000 + "s");
		
		
		//Runs the different methods
		//Farsund = 3715190
		//Trondheim lufthavn, Værnes = 6198111
		//Trondheim = 2399829
		//Helsinki = 1221382
		//Gjemnes = 6225195
		//Kårvåg = 6013683
		//Røros hotell = 1117256
		//Højer, DK = 477934
		//Nordkapp = 1914291
		
		
		int start = 477934;
		int end = 1914291;
		int pointPlace = 1117256;
		int pointType = 2;
		g.dijkstraRun(start, end);
		g.resetNodes();
		g.dijkstraRunPoints(pointPlace, pointType);
		g.resetNodes();
		g.runAstar(start, end);
		g.resetNodes();
		g.runAstarPoints(pointPlace, pointType);
		
		//I think this could be an easier way of doing it instead of having so many classes and objects. Should try if I have the time
		//List<List<Edge>> a = new ArrayList<List<Edge>>();
	}
}
