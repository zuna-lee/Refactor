package zuna.metric.cohesion;

import java.util.ArrayList;
import java.util.Hashtable;

import zuna.metric.Metric;
import zuna.model.MyClass;
import zuna.model.MyField;
import zuna.model.MyMethod;
import zuna.model.Repo;

public class LCOM3 extends Metric {

	private Hashtable<String, Edge> edges = new Hashtable<String, Edge>();
	private int[][] matrix;
	private boolean[] visited;
	private int numOfComponents;

	public LCOM3(Repo p) {
		super(p);
	}

	@Override
	public double getMetric(MyClass c) {

		if (c.getOwnedMethods().size() == 0)
			return 0.0;

		// visited.clear();
		matrix = new int[c.getOwnedMethods().size()][c.getOwnedMethods().size()];
		visited = new boolean[c.getOwnedMethods().size()];

		for (boolean b : visited) {
			b = false;
		}

		getGraph(c);
		// for(String key: this.edges.keySet()){
		// Edge e = this.edges.get(key);
		// System.out.println("4"+"-"+e.id + "-" + e.connected.size());
		// }

		// if ((c.getOwnedMethods().size() > 0 && this.edges.size() == 0)
		// || c.getOwendField().size() == 0) {
		// noOfComp = c.getOwnedMethods().size();
		// } else {
		// noOfComp = getNoOfConnectedComponent(c);
		// }
		numOfComponents = 0;
		for (int i = 0; i < matrix[0].length; i++) {
			if (!visited[i]) {
				numOfComponents++;
				depthFirstSearchForConnectedComponent(i);
			}
		}
		return numOfComponents;
	}

	private void depthFirstSearchForConnectedComponent(int index) {
		visited[index] = true;
		for (int i = 0; i < matrix[0].length; i++) {
			if (matrix[index][i] > 0) {
				if (!visited[i]) {
					depthFirstSearchForConnectedComponent(i);
				}
			}
		}
		return;
	}

	private boolean isConnected(MyMethod m1, MyMethod m2) {
		ArrayList<MyField> files1 = m1.getReferencedField();
		ArrayList<MyField> files2 = m2.getReferencedField();

		MyClass owningClass = m1.getParent();

		boolean conn = false;
		for (MyField f1 : files1) {
			for (MyField f2 : files2) {
				if (f1.getParent().getID().equals(owningClass.getID())
						&& f2.getParent().getID().equals(owningClass.getID())
						&& f1.getID().equals(f2.getID())) {
					conn = true;
					break;
				}
			}
		}

		return conn;
	}

	private void getGraph(MyClass c) {
		ArrayList<MyMethod> methods = c.getOwnedMethods();

		for (int i = 0; i < methods.size(); i++) {
			matrix[i][i]= 1;
			for (int j = i + 1; j < methods.size(); j++) {
				if (this.isConnected(methods.get(i), methods.get(j))) {
					matrix[i][j]= 1;
					matrix[j][i]= 1;
				} else {
					matrix[i][j]= 0;
					matrix[j][i]= 0;
				}
			}
		}
	}

	private void getEdge(String eid, String target) {
		if (this.edges.containsKey(eid)) {
			this.edges.get(eid).addConnection(target);
		} else {
			ArrayList<String> conn = new ArrayList<String>();
			conn.add(target);
			this.edges.put(eid, new Edge(eid, conn));
		}
	}

	// private int getNoOfConnectedComponent(MyClass c) {
	// int comp = 0;
	//
	// for (String key : this.edges.keySet()) {
	// if (!visited.containsKey(key)) {
	// visited.put(key, key);
	// if (this.visit(this.edges.get(key)))
	// comp++;
	// }
	// }
	//
	// ArrayList<MyMethod> mlist = c.getOwnedMethods();
	// for (MyMethod m : mlist) {
	// if (!this.edges.containsKey(m.getID()))
	// comp++;
	// }
	//
	// return comp;
	// }
	//
	// private boolean visit(Edge e) {
	// boolean flg = false;
	// ArrayList<String> conn = e.connected;
	// for (String key : conn) {
	// if (!visited.containsKey(key)) {
	// flg = true;
	// visited.put(key, key);
	// visit(this.edges.get(key));
	// }
	// }
	//
	// return flg;
	// }

	private class Edge {
		private String id;
		private ArrayList<String> connected = new ArrayList<String>();

		public Edge(String id, ArrayList<String> connected) {
			this.id = id;
			this.connected = connected;
		}

		private void addConnection(String id) {
			if (!this.connected.contains(id))
				this.connected.add(id);
		}
	}

}
