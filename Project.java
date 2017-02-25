import java.util.Scanner;

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
		Scanner	input = new Scanner(System.in);
		int numVertices = getNumber(input);
		int numEdgesRQ = getNumber(input);
		
		Graph graph = new Graph(numVertices, numEdgesRQ);
		fillGraph(graph, input, graph.numEdgesRQ);
		graph.connectedComponents();
		int numEdgesP = getNumber(input);
		
		graph.setNumEdgesP(numEdgesP);
		fillGraph(graph, input, graph.numEdgesP);
		graph.cutVertices(0,0,-1);

		graph.printGraph();
	}
}