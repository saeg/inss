package br.usp.each.inss.instrumentation.dua;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Factory;

import br.usp.each.inss.instrumentation.Instrumentator;

public class TestForgottenSleepyDuasFactory {
	
	@Factory
	public Object[] createMaxProgramInstances() {
		List<TestForgottenSleepyDuas> programs = new ArrayList<TestForgottenSleepyDuas>();
		for (Instrumentator instrumentator : DuaInstrumentators.getInstrumentators()) {
			programs.add(new TestForgottenSleepyDuas(instrumentator));
		}
		return programs.toArray();
	}

}
