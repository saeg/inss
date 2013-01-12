package br.usp.each.inss.instrumentation.dua;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.usp.each.inss.instrumentation.Instrumentator;
import br.usp.each.opal.requirement.RequirementTestUtil;

public class TestNextOddProgramDuaInstrumented
	extends TestStatmentDuaInstrumented {

	public TestNextOddProgramDuaInstrumented(Instrumentator instrumentator) {
		super(instrumentator, RequirementTestUtil.NEXT_ODD_PROGRAM);
	}

	@Test
	public void testIsOdd() {
		traverse(0, 6, 9);
		int var = statment.getVarByName("x").getId();
		// True
		Assert.assertTrue(dua(0, 6, var).isCovered());
		Assert.assertTrue(dua(6, 9, var).isCovered());
		Assert.assertTrue(dua(0, 0, 6, var).isCovered());
		// False
		Assert.assertFalse(dua(0, 0, 9, var).isCovered());
		Assert.assertFalse(dua(0, 9, var).isCovered());
	}

	@Test
	public void testNotOdd() {
		traverse( 0, 9);
		int var = statment.getVarByName("x").getId();
		// False
		Assert.assertFalse(dua(0, 6, var).isCovered());
		Assert.assertFalse(dua(6, 9, var).isCovered());
		Assert.assertFalse(dua(0, 0, 6, var).isCovered());
		// True
		Assert.assertTrue(dua(0, 0, 9, var).isCovered());
		Assert.assertTrue(dua(0, 9, var).isCovered());
	}

}
