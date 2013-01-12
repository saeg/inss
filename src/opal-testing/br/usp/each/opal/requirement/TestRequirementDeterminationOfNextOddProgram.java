package br.usp.each.opal.requirement;

import static br.usp.each.opal.requirement.RequirementTestUtil.findDua;
import static br.usp.each.opal.requirement.RequirementTestUtil.findEdge;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.ProgramBlock;

public class TestRequirementDeterminationOfNextOddProgram {
	
	// This is the same example of the presentation...
	private DFGraph nextOdd;
	
	@BeforeTest
	public void init() {
		nextOdd = RequirementTestUtil.NEXT_ODD_PRESENTATION;
	}
	
	@Test
	public void nodeDeterminationGiveANodeForEachProgramBlockWithSameId() {
		Node[] nodes = new NodeDetermination().requirement(nextOdd);
		for (Node node : nodes) {
			ProgramBlock pb = nextOdd.getProgramBlockById(node.getNode());
			Assert.assertNotNull(pb);
		}
	}
	
	@Test
	public void nodeDeterminationGiveANodeForEachProgramBlock() {
		Node[] nodes = new NodeDetermination().requirement(nextOdd);
		Assert.assertEquals(nextOdd.size(), nodes.length);
	}
	
	@Test
	public void edgeDeterminationGiveAnEdgeFromBlock0ToBlock1() {
		// Presentation do not include this one....
		ProgramBlock block0 = nextOdd.getProgramBlockById(0);
		ProgramBlock block1 = nextOdd.getProgramBlockById(1);
		Edge[] edges = new EdgeDetermination().requirement(nextOdd);
		Assert.assertTrue(findEdge(block0, block1, edges));
	}
	
	@Test
	public void edgeDeterminationGiveAnEdgeFromBlock1ToBlock2() {
		ProgramBlock block1 = nextOdd.getProgramBlockById(1);
		ProgramBlock block2 = nextOdd.getProgramBlockById(2);
		Edge[] edges = new EdgeDetermination().requirement(nextOdd);
		Assert.assertTrue(findEdge(block1, block2, edges));
	}
	
	@Test
	public void edgeDeterminationGiveAnEdgeFromBlock2ToBlock3() {
		ProgramBlock block2 = nextOdd.getProgramBlockById(2);
		ProgramBlock block3 = nextOdd.getProgramBlockById(3);
		Edge[] edges = new EdgeDetermination().requirement(nextOdd);
		Assert.assertTrue(findEdge(block2, block3, edges));
	}
	
	@Test
	public void edgeDeterminationGiveAnEdgeFromBlock2ToBlock4() {
		ProgramBlock block2 = nextOdd.getProgramBlockById(2);
		ProgramBlock block4 = nextOdd.getProgramBlockById(4);
		Edge[] edges = new EdgeDetermination().requirement(nextOdd);
		Assert.assertTrue(findEdge(block2, block4, edges));
	}
	
	@Test
	public void edgeDeterminationGiveAnEdgeFromBlock3ToBlock4() {
		ProgramBlock block3 = nextOdd.getProgramBlockById(3);
		ProgramBlock block4 = nextOdd.getProgramBlockById(4);
		Edge[] edges = new EdgeDetermination().requirement(nextOdd);
		Assert.assertTrue(findEdge(block3, block4, edges));
	}
	
	@Test
	public void edgeDeterminationGiveFiveEdges() {
		Edge[] edges = new EdgeDetermination().requirement(nextOdd);
		Assert.assertEquals(5, edges.length);
		// The Presentation do not include node 0, there have only 4 edges.
	}
	
	@Test
	public void duaDeterminationGiveSixDuas() {
		Dua[] duas = new DuaDetermination().requirement(nextOdd);
		Assert.assertEquals(6, duas.length);
	}
	
	@Test
	public void duaDeterminationGiveDef0Use1VarX() {
		ProgramBlock def = nextOdd.getProgramBlockById(0);
		ProgramBlock use = nextOdd.getProgramBlockById(1);
		Dua[] duas = new DuaDetermination().requirement(nextOdd);
		Assert.assertTrue(findDua(def, use, 0, duas));
	}
	
	@Test
	public void duaDeterminationGiveDef0Use3VarX() {
		ProgramBlock def = nextOdd.getProgramBlockById(0);
		ProgramBlock use = nextOdd.getProgramBlockById(3);
		Dua[] duas = new DuaDetermination().requirement(nextOdd);
		Assert.assertTrue(findDua(def, use, 0, duas));
	}
	
	@Test
	public void duaDeterminationGiveDef0Use4VarX() {
		ProgramBlock def = nextOdd.getProgramBlockById(0);
		ProgramBlock use = nextOdd.getProgramBlockById(4);
		Dua[] duas = new DuaDetermination().requirement(nextOdd);
		Assert.assertTrue(findDua(def, use, 0, duas));
	}
	
	@Test
	public void duaDeterminationGiveDef3Use4VarX() {
		ProgramBlock def = nextOdd.getProgramBlockById(3);
		ProgramBlock use = nextOdd.getProgramBlockById(4);
		Dua[] duas = new DuaDetermination().requirement(nextOdd);
		Assert.assertTrue(findDua(def, use, 0, duas));
	}
	
	@Test
	public void duaDeterminationGiveDef1PUse2To3VarX() {
		ProgramBlock def = nextOdd.getProgramBlockById(1);
		ProgramBlock usea = nextOdd.getProgramBlockById(2);
		ProgramBlock useb = nextOdd.getProgramBlockById(3);
		Dua[] duas = new DuaDetermination().requirement(nextOdd);
		Assert.assertTrue(findDua(def, usea, useb, 1, duas));
	}
	
	@Test
	public void duaDeterminationGiveDef1PUse2To4VarX() {
		ProgramBlock def = nextOdd.getProgramBlockById(1);
		ProgramBlock usea = nextOdd.getProgramBlockById(2);
		ProgramBlock useb = nextOdd.getProgramBlockById(4);
		Dua[] duas = new DuaDetermination().requirement(nextOdd);
		Assert.assertTrue(findDua(def, usea, useb, 1, duas));
	}
	
}
