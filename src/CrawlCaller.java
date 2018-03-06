import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class CrawlCaller {


  public static void main(String[] args) {
    try {
      CrawlConfig config = new CrawlConfig();
      Scanner scan = new Scanner(System.in);
      String option;
      do {
        Crawler crawl = new Crawler(config);

        // print current config to user
        System.out.println("Current Config:");
        System.out.println(
            "Enter a choice:\n 1 - DFS crawl\n 2 - BFS crawl");
        System.out.println(
            " 3 - Calculate Page Rank\n q - Quit");
        System.out.println("To change Default config, enter y");

        option = scan.next();

        // timer start
        long startTime = System.nanoTime();

        switch (option) {
          case "1":
          case "DFS crawl":
            crawl.crawlDepthFirst();
            break;

          case "2":
          case "BFS crawl":
            crawl.crawlBreadthFirst(false, false);
            break;

          case "3":
          case "Calculate Page Rank":
            pageRankTests();
            break;

          case "q":
          case "Quit":
            break;
        }
        long endTime = System.nanoTime();

        System.out.println(
            "Time taken for the operation in nanosecs: " + (endTime - startTime));

      } while (!option.toLowerCase().startsWith("q"));
      scan.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private static void pageRankTests() {
    CrawlConfig config = new CrawlConfig();
    TreeMap<String, Double> pageRanks;
    PageRank pr = new PageRank(0.85);

    // 1. Test Graph
    //    System.out.println("Test Graph");
    //    Graph testGraph = new Graph(config.getOutputFolderPath() + "sampleGraph.txt");
    //    pageRanks = pr.calculatePageRank(testGraph);
    //    printMapSortByValues(pageRanks);

    // 2. Graph 1 (BFS)
    System.out.println("BFS Graph1");
    Graph G1 = new Graph(config.getOutputFolderPath() + "G1.txt");
    pageRanks = pr.calculatePageRank(G1);
    printMapSortByValues(pageRanks);

  }

  private static void printMapSortByValues(TreeMap<String, Double> map) {
    List<Map.Entry<String, Double>> list =
        new LinkedList<Map.Entry<String, Double>>(map.entrySet());

    Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
      public int compare(Map.Entry<String, Double> o1,
          Map.Entry<String, Double> o2) {
        return (o2.getValue()).compareTo(o1.getValue());
      }
    });

    Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
    for (Map.Entry<String, Double> entry : list) {
      sortedMap.put(entry.getKey(), entry.getValue());
    }

    for (String page : sortedMap.keySet()) {
      System.out.println(page + ": " + sortedMap.get(page));
    }
  }


}
