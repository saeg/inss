package br.usp.each.opal.requirement;

import static br.usp.each.opal.requirement.RequirementTestUtil.findNode;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.ProgramBlock;

public class TestAnWhileStatementNodesShould {
	
	private DFGraph whileStatment;
	private Node[] nodes;
	
	@BeforeTest
	public void init() {
		whileStatment = RequirementTestUtil.WHILE_STATMENT;
		nodes = new NodeDetermination().requirement(whileStatment);
	}
	
	@Test
	public void haveThreeNodes() {
		Assert.assertEquals(nodes.length, 3);
	}
	
	@Test
	public void haveANodeForEachProgramBlockWithSameId() {
		for (ProgramBlock block : whileStatment) {
			Assert.assertTrue(findNode(block, nodes));
		}
	}
}
