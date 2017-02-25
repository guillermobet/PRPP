import java.util.ArrayList;
import java.util.Set;

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

	public void setNumEdgesP(int numEdgesP) {
		this.numEdgesP = numEdgesP;
	}

	//Union-Find Integer.MAX_VALUE;
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
		this.vertices.get(vertex).articulation = false;
		int depth = d;
		int low = d;
		int childCount = 0;
		int newVertex;
		int[] childInfo = {-1,-1};
		int[] ret =  new int[2];
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
		if ((parent != -1 && this.vertices.get(vertex).articulation) || (parent == -1 && childCount > 1)) {
			this.vertices.get(vertex).articulation = true;
		}
		ret[0] = low;
		ret[1] = depth;
		return ret;
	}
}