import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class PageRank {

  private int CONVERGENCE_THRESHOLD = 4;
  private double d = 0.85;
  private int iterationsLimit;
  private List<Double> perplexity;

  // Constructor with custom damping factor
  public PageRank(double dampingFactor) {
    d = dampingFactor;
    iterationsLimit = Integer.MAX_VALUE;
    perplexity = new ArrayList<Double>();
  }

  // Constructor with custom damping factor and convergence iterations limit
  public PageRank(double dampingFactor, int iterationsLimit) {
    d = dampingFactor;
    this.iterationsLimit = iterationsLimit;
    perplexity = new ArrayList<Double>();
  }

  /*
   * Calculates PageRank
   * 
   * P is the set of all pages; |P| = N
   * S is the set of sink nodes, i.e., pages that have no out links
   * M(p) is the set (without duplicates) of pages that link to page p
   * L(q) is the number of out-links (without duplicates) from page q
   * d is the PageRank damping/teleportation factor; use d = 0.85 as a fairly typical value
   */
  public HashMap<String, Double> calculatePageRank(Graph g) {
    HashMap<String, Double> PR = new HashMap<String, Double>();
    double N = (double) g.size();

    // initial value
    for (String p : g.P()) {
      PR.put(p, 1 / N);
    }

    while (!hasConverged(PR) && iterationsLimit > 0) {
      double sinkPR = 0;

      // calculate total sink PR
      for (String p : g.S()) {
        sinkPR += PR.get(p);
      }

      HashMap<String, Double> newPR = new HashMap<String, Double>();

      for (String p : g.P()) {
        // teleportation
        newPR.put(p, (1 - d) / N);

        // spread remaining sink PR evenly
        newPR.put(p, newPR.get(p) + (d * (sinkPR / N)));

        // pages pointing to p
        for (String q : g.M(p)) {
          // add share of PageRank from in-links
          newPR.put(p, newPR.get(p) + (d * (PR.get(q) / (double) g.L(q))));
        }
      }

      for (String p : g.P()) {
        PR.put(p, newPR.get(p));
      }

      calculatePerplexity(PR);
      iterationsLimit--;
    }
    return PR;
  }

  /* Calculates and keeps track of the perplexity value of the Page Rank */
  private void calculatePerplexity(HashMap<String, Double> PR) {
    double p = Math.pow(2, H(PR));
    perplexity.add(p);
    System.out.println(p);
  }

  /* Calculates the Shannon entropy of the Page Rank distribution */
  private double H(HashMap<String, Double> PR) {
    double perplexity = 0;
    double log2 = Math.log(2);
    for (String page : PR.keySet()) {
      double xi = PR.get(page);
      perplexity += xi * (Math.log(xi) / log2);
    }
    perplexity = -perplexity;
    return perplexity;
  }

  /* Returns true of the Page Rank has converged */
  private boolean hasConverged(HashMap<String, Double> PR) {
    if (perplexity.size() < CONVERGENCE_THRESHOLD)
      return false;

    boolean returnValue = true;
    int i = perplexity.size() - CONVERGENCE_THRESHOLD + 1;
    while (i < perplexity.size()) {
      if (Math.abs(perplexity.get(i - 1) - perplexity.get(i)) >= 1)
        returnValue = false;
      i++;
    }
    return returnValue;
  }
}
