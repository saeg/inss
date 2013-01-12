package br.usp.each.opal;

import org.testng.annotations.Test;

@Test
public class TestGraphMap extends TestGraph {

	@Override
	public Graph<Integer> instance() {
		return new GraphMap<Integer>();
	}	

}
