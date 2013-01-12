package br.usp.each.opal.requirement;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.ProgramBlock;
import br.usp.each.opal.dataflow.DFGraph.Var;

public class TestMaxProgramDuasShould {
	
	private DFGraph max;
	private Dua[] duas;
	
	@BeforeTest
	public void init() {
		max = RequirementTestUtil.MAX_PROGRAM;
		duas = new DuaDetermination().requirement(max);
	}
	
	@Test
	public void haveTwentySixDuas() {
		Assert.assertEquals(duas.length, 26);
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt32OfVariableMax() {
		Var var = max.getVarByName("max");
		ProgramBlock def = max.getProgramBlockById(0);
		ProgramBlock use = max.getProgramBlockById(32);
		Assert.assertTrue(RequirementTestUtil.findDua(def, use, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt15To22OfVariableArraySquareBrackets() {
		Var var = max.getVarByName("array[]");
		ProgramBlock def = max.getProgramBlockById(0);
		ProgramBlock usea = max.getProgramBlockById(15);
		ProgramBlock useb = max.getProgramBlockById(22);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt15To26OfVariableArraySquareBrackets() {
		Var var = max.getVarByName("array[]");
		ProgramBlock def = max.getProgramBlockById(0);
		ProgramBlock usea = max.getProgramBlockById(15);
		ProgramBlock useb = max.getProgramBlockById(26);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt26AndUseAt15To22OfVariableI() {
		Var var = max.getVarByName("i");
		ProgramBlock def = max.getProgramBlockById(26);
		ProgramBlock usea = max.getProgramBlockById(15);
		ProgramBlock useb = max.getProgramBlockById(22);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt26AndUseAt15To26OfVariableI() {
		Var var = max.getVarByName("i");
		ProgramBlock def = max.getProgramBlockById(26);
		ProgramBlock usea = max.getProgramBlockById(15);
		ProgramBlock useb = max.getProgramBlockById(26);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt22AndUseAt15To22OfVariableMax() {
		Var var = max.getVarByName("max");
		ProgramBlock def = max.getProgramBlockById(22);
		ProgramBlock usea = max.getProgramBlockById(15);
		ProgramBlock useb = max.getProgramBlockById(22);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt22AndUseAt15To26OfVariableMax() {
		Var var = max.getVarByName("max");
		ProgramBlock def = max.getProgramBlockById(22);
		ProgramBlock usea = max.getProgramBlockById(15);
		ProgramBlock useb = max.getProgramBlockById(26);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt15To26OfVariableMax() {
		Var var = max.getVarByName("max");
		ProgramBlock def = max.getProgramBlockById(0);
		ProgramBlock usea = max.getProgramBlockById(15);
		ProgramBlock useb = max.getProgramBlockById(26);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt15To22OfVariableMax() {
		Var var = max.getVarByName("max");
		ProgramBlock def = max.getProgramBlockById(0);
		ProgramBlock usea = max.getProgramBlockById(15);
		ProgramBlock useb = max.getProgramBlockById(22);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt15To22OfVariableI() {
		Var var = max.getVarByName("i");
		ProgramBlock def = max.getProgramBlockById(0);
		ProgramBlock usea = max.getProgramBlockById(15);
		ProgramBlock useb = max.getProgramBlockById(22);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt15To26OfVariableI() {
		Var var = max.getVarByName("i");
		ProgramBlock def = max.getProgramBlockById(0);
		ProgramBlock usea = max.getProgramBlockById(15);
		ProgramBlock useb = max.getProgramBlockById(26);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt15To26OfVariableArray() {
		Var var = max.getVarByName("array");
		ProgramBlock def = max.getProgramBlockById(0);
		ProgramBlock usea = max.getProgramBlockById(15);
		ProgramBlock useb = max.getProgramBlockById(26);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt15To22OfVariableArray() {
		Var var = max.getVarByName("array");
		ProgramBlock def = max.getProgramBlockById(0);
		ProgramBlock usea = max.getProgramBlockById(15);
		ProgramBlock useb = max.getProgramBlockById(22);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt26AndUseAt9To32OfVariableI() {
		Var var = max.getVarByName("i");
		ProgramBlock def = max.getProgramBlockById(26);
		ProgramBlock usea = max.getProgramBlockById(9);
		ProgramBlock useb = max.getProgramBlockById(32);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt26AndUseAt9To15OfVariableI() {
		Var var = max.getVarByName("i");
		ProgramBlock def = max.getProgramBlockById(26);
		ProgramBlock usea = max.getProgramBlockById(9);
		ProgramBlock useb = max.getProgramBlockById(15);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt22OfVariableArraySquareBrackets() {
		Var var = max.getVarByName("array[]");
		ProgramBlock def = max.getProgramBlockById(0);
		ProgramBlock use = max.getProgramBlockById(22);
		Assert.assertTrue(RequirementTestUtil.findDua(def, use, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt9To32OfVariableI() {
		Var var = max.getVarByName("i");
		ProgramBlock def = max.getProgramBlockById(0);
		ProgramBlock usea = max.getProgramBlockById(9);
		ProgramBlock useb = max.getProgramBlockById(32);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt9To15OfVariableI() {
		Var var = max.getVarByName("i");
		ProgramBlock def = max.getProgramBlockById(0);
		ProgramBlock usea = max.getProgramBlockById(9);
		ProgramBlock useb = max.getProgramBlockById(15);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt9To32OfVariableArray() {
		Var var = max.getVarByName("array");
		ProgramBlock def = max.getProgramBlockById(0);
		ProgramBlock usea = max.getProgramBlockById(9);
		ProgramBlock useb = max.getProgramBlockById(32);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt9To15OfVariableArray() {
		Var var = max.getVarByName("array");
		ProgramBlock def = max.getProgramBlockById(0);
		ProgramBlock usea = max.getProgramBlockById(9);
		ProgramBlock useb = max.getProgramBlockById(15);
		Assert.assertTrue(RequirementTestUtil.findDua(def, usea, useb, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt26AndUseAt22OfVariableI() {
		Var var = max.getVarByName("i");
		ProgramBlock def = max.getProgramBlockById(26);
		ProgramBlock use = max.getProgramBlockById(22);
		Assert.assertTrue(RequirementTestUtil.findDua(def, use, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt26OfVariableI() {
		Var var = max.getVarByName("i");
		ProgramBlock def = max.getProgramBlockById(0);
		ProgramBlock use = max.getProgramBlockById(26);
		Assert.assertTrue(RequirementTestUtil.findDua(def, use, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt22OfVariableI() {
		Var var = max.getVarByName("i");
		ProgramBlock def = max.getProgramBlockById(0);
		ProgramBlock use = max.getProgramBlockById(22);
		Assert.assertTrue(RequirementTestUtil.findDua(def, use, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt0AndUseAt22OfVariableArray() {
		Var var = max.getVarByName("array");
		ProgramBlock def = max.getProgramBlockById(0);
		ProgramBlock use = max.getProgramBlockById(22);
		Assert.assertTrue(RequirementTestUtil.findDua(def, use, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt22AndUseAt32OfVariableMax() {
		Var var = max.getVarByName("max");
		ProgramBlock def = max.getProgramBlockById(22);
		ProgramBlock use = max.getProgramBlockById(32);
		Assert.assertTrue(RequirementTestUtil.findDua(def, use, var.getId(), duas));
	}
	
	@Test
	public void haveAnDefinitionAt26AndUseAt26OfVariableI() {
		Var var = max.getVarByName("i");
		ProgramBlock def = max.getProgramBlockById(26);
		ProgramBlock use = max.getProgramBlockById(26);
		Assert.assertTrue(RequirementTestUtil.findDua(def, use, var.getId(), duas));
	}
	
}
