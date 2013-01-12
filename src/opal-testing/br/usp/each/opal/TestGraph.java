package br.usp.each.opal;

import java.util.Arrays;
import java.util.Collections;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public abstract class TestGraph {

	public abstract Graph<Integer> instance();

	private Graph<Integer> graph;

	@BeforeClass
	public void before() {
		graph = instance();
	}

	@Test
	public void testAddNode() {
		// True
		Assert.assertTrue(graph.add(0));
		Assert.assertTrue(graph.add(1));
		Assert.assertTrue(graph.add(2));
		Assert.assertTrue(graph.add(3));
		Assert.assertTrue(graph.add(4));
		Assert.assertTrue(graph.add(5));
		// False (Duplicated)
		Assert.assertFalse(graph.add(0));
		Assert.assertFalse(graph.add(1));
		Assert.assertFalse(graph.add(2));
		Assert.assertFalse(graph.add(3));
		Assert.assertFalse(graph.add(4));
		Assert.assertFalse(graph.add(5));
		
		Assert.assertEquals(graph.size(), 6);
	}

	@Test(dependsOnMethods = "testAddNode")
	public void testAddEdge() {
		// True
		Assert.assertTrue(graph.addEdge(0, 1));
		Assert.assertTrue(graph.addEdge(1, 2));
		Assert.assertTrue(graph.addEdge(1, 3));
		Assert.assertTrue(graph.addEdge(2, 3));
		Assert.assertTrue(graph.addEdge(3, 2));
		// False (Duplicated)
		Assert.assertFalse(graph.addEdge(0, 1));
		Assert.assertFalse(graph.addEdge(1, 2));
		Assert.assertFalse(graph.addEdge(1, 3));
		Assert.assertFalse(graph.addEdge(2, 3));
		Assert.assertFalse(graph.addEdge(3, 2));
		// False (one node does not exists)
		Assert.assertFalse(graph.addEdge(640, 1));
		// False (another node does not exists)
		Assert.assertFalse(graph.addEdge(1, 617));
		// False (node does not exists)
		Assert.assertFalse(graph.addEdge(374, 853));

		// Adjacent tests
		Assert.assertTrue(graph.adjacent(0, 1));
		Assert.assertTrue(graph.adjacent(1, 2));
		Assert.assertTrue(graph.adjacent(1, 3));
		Assert.assertTrue(graph.adjacent(2, 3));
		Assert.assertTrue(graph.adjacent(3, 2));
		// False (one node does not exists)
		Assert.assertFalse(graph.adjacent(640, 1));
		// False (another node does not exists)
		Assert.assertFalse(graph.adjacent(1, 617));
		// False (node does not exists)
		Assert.assertFalse(graph.adjacent(374, 853));
		// False (no edge)
		Assert.assertFalse(graph.adjacent(4, 5));
	}

	@Test(dependsOnMethods = "testAddEdge")
	public void testRemoveEdge() {
		// True
		Assert.assertTrue(graph.removeEdge(2, 3));
		// False (one node does not exists)
		Assert.assertFalse(graph.removeEdge(640, 1));
		// False (another node does not exists)
		Assert.assertFalse(graph.removeEdge(1, 617));
		// False (node does not exists)
		Assert.assertFalse(graph.removeEdge(374, 853));
		// False (no edge)
		Assert.assertFalse(graph.removeEdge(2, 3));
		Assert.assertFalse(graph.removeEdge(4, 5));
	}

	@Test(dependsOnMethods = "testRemoveEdge")
	public void testNeighbors() {
		Assert.assertEquals(graph.neighbors(0), Arrays.asList(1));
		Assert.assertEquals(graph.neighbors(1), Arrays.asList(2, 3));
		Assert.assertEquals(graph.neighbors(2), Collections.EMPTY_SET);
		Assert.assertEquals(graph.neighbors(3), Arrays.asList(2));
		Assert.assertEquals(graph.neighbors(4), Collections.EMPTY_SET);
		Assert.assertEquals(graph.neighbors(5), Collections.EMPTY_SET);
		Assert.assertNull(graph.neighbors(7263));
	}
	
	@Test(dependsOnMethods = "testRemoveEdge")
	public void testInverse() {
		Graph<Integer> inverse = graph.inverse();
		Assert.assertEquals(inverse.size(), graph.size());
		Assert.assertTrue(inverse.adjacent(1, 0));
		Assert.assertTrue(inverse.adjacent(2, 1));
		Assert.assertTrue(inverse.adjacent(3, 1));
		Assert.assertTrue(inverse.adjacent(2, 3));		
	}

	@Test(dependsOnMethods = "testAddNode")
	public void testIterable() {
		Integer i = 0;
		for (Integer node : graph) {
			Assert.assertEquals(i, node);
			i++;
		}
	}
	
	@Test(dependsOnMethods = "testAddNode")
	public void testGetNode() {
		// Not Null, Exists
		Assert.assertNotNull(graph.getNode(0));
		Assert.assertNotNull(graph.getNode(1));
		Assert.assertNotNull(graph.getNode(2));
		Assert.assertNotNull(graph.getNode(3));
		Assert.assertNotNull(graph.getNode(4));
		Assert.assertNotNull(graph.getNode(5));
		// Null, not exists
		Assert.assertNull(graph.getNode(11656));
		Assert.assertNull(graph.getNode(13962));
	}
	
	@Test(dependsOnMethods = "testIterable")
	public void testAddNode2() {
		// Not Null
		Node<Integer> node = graph.addNode(6);
		Assert.assertNotNull(node);
		Assert.assertEquals(node.getId(), new Integer(6));
		Assert.assertEquals(node.getId(), graph.getNode(6).getId());
		node = graph.addNode(7);
		Assert.assertNotNull(node);
		Assert.assertEquals(node.getId(), new Integer(7));
		Assert.assertEquals(node.getId(), graph.getNode(7).getId());
			
		// Null, duplicated
		node = graph.addNode(0);
		Assert.assertNull(node);
		node = graph.addNode(1);
		Assert.assertNull(node);
		node = graph.addNode(6);
		Assert.assertNull(node);
		
		// DSL
		node = graph.addNode(8).addNode(9);
		Assert.assertNotNull(node);
		Assert.assertEquals(node.getId(), new Integer(9));
		node = node.getNode(0).addNode(9);
		Assert.assertNull(node);
		Assert.assertNull(graph.getNode(0).getNode(10));
		
		//NullPointerException duplicated node
		try {
			graph.addNode(7);
		} catch (NullPointerException e) {
			Assert.assertNotNull(e);
		}		
	}
	
	@Test(dependsOnMethods = "testAddNode2")
	public void testNodeAddEdge() {
		// True
		Assert.assertTrue(graph.getNode(6).createEdgeTo(7));
		Assert.assertTrue(graph.getNode(7).createEdgeTo(6));
		Assert.assertTrue(graph.getNode(7).createEdgeTo(7));
		
		//False (node does not exists)
		Assert.assertFalse(graph.getNode(6).createEdgeTo(13952));
		
		// False (duplicated)
		Assert.assertFalse(graph.getNode(6).createEdgeTo(7));
		Assert.assertFalse(graph.getNode(7).createEdgeTo(6));
		Assert.assertFalse(graph.getNode(7).createEdgeTo(7));
		
		// Adjacent tests
		Assert.assertTrue(graph.getNode(6).adjacent(7));
		Assert.assertTrue(graph.getNode(7).adjacent(7));
		Assert.assertTrue(graph.getNode(7).adjacent(6));
		// False (one node does not exists)
		Assert.assertFalse(graph.getNode(6).adjacent(67914));
		// False (no edge)
		Assert.assertFalse(graph.getNode(7).adjacent(0));
	}
	
	@Test(dependsOnMethods = "testNodeAddEdge")
	public void testNodeRemoveEdge() {
		// True
		Assert.assertTrue(graph.getNode(6).removeEdgeTo(7));
		// False (one node does not exists)
		Assert.assertFalse(graph.getNode(7).removeEdgeTo(767213));
		// False (no edge)
		Assert.assertFalse(graph.removeEdge(6, 7));
		Assert.assertFalse(graph.removeEdge(7, 1));
	}
	
	@Test(dependsOnMethods = "testRemoveEdge")
	public void testNodeNeighbors() {
		Assert.assertEquals(graph.getNode(0).neighbors(), Arrays.asList(1));
		Assert.assertEquals(graph.getNode(1).neighbors(), Arrays.asList(2, 3));
		Assert.assertEquals(graph.getNode(2).neighbors(), Collections.EMPTY_SET);
		Assert.assertEquals(graph.getNode(3).neighbors(), Arrays.asList(2));
		Assert.assertEquals(graph.getNode(4).neighbors(), Collections.EMPTY_SET);
		Assert.assertEquals(graph.getNode(4).neighbors(), Collections.EMPTY_SET);
	}

}
