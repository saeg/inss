package br.usp.each.opal.requirement;

import static br.usp.each.opal.requirement.RequirementTestUtil.findNode;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.ProgramBlock;

public class TestAnIfStatementNodesShould {

	private DFGraph ifStatment;
	private Node[] nodes;
	
	@BeforeTest
	public void init() {
		ifStatment = RequirementTestUtil.IF_STATMENT;
		nodes = new NodeDetermination().requirement(ifStatment);
	}
	
	@Test
	public void haveThreeNodes() {
		Assert.assertEquals(nodes.length, 3);
	}
	
	@Test
	public void haveANodeForEachProgramBlockWithSameId() {
		for (ProgramBlock block : ifStatment) {
			Assert.assertTrue(findNode(block, nodes));
		}
	}
}
