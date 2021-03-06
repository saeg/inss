package br.usp.each.opal.requirement;

import static br.usp.each.opal.requirement.RequirementTestUtil.findEdge;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.ProgramBlock;

public class TestAnWhileStatementEdgesShould {
	
	private DFGraph whileStatment;
	private Edge[] edges;

	@BeforeTest
	public void init() {
		whileStatment = RequirementTestUtil.WHILE_STATMENT;
		edges = new EdgeDetermination().requirement(whileStatment);
	}
	
	@Test
	public void haveThreeEdges() {
		Assert.assertEquals(edges.length, 3);
	}
	
	@Test
	public void haveAnEdgeFromBlock0ToBlock1() {
		ProgramBlock block0 = whileStatment.getProgramBlockById(0);
		ProgramBlock block1 = whileStatment.getProgramBlockById(1);
		Assert.assertTrue(findEdge(block0, block1, edges));
	}

	@Test
	public void haveAnEdgeFromBlock1ToBlock0() {
		ProgramBlock block1 = whileStatment.getProgramBlockById(1);
		ProgramBlock block0 = whileStatment.getProgramBlockById(0);
		Assert.assertTrue(findEdge(block1, block0, edges));
	}
	
	@Test
	public void haveAnEdgeFromBlock0ToBlock2() {
		ProgramBlock block0 = whileStatment.getProgramBlockById(0);
		ProgramBlock block2 = whileStatment.getProgramBlockById(2);
		Assert.assertTrue(findEdge(block0, block2, edges));
	}

}
