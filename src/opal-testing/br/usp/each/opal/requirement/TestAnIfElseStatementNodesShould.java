package br.usp.each.opal.requirement;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.ProgramBlock;

import static br.usp.each.opal.requirement.RequirementTestUtil.findNode;

public class TestAnIfElseStatementNodesShould {

	private DFGraph ifElse;
	private Node[] nodes;
	
	@BeforeTest
	public void init() {
		ifElse = RequirementTestUtil.IF_ELSE_STATMENT;
		nodes = new NodeDetermination().requirement(ifElse);
	}
	
	@Test
	public void haveFourNodes() {
		Assert.assertEquals(nodes.length, 4);
	}
	
	@Test
	public void haveANodeForEachProgramBlockWithSameId() {
		for (ProgramBlock block : ifElse) {
			Assert.assertTrue(findNode(block, nodes));
		}
	}
}
