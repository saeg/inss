package br.usp.each.inss.instrumentation.traverse;

import java.util.List;

import br.usp.each.inss.instrumentation.probe.Probe;

public class SimpleProbeTraverseStrategy implements ProbeTraverseStrategy {

	@Override
	public void traverse(List<Probe> probes) {
		for (Probe probe : probes) {
			probe.execute();
		}
	}

}
