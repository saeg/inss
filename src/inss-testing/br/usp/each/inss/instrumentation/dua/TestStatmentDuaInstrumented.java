package br.usp.each.inss.instrumentation.dua;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.usp.each.inss.instrumentation.Instrumentator;
import br.usp.each.inss.instrumentation.TestStatment;
import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.requirement.Dua;
import br.usp.each.opal.requirement.DuaDetermination;
import br.usp.each.opal.requirement.Use.Type;

public abstract class TestStatmentDuaInstrumented extends TestStatment {
	
	public TestStatmentDuaInstrumented(Instrumentator instrumentator, DFGraph statment) {
		super(instrumentator, new DuaDetermination(), statment);
	}

	@Test
	public void haveAllDuasNotCoveredWhenStart() {
		for (Dua dua : requirements()) {
			Assert.assertFalse(dua.isCovered());
		}
	}
	
	public Dua dua(int def, int use, int var) {
		for (Dua dua : requirements()) {
			if (dua.getUse().getType() == Type.P_USE)
				continue;
			
			if (dua.getDef() == def 
					&& dua.getUse().getUseNode() == use
					&& dua.getVariable() == var)
				return dua;
		}
		throw new RuntimeException("Dua not found");
	}

	public Dua dua(int def, int usea, int useb, int var) {
		for (Dua dua : requirements()) {
			if (dua.getUse().getType() == Type.C_USE)
				continue;

			if (dua.getDef() == def
					&& dua.getUse().PUse().getOriginNode() == usea
					&& dua.getUse().PUse().getDestNode() == useb
					&& dua.getVariable() == var)
				return dua;
		}
		throw new RuntimeException("Dua not found");
	}
	
	@Override
	public Dua[] requirements() {
		return (Dua[]) super.requirements();
	}

}
