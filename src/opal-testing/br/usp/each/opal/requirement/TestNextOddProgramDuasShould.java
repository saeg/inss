package br.usp.each.opal.requirement;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.DFGraph.Var;
import br.usp.each.opal.dataflow.ProgramBlock;

public class TestNextOddProgramDuasShould {

	private DFGraph nextOdd;
	private Dua[] duas;
	
	@BeforeTest
	public void init() {
		nextOdd = RequirementTestUtil.NEXT_ODD_PROGRAM;
		duas = new DuaDetermination().requirement(nextOdd);
	}
	
	@Test
	public void haveFiveDuas() {
		Assert.assertEquals(duas.length, 5);
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt6OfVariableX() {
		Var var = nextOdd.getVarByName("x");
		ProgramBlock def = nextOdd.getProgramBlockById(0);
		ProgramBlock use = nextOdd.getProgramBlockById(6);
		Assert.assertTrue(RequirementTestUtil.findDua(def, use, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt6AndUseAt9OfVariableX() {
		Var var = nextOdd.getVarByName("x");
		ProgramBlock def = nextOdd.getProgramBlockById(6);
		ProgramBlock use = nextOdd.getProgramBlockById(9);
		Assert.assertTrue(RequirementTestUtil.findDua(def, use, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt9OfVariableX() {
		Var var = nextOdd.getVarByName("x");
		ProgramBlock def = nextOdd.getProgramBlockById(0);
		ProgramBlock use = nextOdd.getProgramBlockById(9);
		Assert.assertTrue(RequirementTestUtil.findDua(def, use, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt0To9OfVariableX() {
		Var var = nextOdd.getVarByName("x");
		ProgramBlock def = nextOdd.getProgramBlockById(0);
		ProgramBlock usea = nextOdd.getProgramBlockById(0);
		ProgramBlock useb = nextOdd.getProgramBlockById(9);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt0To6OfVariableX() {
		Var var = nextOdd.getVarByName("x");
		ProgramBlock def = nextOdd.getProgramBlockById(0);
		ProgramBlock usea = nextOdd.getProgramBlockById(0);
		ProgramBlock useb = nextOdd.getProgramBlockById(6);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
}
