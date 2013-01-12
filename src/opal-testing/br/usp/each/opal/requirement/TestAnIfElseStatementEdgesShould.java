package br.usp.each.opal.requirement;

import static br.usp.each.opal.requirement.RequirementTestUtil.findEdge;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.ProgramBlock;

public class TestAnIfElseStatementEdgesShould {
	
	private DFGraph ifElse;
	private Edge[] edges;

	@BeforeTest
	public void init() {
		ifElse = RequirementTestUtil.IF_ELSE_STATMENT;
		edges = new EdgeDetermination().requirement(ifElse);
	}
	
	@Test
	public void haveFourEdges() {
		Assert.assertEquals(edges.length, 4);
	}
		
	@Test
	public void haveAnEdgeFromBlock0ToBlock1() {
		ProgramBlock block0 = ifElse.getProgramBlockById(0);
		ProgramBlock block1 = ifElse.getProgramBlockById(1);
		Assert.assertTrue(findEdge(block0, block1, edges));
	}
	
	@Test
	public void haveAnEdgeFromBlock0ToBlock2() {
		ProgramBlock block0 = ifElse.getProgramBlockById(0);
		ProgramBlock block2 = ifElse.getProgramBlockById(2);
		Assert.assertTrue(findEdge(block0, block2, edges));
	}
	
	@Test
	public void haveAnEdgeFromBlock1ToBlock3() {
		ProgramBlock block1 = ifElse.getProgramBlockById(1);
		ProgramBlock block3 = ifElse.getProgramBlockById(3);
		Assert.assertTrue(findEdge(block1, block3, edges));
	}
	
	@Test
	public void haveAnEdgeFromBlock2ToBlock3() {
		ProgramBlock block2 = ifElse.getProgramBlockById(2);
		ProgramBlock block3 = ifElse.getProgramBlockById(3);
		Assert.assertTrue(findEdge(block2, block3, edges));
	}

}
