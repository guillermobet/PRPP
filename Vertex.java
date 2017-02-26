import java.util.ArrayList;

public class Vertex {

	/* Members: vertex identifier, connected component number,
	 *			visited boolean, articulation boolean and and
	 *			ArrayList of incident edges
	 */
	public int id, connected, priority;
	public boolean visited, articulation;
	public ArrayList<Edge> incidents;

	/* Constructor:
	 * @param id: vertex identifier
	 */
	public Vertex(int id) {
		this.id = id;
		this.connected = -1;
		this.visited = false;
		this.articulation = false;
		this.incidents = new ArrayList<Edge>();
		//this.incidents.clear();
	}

	/* addIncident: adds an incident edge to the vertex
	 * @param v1: vertex 1 that connects with vertex 2 by the edge
	 * @param v2: vertex 2 that connects with vertex 1 by the edge
	 * @param cost: cost of passing through the edge
	 * @param benefit: benefit of the new edge (just collectable once)
	 */
	public void addIncident(int v1, int v2, int cost, int benefit) {
		this.incidents.add(new Edge(v1, v2, cost, benefit));
	}

	/* printVertex: print the vertex in human-readable format
	 */
	public void printVertex() {
		System.out.printf("\nVertex %d\n", this.id);
		System.out.printf("\tConnected component: %d\n", this.connected);
		System.out.printf("\tVisited: %b\n", this.visited);
		System.out.printf("\tArticulation: %b\n", this.articulation);
		System.out.printf("\tEdges: %d edge(s)\n", this.incidents.size());
		for (int i = 0; i < this.incidents.size(); i++) {
			this.incidents.get(i).printEdge();
		}
	}

}