package br.usp.each.inss.instrumentation.edge;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.usp.each.inss.instrumentation.AllEdges;
import br.usp.each.inss.instrumentation.TestStatment;
import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.requirement.Edge;
import br.usp.each.opal.requirement.EdgeDetermination;

public abstract class TestStatmentEdgeInstrumented extends TestStatment {
	
	public TestStatmentEdgeInstrumented(DFGraph statment) {
		super(new AllEdges(), new EdgeDetermination(), statment);
	}
	
	@Test
	public void haveAllEdgesNotCoveredWhenStart() {
		for (Edge edge : requirements()) {
			Assert.assertFalse(edge.isCovered());
		}
	}
	
	public Edge edge(int from, int to) {
		for (Edge edge : requirements()) {
			if (edge.getFrom() == from && edge.getTo() == to)
				return edge;
		}
		throw new RuntimeException("Edge not found");
	}
	
	@Override
	public Edge[] requirements() {
		return (Edge[]) super.requirements();
	}

}
