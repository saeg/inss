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

public class BitwiseDuaCoverage8 implements Instrumentator {
	
	@Override
	public void instrument(Program p, Requirement[] requirement) {
		Dua[] duas = (Dua[]) requirement;
		
		BitSet live = new BitSet(duas.length);
		BitSet sleepy = new BitSet(duas.length);
		BitSet covered = new BitSet(duas.length);
		
		p.ctx.put("covered", covered);
		
		BitSet temp = new BitSet(duas.length);

		for (int i = 0; i < p.size(); i++) {
			ExecutionEntry e = p.getExecutionEntry(i);
			ProgramBlock b = e.getProgramBlock();
			
			BitSets bitSets = new BitSets(duas, b);
			
			// [0,X,X]
			if (bitSets.gen.isEmpty()) {
				// [0,0,X]
				if (bitSets.sleepy.isEmpty()) {
					// [0,0,0]
					if (bitSets.kill.isEmpty())
						e.addLast(new FastDuaCoverageProbeType0(live, sleepy, bitSets));
					// [0,0,1]
					else
						e.addLast(new FastDuaCoverageProbeType1(live, sleepy, bitSets));
				} 
				// [0,1,X]
				else {
					// [0,1,0]
					if (bitSets.kill.isEmpty())
						e.addLast(new FastDuaCoverageProbeType2(live, sleepy, bitSets));
					// [0,1,1]
					else
						e.addLast(new FastDuaCoverageProbeType3(live, sleepy, bitSets));
				}
			}
			// [1,X,X]
			else {
				// [1,0,X]
				if (bitSets.sleepy.isEmpty()) {
					// [1,0,0]
					if (bitSets.kill.isEmpty())
						e.addLast(new FastDuaCoverageProbeType4(live, sleepy, covered, bitSets, temp));
					// [1,0,1]
					else
						e.addLast(new FastDuaCoverageProbeType5(live, sleepy, covered, bitSets, temp));
				} 
				// [1,1,X]
				else {
					// [1,1,0]
					if (bitSets.kill.isEmpty())
						e.addLast(new FastDuaCoverageProbeType6(live, sleepy, covered, bitSets, temp));
					// [1,1,1]
					else
						e.addLast(new FastDuaCoverageProbeType7(live, sleepy, covered, bitSets, temp));
				}
			}
		}
	}
	
	@Override
	public Program copy(Program p, long id) {
		
		BitSet live = new BitSet();
		BitSet sleepy = new BitSet();
		
		BitSet temp = new BitSet();
		
		Program copy = p.cleanCopyWithId(id);
		for (int i = 0; i < p.size(); i++) {
			List<Probe> probes = p.getExecutionEntry(i).probes();
			ExecutionEntry e = copy.getExecutionEntry(i);
			for (Probe probe : probes) {
				if (probe instanceof FastDuaCoverageProbeType0) {
					FastDuaCoverageProbeType0 fastProbe = (FastDuaCoverageProbeType0) probe;
					FastDuaCoverageProbeType0 newProbe = new FastDuaCoverageProbeType0(live, sleepy);
					newProbe.born = fastProbe.born;
					e.addLast(newProbe);
				} else if (probe instanceof FastDuaCoverageProbeType1) {
					FastDuaCoverageProbeType1 fastProbe = (FastDuaCoverageProbeType1) probe;
					FastDuaCoverageProbeType1 newProbe = new FastDuaCoverageProbeType1(live, sleepy);
					newProbe.born = fastProbe.born;
					newProbe.kill = fastProbe.kill;
					e.addLast(newProbe);
				} else if (probe instanceof FastDuaCoverageProbeType2) {
					FastDuaCoverageProbeType2 fastProbe = (FastDuaCoverageProbeType2) probe;
					FastDuaCoverageProbeType2 newProbe = new FastDuaCoverageProbeType2(live, sleepy);
					newProbe.born = fastProbe.born;
					newProbe.sleepy = fastProbe.sleepy;
					e.addLast(newProbe);
				} else if (probe instanceof FastDuaCoverageProbeType3) {
					FastDuaCoverageProbeType3 fastProbe = (FastDuaCoverageProbeType3) probe;
					FastDuaCoverageProbeType3 newProbe = new FastDuaCoverageProbeType3(live, sleepy);
					newProbe.born = fastProbe.born;
					newProbe.kill = fastProbe.kill;
					newProbe.sleepy = fastProbe.sleepy;
					e.addLast(newProbe);
				} else if (probe instanceof FastDuaCoverageProbeType4) {
					FastDuaCoverageProbeType4 fastProbe = (FastDuaCoverageProbeType4) probe;
					FastDuaCoverageProbeType4 newProbe = new FastDuaCoverageProbeType4(live, sleepy, fastProbe.coveredDuas);
					newProbe.gen = fastProbe.gen;
					newProbe.born = fastProbe.born;
					newProbe.temp = temp;
					e.addLast(newProbe);
				} else if (probe instanceof FastDuaCoverageProbeType5) {
					FastDuaCoverageProbeType5 fastProbe = (FastDuaCoverageProbeType5) probe;
					FastDuaCoverageProbeType5 newProbe = new FastDuaCoverageProbeType5(live, sleepy, fastProbe.coveredDuas);
					newProbe.gen = fastProbe.gen;
					newProbe.born = fastProbe.born;
					newProbe.kill = fastProbe.kill;
					newProbe.temp = temp;
					e.addLast(newProbe);
				} else if (probe instanceof FastDuaCoverageProbeType6) {
					FastDuaCoverageProbeType6 fastProbe = (FastDuaCoverageProbeType6) probe;
					FastDuaCoverageProbeType6 newProbe = new FastDuaCoverageProbeType6(live, sleepy, fastProbe.coveredDuas);
					newProbe.gen = fastProbe.gen;
					newProbe.born = fastProbe.born;
					newProbe.sleepy = fastProbe.sleepy;
					newProbe.temp = temp;
					e.addLast(newProbe);
				} else if (probe instanceof FastDuaCoverageProbeType7) {
					FastDuaCoverageProbeType7 fastProbe = (FastDuaCoverageProbeType7) probe;
					FastDuaCoverageProbeType7 newProbe = new FastDuaCoverageProbeType7(live, sleepy, fastProbe.coveredDuas);
					newProbe.gen = fastProbe.gen;
					newProbe.born = fastProbe.born;
					newProbe.kill = fastProbe.kill;
					newProbe.sleepy = fastProbe.sleepy;
					newProbe.temp = temp;
					e.addLast(newProbe);
				} else {
					e.addLast(probe);
				}
			}
		}
		return copy;
	}

