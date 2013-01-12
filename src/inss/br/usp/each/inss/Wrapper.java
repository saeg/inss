package br.usp.each.inss;

import java.util.List;

import br.usp.each.opal.requirement.Requirement;
import br.usp.each.opal.requirement.RequirementType;
import java.util.ArrayList;

/**
 * @author Maruscia Baklizky
 */
public abstract class Wrapper {
    
    public abstract List<Requirement> getCoveredRequirements(RequirementType type);
    
    public abstract List<Requirement> getUncoveredRequirements(RequirementType type);
    
    public abstract List<Requirement> getRequirements(RequirementType type);
    
    public List<Requirement> getAllRequirements() {
		List<Requirement> requirements = new ArrayList<Requirement>();
		requirements.addAll(getRequirements(RequirementType.NODE));
		requirements.addAll(getRequirements(RequirementType.EDGE));
		requirements.addAll(getRequirements(RequirementType.DUA));
		return requirements;
	}
    
}
