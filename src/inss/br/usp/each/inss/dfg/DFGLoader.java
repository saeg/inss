package br.usp.each.inss.dfg;

import br.usp.each.opal.dataflow.DFGraph;

public interface DFGLoader {
	
	DFGraph load(String clazz, int methodId) throws DFGLoaderException;

}
