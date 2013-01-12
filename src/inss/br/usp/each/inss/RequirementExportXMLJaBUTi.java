package br.usp.each.inss;

import br.usp.each.opal.requirement.Requirement;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import br.usp.each.opal.requirement.RequirementType;
import java.util.List;

/**
 * @author Maruscia Baklizky
 */
public class RequirementExportXMLJaBUTi implements RequirementExport {

    private RequirementWrapper requirementWrapper;
    private String resource, cfg_option, type, base_class, classpath;
    private int test_set;
    private boolean mobility;

    public RequirementExportXMLJaBUTi(RequirementWrapper requirementWrapper) {
        this.requirementWrapper = requirementWrapper;
        this.resource = "default";
        this.cfg_option = "default";
        this.mobility = false;
        this.type = "default";
        this.base_class = "";
        this.classpath = "";
        this.test_set = 0;
    }

    public RequirementExportXMLJaBUTi resource(String resource) {
        this.resource = resource;
        return this;
    }

    public RequirementExportXMLJaBUTi cfg_option(String cfg_option) {
        this.cfg_option = cfg_option;
        return this;
    }

    public RequirementExportXMLJaBUTi mobility(boolean mobility) {
        this.mobility = mobility;
        return this;
    }

    public RequirementExportXMLJaBUTi type(String type) {
        this.type = type;
        return this;
    }

    public RequirementExportXMLJaBUTi base_class(String base_class) {
        this.base_class = base_class;
        return this;
    }

    public RequirementExportXMLJaBUTi classpath(String classpath) {
        this.classpath = classpath;
        return this;
    }

    public RequirementExportXMLJaBUTi setTestSet(int test_set) {
        this.test_set = test_set;
        return this;
    }

    @Override
    public byte[] export() throws IOException {
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("ISO-8859-1");
        doJbtReport(doc);
        return doc.asXML().getBytes();
    }

    private void doJbtReport(Document doc) {
        doc.addElement("jbtreport");
        doProject(doc.getRootElement());
        doClasses(doc.getRootElement());
        doMethods(doc.getRootElement());
        doTestSet(doc.getRootElement());
    }

    private void doProject(Element element) {
        Element elem = element.addElement("project");
        elem.addAttribute("cfg_option", cfg_option);
        elem.addAttribute("mobility", String.format("%b", mobility));
        elem.addAttribute("name", resource);
        elem.addAttribute("type", type);
        elem.addElement("base_class").addAttribute("name", base_class);
        elem.addElement("classpath").addAttribute("path", classpath);
        elem.addElement("avoided_packages");

        doCoverage(elem.addElement("coverage"), requirementWrapper);
    }

    private void doTestSet(Element element) {
        Element elem = element.addElement("test_set").addAttribute("total", String.format("%d", test_set));
        doCoverage(elem.addElement("coverage"), requirementWrapper);
        elem.addElement("test_cases");
    }

    private void doCoverage(Element element, Wrapper wrapper) {
        int totalCoveredNodesei = wrapper.getCoveredRequirements(RequirementType.NODE).size();
        int totalCoveredEdgesei = wrapper.getCoveredRequirements(RequirementType.EDGE).size();
        int totalCoveredUsesei = wrapper.getCoveredRequirements(RequirementType.DUA).size();

        int totalCheckedNodesei = wrapper.getRequirements(RequirementType.NODE).size();
        int totalCheckedEdgesei = wrapper.getRequirements(RequirementType.EDGE).size();
        int totalCheckedUsesei = wrapper.getRequirements(RequirementType.DUA).size();

        float allNodesei = totalCheckedNodesei > 0 ? (float) totalCoveredNodesei / totalCheckedNodesei : 0.0f;
        float allEdgesei = totalCheckedEdgesei > 0 ? (float) totalCoveredEdgesei / totalCheckedEdgesei : 0.0f;
        float allUsesei = totalCheckedUsesei > 0 ? (float) totalCoveredUsesei / totalCheckedUsesei : 0.0f;

        doTypeCoverage(element, "All-Nodes-ei", totalCoveredNodesei, allNodesei, totalCheckedNodesei);
        doTypeCoverage(element, "All-Nodes-ed", 0, 0.0f, 0);
        doTypeCoverage(element, "All-Edges-ei", totalCoveredEdgesei, allEdgesei, totalCheckedEdgesei);
        doTypeCoverage(element, "All-Edges-ed", 0, 0.0f, 0);
        doTypeCoverage(element, "All-Uses-ei", totalCoveredUsesei, allUsesei, totalCheckedUsesei);
        doTypeCoverage(element, "All-Uses-ed", 0, 0.0f, 0);
        doTypeCoverage(element, "All-Pot-Uses-ei", 0, 0.0f, 0);
        doTypeCoverage(element, "All-Pot-Uses-ed", 0, 0.0f, 0);
    }

