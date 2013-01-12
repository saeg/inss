package br.usp.each.inss.instrumentation;

import br.usp.each.inss.Program;
import br.usp.each.opal.requirement.Requirement;

public interface Instrumentator {

	void instrument(Program p, Requirement[] requirement);
	
	Program copy(Program p, long id);
	
}
