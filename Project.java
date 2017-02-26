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
			
			ArrayList<ArrayList<Integer>> primPaths = graph.maxSTPrim(0);
			ArrayList<Integer> primRewards = primPaths.remove(primPaths.size()-1);
			graph.restartVisited();
			

			for (int k = 0; k < primPaths.size(); k++) {
				ArrayList<Integer> myPath = primPaths.get(k);
				int myReward = primRewards.get(k);
				
				graph.restartVisited();
				graph.reconfigureBenefit(myPath);
				
				ArrayList<Integer> myBackPath = graph.modifiedDijkstra(myPath.get(myPath.size()-1), 0);
				int myBackReward = myBackPath.remove(myBackPath.size()-1);

				System.out.println("myPath: " + myPath);
				System.out.println("myBackPath: " + myBackPath);
				System.out.println("myOverallReward: " + (myReward + myBackReward));
				System.out.println("----------");
			}

			//ArrayList<Integer> dijkstra;
			//int backBenefit;

			//int backVertex = primPaths.get(1).remove(primPaths.get(1).size()-1);
/*			for (int i = 0; i < primPaths.size(); i++) {
				graph.reconfigureBenefit(primPaths.get(i));
				dijkstra = graph.modifiedDijkstra(backVertex, 0);
				graph.restartVisited();
				backBenefit = dijkstra.remove(dijkstra.size()-1);
				graph.reconfigureBenefit(dijkstra);
				System.out.println("Dijkstra: " + dijkstra);
				System.out.println("Cycle reward: " + (primCosts.get(i) + backBenefit));
				System.out.println("---------");
			}
*/
			
		}
		catch (FileNotFoundException fnfe) {
			System.out.printf("File \"%d\" not found. Program will abort\n", args[1]);
		}
	}
}