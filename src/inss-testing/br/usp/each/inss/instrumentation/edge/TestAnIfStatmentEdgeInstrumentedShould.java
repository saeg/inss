package br.usp.each.inss.instrumentation.edge;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.usp.each.opal.requirement.RequirementTestUtil;

public class TestAnIfStatmentEdgeInstrumentedShould extends	TestStatmentEdgeInstrumented {
	
	public TestAnIfStatmentEdgeInstrumentedShould() {
		super(RequirementTestUtil.IF_STATMENT);
	}

	@Test
	public void haveAEdgeFromZeroToTwoNotCoveredWhenIfIsTrue() {
		traverse(0, 1, 2);
		Assert.assertTrue(edge(0, 1).isCovered());
		Assert.assertTrue(edge(1, 2).isCovered());
		Assert.assertFalse(edge(0, 2).isCovered());
	}
	
	@Test
	public void haveAEdgeFromZeroToOneAndFromOneToTwoNotCoveredWhenIfIsFalse() {
		traverse(0, 2);
		Assert.assertTrue(edge(0, 2).isCovered());
		Assert.assertFalse(edge(0, 1).isCovered());
		Assert.assertFalse(edge(1, 2).isCovered());
	}

}
