public class Edge implements Comparable<Edge>{

	/* Members: vertex 1, vertex 2, cost and benefit of the edge
	 */
	public int v1, v2, cost, benefit;
	public char type;

	/* Constructor:
	 * @param v1: vertex 1 that connects with vertex 2 by the edge
	 * @param v2: vertex 2 that connects with vertex 1 by the edge
	 * @param cost: cost of passing through the edge
	 * @param benefit: benefit of the new edge (just collectable once)
	 */
	public Edge(int v1, int v2, int cost, int benefit) {
		this.v1 = (v1 < v2) ? v1 : v2;
		this.v2 = (v1 >= v2) ? v1 : v2;
		this.cost = cost;
		this.benefit = benefit;
		if (benefit - 2*cost >= 0) {
			this.type = 'R';
		}
		else if (benefit - cost >= 0) {
			this.type = 'Q';
		}
		else {
			this.type = 'P';
		}
	}

	/* printEdge: print the Edge in human-readable format
	 */
	public void printEdge() {System.out.printf("\t\tV%d - V%d | Cost = %d | Benefit %d | Type %c\n", this.v1, this.v2, this.cost, this.benefit, this.type);}

	public boolean connectsVertex(int v) {return (v == this.v1 || v == this.v2);}

	/* (overriden from Comparable)
	 * compareTo:		compares to edges
	 * @param e:		edge to compare to
	 *
	 * @returns ret:	result of the comparison
	 */
	@Override
	public int compareTo(Edge e) {
		int ret = (((this.v1 == e.v1) && (this.v2 == e.v2)) || (this.v1 == e.v2) && (this.v2 == e.v1)) ? 0 : 1;
		return ret;
	}

}