package br.usp.each.opal.requirement;

import java.util.ArrayList;
import java.util.List;

import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.ProgramBlock;

public class EdgeDetermination implements RequirementDetermination {

	@Override
	public Edge[] requirement(DFGraph graph) {
		List<Edge> edges = new ArrayList<Edge>();
		for (ProgramBlock from : graph) {
			for (ProgramBlock to : graph.neighbors(from)) {
				edges.add(new Edge(from.getId(), to.getId()));
			}
		}
		return edges.toArray(new Edge[edges.size()]);
	}

}
