package br.usp.each.inss.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.usp.each.opal.requirement.Requirement;

public class Requirements implements Serializable {

	private static final long serialVersionUID = 4267968803632724835L;

	private Map<String, ClassRequirement> requirements;

	public Requirements() {
		requirements = new HashMap<String, ClassRequirement>();
	}
	
	public Requirement[] get(String className, int methodId) {
		ClassRequirement classRequirement = requirements.get(className);
		if(classRequirement != null) {
			return classRequirement.get(methodId);
		}
		return null;
	}
	
	public void put(String className, int methodId, Requirement[] r) {
		ClassRequirement classRequirement = requirements.get(className);
		if(classRequirement == null) {
			classRequirement = new ClassRequirement();
			requirements.put(className, classRequirement);
		}
		classRequirement.put(methodId, r);
	}
	
	public Map<String, ClassRequirement> getRequirements() {
		return requirements;
	}

	public static class ClassRequirement implements Serializable {

		private static final long serialVersionUID = 3545709222808543331L;

		private List<Requirement[]> classRequirements;
		
		public ClassRequirement() {
			classRequirements = new ArrayList<Requirement[]>();
		}
		
		public Requirement[] get(int i) {
			if(i >= classRequirements.size())
				return null;
			return classRequirements.get(i);			
		}
		
		public void put(int i, Requirement[] requirements) {
			int j = i + 1;
			while(classRequirements.size() < j)
				classRequirements.add(null);
			classRequirements.set(i, requirements);
		}
		
		public List<Requirement[]> getClassRequirements() {
			return classRequirements;
		}
	}

}
