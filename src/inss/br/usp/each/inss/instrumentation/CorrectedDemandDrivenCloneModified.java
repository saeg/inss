package br.usp.each.inss.instrumentation;

import java.util.Arrays;
import java.util.List;

import br.usp.each.inss.Program;
import br.usp.each.inss.Program.ExecutionEntry;
import br.usp.each.inss.instrumentation.probe.ActiveProbe;
import br.usp.each.inss.instrumentation.probe.Probe;
import br.usp.each.inss.instrumentation.probe.RemoveConditionProbe;
import br.usp.each.opal.dataflow.ProgramBlock;
import br.usp.each.opal.requirement.Dua;
import br.usp.each.opal.requirement.Requirement;

public class CorrectedDemandDrivenCloneModified implements Instrumentator {

	@Override
	public void instrument(Program p, Requirement[] requirement) {
		Dua[] duas = (Dua[]) requirement;
		p.setRequirements(duas);
		if (duas.length > 0) {
			int var;
			int[] vars = new int[p.varSize()];
			
			int[] lastNode = new int[1];
			lastNode[0] = -1;

			for (int j = 0; j < vars.length; j++)
				vars[j] = -2; // in principle, none def of var should be checked

			for (Dua dua : duas) {
				if (!dua.isCovered()) {
					ExecutionEntry def = p.getExecutionEntryById(dua.getDef());
					ExecutionEntry use = p.getExecutionEntryById(dua.getUse().getUseNode());
					var = dua.getVariable();
					// Check whether there is at least one dua [def, use, var] alive
					if(vars[var] == -2)
						vars[var] = -1; // indicate all defs of var should be tracked 
					
					switch (dua.getUse().getType()) {
					case C_USE:
						if (!def.equals(use)) {
							def.addLast(new SeedCUseProbe(p, use.getId(), dua, vars));
						} else {
							use.addFirst(new CUseOnlyProbe(dua, vars));
						}
						break;
					case P_USE:
						if (!def.equals(use)) {
							def.addLast(new SeedPUseProbe(p, use.getId(), dua, vars, lastNode));
						} else {
							use.addFirst(new PUseOnlyProbe(dua, vars, lastNode));
						}
						break;
					}

				}
			}
			for (int i = 0; i < p.size(); i++) {
				ExecutionEntry e = p.getExecutionEntry(i);
				ProgramBlock b = e.getProgramBlock();

				for (int j = 0; j < vars.length; j++) {
					if (vars[j] == -1 && b.getDefs().get(j))
						e.addLast(new DefOnlyProbe(p, e.getId(), vars, j));
				}
				e.addLast(new ActiveProbe(e.getProgramBlock().getId(), lastNode));
			}
		}
	}
	
	@Override
	public Program copy(Program p, long id) {
		int[] vars = new int[p.varSize()];
		Arrays.fill(vars, -2);
		
		int[] lastNode = new int[1];
		lastNode[0] = -1;
		
		for (Requirement r : p.getRequirements()) {
			Dua dua = (Dua) r;
			if (!dua.isCovered()) {
				int var = dua.getVariable();
				// Check whether there is at least one dua [def, use, var] alive
				if (vars[var] == -2)
					vars[var] = -1; // indicate all defs of var should be
									// tracked
			}
		}
		
		Program copy = p.cleanCopyWithId(id);
		for (int i = 0; i < p.size(); i++) {
			List<Probe> probes = p.getExecutionEntry(i).probes();
			ExecutionEntry e = copy.getExecutionEntry(i);
			for (Probe probe : probes) {
				if (probe instanceof ActiveProbe) {
					ActiveProbe activeProbe = (ActiveProbe) probe;
					e.addLast(new ActiveProbe(activeProbe.getNodeId(), lastNode));
				} else if (probe instanceof DefOnlyProbe) {
					DefOnlyProbe defOnlyProbe = (DefOnlyProbe) probe;
					if (vars[defOnlyProbe.var] == -1) {
						e.addLast(new DefOnlyProbe(copy, defOnlyProbe.useIdx, vars, defOnlyProbe.var));
					}
				} else if (probe instanceof CUseOnlyProbe) {
					CUseOnlyProbe cUseOnlyProbe = (CUseOnlyProbe) probe;
					if (!cUseOnlyProbe.dua.isCovered())
						e.addLast(new CUseOnlyProbe(cUseOnlyProbe.dua, vars));
				} else if (probe instanceof PUseOnlyProbe) {
					PUseOnlyProbe pUseOnlyProbe = (PUseOnlyProbe) probe;
					if (!pUseOnlyProbe.dua.isCovered())
						e.addLast(new PUseOnlyProbe(pUseOnlyProbe.dua, vars, lastNode));
				} else if (probe instanceof SeedCUseProbe) {
					SeedCUseProbe seedCUseProbe = (SeedCUseProbe) probe;
					if (!seedCUseProbe.dua.isCovered())
						e.addLast(new SeedCUseProbe(copy, seedCUseProbe.useIdx, seedCUseProbe.dua, vars));
				} else {
					SeedPUseProbe seedPUseProbe = (SeedPUseProbe) probe;
					if (!seedPUseProbe.dua.isCovered())
						e.addLast(new SeedPUseProbe(copy, seedPUseProbe.useIdx, seedPUseProbe.dua, vars, lastNode));
				}
			}
		}
		return copy;
	}

