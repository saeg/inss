package br.usp.each.opal.requirement;

import static br.usp.each.opal.requirement.RequirementTestUtil.findEdge;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.ProgramBlock;

public class TestAnIfStatementEdgesShould {
	
	private DFGraph ifStatment;
	private Edge[] edges;

	@BeforeTest
	public void init() {
		ifStatment = RequirementTestUtil.IF_STATMENT;
		edges = new EdgeDetermination().requirement(ifStatment);
	}
		
	@Test
	public void haveThreeEdges() {
		Assert.assertEquals(edges.length, 3);
	}
	
	@Test
	public void haveAnEdgeFromBlock0ToBlock1() {
		ProgramBlock block0 = ifStatment.getProgramBlockById(0);
		ProgramBlock block1 = ifStatment.getProgramBlockById(1);
		Assert.assertTrue(findEdge(block0, block1, edges));
	}
	
	@Test
	public void haveAnEdgeFromBlock0ToBlock2() {
		ProgramBlock block0 = ifStatment.getProgramBlockById(0);
		ProgramBlock block2 = ifStatment.getProgramBlockById(2);
		Assert.assertTrue(findEdge(block0, block2, edges));
	}
	
	@Test
	public void haveAnEdgeFromBlock1ToBlock2() {
		ProgramBlock block1 = ifStatment.getProgramBlockById(1);
		ProgramBlock block2 = ifStatment.getProgramBlockById(2);
		Assert.assertTrue(findEdge(block1, block2, edges));
	}
	
}
