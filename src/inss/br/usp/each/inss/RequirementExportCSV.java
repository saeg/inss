package br.usp.each.inss;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import br.usp.each.opal.requirement.RequirementType;

import com.generationjava.io.CsvWriter;

/**
 * @author Maruscia Baklizky
 */
public class RequirementExportCSV implements RequirementExport {
	
	private RequirementWrapper requirementWrapper;

	public RequirementExportCSV(RequirementWrapper requirementWrapper) {
		this.requirementWrapper = requirementWrapper;
	}

	@Override
	public byte[] export() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CsvWriter csv = new CsvWriter(new OutputStreamWriter(baos));
		csv.setFieldDelimiter(';');
		csv.setBlockDelimiter('\n');
		doHeader(csv);
		doRequirements(csv, RequirementType.NODE);
		doRequirements(csv, RequirementType.EDGE);
		doRequirements(csv, RequirementType.DUA);
		csv.close();
		return baos.toByteArray();
	}
	
	private void doHeader(CsvWriter csv) throws IOException {
		int maxNumberOfMethodsPerClass = 0;
		for (ClassRequirementWrapper classWrapper : requirementWrapper.getClasses()) {
			if (classWrapper.getMethods().size() > maxNumberOfMethodsPerClass) {
				maxNumberOfMethodsPerClass = classWrapper.getMethods().size();
			}
		}
		csv.writeField("Classes/Methods");
		csv.writeField("Subtotal");
		csv.writeField("");
		for (int i = 0; i < maxNumberOfMethodsPerClass; i++) {
			csv.writeField(String.valueOf(i));
			csv.writeField("");
		}
		csv.endBlock();
	}


	private void doRequirements(CsvWriter csv, RequirementType type) throws IOException {
		csv.writeField(type.name());
		csv.endBlock();
		// For each class... do class statistics...
		for (ClassRequirementWrapper classWrapper : requirementWrapper.getClasses()) {
			csv.writeField(classWrapper.getClassName());
			int classCovered = classWrapper.getCoveredRequirements(type).size();
			int classTotal = classWrapper.getRequirements(type).size();
			float calssPercent = classTotal > 0 ? (float) classCovered / classTotal : 0.0f;
			csv.writeField(String.format("%d/%d", classCovered, classTotal));
			csv.writeField(String.format("%f", calssPercent));
			// For each method... do method statistics...
			for (MethodRequirementWrapper methodWrapper : classWrapper.getMethods()) {
				int covered = methodWrapper.getCoveredRequirements(type).size();
				int total = methodWrapper.getRequirements(type).size();
				float percent = total > 0 ? (float) covered / total : 0.0f;
				csv.writeField(String.format("%d/%d", covered, total));
				csv.writeField(String.format("%f", percent));
			}
			csv.endBlock();
		}
		csv.writeField("TOTAL");
		int statsCovered = requirementWrapper.getCoveredRequirements(type).size();
		int statsTotal = requirementWrapper.getRequirements(type).size();
		float statsPercent = statsTotal > 0 ? (float) statsCovered / statsTotal : 0.0f;
		csv.writeField(String.format("%d/%d", statsCovered, statsTotal));
		csv.writeField(String.format("%f", statsPercent));
		csv.endBlock();
	}

}
