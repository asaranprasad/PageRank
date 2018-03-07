import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
  private CrawlConfig config;
  private Queue<String> frontier;
  private HashSet<String> visited;
  private HashMap<String, HashSet<String>> inLinks;
  private int pagesCrawled;
  FileUtility fu;
  PrintWriter output;
  PrintWriter docsDownload;
  Dictionary dict;


  /* Constructor assuming default crawl config */
  public Crawler() {
    this(new CrawlConfig());
  }


  /* Constructor accepting custom config for the crawler */
  public Crawler(CrawlConfig config) {
    this.config = config;
    fu = new FileUtility();
    frontier = new LinkedList<String>();
    visited = new HashSet<String>();
    frontier.add(config.getSeedURL());
    dict = new Dictionary();
    pagesCrawled = 0;
  }

  /* Set up parameters for DepthFirst crawl */
  public void crawlDepthFirst() {
    try {
      // initialize output-writer handle
      output = new PrintWriter(config.getDepthFirstOutputPath());
      PrintWriter dfsGraph = new PrintWriter(config.getGraph2OutputPath());
      fu.println(output, "Count | Text | Depth | URL");

      // initialize inLinks
      inLinks = new HashMap<String, HashSet<String>>();

      // initialize traversal parameters to that of the seed
      int depth = 1;
      int pageCount = 1;
      String currentURL = frontier.poll();

      // print seed parameters to output
      fu.println(output, pageCount + " | Seed | " + depth + " | " + currentURL);

      // kick-start breadth-first traversal from seed
      dfs(currentURL, depth, pageCount);

      // write to graph output
      writeToGraph(dfsGraph);

      // close output-writer handles
      dfsGraph.close();
      output.close();
    } catch (FileNotFoundException fne) {
      fne.printStackTrace();
    }
  }


  /**
   * Performs Depth First Traversal over a given page
   * 
   * @param page - page handle for this node
   * @param depth - depth of this node
   * @param pageCount - URLs recorded prior to this call
   * @return pageCount post traversal complete
   */
  private int dfs(String thisURL, int depth, int pageCount) {
    // load from URL
    Document page = loadFromURL(thisURL, false);
    visited.add(thisURL);

    // handling redirects
    if (page == null)
      return -1;

    // extract unique crawlable URLs from the page
    List<String[]> urlTxtPairs = getValidURLsFromPage(page);

    // iterate over all URLs parsed or till expected page count reached
    for (int i = 0; i < urlTxtPairs.size()
        && pagesCrawled <= config.getPageCount(); i++) {
      String text = urlTxtPairs.get(i)[1];
      String url = urlTxtPairs.get(i)[0];

      // add inLink
      addInLink(thisURL, url);

      // check if url has already been registered
      if (visited.contains(url))
        continue;

      // write to output
      fu.println(output, (++pageCount) + " | " + text + " | " + depth + " | " + url);

      // if depth has not reached maximum allowed depth and target page count not reached,
      // traverse recursively depth-first
      if (depth < config.getMaxDepth() && pagesCrawled < config.getPageCount()) {
        int dfsPageCount = dfs(url, depth + 1, pageCount);
        if (dfsPageCount == -1)
          continue;
        pageCount = dfsPageCount;
      }
    }
    return pageCount;
  }


  /**
   * Set up parameters for BreadthFirst crawl
   * 
   * @param isFocused - true if a focused crawl
   * @param shouldDownload - true if documents need to be saved
   */
  public void crawlBreadthFirst(boolean isFocused, boolean shouldDownload) {
    try {
      // initialize output-writer handles
      docsDownload = new PrintWriter(config.getdocsDownloadPath());
      output = new PrintWriter(config.getBreadthFirstOutputPath());
      PrintWriter bfsGraph = new PrintWriter(config.getGraph1OutputPath());

      // initialize inLinks
      inLinks = new HashMap<String, HashSet<String>>();

      // separate output handle for focused crawl
      if (isFocused)
        output = new PrintWriter(config.getFocusedCrawlOutputPath());

      // print header to output
      fu.println(output, "Count | Text | Depth | URL");

      // initialize traversal parameters to that of the seed
      int depth = 1;
      int pageCount = 1;
      String currentURL = frontier.poll();
      frontier.add(null); // delimiting levels with null 

      // print seed parameters to output
      fu.println(output, pageCount + " | Seed | " + depth + " | " + currentURL);

      // kick-start breadth-first traversal from seed
      pageCount = bfs(currentURL, depth, pageCount, isFocused);

      // write to graph output
      writeToGraph(bfsGraph);

      // close output-writer handles
      bfsGraph.close();
      output.close();
      docsDownload.close();

    } catch (FileNotFoundException fne) {
      fne.printStackTrace();
    }
  }


  /**
   * Writes to output the in-link graph
   * 
   * @param graph - output writer handle
   */
  private void writeToGraph(PrintWriter graph) {
    int counter = 0;
    for (String urlsVisited : visited) {
      String visitedDocID = getDocID(urlsVisited);
      if (inLinks.containsKey(visitedDocID)) {
        HashSet<String> inLinkDocIds = inLinks.get(visitedDocID);
        fu.print(graph, (++counter) + " | " + visitedDocID + " ");
        System.out.print(counter + " | " + visitedDocID + " ");
        for (String eachDocID : inLinkDocIds) {
          fu.print(graph, eachDocID + " ");
          System.out.print(eachDocID + " ");
        }
        fu.println(graph, "");
        System.out.println("");
      } else { // source
        fu.println(graph, (++counter) + " | " + visitedDocID + " ");
        System.out.println(counter + " | " + visitedDocID + " ");

      }
    }
  }


  /**
   * Performs Breadth First Traversal over a given page
   * 
   * @param thisURL - page handle for this node
   * @param depth - depth of this node
   * @param pageCount - URLs recorded prior to call
   * @param isFocused - true if focused crawl
   * @return pageCount post traversal complete
   */
  private int bfs(String thisURL, int depth, int pageCount, boolean isFocused) {
    // load from URL
    Document page = loadFromURL(thisURL, !isFocused);
    visited.add(thisURL);

    // handling redirects
    if (page == null)
      return -1;

    // extract unique crawlable URLs from the page
    List<String[]> urlTxtPairs = getValidURLsFromPage(page);

    // perform additional filters in case of focused crawl
    if (isFocused)
      performFocusedFilter(urlTxtPairs);

    // iterate over all URLs parsed or till expected page count reached
    for (int i = 0; i < urlTxtPairs.size()
        && pagesCrawled <= config.getPageCount(); i++) {
      String text = urlTxtPairs.get(i)[1];
      String url = urlTxtPairs.get(i)[0];


      // add inLink
      addInLink(thisURL, url);

      // check if URL has already been registered
      if (visited.contains(url))
        continue;
      frontier.add(url);

      // write to output
      fu.println(output, (++pageCount) + " | " + text + " | " + depth + " | " + url);
      //      System.out.println((pageCount) + " | " + text + " | " + depth + " | " + url);


      // return if expected page count reached
      if (pagesCrawled > config.getPageCount())
        return pageCount;
    }

    // if depth has not reached maximum allowed depth
    if (depth < config.getMaxDepth() && pagesCrawled < config.getPageCount()) {
      while (frontier.size() > 0) {
        String next = frontier.poll();
        // keep track of depth of the traversal
        // null represents end of a level
        if (next == null) {
          frontier.add(null);
          depth++;
          next = frontier.poll();
          // consecutive nulls represent end of levels
          if (next == null)
            return pageCount;
        }

        // traverse recursively breadth-first
        int bfsPageCount = bfs(next, depth, pageCount, isFocused);

        //handling redirects
        if (bfsPageCount == -1)
          continue;
        pageCount = bfsPageCount;
        break;
      }
    }

    // page count after this breadth-first call
    return pageCount;
  }


  /**
   * Creates an in-link mapping between the urls, post converting the urls into DocIds
   * 
   * @param inLinkUrl - Map value entry
   * @param docUrl - Map Key
   */
  private void addInLink(String inLinkUrl, String docUrl) {
    String docID = getDocID(docUrl);
    String inLinkDocID = getDocID(inLinkUrl);

    HashSet<String> inLinkDocIDs;
    if (inLinks.containsKey(docID))
      inLinkDocIDs = inLinks.get(docID);
    else
      inLinkDocIDs = new HashSet<String>();

    inLinkDocIDs.add(inLinkDocID);
    inLinks.put(docID, inLinkDocIDs);
  }

  /* Extracts DocId from the given url */
  private String getDocID(String url) {
    return url.substring(url.lastIndexOf('/') + 1, url.length());
  }

  /**
   * Filter URL-Text pairs for valid variations of the given keywords in the config
   * 
   * @param urlTxtPairs - URL and anchor text pair
   */
  private void performFocusedFilter(List<String[]> urlTxtPairs) {
    Iterator<String[]> pairIter = urlTxtPairs.iterator();

    while (pairIter.hasNext()) {
      String[] urlTxtPair = (String[]) pairIter.next();

      // Case Folding
      String url = urlTxtPair[0].toLowerCase();
      String text = urlTxtPair[1].toLowerCase();

      // Parsing the article name from the url
      String articleName = url.substring(url.lastIndexOf('/') + 1, url.length());

      // check if the URL or text contains valid variations of the keywords
      int unmatched = 0;
      for (String keyword : config.getFocusedCrawlKeywords())
        if (matches(keyword, articleName) || matches(keyword, text))
          break;
        else
          unmatched++;

      // when both articleName and text doesn't match with any of the keywords 
      if (unmatched == config.getFocusedCrawlKeywords().size())
        pairIter.remove();
    }
  }


  /* Check if the given text has a valid variation of the keyword */
  private boolean matches(String keyword, String text) {
    keyword = keyword.toLowerCase();
    if (!text.contains(keyword))
      return false;

    // splits word by any unicode character that is not a letter
    String[] words = text.split("\\P{L}+");
    for (String word : words) {
      if (strictMatch(word, keyword))
        return true;
    }
    return false;
  }


  /* 
   * Tries to validate if the given word is a meaningful variation of the keyword
   * using a dictionary of known words in English language
   */
  private boolean strictMatch(String word, String keyword) {
    if (!word.contains(keyword))
      return false;

    // extract words before and after the keyword
    String[] subWords = word.split(keyword);

    boolean isValidWord = true;
    for (String subword : subWords)
      if (!subword.isEmpty())
        // lookup dictionary
        if (!dict.isValidWord(subword))
          isValidWord = isValidWord && false;

    return isValidWord;
  }


  /**
   * Extract valid crawlable URLs from the given page
   * 
   * @param page - node to be crawled
   * @return URL-Text pairs for crawlable unique URLs. 0 - url 1 - text
   */
  private List<String[]> getValidURLsFromPage(Document page) {
    List<String[]> urlTxtPairs = new ArrayList<String[]>();
    HashSet<String> currentSet = new HashSet<String>();

    // sets the domain name as the BaseUri
    page.setBaseUri(config.getBaseUri());

    // Pre-Processing - Exclude non-content portion of the page
    for (String exclusionSelector : config.getCrawlExclusionSelectors())
      page.select(exclusionSelector).remove();
    Elements hyperLinks = page.select(config.getMainContentSelector());

    // Exclude administrative and previously visited URLs
    Iterator<Element> iter = hyperLinks.iterator();
    while (iter.hasNext()) {
      Element anchor = (Element) iter.next();
      String href = anchor.absUrl("href");
      String plainHref = anchor.attr("href");

      // remove administrative URLs and other irrelevant redirections
      boolean removeCondition = !href.contains(config.getBaseUri());
      removeCondition = removeCondition || !plainHref.startsWith(config.getArticleType());
      removeCondition = removeCondition || href.contains("#");
      removeCondition = removeCondition || plainHref.contains(":");

      // exclude pages already visited
      if (removeCondition)
        iter.remove();

      // form a URL-Text pair
      else if (!currentSet.contains(href)) {
        String[] urlTxtPair = new String[2];
        urlTxtPair[0] = href;
        urlTxtPair[1] = anchor.text().trim();
        urlTxtPairs.add(urlTxtPair);
        currentSet.add(href);
      }
    }
    return urlTxtPairs;
  }


  /**
   * @return a document obtained post establishing an HttpRequest to the given URL
   * @param url
   * @param shouldDownload - flag whether the document should be saved
   * 
   */
  private Document loadFromURL(String url, boolean shouldDownload) {
    try {
      // politeness wait
      Thread.sleep(config.getPolitenessWait());

      // handling redirect
      Response response = Jsoup.connect(url).execute();
      if (visited.contains(response.url().toString()))
        return null;

      // connecting to the URL, with a timeout of 20 seconds
      Document page = Jsoup.connect(url).timeout(20000).get();
      pagesCrawled++;

      // printing to console
      System.out.println("Crawling: " + (pagesCrawled) + " | " + url);

      // save documents if asked to
      if (shouldDownload)
        storeDocTrecFormat(url, page);

      return page;
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException ie) {
      ie.printStackTrace();
    }
    return null;
  }


  /* Stores document in TREC recommended format using the available output handle */
  private void storeDocTrecFormat(String url, Document page) {
    LocalTime currentTime = LocalTime.now();
    LocalDate currentDate = LocalDate.now();
    String pageHtml = page.outerHtml();

    fu.println(docsDownload, "<DOC>");
    fu.println(docsDownload, "<DOCNO>WTX-" + currentTime + "</DOCNO>");
    fu.println(docsDownload, "<DOCHDR>");
    fu.println(docsDownload, url);
    fu.println(docsDownload, "Date: " + currentDate);
    fu.println(docsDownload, "Content-type: text/html");
    fu.println(docsDownload, "Content-length: " + String.valueOf(pageHtml.length()));
    fu.println(docsDownload, "Last-modified: " + currentDate);
    fu.println(docsDownload, "</DOCHDR>");
    fu.println(docsDownload, pageHtml);
    fu.println(docsDownload, "</DOC>");
  }


}
