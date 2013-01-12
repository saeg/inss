package br.usp.each.inss;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.usp.each.inss.cache.Requirements;
import br.usp.each.inss.cache.Requirements.ClassRequirement;
import br.usp.each.opal.requirement.Requirement;
import br.usp.each.opal.requirement.RequirementType;

/**
 * @author Maruscia Baklizky
 */
public class RequirementWrapper extends Wrapper {

    /**
     * Associates each class name to a ClassRequirementWrapper
     */
    private Map<String, ClassRequirementWrapper> classRequirementWrapperMap;

    /**
     * Default constructor
     */
    public RequirementWrapper() {
        classRequirementWrapperMap = new HashMap<String, ClassRequirementWrapper>();
    }

    /**
     * Try to get a ClassRequirementWrapper for className. If the association
     * does not exists create a new one with a new ClassRequirementWrapper
     * 
     * @param className
     *            the class name for ClassRequirementWrapper
     * @return ClassRequirementWrapper for className
     */
    public ClassRequirementWrapper addOrGetClassWrapperByName(String className) {
        ClassRequirementWrapper classWrapper = classRequirementWrapperMap.get(className);
        if (classWrapper == null) {
            classWrapper = new ClassRequirementWrapper(className);
            classRequirementWrapperMap.put(className, classWrapper);
        }
        return classWrapper;
    }

    /**
     * This method returns a Collections with all ClassRequirementWrapper holds
     * by this object
     * 
     * @return A Collection of ClassRequirementWrapper
     */
    public Collection<ClassRequirementWrapper> getClasses() {
        return classRequirementWrapperMap.values();
    }

    @Override
    public List<Requirement> getRequirements(RequirementType type) {
        List<Requirement> requirements = new ArrayList<Requirement>();
        for (ClassRequirementWrapper classWrapper : getClasses()) {
            requirements.addAll(classWrapper.getRequirements(type));
        }
        return requirements;
    }

    @Override
    public List<Requirement> getCoveredRequirements(RequirementType type) {
        List<Requirement> covered = new ArrayList<Requirement>();
        for (ClassRequirementWrapper classdWrapper : getClasses()) {
            covered.addAll(classdWrapper.getCoveredRequirements(type));
        }
        return covered;
    }

    @Override
    public List<Requirement> getUncoveredRequirements(RequirementType type) {
        List<Requirement> uncovered = new ArrayList<Requirement>();
        for (ClassRequirementWrapper classdWrapper : getClasses()) {
            uncovered.addAll(classdWrapper.getUncoveredRequirements(type));
        }
        return uncovered;
    }

    public static void load(Requirements requirements, RequirementWrapper wrapper) {
        for (Entry<String, ClassRequirement> entry : requirements.getRequirements().entrySet()) {
            String className = entry.getKey();
            int methodId = 0;
            for (Requirement[] methodRequirements : entry.getValue().getClassRequirements()) {
                MethodRequirementWrapper methodWrapper;
                methodWrapper = wrapper.addOrGetClassWrapperByName(className).addOrGetMethodWrapperById(methodId);
                if (methodRequirements != null) {
                    methodWrapper.addRequirements(methodRequirements);
                }
                methodId++;
            }
        }
    }
}