	private static class FastDuaCoverageProbeType0 implements Probe {
		
		private BitSet liveDuas;
		private BitSet sleepyDuas;
		
		private BitSet born;
		
		public FastDuaCoverageProbeType0(BitSet liveDuas, BitSet sleepyDuas) {
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
		}

		/**
		 * gen = 0 and sleepy = 0 and (kill == 0 and born == ?) [0,0,0] 
		 */
		public FastDuaCoverageProbeType0(BitSet liveDuas, BitSet sleepyDuas, BitSets bitSets) {
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
			
			born = bitSets.born;
		}

		@Override
		public void execute() {
			liveDuas.or(born);
			sleepyDuas.clear();
		}

	}

	private static class FastDuaCoverageProbeType1 implements Probe {
		
		private BitSet liveDuas;
		private BitSet sleepyDuas;
		
		private BitSet born;
		private BitSet kill;
		
		public FastDuaCoverageProbeType1(BitSet liveDuas, BitSet sleepyDuas) {
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
		}

		/**
		 * gen = 0 and sleepy = 0 and (kill != 0 and born = ?) [0,0,1]  
		 */
		public FastDuaCoverageProbeType1(BitSet liveDuas, BitSet sleepyDuas, BitSets bitSets) {
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
			
			born = bitSets.born;
			kill = bitSets.kill;
		}

		@Override
		public void execute() {
			liveDuas.andNot(kill);
			liveDuas.or(born);
			sleepyDuas.clear();
		}

	}

	private static class FastDuaCoverageProbeType2 implements Probe {
		
		private BitSet liveDuas;
		private BitSet sleepyDuas;
		
		private BitSet sleepy;
		private BitSet born;
		
		public FastDuaCoverageProbeType2(BitSet liveDuas, BitSet sleepyDuas) {
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
		}

		/**
		 * gen = 0 and sleepy != 0  and (kill = 0  and born = ?) [0,1,0] 
		 */
		public FastDuaCoverageProbeType2(BitSet liveDuas, BitSet sleepyDuas, BitSets bitSets) {
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
			
			sleepy = bitSets.sleepy;
			born = bitSets.born;
		}

		@Override
		public void execute() {
			liveDuas.or(born);
			sleepyDuas.clear();
			sleepyDuas.or(sleepy);
		}

	}	

	private static class FastDuaCoverageProbeType3 implements Probe {
		
		private BitSet liveDuas;
		private BitSet sleepyDuas;
		
		private BitSet born;
		private BitSet kill;
		private BitSet sleepy;

		public FastDuaCoverageProbeType3(BitSet liveDuas, BitSet sleepyDuas) {
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
		}
		
		/**
		 * gen = 0 and sleepy != 0 and (kill != 0 and born = ?) [0,1,1]
		 */
		public FastDuaCoverageProbeType3(BitSet liveDuas, BitSet sleepyDuas, BitSets bitSets) {
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
			
			born = bitSets.born;
			kill = bitSets.kill;
			sleepy = bitSets.sleepy;
		}

		@Override
		public void execute() {
			liveDuas.andNot(kill);
			liveDuas.or(born);
			sleepyDuas.clear();
			sleepyDuas.or(sleepy);
		}

	}

	private static class FastDuaCoverageProbeType4 implements Probe {
		
		private BitSet liveDuas;
		private BitSet sleepyDuas;
		private BitSet coveredDuas;
		
