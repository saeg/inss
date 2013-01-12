package br.usp.each.inss.instrumentation.edge;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.usp.each.opal.requirement.Edge;
import br.usp.each.opal.requirement.RequirementTestUtil;

public class TestAnWhileStatmentEdgeInstrumentedShould extends TestStatmentEdgeInstrumented {
	
	public TestAnWhileStatmentEdgeInstrumentedShould() {
		super(RequirementTestUtil.WHILE_STATMENT);
	}

	@Test
	public void haveAllEdgesCoveredWhenWhileIsTrue() {
		traverse(0, 1, 0, 2);
		for (Edge edge : requirements()) {
			Assert.assertTrue(edge.isCovered());
		}
	}

	@Test
	public void haveEdgeFromZeroToOneAndFromOneToZeroNotCoveredWhenWhileIsNeverTrue() {
		traverse(0, 2);
		Assert.assertTrue(edge(0, 2).isCovered());
		Assert.assertFalse(edge(0, 1).isCovered());
		Assert.assertFalse(edge(1, 0).isCovered());
	}

}
