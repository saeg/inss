package br.usp.each.inss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.usp.each.opal.requirement.Dua;
import br.usp.each.opal.requirement.Edge;
import br.usp.each.opal.requirement.Node;
import br.usp.each.opal.requirement.Requirement;
import br.usp.each.opal.requirement.RequirementType;

/**
 * @author Maruscia Baklizky
 */
public class MethodRequirementWrapper extends Wrapper {
	
	/**
	 * Method id that this object is holding
	 */
	private int methodId;
	
	private Map<RequirementType, List<Requirement>> methodRequirements;

	/**
	 * Creates a new MethodRequirementWrapper for methodId
	 * 
	 * @param methodId
	 *            method id for MethodRequirementWrapper
	 */
	public MethodRequirementWrapper(int methodId) {
		this.methodId = methodId;
		this.methodRequirements = new HashMap<RequirementType, List<Requirement>>();
		this.methodRequirements.put(RequirementType.NODE, new ArrayList<Requirement>());
		this.methodRequirements.put(RequirementType.EDGE, new ArrayList<Requirement>());
		this.methodRequirements.put(RequirementType.DUA, new ArrayList<Requirement>());
	}
	
	/**
	 * Method id that this object is holding
	 * 
	 * @return method id
	 */
	public int getMethodId() {
		return methodId;
	}

	public void addRequirements(Requirement[] requirements) {
		for (Requirement requirement : requirements) {
			List<Requirement> currentList;
			if (requirement instanceof Node) {
				currentList = getRequirements(RequirementType.NODE);
			} else if (requirement instanceof Edge) {
				currentList = getRequirements(RequirementType.EDGE);
			} else if (requirement instanceof Dua) {
				currentList = getRequirements(RequirementType.DUA);
			} else {
				throw new IllegalArgumentException("Invalid requirement type:" + requirement.getClass());
			}
			currentList.add(requirement);
		}
	}

    @Override
	public List<Requirement> getRequirements(RequirementType type) {
		List<Requirement> currentList = methodRequirements.get(type);
		if (currentList == null) {
			throw new IllegalArgumentException("Invalid requirement type:" + type);
		}
		return currentList;
	}
	
    @Override
	public List<Requirement> getCoveredRequirements(RequirementType type) {
		List<Requirement> covered = new ArrayList<Requirement>();
		for (Requirement requirement : getRequirements(type)) {
			if (requirement.isCovered()) {
				covered.add(requirement);
			}
		}
		return covered;
	}
    
    @Override
    public List<Requirement> getUncoveredRequirements(RequirementType type) {
		List<Requirement> uncovered = new ArrayList<Requirement>();
		for (Requirement requirement : getRequirements(type)) {
			if (!requirement.isCovered()) {
				uncovered.add(requirement);
			}
		}
		return uncovered;
	}        

}
