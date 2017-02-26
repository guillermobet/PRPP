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

	public static ArrayList<Integer> cycleMaker(ArrayList<Integer> myPath, ArrayList<Integer> myBackPath) {
		ArrayList<Integer> myCycle = new ArrayList<Integer>();

		for (int i = 0; i < myPath.size(); i++) {
			myCycle.add(i, myPath.get(i));
		}
		for (int i = 1; i < myBackPath.size(); i++) {
			myCycle.add(myBackPath.get(i));
		}
		return myCycle;
	}

	public static void main (String[] args) {
		try {
			long start = System.currentTimeMillis();
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
			
			ArrayList<Integer> myPath, myBackPath, myCycle, myOptimizedCycle, bestCycle;
			int myReward, myBackReward, bestOverallReward, optimizedOverallReward;

			bestCycle = null;
			bestOverallReward = Integer.MIN_VALUE;

			for (int k = 0; k < primPaths.size(); k++) {
				myPath = primPaths.get(k);
				myReward = primRewards.get(k);
				
				graph.restartVisited();
				graph.reconfigureBenefit(myPath);
				
				myBackPath = graph.modifiedDijkstra(myPath.get(myPath.size()-1), 0);
				myBackReward = myBackPath.remove(myBackPath.size()-1);

				myCycle = cycleMaker(myPath, myBackPath);
				graph.reconfigureBenefit(null);
				//

				myOptimizedCycle = graph.optimizeSolution(myCycle);
				//System.out.println("FOO: " + foo);
				optimizedOverallReward = myReward + myBackReward - myOptimizedCycle.remove(myOptimizedCycle.size()-1);
				graph.reconfigureBenefit(null);


				//System.out.println("Optimized cycle: " + myOptimizedCycle);
				if (bestOverallReward < optimizedOverallReward) {
					bestOverallReward = myReward + myBackReward;
					bestCycle = myCycle;
				}
			}
			
			System.out.println("\nBest cycle found:\n" + bestCycle);
			System.out.println("\nBest overall reward: " + bestOverallReward);
			input.close();
			long stop = System.currentTimeMillis();
			System.out.printf("Time elapsed: %d milliseconds\n", (stop - start));
			//System.out.println("\t" + (stop - start) + "\t\t\t\t" + bestOverallReward + "\t\t\t" + bestCycle);

		}
		catch (FileNotFoundException fnfe) {
			System.out.printf("File \"%d\" not found. Program will abort\n", args[1]);
		}
	}
}