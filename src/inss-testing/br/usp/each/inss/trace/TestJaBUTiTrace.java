package br.usp.each.inss.trace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import br.usp.each.inss.trace.JaBUTiTrace.JaBUTiTraceLine;

public class TestJaBUTiTrace {

	private JaBUTiTrace trace;

	@BeforeClass
	public void before() throws FileNotFoundException {
		File traceFile = new File("projects/inss-testing/data/org.apache.log4j.Level.trc");
		InputStream is = new FileInputStream(traceFile);
		trace = new JaBUTiTrace(is);
	}

	@Test
	public void testNext() {
		// 1
		Assert.assertTrue(trace.hasNext());
		JaBUTiTraceLine line = trace.next();
		Assert.assertEquals(line.getTestName(), "");
		Assert.assertEquals(line.getThreadName(), "Thread[main,5,main]");
		Assert.assertEquals(line.getObjectId(),	"org.apache.log4j.Level25062038");
		Assert.assertEquals(line.getClassName(), "org.apache.log4j.Level");
		Assert.assertEquals(line.getMethodId(), 0);
		Assert.assertEquals(line.getInvokeId(), 0);
		Assert.assertEquals(line.getNodeId(), 0);

		// 2
		Assert.assertTrue(trace.hasNext());
		line = trace.next();
		Assert.assertEquals(line.getTestName(), "");
		Assert.assertEquals(line.getThreadName(), "Thread[main,5,main]");
		Assert.assertEquals(line.getObjectId(),	"org.apache.log4j.Level25062038");
		Assert.assertEquals(line.getClassName(), "org.apache.log4j.Level");
		Assert.assertEquals(line.getMethodId(), 0);
		Assert.assertEquals(line.getInvokeId(), 0);
		Assert.assertEquals(line.getNodeId(), 7);
		
		// 3 - 26
		for (int i = 0; i < 24; i++) {
			Assert.assertTrue(trace.hasNext());
			trace.next();
		}
		
		// 27
		Assert.assertTrue(trace.hasNext());
		line = trace.next();
		Assert.assertEquals(line.getTestName(), "");
		Assert.assertEquals(line.getThreadName(), "Thread[main,5,main]");
		Assert.assertEquals(line.getObjectId(), "STATIC");
		Assert.assertEquals(line.getClassName(), "org.apache.log4j.Level");
		Assert.assertEquals(line.getMethodId(), 2);
		Assert.assertEquals(line.getInvokeId(), 133);
		Assert.assertEquals(line.getNodeId(), 0);

		// Assert false, cause the unit test is not over. Not yet ;)
		Assert.assertFalse(trace.haveAnotherUnitTest());
		// See
		Assert.assertTrue(trace.hasNext());
		
		/* 
		 * if hasNext() returns true means that unit test is not over. 
		 * In this case haveAnotherUnitTest() returns false.
		 */
	}

	@Test(dependsOnMethods = "testNext")
	public void testEndUnitTest() {
		/* 
		 * if hasNext() returns false means that unit test ended.
		 * In this case haveAnotherUnitTest() maybe returns true or false.
		 */
		
		// End the unit test
		while (trace.hasNext()) {
			trace.next();
		}
		// have other?
		Assert.assertTrue(trace.haveAnotherUnitTest());
		// First line of other unit test
		JaBUTiTraceLine line = trace.next();
		Assert.assertEquals(line.getTestName(), "");
		Assert.assertEquals(line.getThreadName(), "Thread[main,5,main]");
		Assert.assertEquals(line.getObjectId(),	"org.apache.log4j.Level30817849");
		Assert.assertEquals(line.getClassName(), "org.apache.log4j.Level");
		Assert.assertEquals(line.getMethodId(), 0);
		Assert.assertEquals(line.getInvokeId(), 0);
		Assert.assertEquals(line.getNodeId(), 0);
	}

	@Test(dependsOnMethods = "testEndUnitTest")
	public void testEndOfTestCase() {
		// while have tests
		do {
			// End the unit test
			while (trace.hasNext()) {
				trace.next();
			}
		} while (trace.haveAnotherUnitTest());
		Assert.assertFalse(trace.hasNext());
		Assert.assertFalse(trace.haveAnotherUnitTest());
		
		/* 
		 * if hasNext() returns false and haveAnotherUnitTest() too: case test is ended.
		 */
	}
}
