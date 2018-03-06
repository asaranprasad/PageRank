import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Graph {
  private HashMap<String, HashSet<String>> graph;

  public Graph(String filePath) {
    graph = (new FileUtility()).textFileToGraph(filePath);
  }

  public Set<String> P() {
    return graph.keySet();
  }

  public int size() {
    return graph.size();
  }

  public Set<String> M(String p) {
    return graph.get(p);
  }
}
