package br.usp.each.inss.instrumentation.dua;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.usp.each.inss.instrumentation.ASSET;
import br.usp.each.inss.instrumentation.AllUsesDemandDriven;
import br.usp.each.inss.instrumentation.BitwiseDuaCoverage;
import br.usp.each.inss.instrumentation.BitwiseDuaCoverage16;
import br.usp.each.inss.instrumentation.BitwiseDuaCoverage8;
import br.usp.each.inss.instrumentation.CorrectedDemandDriven;
import br.usp.each.inss.instrumentation.Instrumentator;
import br.usp.each.inss.instrumentation.MatrixBasedDuaCoverage;
import br.usp.each.inss.instrumentation.MatrixBasedDuaCoverageImproved;
import br.usp.each.inss.instrumentation.OptimizedDemandDriven;
import br.usp.each.inss.instrumentation.traverse.DefaultProbeTraverseStrategy;
import br.usp.each.inss.instrumentation.traverse.ProbeTraverseStrategy;
import br.usp.each.inss.instrumentation.traverse.RemoveConditionProbeTraverseStrategy;
import br.usp.each.inss.instrumentation.traverse.SimpleProbeTraverseStrategy;

public class DuaInstrumentators {
	
	private static final List<Class<? extends Instrumentator>> instrumentators;
	static {
		instrumentators = new ArrayList<Class<? extends Instrumentator>>();
		instrumentators.add(ASSET.class);
		instrumentators.add(AllUsesDemandDriven.class);
		instrumentators.add(OptimizedDemandDriven.class);
		instrumentators.add(CorrectedDemandDriven.class);
		instrumentators.add(BitwiseDuaCoverage.class);
		instrumentators.add(BitwiseDuaCoverage8.class);
		instrumentators.add(BitwiseDuaCoverage16.class);
		instrumentators.add(MatrixBasedDuaCoverage.class);
		instrumentators.add(MatrixBasedDuaCoverageImproved.class);
	}
	
	private static final ProbeTraverseStrategy DEFAULT_PROBE_TRAVERSE_STRATEGY = 
			new DefaultProbeTraverseStrategy();
	private static final ProbeTraverseStrategy REMOVE_CONDITION_PROBE_TRAVERSE_STRATEGY = 
			new RemoveConditionProbeTraverseStrategy();
	private static final ProbeTraverseStrategy SIMPLE_PROBE_TRAVERSE_STRATEGY = 
			new SimpleProbeTraverseStrategy();
	
	private static final Map<Class<? extends Instrumentator>, ProbeTraverseStrategy> traverseMap;
	static {
		traverseMap = new HashMap<Class<? extends Instrumentator>, ProbeTraverseStrategy>();
		traverseMap.put(ASSET.class, SIMPLE_PROBE_TRAVERSE_STRATEGY);
		traverseMap.put(AllUsesDemandDriven.class, DEFAULT_PROBE_TRAVERSE_STRATEGY);
		traverseMap.put(OptimizedDemandDriven.class, REMOVE_CONDITION_PROBE_TRAVERSE_STRATEGY);
		traverseMap.put(CorrectedDemandDriven.class, REMOVE_CONDITION_PROBE_TRAVERSE_STRATEGY);
		traverseMap.put(BitwiseDuaCoverage.class, SIMPLE_PROBE_TRAVERSE_STRATEGY);
		traverseMap.put(BitwiseDuaCoverage8.class, SIMPLE_PROBE_TRAVERSE_STRATEGY);
		traverseMap.put(BitwiseDuaCoverage16.class, SIMPLE_PROBE_TRAVERSE_STRATEGY);
		traverseMap.put(MatrixBasedDuaCoverage.class, SIMPLE_PROBE_TRAVERSE_STRATEGY);
		traverseMap.put(MatrixBasedDuaCoverageImproved.class, SIMPLE_PROBE_TRAVERSE_STRATEGY);
	}
	
	public static List<Instrumentator> getInstrumentators() {
		List<Instrumentator> i = new ArrayList<Instrumentator>(instrumentators.size());
		for (Class<? extends Instrumentator> clazz : instrumentators) {
			try {
				i.add(clazz.newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i;
	}

	public static ProbeTraverseStrategy getTraverse(Class<? extends Instrumentator> clazz) {
		ProbeTraverseStrategy traverse = traverseMap.get(clazz);
		if (traverse == null) {
			traverse = DEFAULT_PROBE_TRAVERSE_STRATEGY;
		}
		return traverse;
	}

}
