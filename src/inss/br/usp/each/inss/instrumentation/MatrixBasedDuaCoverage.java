package br.usp.each.inss.instrumentation;

import java.util.Arrays;
import java.util.List;

import br.usp.each.inss.Program;
import br.usp.each.inss.Program.ExecutionEntry;
import br.usp.each.inss.instrumentation.probe.ActiveProbe;
import br.usp.each.inss.instrumentation.probe.Probe;
import br.usp.each.opal.dataflow.ProgramBlock;
import br.usp.each.opal.requirement.Def;
import br.usp.each.opal.requirement.Dua;
import br.usp.each.opal.requirement.Requirement;
import br.usp.each.opal.requirement.Use;
import br.usp.each.opal.requirement.Use.Type;

public class MatrixBasedDuaCoverage implements Instrumentator {

	@Override
	public void instrument(Program p, Requirement[] requirement) {
		Dua[] duas = (Dua[]) requirement;
		
		Def[] defs = initDefs(duas); // This is my lines indexes
		Use[] uses = initUses(duas); // And this is my columns indexes
		
		UseVarPair[] usePairs = initUsesPair(duas);
		
		// last definition id for each variable... store the line index
		int[] vars = new int[p.varSize()];
		Arrays.fill(vars, -1);
		
		// last executed node... needs to control p-uses
		int[] lastNode = new int[1];
		lastNode[0] = -1;
		
		// it is the DUA coverage matrix
		boolean[][] m = new boolean[uses.length][defs.length];
		
		for (int i = 0; i < defs.length; i++) {
			Def def = defs[i];
			ExecutionEntry e = p.getExecutionEntryById(def.getDef());
			e.addFirst(new DefProbe(i, def.getVariable(), vars));
		}
		
		for (UseVarPair pair : usePairs) {
			Use use = pair.use; // for each USE insert a probe...
			for (int i = 0; i < uses.length; i++) {
				if (use.equals(uses[i])) {
					
					// I find the right index... lets put the probe now
					ExecutionEntry e = p.getExecutionEntryById(use.getUseNode());
					
					if (use.getType() == Type.C_USE) {
						// c-use
						e.addFirst(new UseProbe(i, pair.var, vars, m));
					} else {
						// p-use
						e.addFirst(new PUseProbe(i, pair.var, vars, m, 
								use.PUse().getOriginNode(), lastNode));
					}
				}
			}
		}
		
		for (int i = 0; i < p.size(); i++) {
			ExecutionEntry e = p.getExecutionEntry(i);
			ProgramBlock b = e.getProgramBlock();
			e.addLast(new ActiveProbe(b.getId(), lastNode)); // OH NOT: This overhead is sucks!
			if(p.isExit(b.getId())) {
				e.addLast(new UpdateCoverageProbe(duas, defs, uses, m));
			}
		}
	}
	
	@Override
	public Program copy(Program p, long id) {
		int[] vars = new int[p.varSize()];
		Arrays.fill(vars, -1);
		
		int[] lastNode = new int[1];
		lastNode[0] = -1;
		
		Program copy = p.cleanCopyWithId(id);
		for (int i = 0; i < p.size(); i++) {
			List<Probe> probes = p.getExecutionEntry(i).probes();
			ExecutionEntry e = copy.getExecutionEntry(i);
			for (Probe probe : probes) {
				Class<? extends Probe> probeClass = probe.getClass();
				if (probeClass == DefProbe.class) {
					DefProbe defProbe = (DefProbe) probe;
					e.addLast(new DefProbe(defProbe.defId, defProbe.var, vars));
				} else if (probeClass == UseProbe.class) {
					UseProbe useProbe = (UseProbe) probe;
					e.addLast(new UseProbe(useProbe.useId, useProbe.var, vars, useProbe.m));
				} else if (probeClass == PUseProbe.class) {
					PUseProbe pUseProbe = (PUseProbe) probe;
					e.addLast(new PUseProbe(pUseProbe.useId, pUseProbe.var, vars, 
							pUseProbe.m, pUseProbe.origin, lastNode));
				} else if (probeClass == ActiveProbe.class) {
					ActiveProbe actibeProbe = (ActiveProbe) probe;
					e.addLast(new ActiveProbe(actibeProbe.getNodeId(), lastNode));
				} else {
					e.addLast(probe);
				}
			}
		}
		return copy;
	}
	
