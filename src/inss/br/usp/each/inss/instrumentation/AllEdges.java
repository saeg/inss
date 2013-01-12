package br.usp.each.inss.instrumentation;

import br.usp.each.inss.Program;
import br.usp.each.inss.Program.ExecutionEntry;
import br.usp.each.inss.instrumentation.probe.ActiveProbe;
import br.usp.each.inss.instrumentation.probe.AutoremoveProbe;
import br.usp.each.inss.instrumentation.probe.RemoveConditionProbe;
import br.usp.each.opal.requirement.Edge;
import br.usp.each.opal.requirement.Requirement;

public class AllEdges implements Instrumentator {

	@Override
	public void instrument(Program p, Requirement[] requirement) {
		Edge[] edges = (Edge[]) requirement;
		
		int[] lastNode = new int[1];
		lastNode[0] = -1;
		
		for (Edge edge : edges) {
			if (!edge.isCovered()) {
				ExecutionEntry from = p.getExecutionEntryById(edge.getFrom());
				ExecutionEntry to = p.getExecutionEntryById(edge.getTo());
				if(!from.equals(to))
					from.addFirst(new SeedProbe(to, edge, lastNode));
				else
					from.addFirst(new EdgeProbe(edge, lastNode));
			}
		}
		for (int i = 0; i < p.size(); i++) {
			ExecutionEntry e = p.getExecutionEntry(i);
			e.addLast(new ActiveProbe(e.getProgramBlock().getId(), lastNode));
		}
	}
	
	@Override
	public Program copy(Program p, long id) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	private static class SeedProbe implements AutoremoveProbe {
		
		private ExecutionEntry to;
		private Edge edge;
		private int[] lastNode;

		public SeedProbe(ExecutionEntry to, Edge edge, int[] lastNode) {
			this.to = to;
			this.edge = edge;
			this.lastNode = lastNode;
		}

		@Override
		public void execute() {
			to.addFirst(new EdgeProbe(edge, lastNode));		
		}
		
	}
	
	private static class EdgeProbe implements RemoveConditionProbe {

		private Edge edge;
		private int[] lastNode;

		public EdgeProbe(Edge edge, int[] lastNode) {
			this.edge = edge;
			this.lastNode = lastNode;
		}

		@Override
		public void execute() {
			if(lastNode[0] == edge.getFrom())
				edge.cover();
		}

		@Override
		public boolean condition() {
			return edge.isCovered();
		}
		
	}

}
