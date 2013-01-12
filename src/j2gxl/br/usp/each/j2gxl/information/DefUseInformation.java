package br.usp.each.j2gxl.information;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

import br.jabuti.graph.CFG;
import br.jabuti.graph.CFGNode;
import br.jabuti.graph.GraphNode;
import br.jabuti.graph.RRLiveDefs;
import br.jabuti.verifier.InvalidInstructionException;
import br.jabuti.verifier.InvalidStackArgument;
import br.usp.each.commons.logger.Log;
import br.usp.each.commons.string.StringUtils;
import br.usp.each.j2gxl.gxl.GXLDefUseBuilder;
import br.usp.each.j2gxl.gxl.GXLWriter;

/**
 * Represents a DefUse information
 * 
 * @author Felipe Albuquerque
 */
abstract class DefUseInformation implements Information {
	
	public static final Log log = new Log(DefUseInformation.class);

	private String dir;
	
	/**
	 * Constructor
	 */
	public DefUseInformation() {
		super();
	}
	
	/**
	 * Constructor
	 * 
	 * @param dir the directory in which the GXL files will be written
	 */
	public DefUseInformation(String dir) {
		this.dir = dir;
	}
	
	/**
	 * Generates the DefUse GXL file containing the informations retrieved from 
	 * the Java class
	 *  
	 * @param fromClass the class from which the information will be extracted
	 * @param isSimplified <code>true</code> if the DefUse graph is simplified 
	 * and <code>false</code>, otherwise
	 * @throws IOException when GXL the file cannot be written
	 * @throws BadJavaClassFileException then the class that is being examined 
	 * has a problem
	 */
	@SuppressWarnings("unchecked")
	void generateFile(JavaClass fromClass, boolean isSimplified) 
			throws IOException, BadJavaClassFileException {
		
		if ((this.dir == null) || (this.dir.trim().length() == 0)) {
			throw new IllegalStateException(
					"The directory in which the GXL files will be generated must " 
							+ "be specified. Use the createInstanceToDir(String) " 
							+ "method to create an instance pointing to a dir.");
		}
		
		int cfgOption = CFG.NONE;
		if (isSimplified) {
			cfgOption = CFG.NO_CALL_NODE;
		}

		ClassGen clazz = new ClassGen(fromClass);
		ConstantPoolGen connstantPool = clazz.getConstantPool();
		Method[] methods = clazz.getMethods();
		String className = clazz.getClassName();
		Set<String> fileNames = new HashSet<String>();
		
		for (int posMethod = 0; posMethod < methods.length; posMethod++) {
			Method method = methods[posMethod];
			MethodGen methodGen = new MethodGen(method, className, connstantPool);
			CFG graph = null;
			Set<Integer> addedNodes = new HashSet<Integer>();
			
			try {
				graph = new CFG(methodGen, clazz, cfgOption);
			} catch (InvalidInstructionException invalidInstructionException) {
				throw new BadJavaClassFileException(invalidInstructionException);
			} catch (InvalidStackArgument invalidStackArgument) {
				throw new BadJavaClassFileException(invalidStackArgument);
			}
			RRLiveDefs rral = new RRLiveDefs(RRLiveDefs.PRIMARY);
			graph.roundRobinAlgorithm(rral, true);
			
			GraphNode[] tree = graph.findDFT(true);
			CFGNode g0 = (CFGNode) graph.getEntry(); // First node
			
			for (int posNode = 0; posNode < tree.length; posNode++) {
				CFGNode node = (CFGNode) tree[posNode];
				
				// Get list of defs reaching the node
				Set<List<String>> reachingDefs =  (Set<List<String>>) node.getUserData(RRLiveDefs.defaultLabel);
				
				// Get the list of uses in the node
				Enumeration<String> useKeys = node.getUses().keys();
				while (useKeys.hasMoreElements()) {
					String useVar = useKeys.nextElement();
					
					boolean attribute = true; // In principle, it is an attribute def
					
					for (List<String> pair : reachingDefs) {
						String defVar = pair.get(0);
						if (defVar.equals(useVar)) {
							// There is a def reaching the use, so it is not an attribute def
							attribute = false;
							break;
						}
					}
					if (attribute) {
						// An attribute def should be defined in the first node
						g0.getDefinitions().put(useVar, -1);
					}
				}
			}

			
			Map<String, Integer> variables = this.getVariablesPositions(tree);

			GXLDefUseBuilder builder = new GXLDefUseBuilder(method.getName());
			builder.createVars("v1", variables);
			
			for (int posNode = 0; posNode < tree.length; posNode++) {
				CFGNode fromNode = (CFGNode) tree[posNode];
				
				List<String> defsPositions = 
					this.getDefsUsesPositions(fromNode.getDefinitions(), variables);
				List<String> usesPositions = 
					this.getDefsUsesPositions(fromNode.getUses(), variables);
				List<String> pUsesPositions = 
					this.getDefsUsesPositions(getPUses(fromNode), variables);
				
				if(pUsesPositions.size() > 0)
					usesPositions.clear();
				
				if (addedNodes.add(fromNode.getStart())) {
					builder.createNode( 
							String.valueOf(fromNode.getStart()), 
							defsPositions.toArray(new String[defsPositions.size()]),
							new String[0], // Jabuti does not identify undef vars
							usesPositions.toArray(new String[usesPositions.size()]),
							pUsesPositions.toArray(new String[pUsesPositions.size()]));

					this.addPrimNodesAndEdges(builder, fromNode, variables);
					this.addSecNodesAndEdges(builder, fromNode);
				}
				
			}
			
			if (!fileNames.add(this.createFileName(clazz, method, posMethod))) {
				log.warn("Intrusive: " + this.createFileName(clazz, method, posMethod));
			}
			
			new GXLWriter(builder.getGraph())
					.write(this.dir, this.createFileName(clazz, method, posMethod));
			
		}
	}

