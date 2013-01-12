package br.usp.each.opal.requirement;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.ProgramBlock;

import static br.usp.each.opal.requirement.RequirementTestUtil.findNode;

public class TestAnDoWhileStatementNodesShould {

	private DFGraph doWhile;
	private Node[] nodes;
	
	@BeforeTest
	public void init() {
		doWhile = RequirementTestUtil.DO_WHILE_STATMENT;
		nodes = new NodeDetermination().requirement(doWhile);
	}
	
	@Test
	public void haveThreeNodes() {
		Assert.assertEquals(nodes.length, 3);
	}
	
	@Test
	public void haveANodeForEachProgramBlockWithSameId() {
		for (ProgramBlock block : doWhile) {
			Assert.assertTrue(findNode(block, nodes));
		}
	}
}
