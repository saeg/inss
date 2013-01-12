package br.usp.each.inss.instrumentation.traverse;

import java.util.List;

import br.usp.each.inss.instrumentation.probe.Probe;

public interface ProbeTraverseStrategy {
	
	void traverse(List<Probe> probes);

}
