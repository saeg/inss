package br.usp.each.opal.dataflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.usp.each.opal.Graph;
import br.usp.each.opal.GraphMap;

public class DFGraph extends GraphMap<ProgramBlock> {
	
	private ProgramBlock entranceBlock;
	private List<ProgramBlock> exitBlock;
	
	private Map<String,Var> varNameMap;
	private Map<Integer,Var> varIdMap;
	private Map<Integer, ProgramBlock> nodeMap;
	private List<Var> varList;
	private List<ProgramBlock> nodeList;
	private String className;
	private int methodId;
	
	public DFGraph(String className, int methodId) {
		varNameMap = new HashMap<String, Var>();
		varIdMap = new HashMap<Integer,Var>();
		nodeMap = new HashMap<Integer, ProgramBlock>();
		varList = new ArrayList<Var>();
		nodeList = new ArrayList<ProgramBlock>();
		exitBlock = new ArrayList<ProgramBlock>();
		this.className = className;
		this.methodId = methodId;
	}
			
	@Override
	public boolean add(ProgramBlock n) {
		if(nodeMap.containsKey(n.getId())) {
			return false;
		} else {
			nodeMap.put(n.getId(), n);
			nodeList.add(n);
			entranceBlock = null;
			exitBlock.clear();
			return super.add(n);
		}	
	}
	
	@Override
	public boolean addEdge(ProgramBlock from, ProgramBlock to) {
		entranceBlock = null;
		exitBlock.clear();
		return super.addEdge(from, to);
	}
			
	private void init() {
		for (ProgramBlock n : this) {
			if(neighbors(n).size() == 0) {
				exitBlock.add(n);
			}
		}
		Graph<ProgramBlock> inverse = inverse();
		for (ProgramBlock n : inverse) {
			if(inverse.neighbors(n).size() == 0) {
				entranceBlock = n;
				break;
			}
		}
	}
		
	public ProgramBlock entranceBlock() {
		if(entranceBlock == null)
			init();
		return entranceBlock;
	}
	
	public List<ProgramBlock> exitBlock() {
		if(exitBlock.isEmpty())
			init();
		return exitBlock;
	}
	
	public boolean addVar(String name, int id) {
		if(varNameMap.containsKey(name)) {
			return false;
		} else if (varIdMap.containsKey(id)) {
			return false;
		} else {
			Var var = new Var(id,name);
			varNameMap.put(name, var);
			varIdMap.put(id, var);
			varList.add(var);
			return true;
		}	
	}
	
	public int varSize() {
		return varList.size();
	}
	
	public Var getVarByName(String name) {
		return varNameMap.get(name);
	}
	
	public ProgramBlock getProgramBlockById(Integer id) {
		return nodeMap.get(id);
	}
	
	public Var getVar(int i) {
		return varList.get(i);
	}
	
	public ProgramBlock getProgramBlock(int i) {
		return nodeList.get(i);
	}
	
	public String getClassName() {
		return className;
	}
	
	public int getMethodId() {
		return methodId;
	}
		
	public static class Var {
		
		private int id;
		
		private String name;
		
		public Var(int id, String name) {
			this.id = id;
			this.name = name;
		}
		
		public int getId() {
			return id;
		}
		
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append('(');
			sb.append(name);
			sb.append(',');
			sb.append(id);
			sb.append(')');
			return sb.toString();
		}
	}
}
