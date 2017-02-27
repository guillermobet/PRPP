import java.util.Comparator;

public class EdgeComparator implements Comparator<Edge> {

	/* (overriden from Comparator) (necessary to implement a PriorityQueue<Edge>)
	 * compare:		compares two edges
	 * @param e1:	first edge
	 * @param e2:	second edge
	 *
	 * returns:		the result of the comparison
	 */
	@Override
	public int compare(Edge e1, Edge e2) {
		if ((e1.benefit - e1.cost) < (e2.benefit - e2.cost)) {
			return 1;
		}
		else if ((e1.benefit - e1.cost) > (e2.benefit - e2.cost)) {
			return -1;
		}
		return 0;
	}
}