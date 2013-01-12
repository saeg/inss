package br.usp.each.inss.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.usp.each.inss.Program;

public class Programs {

	private Map<String, ClassProgram> programs;

	public Programs() {
		programs = new HashMap<String, ClassProgram>();
	}
	
	public Program get(String className, int methodId) {
		ClassProgram classProgram = programs.get(className);
		if(classProgram != null) {
			return classProgram.get(methodId);
		}
		return null;
	}
	
	public void put(String className, int methodId, Program p) {
		ClassProgram classRequirement = programs.get(className);
		if(classRequirement == null) {
			classRequirement = new ClassProgram();
			programs.put(className, classRequirement);
		}
		classRequirement.put(methodId, p);
	}
	
	public Map<String, ClassProgram> getPrograms() {
		return programs;
	}

	public static class ClassProgram {

		private List<Program> classProgram;
		
		public ClassProgram() {
			classProgram = new ArrayList<Program>();
		}
		
		public Program get(int i) {
			if(i >= classProgram.size())
				return null;
			return classProgram.get(i);			
		}
		
		public void put(int i, Program program) {
			int j = i + 1;
			while(classProgram.size() < j)
				classProgram.add(null);
			classProgram.set(i, program);
		}
		
		public List<Program> getClassPrograms() {
			return classProgram;
		}
		
	}

}
