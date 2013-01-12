package br.usp.each.opal.gxl;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.BitSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.DFGraph.Var;
import br.usp.each.opal.dataflow.ProgramBlock;

public class TestGXLDefUseReader {

	private DFGraph graph;

	@BeforeClass
	public void before() throws IOException, SAXException {
		String dir = "projects/opal-testing/data";
		String fileName = "defuse_Sort_sort(int[], int).gxl";
		String graphName = "sort";
		GXLDefUseReader reader = new GXLDefUseReader(dir, fileName, graphName);
		graph = reader.getDFGraph("Sort", 0);
	}

	@Test
	public void testGetVar() {
		Var var;
		// By Name
		var = graph.getVarByName("L@0");
		assertEquals(var.getId(), 0);
		assertEquals(var.getName(), "L@0");

		var = graph.getVarByName("L@1");
		assertEquals(var.getId(), 1);
		assertEquals(var.getName(), "L@1");

		var = graph.getVarByName("L@1[]");
		assertEquals(var.getId(), 2);
		assertEquals(var.getName(), "L@1[]");

		var = graph.getVarByName("L@2");
		assertEquals(var.getId(), 3);
		assertEquals(var.getName(), "L@2");

		// By add Order
		var = graph.getVar(4);
		assertEquals(var.getId(), 4);
		assertEquals(var.getName(), "L@3");

		var = graph.getVar(5);
		assertEquals(var.getId(), 5);
		assertEquals(var.getName(), "L@4");

		var = graph.getVar(6);
		assertEquals(var.getId(), 6);
		assertEquals(var.getName(), "L@5");

		var = graph.getVar(7);
		assertEquals(var.getId(), 7);
		assertEquals(var.getName(), "L@6");
	}

	@Test
	public void testGetNode() {
		ProgramBlock node;
		// By Id
		node = graph.getProgramBlockById(0);
		assertEquals(node.getId(), 0);
		assertEquals(node.getDefs(), bitset(0, 1, 3, 4, 5));
		assertEquals(node.getCUses(), bitset());
		assertEquals(node.getPUses(), bitset());
		assertEquals(node.getRefs(), bitset());
		testEdge(graph.neighbors(node), 5);

		node = graph.getProgramBlockById(5);
		assertEquals(node.getId(), 5);
		assertEquals(node.getDefs(), bitset());
		assertEquals(node.getCUses(), bitset());
		assertEquals(node.getPUses(), bitset(3, 4));
		assertEquals(node.getRefs(), bitset());
		testEdge(graph.neighbors(node), 10, 73);

		node = graph.getProgramBlockById(10);
		assertEquals(node.getId(), 10);
		assertEquals(node.getDefs(), bitset(7, 6));
		assertEquals(node.getCUses(), bitset(1, 2, 4));
		assertEquals(node.getPUses(), bitset());
		assertEquals(node.getRefs(), bitset());
		testEdge(graph.neighbors(node), 20);

		node = graph.getProgramBlockById(20);
		assertEquals(node.getId(), 20);
		assertEquals(node.getDefs(), bitset());
		assertEquals(node.getCUses(), bitset());
		assertEquals(node.getPUses(), bitset(7, 3));
		assertEquals(node.getRefs(), bitset());
		testEdge(graph.neighbors(node), 26, 51);

		node = graph.getProgramBlock(4);
		assertEquals(node.getId(), 26);
		assertEquals(node.getDefs(), bitset());
		assertEquals(node.getCUses(), bitset(1, 2, 6, 7));
		assertEquals(node.getPUses(), bitset(1, 2, 6, 7));
		assertEquals(node.getRefs(), bitset());
		testEdge(graph.neighbors(node), 35, 45);

		node = graph.getProgramBlock(5);
		assertEquals(node.getId(), 35);
		assertEquals(node.getDefs(), bitset(5, 6));
		assertEquals(node.getCUses(), bitset(1, 2, 7));
		assertEquals(node.getPUses(), bitset());
		assertEquals(node.getRefs(), bitset());
		testEdge(graph.neighbors(node), 45);

		node = graph.getProgramBlock(6);
		assertEquals(node.getId(), 45);
		assertEquals(node.getDefs(), bitset(7));
		assertEquals(node.getCUses(), bitset(7));
		assertEquals(node.getPUses(), bitset());
		assertEquals(node.getRefs(), bitset());
		testEdge(graph.neighbors(node), 20);

		node = graph.getProgramBlock(7);
		assertEquals(node.getId(), 51);
		assertEquals(node.getDefs(), bitset(2, 4, 7));
		assertEquals(node.getCUses(), bitset(1, 2, 4, 5, 6));
		assertEquals(node.getPUses(), bitset());
		assertEquals(node.getRefs(), bitset());
		testEdge(graph.neighbors(node), 5);

		node = graph.getProgramBlock(8);
		assertEquals(node.getId(), 73);
		assertEquals(node.getDefs(), bitset());
		assertEquals(node.getCUses(), bitset());
		assertEquals(node.getPUses(), bitset());
		assertEquals(node.getRefs(), bitset());
		testEdge(graph.neighbors(node));

		assertEquals(graph.entranceBlock().getId(), 0);
		assertEquals(graph.exitBlock().get(0).getId(), 73);
	}

	private void testEdge(Set<ProgramBlock> edges, int... dest) {
		assertEquals(edges.size(), dest.length);
		for (int i = 0; i < dest.length; i++) {
			Assert.assertTrue(edges.contains(graph.getProgramBlockById(dest[i])));
		}
	}

	private BitSet bitset(int... is) {
		BitSet bs = new BitSet();
		for (int i : is) {
			bs.set(i);
		}
		return bs;
	}
}
