import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Project {

	/* getNumber:		reads a number from a file with alphanumeric values
	 * @param input:	scanner to read the input
	 *
	 * @returns number: the number read
	 */
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

	/* fillGraph:		fills an empty graph structure with the file being read
	 *					with the Scanner input
	 * @param graph:	graph structure
	 * @param input:	input scanner
	 * @numEdges:		number of edges
	 */
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

	/* cycleMaker:			concatenates two paths
	 * @param myPath:		first path	
	 * @param myBackPath:	second path
	 *
	 * @returns:			the concatenation of the two paths
	 */
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

			ArrayList<ArrayList<Integer>> primPaths = graph.maxSTPrim(0);
			ArrayList<Integer> primRewards = primPaths.remove(primPaths.size()-1);
			graph.restartVisited();
			
			ArrayList<Integer> myPath, myBackPath, myCycle, myOptimizedCycle, bestCycle;
			int myReward, myBackReward, bestOverallReward, optimizedOverallReward, optimizacionImprovement;

			bestCycle = null;
			bestOverallReward = Integer.MIN_VALUE;

			for (int k = 0; k < primPaths.size(); k++) {
				myPath = primPaths.get(k);
				myReward = primRewards.get(k);

				if (myPath.get(0) != 0) {continue;}

				graph.restartVisited();
				graph.reconfigureBenefit(myPath);
				
				myBackPath = graph.modifiedDijkstra(myPath.get(myPath.size()-1), 0);
				myBackReward = myBackPath.remove(myBackPath.size()-1);

				myCycle = cycleMaker(myPath, myBackPath);
				graph.reconfigureBenefit(null);

				myOptimizedCycle = graph.optimizeSolution(myCycle);
				optimizacionImprovement = myOptimizedCycle.remove(myOptimizedCycle.size()-1);
				optimizedOverallReward = myReward + myBackReward + optimizacionImprovement;
				graph.reconfigureBenefit(null);

				if (bestOverallReward < optimizedOverallReward) {
					bestOverallReward = optimizedOverallReward;
					bestCycle = myCycle;
				}
			}
			
			System.out.println("\nBest cycle found:\n");
			for (Integer i : bestCycle) {
				if (i != 0) {
					System.out.print((i+1) + " ");
				}
				else {
					System.out.print("d ");
				}
			}
			System.out.println("\n\nBest overall reward: " + bestOverallReward);
			input.close();
			long stop = System.currentTimeMillis();
			System.out.printf("\nTime elapsed: %d milliseconds\n", (stop - start));
			System.out.println("\nGenerating output file \"" + args[0] + "-salida.txt\"");

			BufferedWriter bw = null;
			FileWriter fw = null;
			
			try {
				fw = new FileWriter(args[0] + "-salida.txt");
				bw = new BufferedWriter(fw);
				bw.write(String.valueOf(bestOverallReward));
				bw.write("\n");
				for (int i = 0; i < bestCycle.size(); i++) {
					if (bestCycle.get(i) != 0){
						bw.write(String.valueOf(bestCycle.get(i)+1) + " ");
					}
					else {
						bw.write("d ");
					}
				}
				if (bw != null) {bw.close();}
				if (fw != null) {fw.close();}
			}
			catch (IOException e) {}
			System.out.println("\nSuccessfully generated output file \"" + args[0] + "-salida.txt\"\n");
		}
		catch (FileNotFoundException fnfe) {
			System.out.printf("File \"%d\" not found. Program will abort\n", args[1]);
		}
	}
}