	private static class DefProbe implements Probe {

		private int defId;
		
		private int var;

		private int[] vars;

		public DefProbe(int defId, int var, int[] vars) {
			this.defId = defId;
			this.var = var;
			this.vars = vars;
		}

		@Override
		public void execute() {
			vars[var] = defId;
		}
		
	}
	
	private static class UseProbe implements Probe {

		int useId;
		
		int var;
		
		private int[] vars;

		boolean[][] m;

		public UseProbe(int useId, int var, int[] vars, boolean[][] m) {
			this.useId = useId;
			this.var = var;
			this.vars = vars;
			this.m = m;
		}

		@Override
		public void execute() {
			int defId = vars[var];
			if (defId != -1)
				m[useId][defId] = true;
		}
		
	}
	
	private static class PUseProbe extends UseProbe implements Probe {

		private int origin;
		
		private int[] lastNode;
		
		public PUseProbe(int useId, int var, int[] vars, boolean[][] m, int origin, int[] lastNode) {
			super(useId, var, vars, m);
			this.origin = origin;
			this.lastNode = lastNode;
		}

		@Override
		public void execute() {
			if (origin == lastNode[0])
				super.execute();
		}
		
	}
	
	private static class UpdateCoverageProbe implements Probe {

		private Dua[] duas;
		
		private Def[] defs;
		
		private Use[] uses;
		
		private boolean[][] m;

		public UpdateCoverageProbe(Dua[] duas, Def[] defs, Use[] uses, boolean[][] m) {
			this.duas = duas;
			this.defs = defs;
			this.uses = uses;
			this.m = m;
		}

		@Override
		public void execute() {
			for (Dua dua : duas) {
				Use use = dua.getUse();
				Def def = new Def(dua.getDef(), dua.getVariable());
				int useId, defId;
				for (useId = 0; useId < uses.length; useId++) {
					if (uses[useId].equals(use))
						break;
				}
				for (defId = 0; defId < defs.length; defId++) {
					if (defs[defId].equals(def))
						break;
				}
				if (m[useId][defId])
					dua.cover();
			}
		}
		
	}

	private Def[] initDefs(Dua[] duas) {
		Def[] defs = new Def[duas.length];
		
		int i = 0;
		for (Dua dua : duas) {
			
			Def def = new Def(dua.getDef(), dua.getVariable());
			
			boolean add = true;
			for (int j = 0; j < i; j++) {
				if (defs[j].equals(def)) {
					add = false;
					break;
				}
			}
			
			if(add) {
				defs[i++] = def;
			}
		}
		
		return Arrays.copyOf(defs, i);
	}

	private Use[] initUses(Dua[] duas) {
		Use[] uses = new Use[duas.length];
		
		int i = 0;
		for (Dua dua : duas) {
			
			Use use = dua.getUse();
			
			boolean add = true;
			for (int j = 0; j < i; j++) {
				if (uses[j].equals(use)) {
					add = false;
					break;
				}
			}
			
			if(add) {
				uses[i++] = use;
			}
		}
		return Arrays.copyOf(uses, i);
	}
	
	private UseVarPair[] initUsesPair(Dua[] duas) {
		UseVarPair[] pairs = new UseVarPair[duas.length];
		
		int i = 0;
		for (Dua dua : duas) {
			
			UseVarPair pair = new UseVarPair(dua.getUse(), dua.getVariable());
			
			boolean add = true;
			for (int j = 0; j < i; j++) {
				if (pairs[j].equals(pair)) {
					add = false;
					break;
				}
			}
			
			if(add) {
				pairs[i++] = pair;
			}
		}
		
		return Arrays.copyOf(pairs, i);
	}
	
	private class UseVarPair {
		
		private final Use use;
		
		private final int var;
		
		public UseVarPair(Use use, int var) {
			this.use = use;
			this.var = var;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof UseVarPair) {
				UseVarPair other = UseVarPair.class.cast(obj);
				return other.use.equals(this.use)
						&& other.var == this.var;
				
			}
			return false;
		}
		
	}
	
}
