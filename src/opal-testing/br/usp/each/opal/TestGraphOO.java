package br.usp.each.opal;

import org.testng.annotations.Test;

@Test
public class TestGraphOO extends TestGraph {

	@Override
	public Graph<Integer> instance() {
		return new GraphOO<Integer>();
	}	

}
