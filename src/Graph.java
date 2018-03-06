import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Graph {
  private HashMap<String, HashSet<String>> inLinksGraph;
  private HashMap<String, HashSet<String>> outLinksGraph;

  public Graph(String filePath) {
    inLinksGraph = (new FileUtility()).textFileToGraph(filePath);
    outLinksGraph = new HashMap<String, HashSet<String>>();
    generateOutLinksGraph();
  }

  private void generateOutLinksGraph() {
    for (String page : P()) {
      HashSet<String> outlinks = new HashSet<String>();
      for (String otherPages : P()) {
        //        if (otherPages.equals(page))
        //          continue;
        if (M(otherPages).contains(page))
          outlinks.add(otherPages);
      }
      outLinksGraph.put(page, outlinks);
    }
  }

  public Set<String> P() {
    return inLinksGraph.keySet();
  }

  public int size() {
    return inLinksGraph.size();
  }

  public Set<String> M(String p) {
    return inLinksGraph.get(p);
  }

  public Set<String> S() {
    HashSet<String> sinks = new HashSet<String>();
    for (String page : outLinksGraph.keySet()) {
      if (outLinksGraph.get(page).isEmpty())
        sinks.add(page);
    }
    return sinks;
  }

  public Set<String> sources() {
    HashSet<String> sources = new HashSet<String>();
    for (String page : inLinksGraph.keySet()) {
      if (inLinksGraph.get(page).isEmpty())
        sources.add(page);
    }
    return sources;
  }

  public int L(String q) {
    return outLinksGraph.get(q).size();
  }
}
