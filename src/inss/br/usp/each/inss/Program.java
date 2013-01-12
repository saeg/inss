package br.usp.each.inss;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.usp.each.inss.instrumentation.probe.Probe;
import br.usp.each.inss.instrumentation.traverse.DefaultProbeTraverseStrategy;
import br.usp.each.inss.instrumentation.traverse.ProbeTraverseStrategy;
import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.ProgramBlock;
import br.usp.each.opal.requirement.Requirement;

public class Program {
	
	private static ProbeTraverseStrategy traverseStrategy = new DefaultProbeTraverseStrategy();

	private long id;
	private ExecutionEntry[] table;
	private ProgramBlock entrance;
	private List<ProgramBlock> exit;
	private int varSize;
	public Map<String, Object> ctx;
	
	private Requirement[] requirements;
	
	private Program() {
	}

	public Program(long id, DFGraph g) {
		this.id = id;
		table = new ExecutionEntry[g.size()];
		int i = 0;
		for (ProgramBlock block : g) {
			table[i] = new ExecutionEntry(i, block);
			i++;
		}
		entrance = g.entranceBlock();
		exit = g.exitBlock();
		varSize = g.varSize();
		ctx = new HashMap<String, Object>();
	}
	
	public long getId() {
		return id;
	}
	
	public int varSize() {
		return varSize;
	}

	public ExecutionEntry getExecutionEntryById(int nodeId) {
		for(int i = 0; i < table.length; i++) {
			if(table[i].getProgramBlock().getId() == nodeId)
				return table[i];
		}
		return null;
	}
	
	public ExecutionEntry getExecutionEntry(int i) {
		return table[i];
	}
	
	public int size() {
		return table.length;
	}

	public void clear() {
		for (int i = 0; i < table.length; i++) {
			table[i].removeAllProbes();
		}
	}

	public boolean isEntrance(int nodeId) {
		return nodeId == entrance.getId();
	}

	public boolean isExit(int nodeId) {
		for (ProgramBlock block : exit) {
			if(block.getId() == nodeId)
				return true;
		}
		return false;
	}
	
	public static void setTraverseStrategy(ProbeTraverseStrategy traverseStrategy) {
		Program.traverseStrategy = traverseStrategy;
	}
	
	public Program cleanCopyWithId(long id) {
		Program copy = new Program();
		copy.id = id;
		copy.table = new ExecutionEntry[this.table.length];
		copy.entrance = this.entrance;
		copy.exit = this.exit;
		copy.varSize = this.varSize;
		for (int i = 0; i < copy.table.length; i++) {
			copy.table[i] = new ExecutionEntry(i, this.table[i].programBlock);
		}
		copy.ctx = ctx;
		return copy;
	}
	
	public static class ExecutionEntry {
		
		private int id;

		private ProgramBlock programBlock;

		private List<Probe> probes;

		public ExecutionEntry(int id, ProgramBlock programBlock) {
			this.id = id;
			this.programBlock = programBlock;
			this.probes = new LinkedList<Probe>();
		}
		
		public int getId() {
			return id;
		}

		public ProgramBlock getProgramBlock() {
			return programBlock;
		}

		public void addLast(Probe p) {
			probes.add(p);
		}

		public void addFirst(Probe p) {
			probes.add(0, p);
		}

		public void remove(Probe p) {
			probes.remove(p);
		}

		public void removeAllProbes() {
			probes.clear();
		}
		
		public List<Probe> probes() {
			return probes;
		}

		public void traverse() {
			Program.traverseStrategy.traverse(probes);
		}
	}

	public Requirement[] getRequirements() {
		return requirements;
	}
	
	public void setRequirements(Requirement[] requirements) {
		this.requirements = requirements;
	}
}