    private void doTypeCoverage(Element element, String type, int covered, float percentage, int required) {
        Element elem = element.addElement(type);
        elem.addAttribute("covered", String.format("%d", covered));
        elem.addAttribute("percentage", String.format("%f", percentage));
        elem.addAttribute("required", String.format("%d", required));
    }

    private void doClasses(Element element) {
        Element elem = element.addElement("classes");

        for (ClassRequirementWrapper classRequirementWrapper : requirementWrapper.getClasses()) {

            Element e = elem.addElement("class");
            e.addAttribute("name", classRequirementWrapper.getClassName());
            e.addElement("extends").addAttribute("level", "1").addAttribute("name", "java.lang.Object");
            e.addElement("implements");
            e.addElement("source").addAttribute("name", "");

            doCoverage(e.addElement("coverage"), classRequirementWrapper);

        }
    }

    private void doMethods(Element element) {
        Element elem = element.addElement("methods");

        for (ClassRequirementWrapper classRequirementWrapper : requirementWrapper.getClasses()) {

            for (MethodRequirementWrapper methodRequirementWrapper : classRequirementWrapper.getMethods()) {

                Element e = elem.addElement("method");
                e.addAttribute("class_name", classRequirementWrapper.getClassName());
                e.addAttribute("full_name", classRequirementWrapper.getClassName().concat("." + String.format("%d", methodRequirementWrapper.getMethodId())));
                e.addAttribute("name", String.format("%d", methodRequirementWrapper.getMethodId()));

                doCoverage(e.addElement("coverage"), methodRequirementWrapper);
                doRequirements(e.addElement("requirements"), methodRequirementWrapper);
                e.addElement("infeasible");
                e.addElement("inactive");
            }
        }
    }

    private void doRequirements(Element element, Wrapper wrapper) {
        doTypeRequirements(element, "All-Nodes-ei", wrapper.getCoveredRequirements(RequirementType.NODE), wrapper.getUncoveredRequirements(RequirementType.NODE));
        doTypeRequirements(element, "All-Edges-ei", wrapper.getCoveredRequirements(RequirementType.EDGE), wrapper.getUncoveredRequirements(RequirementType.EDGE));
        doTypeRequirements(element, "All-Uses-ei", wrapper.getCoveredRequirements(RequirementType.DUA), wrapper.getUncoveredRequirements(RequirementType.DUA));
    }

    private void doTypeRequirements(Element element, String type, List<Requirement> coveredRequirements, List<Requirement> uncoveredRequirements) {
        Element elem = element.addElement(type).addElement("uncovered");
        for (Requirement r : uncoveredRequirements) {
            doRequirement(elem, r);
        }
        elem = element.addElement(type).addElement("covered");
        for (Requirement r : coveredRequirements) {
            doRequirement(elem, r);
        }
    }

    private void doRequirement(Element element, Requirement r) {
        element.addElement("requirement").addAttribute("weight", "0").addText((r.toString()));
    }
}