	/**
	 * Gets the defs and uses positions from the given variables map
	 * 
	 * @param defsUses the defs and uses
	 * @param variables the variables map
	 * @return the defs and uses positions
	 */
	private List<String> getDefsUsesPositions(Map<String, Integer> defsUses,
			Map<String, Integer> variables) {
		Set<String> keysDefsUses = defsUses.keySet();
		List<String> defsUsesPositions = new ArrayList<String>();
		
		for (String element : keysDefsUses) {
			defsUsesPositions.add(variables.get(element).toString());
		}
		
		return defsUsesPositions;
	}

	/**
	 * Gets the variables and positions from the GraphNode tree
	 * 
	 * @param tree the GraphNode tree
	 * @return the variables and positions
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Integer> getVariablesPositions(GraphNode[] tree) {
		int posVariable = 0;
		Map<String, Integer> variablesPositions = null;
		Set<String> variables = new TreeSet<String>();
		
		for (GraphNode graphNode : tree) {
			CFGNode node = (CFGNode) graphNode;
			Enumeration<String> keys = node.getUses().keys();
			
			while (keys.hasMoreElements()) {
				variables.add(keys.nextElement());
			}
			
			keys = node.getDefinitions().keys();
			
			while (keys.hasMoreElements()) {
				variables.add(keys.nextElement());
			}
			
		}

		variablesPositions = new HashMap<String, Integer>();
		
		for (String variable : variables) {
			variablesPositions.put(variable, posVariable);
			posVariable++;
		}
		
		return variablesPositions;
	}

	/**
	 * Creates a file name, given a class and a method
	 * 
	 * @param clazz the class
	 * @param method the method
	 * @param method_id number identifying the method
	 * @return the file name created
	 */
	private String createFileName(ClassGen clazz, Method method, int methodId) {
		final String fileNameTemplate = "defuse_{0}_{1}_{2}({3}).{4}";
		
		StringBuffer buffer = new StringBuffer();
		for (Type type : method.getArgumentTypes()) {
			String typeStr = type.toString();
			buffer.append(typeStr.substring(typeStr.lastIndexOf('.') + 1));
			buffer.append(", ");
		}
		String parameters = buffer.toString();
		
		if (!StringUtils.isEmpty(parameters)) {
			parameters = parameters.substring(0, parameters.length() - 2);	
		}

		return MessageFormat.format(
				fileNameTemplate, 
				clazz.getClassName(),
				Integer.toString(methodId), 
				method.getName().replaceAll("<", "[").replaceAll(">", "]"),
				parameters,
				GXL_FILE_EXTENSION);
	}

	/**
	 * Adds the primary nodes to the builder
	 * 
	 * @param builder the builder
	 * @param fromNode the node from which the edges to the other nodes come
	 * @param variables mapping of variables into id (integers); needed to find puses.
	 */
	@SuppressWarnings("unchecked")
	private void addPrimNodesAndEdges(GXLDefUseBuilder builder, CFGNode fromNode, Map<String, Integer> variables) {
		this.addChildrenEdgesAndNodes(builder, fromNode, fromNode.getPrimNext(), true, variables);
	}
	
	/**
	 * Adds the secondary nodes to the builder
	 * 
	 * @param builder the builder
	 * @param fromNode the node from which the edges to the children nodes come
	 */
	@SuppressWarnings("unchecked")
	private void addSecNodesAndEdges(GXLDefUseBuilder builder, CFGNode fromNode) {
		this.addChildrenEdgesAndNodes(builder, fromNode, fromNode.getSecNext(), false, null);
	}

	/**
	 * Adds the children nodes to the builder
	 * 
	 * @param writer the builder
	 * @param fromNode the node from which the edges to the children nodes come
	 * @param childNodes the children nodes
	 * @param isPrimNodes <code>true</code> if the children nodes are primary
	 * @param variables mapping of variables into id (integers)
	 * nodes and <code>false</code> otherwise
	 */
	private void addChildrenEdgesAndNodes(GXLDefUseBuilder builder, CFGNode fromNode, 
			List<CFGNode> childNodes, boolean isPrimNodes, Map<String, Integer> variables) {
		final String idTemplate = "{0}_from_{1, number, #}_to_{2, number, #}";
		String idPrefix = null;
		
		List<String> pUsesPositions;
		
		if (isPrimNodes) {
			idPrefix = "prim";
			pUsesPositions = this.getDefsUsesPositions(getPUses(fromNode), variables);
		} else {
			idPrefix = "sec";
			pUsesPositions = new ArrayList<String>();
		}
		
		for (int posChildNodes = 0; posChildNodes < childNodes.size(); posChildNodes++) {
			CFGNode toNode = childNodes.get(posChildNodes);
			
			builder.createEdge(
					MessageFormat.format(
							idTemplate, 
							idPrefix, 
							fromNode.getStart(),
							toNode.getStart()),
					String.valueOf(toNode.getStart()),
					String.valueOf(fromNode.getStart()), 
					pUsesPositions.toArray(new String[pUsesPositions.size()])); // JaBUTi doesn't define p-uses in CFGNode class
		}
		
	}

	/**
	 * Finds the puses in a CFGNode.
	 * 
	 * @param CFGNode
	 * @return Map<String, Integer> where the String represents the var and the integer
	 * the pc value.
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Integer> getPUses(CFGNode fromNode) {
		Map<String, Integer> pUses = new HashMap<String, Integer>();
		// Check whether there are more than one successor
		if (fromNode.getPrimNext().size() > 1) {
			pUses.putAll(fromNode.getUses());
		}
		return pUses;
	}
}
