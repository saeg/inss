package br.usp.each.opal.requirement;

import br.usp.each.opal.dataflow.DFGraph;

public interface RequirementDetermination {

	Requirement[] requirement(DFGraph graph) throws RequirementDeterminationException;
	
}
