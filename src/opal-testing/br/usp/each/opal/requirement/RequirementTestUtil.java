package br.usp.each.opal.requirement;

import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.ProgramBlock;
import br.usp.each.opal.requirement.Use.Type;

public class RequirementTestUtil {
	
	public static final DFGraph IF_STATMENT = initIf();
	public static final DFGraph IF_ELSE_STATMENT = initIfElse();
	public static final DFGraph WHILE_STATMENT = initWhile();
	public static final DFGraph DO_WHILE_STATMENT = initDoWhile();
	public static final DFGraph NEXT_ODD_PRESENTATION = initNextOddPresentation();
	public static final DFGraph NEXT_ODD_PROGRAM = initNextOdd();
	public static final DFGraph MAX_PROGRAM = initMax();
	
	public static boolean findNode(ProgramBlock block, Node[] nodes) {
		for (Node node : nodes) {
			if (node.getNode() == block.getId())
				return true;
		}
		return false;
	}
	
	public static boolean findEdge(ProgramBlock block0, ProgramBlock block1, Edge[] edges) {
		boolean found = false;
		for (Edge edge : edges) {
			if (edge.getFrom() == block0.getId()
					&& edge.getTo() == block1.getId()) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	public static boolean findDua(ProgramBlock def, ProgramBlock use, int var, Dua[] duas) {
		boolean found = false;
		for (Dua dua : duas) {
			if (dua.getUse().getType() == Type.P_USE)
				continue;

			if (dua.getDef() == def.getId()
					&& dua.getUse().getUseNode() == use.getId()
					&& dua.getVariable() == var) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	public static boolean findDua(ProgramBlock def, ProgramBlock usea, ProgramBlock useb, int var, Dua[] duas) {
		boolean found = false;
		for (Dua dua : duas) {
			if (dua.getUse().getType() == Type.C_USE)
				continue;

			PUse use = dua.getUse().PUse();
			if (dua.getDef() == def.getId()
					&& use.getOriginNode() == usea.getId()
					&& use.getDestNode() == useb.getId()
					&& dua.getVariable() == var) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	private static DFGraph initIf() {
		DFGraph ifStatement = new DFGraph("if", 0);
		ProgramBlock block0 = new ProgramBlock(0);
		ProgramBlock block1 = new ProgramBlock(1);
		ProgramBlock block2 = new ProgramBlock(2);
		ifStatement.add(block0);
		ifStatement.add(block1);
		ifStatement.add(block2);
		ifStatement.addEdge(block0, block1);
		ifStatement.addEdge(block1, block2);
		ifStatement.addEdge(block0, block2);
		return ifStatement;
	}
	
	private static DFGraph initIfElse() {
		DFGraph ifElse = new DFGraph("ifElse", 0);
		ProgramBlock block0 = new ProgramBlock(0);
		ProgramBlock block1 = new ProgramBlock(1);
		ProgramBlock block2 = new ProgramBlock(2);
		ProgramBlock block3 = new ProgramBlock(3);
		ifElse.add(block0);
		ifElse.add(block1);
		ifElse.add(block2);
		ifElse.add(block3);
		ifElse.addEdge(block0, block1);
		ifElse.addEdge(block0, block2);
		ifElse.addEdge(block1, block3);
		ifElse.addEdge(block2, block3);
		return ifElse;
	}
	
	private static DFGraph initWhile() {
		DFGraph whileStatment = new DFGraph("while", 0);
		ProgramBlock block0 = new ProgramBlock(0);
		ProgramBlock block1 = new ProgramBlock(1);
		ProgramBlock block2 = new ProgramBlock(2);
		whileStatment.add(block0);
		whileStatment.add(block1);
		whileStatment.add(block2);
		whileStatment.addEdge(block0, block1);
		whileStatment.addEdge(block1, block0);
		whileStatment.addEdge(block0, block2);
		return whileStatment;
	}
	
	private static DFGraph initDoWhile() {
		DFGraph doWhile = new DFGraph("doWhile",0);
		ProgramBlock block0 = new ProgramBlock(0);
		ProgramBlock block1 = new ProgramBlock(1);
		ProgramBlock block2 = new ProgramBlock(2);
		doWhile.add(block0);
		doWhile.add(block1);
		doWhile.add(block2);
		doWhile.addEdge(block0, block1);
		doWhile.addEdge(block1, block0);
		doWhile.addEdge(block1, block2);
		return doWhile;
	}
	
	private static DFGraph initNextOddPresentation() {
		DFGraph nextOdd = new DFGraph("nextOddPresentation", 0);
		// Set up graph
		ProgramBlock block0 = new ProgramBlock(0);
		ProgramBlock block1 = new ProgramBlock(1);
		ProgramBlock block2 = new ProgramBlock(2);
		ProgramBlock block3 = new ProgramBlock(3);
		ProgramBlock block4 = new ProgramBlock(4);
		nextOdd.add(block0);
		nextOdd.add(block1);
		nextOdd.add(block2);
		nextOdd.add(block3);
		nextOdd.add(block4);
		nextOdd.addEdge(block0, block1);
		nextOdd.addEdge(block1, block2);
		nextOdd.addEdge(block2, block3);
		nextOdd.addEdge(block2, block4);
		nextOdd.addEdge(block3, block4);
		// Set up vars
		nextOdd.addVar("x", 0);
		nextOdd.addVar("issOdd", 1);
		// Block 0 set var x (Parameter)
		block0.def(0);
		// Block 1 use var x to determine var isOdd 
		block1.cuse(0);
		block1.def(1); 
		// If on var issOdd
		block2.puse(1);
		// x = x + 1
		block3.cuse(0);
		block3.def(0);
		// x = x + 1
		block4.cuse(0);
		block4.def(0);
		return nextOdd;
	}
	
	private static DFGraph initNextOdd() {
		DFGraph nextOdd = new DFGraph("nextOdd", 0);
		// Set up graph
		ProgramBlock block0 = new ProgramBlock(0);
		ProgramBlock block6 = new ProgramBlock(6);
		ProgramBlock block9 = new ProgramBlock(9);
		nextOdd.add(block0);
		nextOdd.add(block6);
		nextOdd.add(block9);
		nextOdd.addEdge(block0, block6);
		nextOdd.addEdge(block0, block9);
		nextOdd.addEdge(block6, block9);
		// Set up vars
		nextOdd.addVar("this", 0);
		nextOdd.addVar("x", 1);
		// Block 0 set var x (Parameter) and this.
		block0.def(0);
		block0.def(1);
		block0.puse(1); // If on Block 0
		// x = x + 1
		block6.cuse(1);
		block6.def(1);
		// x = x + 1
		block9.cuse(1);
		block9.def(1);
		return nextOdd;
	}
	
	private static DFGraph initMax() {
		DFGraph max = new DFGraph("max", 0);
		ProgramBlock block0 = new ProgramBlock(0);
		ProgramBlock block9 = new ProgramBlock(9);
		ProgramBlock block15 = new ProgramBlock(15);
		ProgramBlock block22 = new ProgramBlock(22);
		ProgramBlock block26 = new ProgramBlock(26);
		ProgramBlock block32 = new ProgramBlock(32);
		max.add(block0);
		max.add(block9);
		max.add(block15);
		max.add(block22);
		max.add(block26);
		max.add(block32);
		max.addEdge(block0, block9);
		max.addEdge(block9, block15);
		max.addEdge(block9, block32);
		max.addEdge(block15, block22);
		max.addEdge(block15, block26);
		max.addEdge(block22, block26);
		max.addEdge(block26, block9);
		// Set up vars
		max.addVar("this", 0);
		max.addVar("array", 1);
		max.addVar("array[]", 2);
		max.addVar("i", 3);
		max.addVar("max", 4);
		
		/* 
		 * public int max(int[] array)
		 *     int i = 0
		 *     int max = array[++i]
		 */
		block0.def(0);
		block0.def(1);
		block0.def(2);
		block0.def(3);
		block0.def(4);
		block0.cuse(2);
		// while (i < array.length)
		block9.puse(1);
		block9.puse(3);
		// if (array[i] > max)
		block15.puse(1);
		block15.puse(2);
		block15.puse(3);
		block15.puse(4);
		// max = array[i]
		block22.def(4);
		block22.cuse(1);
		block22.cuse(2);
		block22.cuse(3);
		// i = i + 1
		block26.def(3);
		block26.cuse(3);
		// return max
		block32.cuse(4);
		return max;
	}
	
}
