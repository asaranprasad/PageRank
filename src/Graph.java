import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Graph {
  private HashMap<String, HashSet<String>> inLinksGraph;
  private HashMap<String, HashSet<String>> outLinksGraph;

  /* Load graph from file */
  public Graph(String filePath) {
    inLinksGraph = (new FileUtility()).textFileToGraph(filePath);
    outLinksGraph = new HashMap<String, HashSet<String>>();
    generateOutLinksGraph();
  }

  /* Generates a datastructure to store outlinks from nodes */
  private void generateOutLinksGraph() {
    for (String page : P()) {
      HashSet<String> outlinks = new HashSet<String>();
      for (String otherPages : P()) {
        if (M(otherPages).contains(page))
          outlinks.add(otherPages);
      }
      outLinksGraph.put(page, outlinks);
    }
  }

  /* Returns the set of all nodes in the graph */
  public Set<String> P() {
    return inLinksGraph.keySet();
  }

  /* Returns the size of the graph */
  public int size() {
    return inLinksGraph.size();
  }

  /* Returns the set (without duplicates) of pages that link to page p */
  public Set<String> M(String p) {
    return inLinksGraph.get(p);
  }

  /* Returns the set of sinks in the graph */
  public Set<String> S() {
    HashSet<String> sinks = new HashSet<String>();
    for (String page : outLinksGraph.keySet()) {
      if (outLinksGraph.get(page).isEmpty())
        sinks.add(page);
    }
    return sinks;
  }

  /* Returns the set of sources in the graph */
  public Set<String> sources() {
    HashSet<String> sources = new HashSet<String>();
    for (String page : inLinksGraph.keySet()) {
      if (inLinksGraph.get(page).isEmpty())
        sources.add(page);
    }
    return sources;
  }

  /* Returns the number of out-links (without duplicates) from page q */
  public int L(String q) {
    return outLinksGraph.get(q).size();
  }
}
