package br.usp.each.inss;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.usp.each.opal.requirement.Requirement;
import br.usp.each.opal.requirement.RequirementType;

/**
 * @author Maruscia Baklizky
 */
public class ClassRequirementWrapper extends Wrapper {
	
	/**
	 * Class name that this object is holding
	 */
	private String className;
	
	/**
	 * Associates each method id (Integer value) to a MethodRequirementWrapper
	 */
	private Map<Integer, MethodRequirementWrapper> methodRequirementWrapperMap;
	
	/**
	 * Creates a new ClassRequirementWrapper for className
	 * 
	 * @param className
	 *            class name for ClassRequirementWrapper
	 */
	public ClassRequirementWrapper(String className) {
		methodRequirementWrapperMap = new HashMap<Integer, MethodRequirementWrapper>();
		this.className = className;
	}
	
	/**
	 * Class name that this object is holding
	 * 
	 * @return class name
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * Try to get a MethodRequirementWrapper for methodId. If the association
	 * does not exists create a new one with a new MethodRequirementWrapper
	 * 
	 * @param methodId
	 *            the method id (Integer value) for MethodRequirementWrapper
	 * @return MethodRequirementWrapper for method id
	 */
	public MethodRequirementWrapper addOrGetMethodWrapperById(int methodId) {
		MethodRequirementWrapper methodWrapper = methodRequirementWrapperMap.get(methodId);
		if (methodWrapper == null) {
			methodWrapper = new MethodRequirementWrapper(methodId);
			methodRequirementWrapperMap.put(methodId, methodWrapper);
		}
		return methodWrapper;
	}
	
	/**
	 * This method returns a Collections with all MethodRequirementWrapper holds
	 * by this object
	 * 
	 * @return A Collection of MethodRequirementWrapper
	 */
	public Collection<MethodRequirementWrapper> getMethods() {
		return methodRequirementWrapperMap.values();
	}

    @Override
	public List<Requirement> getRequirements(RequirementType type) {
		List<Requirement> requirements = new ArrayList<Requirement>();
		for (MethodRequirementWrapper methodWrapper : getMethods()) {
			requirements.addAll(methodWrapper.getRequirements(type));
		}
		return requirements;
	}
	
    @Override
	public List<Requirement> getCoveredRequirements(RequirementType type) {
		List<Requirement> covered = new ArrayList<Requirement>();
		for (MethodRequirementWrapper methodWrapper : getMethods()) {
			covered.addAll(methodWrapper.getCoveredRequirements(type));
		}
		return covered;
	}
    
    @Override
    public List<Requirement> getUncoveredRequirements(RequirementType type) {
		List<Requirement> uncovered = new ArrayList<Requirement>();
		for (MethodRequirementWrapper methodWrapper : getMethods()) {
			uncovered.addAll(methodWrapper.getUncoveredRequirements(type));
		}
		return uncovered;
	}

}
