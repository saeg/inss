package br.usp.each.inss;

import java.io.IOException;

import junit.framework.Assert;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import br.usp.each.inss.Program.ExecutionEntry;
import br.usp.each.inss.instrumentation.probe.Probe;
import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.gxl.GXLDefUseReader;

public class TestProgram {

	private Program p;

	@BeforeClass
	public void before() throws IOException, SAXException {
		String dir = "projects/inss-testing/data/program";
		String defUseFile = "defuse_foo.bar.Arrays_1_max(int[]).gxl";
		String methodName = "max";
		GXLDefUseReader defUseReader = new GXLDefUseReader(dir, defUseFile, methodName);
		DFGraph dfGraph = defUseReader.getDFGraph("foo.bar.Arrays", 1);
		p = new Program(0, dfGraph);
	}

	@Test
	public void testProgram() {
		Assert.assertEquals(6, p.size());
		ExecutionEntry e;
		e = p.getExecutionEntry(0);
		Assert.assertEquals(0, e.getProgramBlock().getId());
		Assert.assertEquals(p.getExecutionEntryById(0), e);
		
		e = p.getExecutionEntry(1);
		Assert.assertEquals(26, e.getProgramBlock().getId());
		Assert.assertEquals(p.getExecutionEntryById(26), e);
		
		e = p.getExecutionEntry(2);
		Assert.assertEquals(32, e.getProgramBlock().getId());
		Assert.assertEquals(p.getExecutionEntryById(32), e);
		
		e = p.getExecutionEntryById(12);
		Assert.assertEquals(12, e.getProgramBlock().getId());
		Assert.assertEquals(p.getExecutionEntry(3), e);
		
		e = p.getExecutionEntryById(19);
		Assert.assertEquals(19, e.getProgramBlock().getId());
		Assert.assertEquals(p.getExecutionEntry(4), e);
		
		e = p.getExecutionEntryById(23);
		Assert.assertEquals(23, e.getProgramBlock().getId());
		Assert.assertEquals(p.getExecutionEntry(5), e);
		
		Assert.assertTrue(p.isEntrance(0));
		Assert.assertTrue(p.isExit(32));
	}
	
	@Test
	public void testInvalidExecutionEntry() {
		ExecutionEntry e = p.getExecutionEntryById(947902788);
		Assert.assertNull(e);
	}
	
	@Test
	public void testProbes() {
		ExecutionEntry e = p.getExecutionEntryById(0);
		Assert.assertEquals(0, e.getProgramBlock().getId());

		class Box {
			private int i = 0;
		}
		final Box box = new Box();
		Assert.assertEquals(0, box.i);

		e.traverse();
		Assert.assertEquals(0, box.i);

		e.addFirst(new Probe() {
			@Override
			public void execute() {
				box.i++;
			}
		});
		e.traverse();
		Assert.assertEquals(1, box.i);

		e.addLast(new Probe() {
			@Override
			public void execute() {
				box.i *= 10;
			}
		});
		e.traverse();
		Assert.assertEquals(20, box.i);

		p.clear();
		e.traverse();
		Assert.assertEquals(20, box.i);
	}
}
