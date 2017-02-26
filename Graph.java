import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;

public class Graph {

	/* Members: number of vertices, number of R and Q edges
	 * 			and ArrayList of Vertex structure
	 */
	public int numVertices, numEdgesRQ, numEdgesP;
	public ArrayList<Vertex> vertices;

	/* Constructor:
	 * @param numVertices: number of vertices
	 * @param numEdgesRQ: number of edges 
	 */
	public Graph(int numVertices, int numEdgesRQ) {
		this.numVertices = numVertices;
		this.numEdgesRQ = numEdgesRQ;
		vertices = new ArrayList<Vertex>(numVertices);
		for (int i = 1; i <= numVertices; i++) {
			vertices.add(new Vertex(i));
		}
	}

	/* printGraph: print the graph in human-readable format
	 */
	public void printGraph() {
		for (int i = 0; i < this.numVertices; i++) {
			this.vertices.get(i).printVertex();
		}
		System.out.printf("\nTotal number of vertices: %d\nTotal number of edges: %d\n\n", this.numVertices, this.numEdgesRQ + this.numEdgesP);
	}

	public void setNumEdgesP(int numEdgesP) {this.numEdgesP = numEdgesP;}

	//Union-Find;
	public void connectedComponents() {
		int[] unionFind = new int[this.numVertices];
		int nextComponent = 1;
		for (int i = 0; i < this.numVertices; i++) {
			unionFind[i] = i+1;
		}
		for (Vertex v : this.vertices) {
			for (Edge e : this.vertices.get(v.id-1).incidents) {
				if (unionFind[e.v1-1] != unionFind[e.v2-1]) {
					unionFind[e.v1-1] = Math.min(unionFind[e.v1-1], unionFind[e.v2-1]);
					unionFind[e.v2-1] = Math.min(unionFind[e.v1-1], unionFind[e.v2-1]);
				}
			}
		}
		for (int i = 0; i < this.numVertices; i++) {
			this.vertices.get(i).connected = unionFind[i];
		}
	}

	public void restartVisited() {
		for (int i = 0; i < this.numVertices; i++) {
			this.vertices.get(i).visited = false;
		}
	}

	public void cutVertices(int vertex, int d, int parent) {
		innerCutVertices(0,0,-1);
		restartVisited();
	}

	public int[] innerCutVertices(int vertex, int d, int parent) {
		this.vertices.get(vertex).visited = true;
		int depth = d;
		int low = d;
		int childCount = 0;
		int newVertex;
		int[] childInfo = {-1,-1}; //low, depth
		int[] ret =  new int[2];
		this.vertices.get(vertex).articulation = false;
		for (Edge e : this.vertices.get(vertex).incidents) {
			newVertex = (e.v1-1 == vertex) ? e.v2-1: e.v1-1;
			if (!this.vertices.get(newVertex).visited) {
				childInfo = innerCutVertices(newVertex, d+1, vertex);
				childCount += 1;
				if (childInfo[0] >= depth) {
					this.vertices.get(vertex).articulation = true;
				}
				low = Math.min(low, childInfo[0]);
			}
			else if (newVertex != parent) {
				low = Math.min(low, childInfo[1]);
			}
		}
		//System.out.printf("PRE Vertex %d - articulation: %b\n", vertex+1, this.vertices.get(vertex).articulation);
		if ((parent != -1 && this.vertices.get(vertex).articulation) || (parent == -1 && childCount > 1)) {
			this.vertices.get(vertex).articulation = true;
		}
		//System.out.printf("POST Vertex %d - articulation: %b\n", vertex+1, this.vertices.get(vertex).articulation);
		ret[0] = low;
		ret[1] = depth;
		return ret;
	}

	public ArrayList<ArrayList<Integer>> rebuildPaths(ArrayList<Integer> predecessor, ArrayList<Integer> costBenefit) {
		int maxCost = Integer.MIN_VALUE;
		ArrayList<Integer> maxVertices = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> paths;
		int pred;
		
		for (int i = 0; i < costBenefit.size(); i++) {
			maxCost = Math.max(maxCost,costBenefit.get(i));
		}
		for (int i = 0; i < costBenefit.size(); i++) {
			if (maxCost == costBenefit.get(i)) {
				maxVertices.add(i);
			}
		}

		paths = new ArrayList<ArrayList<Integer>>(maxVertices.size()+1);

		for (int i = 0; i < maxVertices.size()+1; i++) {
			paths.add(new ArrayList<Integer>());
		}

		for (int i = 0; i < paths.size()-1; i++) {
			pred = maxVertices.get(i);
			paths.get(i).add(0, pred);
			//System.out.println(pred);
			while (predecessor.get(pred) != -1) {
				pred = predecessor.get(pred);
				paths.get(i).add(0, pred);
			}
			//paths.get(i).add(0, pred);
		}

		for (int i = 0; i < maxVertices.size(); i++) {
			paths.get(paths.size()-1).add(costBenefit.get(maxVertices.get(i)));
		}

		return paths;
	}

	public ArrayList<Integer> modifiedDijkstra(int vertex) {
		return null;
	}

	public ArrayList<ArrayList<Integer>> maxSTPrim(int vertex) {
		ArrayList<Integer> costBenefit = new ArrayList<Integer>();
		ArrayList<Integer> predecessor = new ArrayList<Integer>();
		Comparator<Edge> edgeComparator = new EdgeComparator();
		PriorityQueue<Edge> pqueue = new PriorityQueue<Edge>(this.numEdgesP + this.numEdgesRQ, edgeComparator);
		ArrayList<ArrayList<Integer>> paths;
		Edge currentEdge;
		int previousVertex = vertex;
		int currentVertex;

		for (int i = 0; i < this.numVertices; i++) {
			costBenefit.add(Integer.MIN_VALUE);
			predecessor.add(-1);
		}

		
		for (Edge e : this.vertices.get(vertex).incidents) {
			pqueue.add(e);
		}

		costBenefit.set(vertex, 0);
		predecessor.set(vertex, -1);
		this.vertices.get(vertex).visited = true;

		while (pqueue.size() > 0) {
			currentEdge = pqueue.remove();
			if (!this.vertices.get(currentEdge.v1-1).visited || !this.vertices.get(currentEdge.v2-1).visited) {
				previousVertex = (this.vertices.get(currentEdge.v1-1).visited) ? currentEdge.v1-1 : currentEdge.v2-1;
				currentVertex = (this.vertices.get(currentEdge.v1-1).visited) ? currentEdge.v2-1 : currentEdge.v1-1;
				this.vertices.get(currentVertex).visited = true;
				predecessor.set(currentVertex, previousVertex);
				costBenefit.set(currentVertex, costBenefit.get(previousVertex) + (currentEdge.benefit - currentEdge.cost));
				for (Edge e : this.vertices.get(previousVertex).incidents) {
					if (e.compareTo(currentEdge) == 0) {
						e.benefit = (e.benefit >= 0) ? -e.benefit : e.benefit;
						currentEdge.benefit = (currentEdge.benefit >= 0) ? -currentEdge.benefit : currentEdge.benefit;
					}
				}
				for (Edge e : this.vertices.get(currentVertex).incidents) {
					if (e.compareTo(currentEdge) == 0) {
						e.benefit = (e.benefit >= 0) ? -e.benefit : e.benefit;
						currentEdge.benefit = (currentEdge.benefit >= 0) ? -currentEdge.benefit : currentEdge.benefit;
					}
					else if (e.compareTo(currentEdge) == 1) {
						pqueue.add(e);
					}
				}
			}
		}

		return rebuildPaths(predecessor, costBenefit);

	}
}