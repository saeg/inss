package br.usp.each.inss.instrumentation.dua;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Factory;

import br.usp.each.inss.instrumentation.Instrumentator;

public class TestNextOddProgramFactory {
	
	@Factory
	public Object[] createNextOddInstances() {
		List<TestNextOddProgramDuaInstrumented> programs = new ArrayList<TestNextOddProgramDuaInstrumented>();
		for (Instrumentator instrumentator : DuaInstrumentators.getInstrumentators()) {
			programs.add(new TestNextOddProgramDuaInstrumented(instrumentator));
		}
		return programs.toArray();
	}

}
