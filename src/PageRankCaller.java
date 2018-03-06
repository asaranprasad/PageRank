import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PageRankCaller {


  public static void main(String[] args) {
    try {
      CrawlConfig config = new CrawlConfig();
      Scanner scan = new Scanner(System.in);
      String option;
      do {
        Crawler crawl = new Crawler(config);

        System.out.println("\nEnter a choice:\n 1 - DFS crawl\n 2 - BFS crawl");
        System.out.println(" 3 - Calculate Page Rank\n q - Quit");

        option = scan.next();

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

      } while (!option.toLowerCase().startsWith("q"));
      scan.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private static void pageRankTests() {
    HashMap<String, Double> pageRanks;
    PageRank pr;
    String parentFolder = new CrawlConfig().getOutputFolderPath();
    FileUtility fu = new FileUtility();
    int limit = 50;

    // 1. Test Graph
    //    System.out.println("\nTest Graph");
    //    Graph testGraph = new Graph(config.getOutputFolderPath() + "sampleGraph.txt");
    //    pageRanks = pr.calculatePageRank(testGraph);
    //    printPageRanksSortByScores(pageRanks,parentFolder+"Test.txt",limit);

    // 2. Graph 1 (BFS)
    System.out.println("BASE LINE - - - - - - - - - - - - - - - - - - - - - - -");
    System.out.println("\nBFS Graph1");
    Graph G1 = new Graph(parentFolder + "G1.txt");
    pr = new PageRank(0.85);
    pageRanks = pr.calculatePageRank(G1);
    System.out.println("Sources #: " + G1.sources().size());
    System.out.println("Sinks #: " + G1.S().size());
    printPageRanksSortByScores(pageRanks, parentFolder + "Task2bG1.txt", limit);

    // 3. Graph 2 (DFS)
    System.out.println("\nDFS Graph2");
    Graph G2 = new Graph(parentFolder + "G2.txt");
    pr = new PageRank(0.85);
    pageRanks = pr.calculatePageRank(G2);
    System.out.println("Sources #: " + G2.sources().size());
    System.out.println("Sinks #: " + G2.S().size());
    printPageRanksSortByScores(pageRanks, parentFolder + "Task2bG2.txt", limit);

    System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - -");


    // 4. Damping Factor - 0.55 - Graph 1 (BFS)
    System.out.println("\nBFS Graph1 - Damping Factor - 0.55");
    G1 = new Graph(parentFolder + "G1.txt");
    pr = new PageRank(0.55);
    pageRanks = pr.calculatePageRank(G1);
    printPageRanksSortByScores(pageRanks, parentFolder + "Task2ciG1.txt", limit);

    // 5. Damping Factor - 0.55 - Graph 2 (DFS)
    System.out.println("\nDFS Graph2 - Damping Factor - 0.55");
    G2 = new Graph(parentFolder + "G2.txt");
    pr = new PageRank(0.55);
    pageRanks = pr.calculatePageRank(G2);
    printPageRanksSortByScores(pageRanks, parentFolder + "Task2ciG2.txt", limit);


    // 6. Page Rank 4 iterations - Graph 1 (BFS)
    System.out.println("\nBFS Graph1 - Page Rank 4 iterations");
    G1 = new Graph(parentFolder + "G1.txt");
    pr = new PageRank(0.85, 4);
    pageRanks = pr.calculatePageRank(G1);
    printPageRanksSortByScores(pageRanks, parentFolder + "Task2ciiG1.txt", limit);

    // 7. Page Rank 4 iterations - Graph 2 (DFS)
    System.out.println("\nDFS Graph2 - Page Rank 4 iterations");
    G2 = new Graph(parentFolder + "G2.txt");
    pr = new PageRank(0.85, 4);
    pageRanks = pr.calculatePageRank(G2);
    printPageRanksSortByScores(pageRanks, parentFolder + "Task2ciiG2.txt", limit);

    // 8. Sort based on raw in-link count - Graph 1 (BFS)
    System.out.println("\nBFS Graph1 - Sort based on raw in-link count");
    HashMap<String, HashSet<String>> map1 = fu.textFileToGraph(parentFolder + "G1.txt");
    printInLinkCounts(map1, parentFolder + "Task2ciiiG1.txt", limit);

    // 9. Sort based on raw in-link count - Graph 2 (DFS)
    System.out.println("\nDFS Graph2 - Sort based on raw in-link count");
    HashMap<String, HashSet<String>> map2 = fu.textFileToGraph(parentFolder + "G2.txt");
    printInLinkCounts(map2, parentFolder + "Task2ciiiG2.txt", limit);

  }


  private static void printPageRanksSortByScores(HashMap<String, Double> map,
      String printPath, int limitLines) {
    try {
      PrintWriter pr = new PrintWriter(printPath);
      FileUtility fu = new FileUtility();

      List<Map.Entry<String, Double>> list =
          new LinkedList<Map.Entry<String, Double>>(map.entrySet());

      Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
        public int compare(Map.Entry<String, Double> o1,
            Map.Entry<String, Double> o2) {
          return (o2.getValue()).compareTo(o1.getValue());
        }
      });

      for (Map.Entry<String, Double> entry : list) {
        if (limitLines < 1)
          break;
        String page = entry.getKey();
        Double score = entry.getValue();
        fu.println(pr, page + " " + score);
        //        System.out.println(page + " " + score);
        limitLines--;
      }
    } catch (FileNotFoundException fne) {
      fne.printStackTrace();
    }
  }

  private static void printInLinkCounts(HashMap<String, HashSet<String>> map1,
      String printPath, int limit) {
    HashMap<String, Double> map = new HashMap<String, Double>();

    for (Map.Entry<String, HashSet<String>> entry : map1.entrySet())
      map.put(entry.getKey(), (double) entry.getValue().size());

    printPageRanksSortByScores(map, printPath, limit);
  }

}
