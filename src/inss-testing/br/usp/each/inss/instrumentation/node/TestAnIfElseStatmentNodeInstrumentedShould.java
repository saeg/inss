package br.usp.each.inss.instrumentation.node;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.usp.each.opal.requirement.RequirementTestUtil;

public class TestAnIfElseStatmentNodeInstrumentedShould extends TestStatmentNodeInstrumented {
	
	public TestAnIfElseStatmentNodeInstrumentedShould() {
		super(RequirementTestUtil.IF_ELSE_STATMENT);
	}
	
	@Test
	public void haveNodeOneNotCoveredWhenIfIsFalse() {
		traverse(0, 2, 3);
		Assert.assertTrue(node(0).isCovered());
		Assert.assertTrue(node(2).isCovered());
		Assert.assertTrue(node(3).isCovered());
		Assert.assertFalse(node(1).isCovered());
	}

	@Test
	public void haveNodeTwoNotCoveredWhenIfIsTrue() {
		traverse(0, 1, 3);
		Assert.assertTrue(node(0).isCovered());
		Assert.assertTrue(node(1).isCovered());
		Assert.assertTrue(node(3).isCovered());
		Assert.assertFalse(node(2).isCovered());
	}

}
