package br.usp.each.inss.instrumentation;

import java.util.BitSet;
import java.util.List;

import br.usp.each.inss.Program;
import br.usp.each.inss.Program.ExecutionEntry;
import br.usp.each.inss.instrumentation.probe.Probe;
import br.usp.each.opal.dataflow.ProgramBlock;
import br.usp.each.opal.requirement.Dua;
import br.usp.each.opal.requirement.Requirement;
import br.usp.each.opal.requirement.Use.Type;

public class BitwiseDuaCoverage implements Instrumentator {

	@Override
	public void instrument(Program p, Requirement[] requirement) {
		Dua[] duas = (Dua[]) requirement;
		
		BitSet live = new BitSet(duas.length);
		BitSet sleepy = new BitSet(duas.length);
		BitSet covered = new BitSet(duas.length);
	
		p.ctx.put("covered", covered);
		
		for (int i = 0; i < p.size(); i++) {
			ExecutionEntry e = p.getExecutionEntry(i);
			ProgramBlock b = e.getProgramBlock();
			e.addLast(new FastDuaCoverageProbe(b, duas, live, sleepy, covered));
		}
	}
	
	@Override
	public Program copy(Program p, long id) {
		
		BitSet live = new BitSet();
		BitSet sleepy = new BitSet();
		
		Program copy = p.cleanCopyWithId(id);
		for (int i = 0; i < p.size(); i++) {
			List<Probe> probes = p.getExecutionEntry(i).probes();
			ExecutionEntry e = copy.getExecutionEntry(i);
			for (Probe probe : probes) {
				if (probe instanceof FastDuaCoverageProbe) {
					FastDuaCoverageProbe fastProbe = (FastDuaCoverageProbe) probe;
					FastDuaCoverageProbe newProbe = new FastDuaCoverageProbe(live, sleepy, fastProbe.coveredDuas);
					newProbe.duas = fastProbe.duas;
					newProbe.gen = fastProbe.gen;
					newProbe.born = fastProbe.born;
					newProbe.kill = fastProbe.kill;
					newProbe.sleepy = fastProbe.sleepy;
					e.addLast(newProbe);
				} else {
					e.addLast(probe);
				}
			}
		}
		return copy;
	}

	private static class FastDuaCoverageProbe implements Probe {
		
		private Dua[] duas;
		private BitSet liveDuas;
		private BitSet sleepyDuas;
		private BitSet coveredDuas;
		
		private BitSet gen;
		private BitSet born;
		private BitSet kill;
		private BitSet sleepy;
		
		public FastDuaCoverageProbe(BitSet liveDuas, BitSet sleepyDuas, BitSet coveredDuas) {
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
			this.coveredDuas = coveredDuas;
		}

		public FastDuaCoverageProbe(ProgramBlock b, Dua[] duas,
				BitSet liveDuas, BitSet sleepyDuas, BitSet coveredDuas) {
			
			this.duas = duas;
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
			this.coveredDuas = coveredDuas;
			
			gen = new BitSet(duas.length);
			born = new BitSet(duas.length);
			kill = new BitSet(duas.length);
			sleepy = new BitSet(duas.length);

			for (Dua dua : duas) {
				if (dua.getUse().getUseNode() == b.getId())
					gen.set(dua.getId());

				if (dua.getDef() == b.getId()) {
					born.set(dua.getId());
				}

				if (dua.getDef() != b.getId() && b.isDef(dua.getVariable()))
					kill.set(dua.getId());
				
				if (dua.getUse().getType() == Type.P_USE) {
					int origin = dua.getUse().PUse().getOriginNode();
					if (origin != b.getId()) {
						sleepy.set(dua.getId());
					}
				}
			}
		}

		@Override
		public void execute() {
			BitSet temp = new BitSet(duas.length);
			temp.or(liveDuas);
			temp.andNot(sleepyDuas);
			temp.and(gen);
			coveredDuas.or(temp);
			liveDuas.andNot(kill);
			liveDuas.or(born);
			sleepyDuas.clear();
			sleepyDuas.or(sleepy);
		}

	}
}
