import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class FileUtility {
  public List<String> textFileToList(String filePath) {
    List<String> lines = new ArrayList<String>();
    try {
      Scanner sc = new Scanner(new File(filePath));
      while (sc.hasNextLine())
        lines.add(sc.nextLine());
      sc.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return lines;
  }

  public HashMap<String, HashSet<String>> textFileToGraph(String filePath) {
    List<String> lines = new ArrayList<String>();
    try {
      Scanner sc = new Scanner(new File(filePath));
      while (sc.hasNextLine())
        lines.add(sc.nextLine());
      sc.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    HashMap<String, HashSet<String>> graph = new HashMap<String, HashSet<String>>();
    for (String line : lines) {
      line = line.substring(line.indexOf('|') + 1, line.length()).trim();
      String[] docIds = line.split(" ");
      HashSet<String> inLinks = new HashSet<String>();
      for (int i = 1; i < docIds.length; i++) {
        inLinks.add(docIds[i]);
      }
      graph.put(docIds[0], inLinks);
    }

    return graph;
  }

  /**
   * Prints string to the given handle one line at a time
   * 
   * @param output - output handle
   * @param string - output string
   */
  public void println(PrintWriter output, String string) {
    output.println(string);
    output.flush();
  }


  /**
   * Prints string to the given handle one string at a time
   * 
   * @param output - output handle
   * @param string - output string
   */
  public void print(PrintWriter output, String string) {
    output.print(string);
    output.flush();
  }

}
