package br.usp.each.inss.instrumentation.traverse;

import java.util.List;

import br.usp.each.inss.instrumentation.probe.Probe;

public class DoNothingProbeTraverseStrategy implements ProbeTraverseStrategy {

	@Override
	public void traverse(List<Probe> probes) {
		// Do nothing
	}

}
