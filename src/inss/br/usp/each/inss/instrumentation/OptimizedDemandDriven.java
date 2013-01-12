package br.usp.each.inss.instrumentation;

import br.usp.each.inss.Program;
import br.usp.each.inss.Program.ExecutionEntry;
import br.usp.each.inss.instrumentation.probe.ActiveProbe;
import br.usp.each.inss.instrumentation.probe.RemoveConditionProbe;
import br.usp.each.opal.requirement.Dua;
import br.usp.each.opal.requirement.Requirement;

public class OptimizedDemandDriven implements Instrumentator {

	@Override
	public void instrument(Program p, Requirement[] requirement) {
		Dua[] duas = (Dua[]) requirement;
		int[] vars = new int[p.varSize()];
		for (int i = 0; i < vars.length; i++) {
			vars[i] = -1;
		}
		
		int[] lastNode = new int[1];
		lastNode[0] = -1;
		
		if (duas.length > 0) {
			for (Dua dua : duas) {
				if (!dua.isCovered()) {
					ExecutionEntry def = p.getExecutionEntryById(dua.getDef());
					ExecutionEntry use = p.getExecutionEntryById(dua.getUse().getUseNode());
					switch (dua.getUse().getType()) {
					case C_USE:
						if (!def.equals(use))
							def.addLast(new SeedCUseProbe(def, use, dua, vars));
						else {
							use.addFirst(new CUseOnlyProbe(dua, vars));
							use.addLast(new DefOnlyProbe(def, dua, vars, dua.getVariable()));
						}
						break;
					case P_USE:
						if (!def.equals(use))
							def.addLast(new SeedPUseProbe(def, use, dua, vars,lastNode));
						else {
							use.addFirst(new PUseOnlyProbe(dua, vars, lastNode));
							use.addLast(new DefOnlyProbe(def, dua, vars, dua.getVariable()));
						}
						break;
					}

				}
			}
			for (int i = 0; i < p.size(); i++) {
				ExecutionEntry e = p.getExecutionEntry(i);
				e.addLast(new ActiveProbe(e.getProgramBlock().getId(), lastNode));
			}
		}
	}

	private static class SeedPUseProbe implements RemoveConditionProbe {
		
		private ExecutionEntry def;
		private ExecutionEntry use;
		private Dua dua;
		private int[] vars;
		private int[] lastNode;
		private int var;
		private boolean remove = false;

		public SeedPUseProbe(ExecutionEntry def, ExecutionEntry use, Dua dua, int[] vars, int[] lastNode) {
			this.use = use;
			this.def = def;
			this.dua = dua;
			this.vars = vars;
			this.lastNode = lastNode;
			this.var = dua.getVariable();
		}

		@Override
		public void execute() {
			if (remove)
				return;
			
			vars[var] = def.getProgramBlock().getId();
			use.addFirst(new PUseProbe(dua, vars, lastNode, this));
		}

		public void remove() {
			remove = true;
		}

		public boolean condition() {
			return remove;
		}

	}
	
	@Override
	public Program copy(Program p, long id) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	private static class SeedCUseProbe implements RemoveConditionProbe {
		
		private ExecutionEntry def;
		private ExecutionEntry use;
		private Dua dua;
		private int[] vars;
		private int var;
		private boolean remove = false;

		public SeedCUseProbe(ExecutionEntry def, ExecutionEntry use, Dua dua, int[] vars) {
			this.use = use;
			this.def = def;
			this.dua = dua;
			this.vars = vars;
			this.var = dua.getVariable();
		}

		@Override
		public void execute() {
			if (remove)
				return;
			
			vars[var] = def.getProgramBlock().getId();
			use.addFirst(new CUseProbe(dua, vars, this));
		}

		public void remove() {
			remove = true;
		}

		public boolean condition() {
			return remove;
		}

	}

	private static class CUseProbe implements RemoveConditionProbe {

		private Dua dua;
		private int[] vars;
		private SeedCUseProbe s;

		public CUseProbe(Dua dua, int[] vars, SeedCUseProbe s) {
			this.dua = dua;
			this.vars = vars;
			this.s = s;
		}

		@Override
		public void execute() {
			if (vars[dua.getVariable()] == dua.getDef()) {
				dua.cover();
				s.remove();
			}
		}

		public boolean condition() {
			return (dua.isCovered() || vars[dua.getVariable()] != dua.getDef());
		}

	}

	private static class PUseProbe implements RemoveConditionProbe {

		private Dua dua;
		private int[] vars;
		private int[] lastNode;
		private SeedPUseProbe s;

		public PUseProbe(Dua dua, int[] vars, int[] lastNode, SeedPUseProbe s) {
			this.dua = dua;
			this.vars = vars;
			this.lastNode = lastNode;
			this.s = s;
		}

		@Override
		public void execute() {
			if (vars[dua.getVariable()] == dua.getDef())
				if (lastNode[0] == dua.getUse().PUse().getOriginNode()) {
					dua.cover();
					s.remove();
				}
		}

		public boolean condition() {
			return (dua.isCovered() || vars[dua.getVariable()] != dua.getDef());
		}

	}

	private static class PUseOnlyProbe implements RemoveConditionProbe {

		private Dua dua;
		private int[] vars;
		private int[] lastNode;

		public PUseOnlyProbe(Dua dua, int[] vars, int[] lastNode) {
			this.dua = dua;
			this.vars = vars;
			this.lastNode = lastNode;
		}

		@Override
		public void execute() {

			if (vars[dua.getVariable()] == dua.getDef())
				if (lastNode[0] == dua.getUse().PUse().getOriginNode()) {
					dua.cover();
				}
		}

		public boolean condition() {
			return dua.isCovered();
		}
	}

	private static class CUseOnlyProbe implements RemoveConditionProbe {

		private Dua dua;
		private int[] vars;

		public CUseOnlyProbe(Dua dua, int[] vars) {
			this.dua = dua;
			this.vars = vars;
		}

		@Override
		public void execute() {
			if (vars[dua.getVariable()] == dua.getDef()) {
				dua.cover();
			}
		}

		public boolean condition() {
			return dua.isCovered();
		}

	}

	private static class DefOnlyProbe implements RemoveConditionProbe {

		private ExecutionEntry def;
		private int[] vars;
		private int var;
		private Dua dua;

		public DefOnlyProbe(ExecutionEntry def, Dua dua, int[] vars, int var) {
			this.def = def;
			this.vars = vars;
			this.var = var;
			this.dua = dua;
		}

		@Override
		public void execute() {
			vars[var] = def.getProgramBlock().getId();
		}

		@Override
		public boolean condition() {
			return dua.isCovered();
		}
	}

}