	private static class SeedPUseProbe implements RemoveConditionProbe {

		private Program p;
		private int useIdx;
		private Dua dua;
		private int[] vars;
		private int[] lastNode;
		private boolean remove = false;
		private boolean inserted = false;

		public SeedPUseProbe(Program p, int useIdx, Dua dua, int[] vars, int[] lastNode) {
			this.p = p;
			this.useIdx = useIdx;
			this.dua = dua;
			this.vars = vars;
			this.lastNode = lastNode;
		}

		@Override
		public void execute() {
			if (remove)
				return;
			if (!inserted) {
				p.getExecutionEntry(useIdx).addFirst(new PUseProbe(dua, vars, lastNode, this));
				inserted = true;
			}
		}

		public void remove() {
			remove = true;
		}

		@Override
		public boolean condition() {
			return remove;
		}

		public void reinsert() {
			inserted = false;

		}

	}

	private static class SeedCUseProbe implements RemoveConditionProbe {

		private Program p;
		private int useIdx;
		private Dua dua;
		private int[] vars;
		private boolean remove = false;
		private boolean inserted = false;

		public SeedCUseProbe(Program p, int useIdx, Dua dua, int[] vars) {
			this.p = p;
			this.useIdx = useIdx;
			this.dua = dua;
			this.vars = vars;
		}

		@Override
		public void execute() {
			if (remove)
				return;
			if (!inserted) {
				p.getExecutionEntry(useIdx).addFirst(new CUseProbe(dua, vars, this));
				inserted = true;
			}
		}

		public void remove() {
			remove = true;
		}

		@Override
		public boolean condition() {
			return remove;
		}

		public void reinsert() {
			inserted = false;

		}
	}

	private static class CUseProbe implements RemoveConditionProbe {

		private Dua dua;
		private int[] vars;
		private SeedCUseProbe s;
		private int var, def;
		private boolean remove = false;

		public CUseProbe(Dua dua, int[] vars, SeedCUseProbe s) {
			this.dua = dua;
			this.vars = vars;
			this.s = s;
			this.var = dua.getVariable();
			this.def = dua.getDef();
		}

		@Override
		public void execute() {
			if (remove)
				return;

			if (vars[var] == def) {
				dua.cover();
				s.remove();
				remove = true;
			}
		}

		@Override
		public boolean condition() {
			boolean getout = remove || vars[var] != def;
			if (getout)
				s.reinsert();

			return (getout);
		}
	}

	private static class PUseProbe implements RemoveConditionProbe {

		private Dua dua;
		private int[] vars;
		private int[] lastNode;
		private SeedPUseProbe s;
		private int var, def, orig;
		private boolean remove = false;

		public PUseProbe(Dua dua, int[] vars, int[] lastNode, SeedPUseProbe s) {
			this.dua = dua;
			this.vars = vars;
			this.lastNode = lastNode;
			this.s = s;
			this.var = dua.getVariable();
			this.def = dua.getDef();
			this.orig = dua.getUse().PUse().getOriginNode();
		}

		@Override
		public void execute() {
			if (remove)
				return;
			if (vars[var] == def)
				if (lastNode[0] == orig) {
					dua.cover();
					s.remove();
					remove = true;
				}
		}

		@Override
		public boolean condition() {
			boolean getout = remove || vars[var] != def;
			if (getout)
				s.reinsert();

			return (getout);
		}

	}

	private static class PUseOnlyProbe implements RemoveConditionProbe {

		private Dua dua;
		private int[] vars;
		private int[] lastNode;
		private int var, def, orig;
		private boolean remove = false;

		public PUseOnlyProbe(Dua dua, int[] vars, int[] lastNode) {
			this.dua = dua;
			this.vars = vars;
			this.lastNode = lastNode;
			this.var = dua.getVariable();
			this.def = dua.getDef();
			this.orig = dua.getUse().PUse().getOriginNode();
		}

		@Override
		public void execute() {
			if (remove)
				return;
			if (vars[var] == def)
				if (lastNode[0] == orig) {
					dua.cover();
					remove = true;
				}
		}

		@Override
		public boolean condition() {
			return remove;
		}
	}

	private static class CUseOnlyProbe implements RemoveConditionProbe {

		private Dua dua;
		private int[] vars;
		private int var, def;
		private boolean remove = false;

		public CUseOnlyProbe(Dua dua, int[] vars) {
			this.dua = dua;
			this.vars = vars;
			this.var = dua.getVariable();
			this.def = dua.getDef();
		}

		@Override
		public void execute() {
			if (remove)
				return;
			if (vars[var] == def) {
				dua.cover();
				remove = true;
			}
		}

		@Override
		public boolean condition() {
			return remove;
		}

	}

	private static class DefOnlyProbe implements Probe {

		private Program p;
		private int useIdx;
		private int[] vars;
		private int var;

		public DefOnlyProbe(Program p, int useIdx, int[] vars, int var) {
			this.p = p;
			this.useIdx = useIdx;
			this.vars = vars;
			this.var = var;
		}

		@Override
		public void execute() {
			vars[var] = p.getExecutionEntry(useIdx).getProgramBlock().getId();
		}

	}

}
