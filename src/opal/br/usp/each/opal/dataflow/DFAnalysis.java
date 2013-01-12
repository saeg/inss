package br.usp.each.opal.dataflow;

import java.util.BitSet;

import br.usp.each.opal.GraphMap;
import br.usp.each.opal.dataflow.DFAnalysis.AnalysisBlock;

public class DFAnalysis extends GraphMap<AnalysisBlock> {
	
	private int vars;

	public DFAnalysis(DFGraph graph) {
		vars = graph.varSize();
		
		// Add Nodes and Edges
		for (ProgramBlock from : graph) {
			AnalysisBlock a, b;
			add(a = new AnalysisBlock(from));
			for (ProgramBlock to : graph.neighbors(from)) {
				add(b = new AnalysisBlock(to));
				addEdge(a, b);
			}
		}
	}
	
	private void init() {
		int pos = 0;
		for (AnalysisBlock b : this) {
			b.in = new BitSet(vars * size());
			b.out = new BitSet(vars * size());
			b.gen = new BitSet(vars * size());
			b.kill = new BitSet(vars * size());
			b.pos = pos;
			pos++;
		}
		for (AnalysisBlock b : this) {
			computeGenAndKill(b);
			b.out.or(b.gen);
		}
	}
	
	public void computeInAndOut() {
		init();
		
		// I use inverse to compute predecessors
		GraphMap<AnalysisBlock> inverse = inverse();
		
		boolean changed = true;
		while (changed) {
			changed = false;
			for (AnalysisBlock b : this) {
				
				// in[B] := U out[P] | P a predecessor of B
				b.in.clear();
				for (AnalysisBlock pred : inverse.neighbors(b))
					b.in.or(pred.out);
				
				// oldout := out
				BitSet oldout = new BitSet(vars * size());
				oldout.or(b.out);
				
				// out[B] := gen[B] U (in[B] - kill[B])
				BitSet temp = new BitSet(vars * size());
				temp.or(b.in);
				temp.andNot(b.kill); // temp := in[B] - kill[B] ;)
				
				b.out.clear();
				b.out.or(b.gen);
				b.out.or(temp); // out[B] := gen[B] U temp
				
				if(!b.out.equals(oldout))
					changed = true;
			}
		}
	}

	/* 
	 * This method can be a little confused, so...
	 * 
	 * For each variable in the Graph, check if block B define the current variable V.
	 * If YES set the 'pair' (V, B) as true in the "gen" and for all others block B',
	 * where V is definied, set the 'pair' (V, B') as true in the "kill".
	 */
	private void computeGenAndKill(AnalysisBlock b) {
		// For each variable...
		for (int i = 0; i < vars; i++) {
			if(b.programBlock.isDef(i)) {
				b.gen.set(b.pos * vars + i);
				for (AnalysisBlock other : this) {
					if(other != b && other.programBlock.isDef(i)) {
						b.kill.set(other.pos * vars + i);
					}
				}
			}
		}
	}
	
	public int varSize() {
		return vars;
	}

	public static class AnalysisBlock {
		
		private int pos;
		private BitSet in;
		private BitSet out;
		private BitSet gen;
		private BitSet kill;
		private ProgramBlock programBlock;
		
		public AnalysisBlock(ProgramBlock programBlock) {
			this.programBlock = programBlock;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof AnalysisBlock) {
				AnalysisBlock other = (AnalysisBlock) obj;
				return this.programBlock.getId() == other.programBlock.getId();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return programBlock.getId() * 31;
		}
		
		public BitSet getIn() {
			return in;
		}
		
		public BitSet getOut() {
			return out;
		}
		
		public ProgramBlock getProgramBlock() {
			return programBlock;
		}

		public int getPos() {
			return pos;
		}

			
	}

}
