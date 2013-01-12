package br.usp.each.inss.instrumentation.node;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.usp.each.opal.requirement.Node;
import br.usp.each.opal.requirement.RequirementTestUtil;

public class TestAnIfStatmentNodeInstrumentedShould extends TestStatmentNodeInstrumented {
	
	public TestAnIfStatmentNodeInstrumentedShould() {
		super(RequirementTestUtil.IF_STATMENT);
	}

	@Test
	public void haveAllNodesCoveredWhenIfIsTrue() {
		traverse(0, 1, 2);
		for (Node node : requirements()) {
			Assert.assertTrue(node.isCovered());
		}
	}
	
	@Test
	public void haveNodeOneNotCoveredWhenIfIsFalse() {
		traverse(0, 2);
		Assert.assertTrue(node(0).isCovered());
		Assert.assertTrue(node(2).isCovered());
		Assert.assertFalse(node(1).isCovered());
	}

}
