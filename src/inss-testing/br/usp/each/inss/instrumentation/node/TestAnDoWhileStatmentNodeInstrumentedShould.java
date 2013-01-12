package br.usp.each.inss.instrumentation.node;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.usp.each.opal.requirement.Node;
import br.usp.each.opal.requirement.RequirementTestUtil;

public class TestAnDoWhileStatmentNodeInstrumentedShould extends TestStatmentNodeInstrumented {
	
	public TestAnDoWhileStatmentNodeInstrumentedShould() {
		super(RequirementTestUtil.DO_WHILE_STATMENT);
	}

	@Test
	public void haveAllNodesCoveredWhenWhileIsTrue() {
		traverse(0, 1, 0, 1, 2);
		for (Node node : requirements()) {
			Assert.assertTrue(node.isCovered());
		}
	}
	
	@Test
	public void haveAllNodesCoveredWhenWhileIsNeverTrue() {
		traverse(0, 1, 2);
		for (Node node : requirements()) {
			Assert.assertTrue(node.isCovered());
		}
	}

}
