package br.usp.each.opal.requirement;

import java.util.ArrayList;
import java.util.List;

import br.usp.each.opal.dataflow.DFAnalysis;
import br.usp.each.opal.dataflow.DFAnalysis.AnalysisBlock;
import br.usp.each.opal.dataflow.DFGraph;

public class DuaDetermination implements RequirementDetermination {

	@Override
	public Dua[] requirement(DFGraph graph) {
		DFAnalysis analysis = new DFAnalysis(graph);
		analysis.computeInAndOut();
		
		int id = 0;
		List<Dua> duas = new ArrayList<Dua>();
		
		// for each block b
		for (AnalysisBlock b : analysis) {
			// for each variable v
			for (int v = 0; v < analysis.varSize(); v++) {
				// if v is used in block b
				if (b.getProgramBlock().isCUse(v)) {
					CUse use = new CUse(b.getProgramBlock().getId());
					// find blocks (def) where definitions of v are propagated to b
					// i.e, definitions that reach b
					for (AnalysisBlock def : analysis)
						if(b.getIn().get(def.getPos() * analysis.varSize() + v))
							duas.add(new Dua(id++, def.getProgramBlock().getId(), use, v));
				}
				else if (b.getProgramBlock().isPUse(v)) {
					for (AnalysisBlock puse : analysis.neighbors(b)) {
						PUse use = new PUse(b.getProgramBlock().getId(), puse.getProgramBlock().getId());
						for (AnalysisBlock def : analysis)
							if(b.getOut().get(def.getPos() * analysis.varSize() + v))
								duas.add(new Dua(id++, def.getProgramBlock().getId(), use, v));
					}
				}
			}
		}
		
		return duas.toArray(new Dua[duas.size()]);
	}

}
