package br.usp.each.inss.cache;

import java.util.ArrayList;
import java.util.List;

import br.usp.each.commons.logger.Log;
import br.usp.each.inss.dfg.DFGLoader;
import br.usp.each.opal.dataflow.DFGraph;

import com.whirlycott.cache.Cache;
import com.whirlycott.cache.CacheException;
import com.whirlycott.cache.CacheManager;

public class DefUseCache {

	private Cache cache;
	private DFGLoader loader;
	
	private static final Log log = new Log(DefUseCache.class);

	public DefUseCache(DFGLoader loader) throws  CacheException {
		this.loader = loader;
		cache = CacheManager.getInstance().getCache("DFGraph");
	}

	public DFGraph get(String className, int methodId) {
		ClassGraphs classGraphs = (ClassGraphs) cache.retrieve(className);
		if (classGraphs == null) {
			classGraphs = new ClassGraphs();
			cache.store(className, classGraphs);
		}
		DFGraph graph = classGraphs.get(methodId);
		if (graph == null) {
			try {
				graph = loader.load(className, methodId);
				classGraphs.put(methodId, graph);
			} catch (Exception e) {
				log.error("Error loading DFGraph for class: %s and method: %d",
						e, className, methodId);
				System.exit(1);
			}
		}
		return graph;
	}
	
	public static class ClassGraphs {

		private List<DFGraph> classGraphs;

		public ClassGraphs() {
			classGraphs = new ArrayList<DFGraph>();
		}

		public DFGraph get(int i) {
			if (i >= classGraphs.size())
				return null;
			return classGraphs.get(i);
		}

		public void put(int i, DFGraph graph) {
			int j = i + 1;
			while (classGraphs.size() < j)
				classGraphs.add(null);
			classGraphs.set(i, graph);
		}

	}

}
