package br.usp.each.inss.instrumentation;

import java.util.List;

import br.usp.each.inss.Program;
import br.usp.each.inss.Program.ExecutionEntry;
import br.usp.each.inss.instrumentation.probe.Probe;
import br.usp.each.opal.dataflow.ProgramBlock;
import br.usp.each.opal.requirement.Dua;
import br.usp.each.opal.requirement.Requirement;

public class ASSET implements Instrumentator {

	@Override
	public void instrument(Program p, Requirement[] requirement) {
		Dua[] duas = (Dua [])requirement;
		Automaton[] automatons = new Automaton[duas.length];
		for (int i = 0; i < automatons.length; i++) {
			Dua d = duas[i];
			switch (d.getUse().getType()) {
			case C_USE:
				automatons[i] = new CUseAutomaton(d);
				break;
			case P_USE:
				automatons[i] = new PUseAutomaton(d);
				break;
			}
		}
		for (int i = 0; i < p.size(); i++) {
			ExecutionEntry e = p.getExecutionEntry(i);
			e.addFirst(new AssetProbe(e.getProgramBlock(), duas, automatons));
		}
	}
	
	@Override
	public Program copy(Program p, long id) {
		Automaton[] automatons = null;
		
		Program copy = p.cleanCopyWithId(id);
		for (int i = 0; i < p.size(); i++) {
			List<Probe> probes = p.getExecutionEntry(i).probes();
			ExecutionEntry e = copy.getExecutionEntry(i);
			for (Probe probe : probes) {
				AssetProbe assetProbe = (AssetProbe) probe;
				
				if (automatons == null) {
					automatons = new Automaton[assetProbe.automatons.length];
					for (int j = 0; j < automatons.length; j++) {
						Dua d = assetProbe.duas[j];
						switch (d.getUse().getType()) {
						case C_USE:
							automatons[j] = new CUseAutomaton(d);
							break;
						case P_USE:
							automatons[j] = new PUseAutomaton(d);
							break;
						}
					}
				}
				
				e.addLast(new AssetProbe(assetProbe.programBlock, assetProbe.duas, automatons));
			}
		}
		return copy;
	}

	private static class AssetProbe implements Probe {

		private ProgramBlock programBlock;
		private Dua[] duas;
		private Automaton[] automatons;

		public AssetProbe(ProgramBlock programBlock, Dua[] duas, Automaton[] automatons) {
			this.programBlock = programBlock;
			this.duas = duas;
			this.automatons = automatons;
		}

		@Override
		public void execute() {
			for (int i = 0; i < duas.length; i++) {
				Dua d = duas[i];
				if (!d.isCovered()) {
					automatons[i].walk(programBlock);
				}
			}
		}
	}

	private abstract class Automaton {

		int currentSate;
		Dua dua;

		public Automaton(Dua dua) {
			this.currentSate = 0;
			this.dua = dua;
		}

		public abstract void walk(ProgramBlock block);

	}

	private class CUseAutomaton extends Automaton {

		public CUseAutomaton(Dua dua) {
			super(dua);
		}

		@Override
		public void walk(ProgramBlock block) {
			switch (currentSate) {
			case 0:
				if (block.getId() == dua.getDef())
					currentSate = 1;
				break;
			case 1:
				if (block.getId() == dua.getUse().getUseNode()) {
					currentSate = 2;
					dua.cover();
				} else if (block.isDef(dua.getVariable()) && block.getId() != dua.getDef()) {
					currentSate = 0;
				}
				break;
			}
		}
	}

	private class PUseAutomaton extends Automaton {

		public PUseAutomaton(Dua dua) {
			super(dua);
		}

		@Override
		public void walk(ProgramBlock block) {
			switch (currentSate) {
			case 0:
				if (block.getId() == dua.getDef()) {
					currentSate = 1;
					if (dua.getDef() == dua.getUse().PUse().getOriginNode()) {
						currentSate = 2;
					}
				}
				break;
			case 1:
				if (block.getId() == dua.getUse().PUse().getOriginNode()) {
					currentSate = 2;
				} else if (block.isDef(dua.getVariable()) && block.getId() != dua.getDef()) {
					currentSate = 0;
				}
				break;
			case 2:
				if (block.getId() == dua.getUse().getUseNode()) {
					currentSate = 3;
					dua.cover();
				} else if (block.isDef(dua.getVariable()) && block.getId() != dua.getDef()) {
					currentSate = 0;
				} else {
					currentSate = 1;
				}
				break;
			}
		}
	}

}