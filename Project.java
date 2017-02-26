import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class Project {

	public static int getNumber(Scanner input) {
		String[] line = input.nextLine().split(" ");
		int number = -1;
		for (String word : line) {
			try {
				number = Integer.parseInt(word);
			}
			catch (NumberFormatException nfe) {}
		}
		return number;
	}

	public static void fillGraph(Graph graph, Scanner input, int numEdges) {
		String[] edgeStr;
		int[] edgeInt = {0, 0, 0, 0};
		for (int i = 0; i < numEdges; i++) {
			edgeStr = input.nextLine().split(" ");
			for (int j = 0; j < edgeInt.length; j++) {
				edgeInt[j] = Integer.parseInt(edgeStr[j]);
			}
			graph.vertices.get(edgeInt[0]-1).addIncident(edgeInt[0],edgeInt[1],edgeInt[2],edgeInt[3]);
			graph.vertices.get(edgeInt[1]-1).addIncident(edgeInt[0],edgeInt[1],edgeInt[2],edgeInt[3]);
		}
	}

	public static void main (String[] args) {
		try {
			Scanner	input = new Scanner(new File(args[0]));
			int numVertices = getNumber(input);
			int numEdgesRQ = getNumber(input);
			
			Graph graph = new Graph(numVertices, numEdgesRQ);
			fillGraph(graph, input, graph.numEdgesRQ);
			graph.connectedComponents();
			int numEdgesP = getNumber(input);
			
			graph.setNumEdgesP(numEdgesP);
			fillGraph(graph, input, graph.numEdgesP);

			//graph.cutVertices(0,0,-1); // check
			
			ArrayList<ArrayList<Integer>> paths = graph.maxSTPrim(0);
			graph.restartVisited();

			for (ArrayList<Integer> i : paths) {
				System.out.println(i);
			}
			
			graph.printGraph();
		}
		catch (FileNotFoundException fnfe) {
			System.out.printf("File \"%d\" not found. Program will abort\n", args[1]);
		}
	}
}

		/* MAIN DEBUG
		
		Comparator<Edge> comparator = new EdgeComparator();
		PriorityQueue<Edge> pq = new PriorityQueue<Edge>(graph.numEdgesP + graph.numEdgesRQ, comparator);

		for (Edge e : graph.vertices.get(4).incidents) {
			pq.add(e);
		}

		while (pq.size() > 0) {
			pq.remove().printEdge();
		}
		*/