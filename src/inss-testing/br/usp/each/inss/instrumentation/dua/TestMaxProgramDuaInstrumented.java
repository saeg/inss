package br.usp.each.inss.instrumentation.dua;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.usp.each.inss.instrumentation.Instrumentator;
import br.usp.each.opal.requirement.RequirementTestUtil;

public class TestMaxProgramDuaInstrumented 
	extends TestStatmentDuaInstrumented {
	
	public TestMaxProgramDuaInstrumented(Instrumentator instrumentator) {
		super(instrumentator, RequirementTestUtil.MAX_PROGRAM);
	}

	@Test
	public void test1() {
		// Example input {1}
		traverse(0, 9, 32);

		int var;
		var = statment.getVarByName("max").getId();
		Assert.assertTrue(dua(0, 32, var).isCovered());
		Assert.assertFalse(dua(22, 32, var).isCovered());
		Assert.assertFalse(dua(0, 15, 26, var).isCovered());
		Assert.assertFalse(dua(0, 15, 22, var).isCovered());
		Assert.assertFalse(dua(22, 15, 26, var).isCovered());
		Assert.assertFalse(dua(22, 15, 22, var).isCovered());

		var = statment.getVarByName("i").getId();
		Assert.assertFalse(dua(0, 26, var).isCovered());
		Assert.assertFalse(dua(0, 22, var).isCovered());
		Assert.assertFalse(dua(26, 22, var).isCovered());
		Assert.assertTrue(dua(0, 9, 32, var).isCovered());
		Assert.assertFalse(dua(0, 9, 15, var).isCovered());
		Assert.assertFalse(dua(0, 15, 26, var).isCovered());
		Assert.assertFalse(dua(0, 15, 22, var).isCovered());
		Assert.assertFalse(dua(26, 9, 32, var).isCovered());
		Assert.assertFalse(dua(26, 9, 15, var).isCovered());
		Assert.assertFalse(dua(26, 15, 26, var).isCovered());
		Assert.assertFalse(dua(26, 15, 22, var).isCovered());
		Assert.assertFalse(dua(26, 26, var).isCovered());

		var = statment.getVarByName("array").getId();
		Assert.assertFalse(dua(0, 22, var).isCovered());
		Assert.assertTrue(dua(0, 9, 32, var).isCovered());
		Assert.assertFalse(dua(0, 9, 15, var).isCovered());
		Assert.assertFalse(dua(0, 15, 26, var).isCovered());
		Assert.assertFalse(dua(0, 15, 22, var).isCovered());

		var = statment.getVarByName("array[]").getId();
		Assert.assertFalse(dua(0, 22, var).isCovered());
		Assert.assertFalse(dua(0, 15, 26, var).isCovered());
		Assert.assertFalse(dua(0, 15, 22, var).isCovered());
	}

	@Test
	public void test2() {
		// Example input {1, 2}
		traverse(0, 9, 15, 22, 26, 9, 32);

		int var;
		var = statment.getVarByName("max").getId();
		Assert.assertFalse(dua(0, 32, var).isCovered());
		Assert.assertTrue(dua(22, 32, var).isCovered());
		Assert.assertFalse(dua(0, 15, 26, var).isCovered());
		Assert.assertTrue(dua(0, 15, 22, var).isCovered());
		Assert.assertFalse(dua(22, 15, 26, var).isCovered());
		Assert.assertFalse(dua(22, 15, 22, var).isCovered());

		var = statment.getVarByName("i").getId();
		Assert.assertTrue(dua(0, 26, var).isCovered());
		Assert.assertTrue(dua(0, 22, var).isCovered());
		Assert.assertFalse(dua(26, 22, var).isCovered());
		Assert.assertFalse(dua(0, 9, 32, var).isCovered());
		Assert.assertTrue(dua(0, 9, 15, var).isCovered());
		Assert.assertFalse(dua(0, 15, 26, var).isCovered());
		Assert.assertTrue(dua(0, 15, 22, var).isCovered());
		Assert.assertTrue(dua(26, 9, 32, var).isCovered());
		Assert.assertFalse(dua(26, 9, 15, var).isCovered());
		Assert.assertFalse(dua(26, 15, 26, var).isCovered());
		Assert.assertFalse(dua(26, 15, 22, var).isCovered());
		Assert.assertFalse(dua(26, 26, var).isCovered());

		var = statment.getVarByName("array").getId();
		Assert.assertTrue(dua(0, 22, var).isCovered());
		Assert.assertTrue(dua(0, 9, 32, var).isCovered());
		Assert.assertTrue(dua(0, 9, 15, var).isCovered());
		Assert.assertFalse(dua(0, 15, 26, var).isCovered());
		Assert.assertTrue(dua(0, 15, 22, var).isCovered());

		var = statment.getVarByName("array[]").getId();
		Assert.assertTrue(dua(0, 22, var).isCovered());
		Assert.assertFalse(dua(0, 15, 26, var).isCovered());
		Assert.assertTrue(dua(0, 15, 22, var).isCovered());
	}

	@Test
	public void test3() {
		// Example input {2, 1}
		traverse(0, 9, 15, 26, 9, 32);

		int var;
		var = statment.getVarByName("max").getId();
		Assert.assertTrue(dua(0, 32, var).isCovered());
		Assert.assertFalse(dua(22, 32, var).isCovered());
		Assert.assertTrue(dua(0, 15, 26, var).isCovered());
		Assert.assertFalse(dua(0, 15, 22, var).isCovered());
		Assert.assertFalse(dua(22, 15, 26, var).isCovered());
		Assert.assertFalse(dua(22, 15, 22, var).isCovered());

		var = statment.getVarByName("i").getId();
		Assert.assertTrue(dua(0, 26, var).isCovered());
		Assert.assertFalse(dua(0, 22, var).isCovered());
		Assert.assertFalse(dua(26, 22, var).isCovered());
		Assert.assertFalse(dua(0, 9, 32, var).isCovered());
		Assert.assertTrue(dua(0, 9, 15, var).isCovered());
		Assert.assertTrue(dua(0, 15, 26, var).isCovered());
		Assert.assertFalse(dua(0, 15, 22, var).isCovered());
		Assert.assertTrue(dua(26, 9, 32, var).isCovered());
		Assert.assertFalse(dua(26, 9, 15, var).isCovered());
		Assert.assertFalse(dua(26, 15, 26, var).isCovered());
		Assert.assertFalse(dua(26, 15, 22, var).isCovered());
		Assert.assertFalse(dua(26, 26, var).isCovered());

		var = statment.getVarByName("array").getId();
		Assert.assertFalse(dua(0, 22, var).isCovered());
		Assert.assertTrue(dua(0, 9, 32, var).isCovered());
		Assert.assertTrue(dua(0, 9, 15, var).isCovered());
		Assert.assertTrue(dua(0, 15, 26, var).isCovered());
		Assert.assertFalse(dua(0, 15, 22, var).isCovered());

		var = statment.getVarByName("array[]").getId();
		Assert.assertFalse(dua(0, 22, var).isCovered());
		Assert.assertTrue(dua(0, 15, 26, var).isCovered());
		Assert.assertFalse(dua(0, 15, 22, var).isCovered());
	}

	@Test
	public void test4() {
		// Example input {1, 2, 3}
		traverse(0, 9, 15, 22, 26, 9, 15, 22, 26, 9,32);

		int var;
		var = statment.getVarByName("max").getId();
		Assert.assertFalse(dua(0, 32, var).isCovered());
		Assert.assertTrue(dua(22, 32, var).isCovered());
		Assert.assertFalse(dua(0, 15, 26, var).isCovered());
		Assert.assertTrue(dua(0, 15, 22, var).isCovered());
		Assert.assertFalse(dua(22, 15, 26, var).isCovered());
		Assert.assertTrue(dua(22, 15, 22, var).isCovered());

		var = statment.getVarByName("i").getId();
		Assert.assertTrue(dua(0, 26, var).isCovered());
		Assert.assertTrue(dua(0, 22, var).isCovered());
		Assert.assertTrue(dua(26, 22, var).isCovered());
		Assert.assertFalse(dua(0, 9, 32, var).isCovered());
		Assert.assertTrue(dua(0, 9, 15, var).isCovered());
		Assert.assertFalse(dua(0, 15, 26, var).isCovered());
		Assert.assertTrue(dua(0, 15, 22, var).isCovered());
		Assert.assertTrue(dua(26, 9, 32, var).isCovered());
		Assert.assertTrue(dua(26, 9, 15, var).isCovered());
		Assert.assertFalse(dua(26, 15, 26, var).isCovered());
		Assert.assertTrue(dua(26, 15, 22, var).isCovered());
		Assert.assertTrue(dua(26, 26, var).isCovered());

		var = statment.getVarByName("array").getId();
		Assert.assertTrue(dua(0, 22, var).isCovered());
		Assert.assertTrue(dua(0, 9, 32, var).isCovered());
		Assert.assertTrue(dua(0, 9, 15, var).isCovered());
		Assert.assertFalse(dua(0, 15, 26, var).isCovered());
		Assert.assertTrue(dua(0, 15, 22, var).isCovered());

		var = statment.getVarByName("array[]").getId();
		Assert.assertTrue(dua(0, 22, var).isCovered());
		Assert.assertFalse(dua(0, 15, 26, var).isCovered());
		Assert.assertTrue(dua(0, 15, 22, var).isCovered());
	}

	@Test
	public void test5() {
		// Example input {1, 3, 2}
		traverse(0, 9, 15, 22, 26, 9, 15, 26, 9, 32);

		int var;
		var = statment.getVarByName("max").getId();
		Assert.assertFalse(dua(0, 32, var).isCovered());
		Assert.assertTrue(dua(22, 32, var).isCovered());
		Assert.assertFalse(dua(0, 15, 26, var).isCovered());
		Assert.assertTrue(dua(0, 15, 22, var).isCovered());
		Assert.assertTrue(dua(22, 15, 26, var).isCovered());
		Assert.assertFalse(dua(22, 15, 22, var).isCovered());

		var = statment.getVarByName("i").getId();
		Assert.assertTrue(dua(0, 26, var).isCovered());
		Assert.assertTrue(dua(0, 22, var).isCovered());
		Assert.assertFalse(dua(26, 22, var).isCovered());
		Assert.assertFalse(dua(0, 9, 32, var).isCovered());
		Assert.assertTrue(dua(0, 9, 15, var).isCovered());
		Assert.assertFalse(dua(0, 15, 26, var).isCovered());
		Assert.assertTrue(dua(0, 15, 22, var).isCovered());
		Assert.assertTrue(dua(26, 9, 32, var).isCovered());
		Assert.assertTrue(dua(26, 9, 15, var).isCovered());
		Assert.assertTrue(dua(26, 15, 26, var).isCovered());
		Assert.assertFalse(dua(26, 15, 22, var).isCovered());
		Assert.assertTrue(dua(26, 26, var).isCovered());

		var = statment.getVarByName("array").getId();
		Assert.assertTrue(dua(0, 22, var).isCovered());
		Assert.assertTrue(dua(0, 9, 32, var).isCovered());
		Assert.assertTrue(dua(0, 9, 15, var).isCovered());
		Assert.assertTrue(dua(0, 15, 26, var).isCovered());
		Assert.assertTrue(dua(0, 15, 22, var).isCovered());

		var = statment.getVarByName("array[]").getId();
		Assert.assertTrue(dua(0, 22, var).isCovered());
		Assert.assertTrue(dua(0, 15, 26, var).isCovered());
		Assert.assertTrue(dua(0, 15, 22, var).isCovered());
	}

	@Test
	public void test6() {
		// Example input {2, 1, 3}
		traverse(0, 9, 15, 26, 9, 15, 22, 26, 9, 32);

		int var;
		var = statment.getVarByName("max").getId();
		Assert.assertFalse(dua(0, 32, var).isCovered());
		Assert.assertTrue(dua(22, 32, var).isCovered());
		Assert.assertTrue(dua(0, 15, 26, var).isCovered());
		Assert.assertTrue(dua(0, 15, 22, var).isCovered());
		Assert.assertFalse(dua(22, 15, 26, var).isCovered());
		Assert.assertFalse(dua(22, 15, 22, var).isCovered());

		var = statment.getVarByName("i").getId();
		Assert.assertTrue(dua(0, 26, var).isCovered());
		Assert.assertFalse(dua(0, 22, var).isCovered());
		Assert.assertTrue(dua(26, 22, var).isCovered());
		Assert.assertFalse(dua(0, 9, 32, var).isCovered());
		Assert.assertTrue(dua(0, 9, 15, var).isCovered());
		Assert.assertTrue(dua(0, 15, 26, var).isCovered());
		Assert.assertFalse(dua(0, 15, 22, var).isCovered());
		Assert.assertTrue(dua(26, 9, 32, var).isCovered());
		Assert.assertTrue(dua(26, 9, 15, var).isCovered());
		Assert.assertFalse(dua(26, 15, 26, var).isCovered());
		Assert.assertTrue(dua(26, 15, 22, var).isCovered());
		Assert.assertTrue(dua(26, 26, var).isCovered());

		var = statment.getVarByName("array").getId();
		Assert.assertTrue(dua(0, 22, var).isCovered());
		Assert.assertTrue(dua(0, 9, 32, var).isCovered());
		Assert.assertTrue(dua(0, 9, 15, var).isCovered());
		Assert.assertTrue(dua(0, 15, 26, var).isCovered());
		Assert.assertTrue(dua(0, 15, 22, var).isCovered());

		var = statment.getVarByName("array[]").getId();
		Assert.assertTrue(dua(0, 22, var).isCovered());
		Assert.assertTrue(dua(0, 15, 26, var).isCovered());
		Assert.assertTrue(dua(0, 15, 22, var).isCovered());
	}

	@Test(enabled = false) // Same path test 5
	public void test7() {
		// Example input {2, 3, 1}
		traverse(0, 9, 15, 22, 26, 9, 15, 26, 9, 32);
	}

	@Test
	public void test8() {
		// Example input {3, 1, 2}
		traverse(0, 9, 15, 26, 9, 15, 26, 9, 32);

		int var;
		var = statment.getVarByName("max").getId();
		Assert.assertTrue(dua(0, 32, var).isCovered());
		Assert.assertFalse(dua(22, 32, var).isCovered());
		Assert.assertTrue(dua(0, 15, 26, var).isCovered());
		Assert.assertFalse(dua(0, 15, 22, var).isCovered());
		Assert.assertFalse(dua(22, 15, 26, var).isCovered());
		Assert.assertFalse(dua(22, 15, 22, var).isCovered());

		var = statment.getVarByName("i").getId();
		Assert.assertTrue(dua(0, 26, var).isCovered());
		Assert.assertFalse(dua(0, 22, var).isCovered());
		Assert.assertFalse(dua(26, 22, var).isCovered());
		Assert.assertFalse(dua(0, 9, 32, var).isCovered());
		Assert.assertTrue(dua(0, 9, 15, var).isCovered());
		Assert.assertTrue(dua(0, 15, 26, var).isCovered());
		Assert.assertFalse(dua(0, 15, 22, var).isCovered());
		Assert.assertTrue(dua(26, 9, 32, var).isCovered());
		Assert.assertTrue(dua(26, 9, 15, var).isCovered());
		Assert.assertTrue(dua(26, 15, 26, var).isCovered());
		Assert.assertFalse(dua(26, 15, 22, var).isCovered());
		Assert.assertTrue(dua(26, 26, var).isCovered());

		var = statment.getVarByName("array").getId();
		Assert.assertFalse(dua(0, 22, var).isCovered());
		Assert.assertTrue(dua(0, 9, 32, var).isCovered());
		Assert.assertTrue(dua(0, 9, 15, var).isCovered());
		Assert.assertTrue(dua(0, 15, 26, var).isCovered());
		Assert.assertFalse(dua(0, 15, 22, var).isCovered());

		var = statment.getVarByName("array[]").getId();
		Assert.assertFalse(dua(0, 22, var).isCovered());
		Assert.assertTrue(dua(0, 15, 26, var).isCovered());
		Assert.assertFalse(dua(0, 15, 22, var).isCovered());
	}

	@Test(enabled = false) // Same path test 8
	public void test9() {
		// Example input {3, 2, 1}
		traverse(0, 9, 15, 26, 9, 15, 26, 9, 32);
	}

}
