package br.usp.each.inss.instrumentation.edge;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.usp.each.opal.requirement.RequirementTestUtil;

public class TestAnIfElseStatmentEdgeInstrumentedShould extends	TestStatmentEdgeInstrumented {
	
	public TestAnIfElseStatmentEdgeInstrumentedShould() {
		super(RequirementTestUtil.IF_ELSE_STATMENT);
	}

	@Test
	public void haveEdgeFromZeroToOneAndFromOneToThreeNotCoveredWhenIfIsFalse() {
		traverse(0, 2, 3);
		Assert.assertTrue(edge(0, 2).isCovered());
		Assert.assertTrue(edge(2, 3).isCovered());
		Assert.assertFalse(edge(0, 1).isCovered());
		Assert.assertFalse(edge(1, 3).isCovered());
	}

	@Test
	public void haveEdgeFromZeroToTwoAndFromTwoToThreeNotCoveredWhenIfIsTrue() {
		traverse(0, 1, 3);
		Assert.assertFalse(edge(0, 2).isCovered());
		Assert.assertFalse(edge(2, 3).isCovered());
		Assert.assertTrue(edge(0, 1).isCovered());
		Assert.assertTrue(edge(1, 3).isCovered());
	}
	
}
