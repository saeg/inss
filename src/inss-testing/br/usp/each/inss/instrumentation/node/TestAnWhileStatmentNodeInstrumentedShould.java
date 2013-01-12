package br.usp.each.inss.instrumentation.node;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.usp.each.opal.requirement.Node;
import br.usp.each.opal.requirement.RequirementTestUtil;

public class TestAnWhileStatmentNodeInstrumentedShould extends TestStatmentNodeInstrumented {

	public TestAnWhileStatmentNodeInstrumentedShould() {
		super(RequirementTestUtil.WHILE_STATMENT);
	}

	@Test
	public void haveAllNodesCoveredWhenWhileIsTrue() {
		traverse(0, 1, 0, 2);
		for (Node node : requirements()) {
			Assert.assertTrue(node.isCovered());
		}
	}

	@Test
	public void haveNodeOneNotCoveredWhenWhileIsNeverTrue() {
		traverse(0, 2);
		Assert.assertTrue(node(0).isCovered());
		Assert.assertTrue(node(2).isCovered());
		Assert.assertFalse(node(1).isCovered());
	}

}