		private BitSet temp;
		
		private BitSet gen;
		private BitSet born;
		
		public FastDuaCoverageProbeType4(BitSet liveDuas, BitSet sleepyDuas, BitSet coveredDuas) {
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
			this.coveredDuas = coveredDuas;
		}

		/**
		 * gen != 0 and sleepy = 0 and (kill = 0  and born = ?) [1,0,0]
		 */
		public FastDuaCoverageProbeType4(BitSet liveDuas, BitSet sleepyDuas,
				BitSet coveredDuas, BitSets bitSets, BitSet temp) {
			
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
			this.coveredDuas = coveredDuas;
			
			this.temp = temp;
			
			gen = bitSets.gen;
			born = bitSets.born;
		}

		@Override
		public void execute() {
			temp.clear();
			temp.or(liveDuas);
			temp.andNot(sleepyDuas);
			temp.and(gen);
			coveredDuas.or(temp);
			liveDuas.or(born);
			sleepyDuas.clear();
		}

	}	

	private static class FastDuaCoverageProbeType5 implements Probe {
		
		private BitSet liveDuas;
		private BitSet sleepyDuas;
		private BitSet coveredDuas;
		
		private BitSet temp;
		
		private BitSet gen;
		private BitSet born;
		private BitSet kill;
		
		public FastDuaCoverageProbeType5(BitSet liveDuas, BitSet sleepyDuas, BitSet coveredDuas) {
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
			this.coveredDuas = coveredDuas;
		}

		/**
		 * gen != 0 and sleepy = 0 and (kill != 0  and born = ?) [1,0,1]
		 */
		public FastDuaCoverageProbeType5(BitSet liveDuas, BitSet sleepyDuas,
				BitSet coveredDuas, BitSets bitSets, BitSet temp) {
			
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
			this.coveredDuas = coveredDuas;
			
			this.temp = temp;
			
			gen = bitSets.gen;
			born = bitSets.born;
			kill = bitSets.kill;
		}

		@Override
		public void execute() {
			temp.clear();
			temp.or(liveDuas);
			temp.andNot(sleepyDuas);
			temp.and(gen);
			coveredDuas.or(temp);
			liveDuas.andNot(kill);
			liveDuas.or(born);
			sleepyDuas.clear();
		}

	}	

	private static class FastDuaCoverageProbeType6 implements Probe {
		
		private BitSet liveDuas;
		private BitSet sleepyDuas;
		private BitSet coveredDuas;
		
		private BitSet temp;
		
		private BitSet gen;
		private BitSet sleepy;
		private BitSet born;
		
		public FastDuaCoverageProbeType6(BitSet liveDuas, BitSet sleepyDuas, BitSet coveredDuas) {
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
			this.coveredDuas = coveredDuas;
		}
		
		/**
		 * gen != 0 and sleepy != 0 and (kill = 0  and born = ?) [1,1,0] 
		 */
		public FastDuaCoverageProbeType6(BitSet liveDuas, BitSet sleepyDuas,
				BitSet coveredDuas, BitSets bitSets, BitSet temp) {
			
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
			this.coveredDuas = coveredDuas;
			
			this.temp = temp;
			
			gen = bitSets.gen;
			sleepy = bitSets.sleepy;
			born = bitSets.born;
		}

		@Override
		public void execute() {
			temp.clear();
			temp.or(liveDuas);
			temp.andNot(sleepyDuas);
			temp.and(gen);
			coveredDuas.or(temp);
			liveDuas.or(born);
			sleepyDuas.clear();
			sleepyDuas.or(sleepy);
		}

	}	

	private static class FastDuaCoverageProbeType7 implements Probe {
		
		private BitSet liveDuas;
		private BitSet sleepyDuas;
		private BitSet coveredDuas;
		
		private BitSet temp;
		
		private BitSet gen;
		private BitSet born;
		private BitSet kill;
		private BitSet sleepy;
		
		public FastDuaCoverageProbeType7(BitSet liveDuas, BitSet sleepyDuas, BitSet coveredDuas) {
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
			this.coveredDuas = coveredDuas;
		}

		/**
		 * gen != 0 and sleepy != 0 and  (kill != 0  and born = ?) [1,1,1]
		 */
		public FastDuaCoverageProbeType7(BitSet liveDuas, BitSet sleepyDuas,
				BitSet coveredDuas, BitSets bitSets, BitSet temp) {
			
			this.liveDuas = liveDuas;
			this.sleepyDuas = sleepyDuas;
			this.coveredDuas = coveredDuas;
			
			this.temp = temp;
			
			gen = bitSets.gen;
			born = bitSets.born;
			kill = bitSets.kill;
			sleepy = bitSets.sleepy;
		}

		@Override
		public void execute() {
			temp.clear();
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

	private static class BitSets {
		
		private BitSet gen;
		private BitSet born;
		private BitSet kill;
		private BitSet sleepy;

		public BitSets(Dua[] duas, ProgramBlock b) {
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
		
	}

}
