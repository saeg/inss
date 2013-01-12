package br.usp.each.inss.instrumentation;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.usp.each.inss.Program;
import br.usp.each.inss.instrumentation.dua.DuaInstrumentators;
import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.requirement.Requirement;
import br.usp.each.opal.requirement.RequirementDetermination;

public abstract class TestStatment {
	
	protected DFGraph statment;
	private Program program;
	private Requirement[] requirements;
	
	protected Instrumentator instrumentator;
	private RequirementDetermination determination;
	
	public TestStatment(
			Instrumentator instrumentator,
			RequirementDetermination determination,
			DFGraph statment) {
		
		this.instrumentator = instrumentator;
		this.determination = determination;
		this.statment = statment;
	}
	
	@BeforeMethod
	public void init() {
		program = new Program(0, statment);
		requirements = determination.requirement(statment);
		instrumentator.instrument(program, requirements);
		Program.setTraverseStrategy(DuaInstrumentators.getTraverse(instrumentator.getClass()));
	}
	
	@Test
	public void haveAllRequirementsNotCoveredWhenStart() {
		for (Requirement r : requirements) {
			Assert.assertFalse(r.isCovered());
		}
	}
	
	public void traverse(int... path) {
		for (int i : path) {
			program.getExecutionEntryById(i).traverse();
		}
	}
	
	public Requirement[] requirements() {
		return requirements;
	}
	
	@Override
	public String toString() {
		return instrumentator.getClass().getSimpleName();
	}

}
