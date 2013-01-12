package br.usp.each.opal.requirement;

import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.ProgramBlock;

public class NodeDetermination implements RequirementDetermination {

	@Override
	public Node[] requirement(DFGraph graph) {
		Node[] nodes = new Node[graph.size()];
		int i = 0;
		for (ProgramBlock node : graph) {
			nodes[i++] = new Node(node.getId());
		}
		return nodes;
	}

}
