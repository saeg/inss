package br.usp.each.inss.instrumentation.dua;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import br.usp.each.inss.instrumentation.Instrumentator;
import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.ProgramBlock;

public class TestForgottenSleepyDuas
	extends TestStatmentDuaInstrumented {
	
	private static DFGraph graph = new DFGraph("sleepyDuasTestStatment", 0);
	static {
		ProgramBlock b0, b1, b2, b3;
		graph.addVar("x", 0);
		graph.add(b0 = new ProgramBlock(0));
		graph.add(b1 = new ProgramBlock(1));
		graph.add(b2 = new ProgramBlock(2));
		graph.add(b3 = new ProgramBlock(3));
		graph.addEdge(b0, b1);
		graph.addEdge(b0, b3);
		graph.addEdge(b1, b2);
		graph.addEdge(b1, b3);
		graph.addEdge(b2, b3);
		b0.def(0);
		b0.puse(0);
	}

	public TestForgottenSleepyDuas(Instrumentator instrumentator) {
		super(instrumentator, graph);
	}

	@Test
	public void testSleepyDuasIsNotForget() {
		Reporter.log("Using Intrumenter:" + instrumentator.getClass().getSimpleName());
		traverse(0, 1, 2, 3);
		
		int var = statment.getVarByName("x").getId();
		
		Assert.assertFalse(dua(0, 0, 3, var).isCovered());
	}

}
