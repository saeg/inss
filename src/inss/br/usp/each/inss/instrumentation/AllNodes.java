package br.usp.each.inss.instrumentation;

import br.usp.each.inss.Program;
import br.usp.each.inss.Program.ExecutionEntry;
import br.usp.each.inss.instrumentation.probe.AutoremoveProbe;
import br.usp.each.opal.requirement.Node;
import br.usp.each.opal.requirement.Requirement;

public class AllNodes implements Instrumentator {

	@Override
	public void instrument(Program p, Requirement[] requirement) {
		Node[] nodes = (Node[]) requirement;
		for (Node node : nodes) {
			if (!node.isCovered()) {
				ExecutionEntry e = p.getExecutionEntryById(node.getNode());
				e.addFirst(new NodeProbe(node));
			}
		}
	}
	
	@Override
	public Program copy(Program p, long id) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	private static class NodeProbe implements AutoremoveProbe {
		
		private Node requirement;

		public NodeProbe(Node requirement) {
			this.requirement = requirement;
		}

		@Override
		public void execute() {
			requirement.cover();
		}
		
	}

}
