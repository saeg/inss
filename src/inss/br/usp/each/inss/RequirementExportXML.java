package br.usp.each.inss;

import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import br.usp.each.opal.requirement.RequirementType;

/**
 * @author Maruscia Baklizky
 */
public class RequirementExportXML implements RequirementExport {

    private RequirementWrapper requirementWrapper;
    
    private String resource;

    public RequirementExportXML(RequirementWrapper requirementWrapper) {
        this.requirementWrapper = requirementWrapper;
        this.resource = "default";
    }

    public RequirementExportXML resource(String resource) {
        this.resource = resource;
        return this;
    }

    @Override
    public byte[] export() throws IOException {
        int totalCoveredNodesei = requirementWrapper.getCoveredRequirements(RequirementType.NODE).size();
        int totalCoveredEdgesei = requirementWrapper.getCoveredRequirements(RequirementType.EDGE).size();
        int totalCoveredUsesei = requirementWrapper.getCoveredRequirements(RequirementType.DUA).size();

        int totalCheckedNodesei = requirementWrapper.getRequirements(RequirementType.NODE).size();
        int totalCheckedEdgesei = requirementWrapper.getRequirements(RequirementType.EDGE).size();
        int totalCheckedUsesei = requirementWrapper.getRequirements(RequirementType.DUA).size();

        float allNodesei = totalCoveredNodesei > 0 ? (float) totalCoveredNodesei / totalCheckedNodesei : 0.0f;
        float allEdgesei = totalCoveredEdgesei > 0 ? (float) totalCoveredEdgesei / totalCheckedEdgesei : 0.0f;
        float allUsesei = totalCoveredUsesei > 0 ? (float) totalCoveredUsesei / totalCheckedUsesei : 0.0f;


        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("ISO-8859-1");
        doc.addElement("GenericItems");

        doGenericItem(doc, "All-Nodes-ei", allNodesei);
        doGenericItem(doc, "All-Nodes-ed", 0.0f);
        doGenericItem(doc, "All-Edges-ei", allEdgesei);
        doGenericItem(doc, "All-Edges-ed", 0.0f);
        doGenericItem(doc, "All-Uses-ei", allUsesei);
        doGenericItem(doc, "All-Uses-ed", 0.0f);
        doGenericItem(doc, "All-Pot-Uses-ei", 0.0f);
        doGenericItem(doc, "All-Pot-Uses-ed", 0.0f);

        return doc.asXML().getBytes();
    }

    private void doGenericItem(Document doc, String metric, float value) {
        Element element = doc.addElement("GenericItem");
        element.addElement("resource").addText(resource);
        element.addElement("metric").addText(metric);
        element.addElement("value").addText(String.format("%f", value));
    }
}
