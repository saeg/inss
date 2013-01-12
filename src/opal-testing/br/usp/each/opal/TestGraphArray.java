package br.usp.each.opal;

import org.testng.annotations.Test;

@Test
public class TestGraphArray extends TestGraph {

	@Override
	public Graph<Integer> instance() {
		return new GraphArray<Integer>();
	}	

}
