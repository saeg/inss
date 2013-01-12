package br.usp.each.inss.executor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.BitSet;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.usp.each.commons.logger.Log;
import br.usp.each.inss.Program;
import br.usp.each.inss.cache.DefUseCache;
import br.usp.each.inss.cache.Programs;
import br.usp.each.inss.cache.Programs.ClassProgram;
import br.usp.each.inss.cache.Requirements;
import br.usp.each.inss.instrumentation.BitwiseDuaCoverage;
import br.usp.each.inss.instrumentation.BitwiseDuaCoverage16;
import br.usp.each.inss.instrumentation.BitwiseDuaCoverage8;
import br.usp.each.inss.instrumentation.Instrumentator;
import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.requirement.Dua;
import br.usp.each.opal.requirement.Requirement;
import br.usp.each.opal.requirement.RequirementDetermination;
import br.usp.each.opal.requirement.RequirementDeterminationException;

public class Simulator implements ExecutorManager {
	
	private final static Program SENTINELLA = 
			new Program(Long.MIN_VALUE, new DFGraph("Sentinella", 0));
	
	private final Map<Thread, Deque<Program>> stacks = new HashMap<Thread, Deque<Program>>();

	private final Requirements requirements = new Requirements();
	
	private final Programs programs = new Programs();
	
	private final DefUseCache cache;

	private final Instrumentator instrumentator;

	private final RequirementDetermination determination;
	
	private final static Log logger = new Log(Simulator.class);
	
	public Simulator(DefUseCache cache, Instrumentator instrumentator, RequirementDetermination determination) {
		this.cache = cache;
		this.instrumentator = instrumentator;
		this.determination = determination;
	}
	
	@Override
	public void execute(Thread thread, Object object, String clazz, int method, long invoke, int nodeId) {
		// Get current thread stack
		Deque<Program> stack = stacks.get(thread);
		if(stack == null) {
			// Thread stack not found... lets create a new one
			stacks.put(thread, stack = new LinkedList<Program>());
			stack.push(SENTINELLA);
		}
		
		Program p = stack.getFirst();
		
		// Means that the first node
		if(invoke > p.getId()) {
			
			p = programs.get(clazz, method);
			if (p == null) {
				DFGraph graph;
				Requirement[] requirement;
							
				graph = cache.get(clazz, method);
				requirement = requirements.get(clazz, method);
				if (requirement == null) {
					try {
						requirement = determination.requirement(graph);
					} catch (RequirementDeterminationException e) {
						logger.error("Error getting requirements for class: %s and method: %d",
								e, clazz, method);
						System.exit(1);
					}
					requirements.put(clazz, method, requirement);
				}
				
				p = new Program(-1, graph);
				instrumentator.instrument(p, requirement);
				programs.put(clazz, method, p);
			}
			p = instrumentator.copy(p, invoke);
			stack.addFirst(p);
			
		} else {
			while(stack.getFirst().getId() > invoke)
				stack.removeFirst();
			p = stack.getFirst();
		}
		
		p.getExecutionEntryById(nodeId).traverse();
		
		if (p.isExit(nodeId))
			stack.removeFirst();
	}

	@Override
	public void exportRequirements(String outputFile) throws IOException {
		if (instrumentator instanceof BitwiseDuaCoverage
				|| instrumentator instanceof BitwiseDuaCoverage8
				|| instrumentator instanceof BitwiseDuaCoverage16) {
			// I need to export covered bit-set
			
			Map<String, ClassProgram> pMap = programs.getPrograms();
			for (String className : pMap.keySet()) {
				List<Program> programs = pMap.get(className).getClassPrograms();
				for (int i = 0; i < programs.size(); i++) {
					Program program = programs.get(i);
					if (program != null) {
						BitSet covered = (BitSet) program.ctx.get("covered");
						Dua[] duas = (Dua[]) requirements.get(className, i);
						
						for (int j = 0; j < duas.length; j++) {
							if(covered.get(j))
								duas[j].cover();
						}
					}
				}
			}
		}
		FileOutputStream fileOut = new FileOutputStream(outputFile);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(requirements);
		out.close();
		fileOut.close();
	}

}
