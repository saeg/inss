package br.usp.each.inss.instrumentation;

import br.usp.each.inss.Program;
import br.usp.each.opal.requirement.Requirement;

public class EmptyInstrumenter implements Instrumentator {

	@Override
	public void instrument(Program p, Requirement[] requirement) {
		// Simple. DO NOTHING HERE
	}

	@Override
	public Program copy(Program p, long id) {
		return p.cleanCopyWithId(id);
	}

}
