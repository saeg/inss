package br.usp.each.inss.instrumentation.dua;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Factory;

import br.usp.each.inss.instrumentation.Instrumentator;

public class TestMaxProgramFactory {
	
	@Factory
	public Object[] createMaxProgramInstances() {
		List<TestMaxProgramDuaInstrumented> programs = new ArrayList<TestMaxProgramDuaInstrumented>();
		for (Instrumentator instrumentator : DuaInstrumentators.getInstrumentators()) {
			programs.add(new TestMaxProgramDuaInstrumented(instrumentator));
		}
		return programs.toArray();
	}

}
