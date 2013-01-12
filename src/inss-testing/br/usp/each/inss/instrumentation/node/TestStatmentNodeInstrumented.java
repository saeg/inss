package br.usp.each.inss.instrumentation.node;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.usp.each.inss.instrumentation.AllNodes;
import br.usp.each.inss.instrumentation.TestStatment;
import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.requirement.Node;
import br.usp.each.opal.requirement.NodeDetermination;

public abstract class TestStatmentNodeInstrumented extends TestStatment {

	public TestStatmentNodeInstrumented(DFGraph statment) {
		super(new AllNodes(), new NodeDetermination(), statment);
	}

	@Test
	public void haveAllNodesNotCoveredWhenStart() {
		for (Node node : requirements()) {
			Assert.assertFalse(node.isCovered());
		}
	}
	
	public Node node(int nodeId) {
		for (Node node : requirements()) {
			if (node.getNode() == nodeId)
				return node;
		}
		throw new RuntimeException("Node not found");
	}
	
	@Override
	public Node[] requirements() {
		return (Node[]) super.requirements();
	}
	
}
