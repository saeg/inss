package br.usp.each.inss.instrumentation;

import java.util.Arrays;
import java.util.List;

import br.usp.each.inss.Program;
import br.usp.each.inss.Program.ExecutionEntry;
import br.usp.each.inss.instrumentation.probe.ActiveProbe;
import br.usp.each.inss.instrumentation.probe.AutoremoveProbe;
import br.usp.each.inss.instrumentation.probe.Probe;
import br.usp.each.inss.instrumentation.probe.RemoveConditionProbe;
import br.usp.each.opal.dataflow.ProgramBlock;
import br.usp.each.opal.requirement.Dua;
import br.usp.each.opal.requirement.Requirement;

public class AllUsesDemandDriven implements Instrumentator {

	@Override
	public void instrument(Program p, Requirement[] requirement) {
		Dua[] duas = (Dua[]) requirement;
		int[] vars = new int[p.varSize()];
		for (int i = 0; i < vars.length; i++) {
			vars[i] = -1;
		}
		
		int[] lastNode = new int[1];
		lastNode[0] = -1;
		
		if(duas.length > 0) {
			for (Dua dua : duas) {
				if(!dua.isCovered()) {
					ExecutionEntry def = p.getExecutionEntryById(dua.getDef());
					ExecutionEntry use = p.getExecutionEntryById(dua.getUse().getUseNode());
					if (!def.equals(use))
						def.addFirst(new SeedProbe(p, use.getId(), dua, vars, lastNode));
					else
						switch (dua.getUse().getType()) {
						case P_USE:							
							def.addFirst(new PUseDuaProbe(dua, vars, lastNode));
							break;
						case C_USE:
							def.addFirst(new DuaProbe(dua, vars));
							break;
						}
				}
			}
			for (int i = 0; i < p.size(); i++) {
				ExecutionEntry e = p.getExecutionEntry(i);
				e.addLast(new DefProbe(e.getProgramBlock(), vars));
				e.addLast(new ActiveProbe(e.getProgramBlock().getId(), lastNode));
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
				if (probe instanceof ActiveProbe) {
					ActiveProbe activeProbe = (ActiveProbe) probe;
					e.addLast(new ActiveProbe(activeProbe.getNodeId(), lastNode));
				} else if (probe instanceof DefProbe) {
					DefProbe defProbe = (DefProbe) probe;
					e.addLast(new DefProbe(defProbe.programBlock, vars));
				} else if (probe instanceof SeedProbe) {
					SeedProbe seedProbe = (SeedProbe) probe;
					e.addLast(new SeedProbe(copy, seedProbe.useIdx, seedProbe.dua, vars, lastNode));
				} else if (probe instanceof PUseDuaProbe) {
					PUseDuaProbe pUseDuaProbe = (PUseDuaProbe) probe;
					e.addLast(new PUseDuaProbe(pUseDuaProbe.dua, vars, lastNode));
				} else {
					DuaProbe duaProbe = (DuaProbe) probe;
					e.addLast(new DuaProbe(duaProbe.dua, vars));
				}
			}
		}
		return copy;
	}
	
	private static class SeedProbe implements AutoremoveProbe {
		
		private Program p;
		private int useIdx;
		private Dua dua;
		private int[] vars;
		private int[] lastNode;
		
		public SeedProbe(Program p, int useIdx, Dua dua, int[] vars, int[] lastNode) {
			this.p = p;
			this.useIdx = useIdx;
			this.dua = dua;
			this.vars = vars;
			this.lastNode = lastNode;
		}

		@Override
		public void execute() {
			switch (dua.getUse().getType()) {
			case C_USE:
				p.getExecutionEntry(useIdx).addFirst(new DuaProbe(dua, vars));
				break;
			case P_USE:
				p.getExecutionEntry(useIdx).addFirst(new PUseDuaProbe(dua, vars, lastNode));
				break;
			}
		}
		
	}
	
	private static class DefProbe implements Probe {

		private ProgramBlock programBlock;
		private int[] vars;
		
		public DefProbe(ProgramBlock programBlock, int[] vars) {
			this.programBlock = programBlock;
			this.vars = vars;
		}

		@Override
		public void execute() {
			for (int i = 0; i < vars.length; i++) {
				if(programBlock.isDef(i))
					vars[i] = programBlock.getId();
			}			
		}
		
	}
	
	private static class DuaProbe implements RemoveConditionProbe {
		
		private Dua dua;
		private int[] vars;
		
		public DuaProbe(Dua dua, int[] vars) {
			this.dua = dua;
			this.vars = vars;
		}

		@Override
		public void execute() {
			if(vars[dua.getVariable()] == dua.getDef())
				dua.cover();
		}

		@Override
		public boolean condition() {
			return dua.isCovered();
		}
		
	}
	
	private static class PUseDuaProbe implements RemoveConditionProbe {
		
		private Dua dua;
		private int[] vars;
		private int[] lastNode;
		
		public PUseDuaProbe(Dua dua, int[] vars, int[] lastNode) {
			this.dua = dua;
			this.vars = vars;
			this.lastNode = lastNode;
		}

		@Override
		public void execute() {
			if(vars[dua.getVariable()] == dua.getDef())
				if(lastNode[0] == dua.getUse().PUse().getOriginNode())
					dua.cover();
		}

		@Override
		public boolean condition() {
			return dua.isCovered();
		}
		
	}

}
