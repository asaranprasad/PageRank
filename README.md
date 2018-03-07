# Page Rank - Java Implementation

Task1: Obtaining directed web graphs.
Task2: Link analysis: Implementations.

## Setting up

The ./src/ folder consists of source code files
```
1. PageRankCaller.java - Consists of Test Methods to kick start the program.
2. PageRank.java       - Implementation of PageRank algorithm.
3. Graph.java          - Support Class for PageRank. Manages inlinks and outlinks. 
4. CrawlConfig.java    - Contains configurable parameters for the crawl.
5. Crawler.java        - Has the crawler logic implemented.
6. FileUtility.java    - Utility Class for accessing flat files.
7. Dictionary.java     - Accesses and maintains a dictionary of English words.
```

The ./output/ folder consists of sample outputs of the PageRank computation
```
1. G1.txt               - Graph over set of 1000 URLs from Breadth First crawling.
2. G2.txt               - Graph over set of 1000 URLs from Depth First crawling.
3. SimpleStatistics.txt - A brief report on simple statistics over G1 and G2.
4. Perplexity.txt       - Listing perplexity values in each round until convergence.
5. Task2bG1.txt         - PageRank run on G1 with default configuration.
6. Task2bG2.txt         - PageRank run on G2 with default configuration.
7. Task2cAnalysis       - Analysis on the results obtained from PageRank variations.
8. Task2ciG1.txt        - Results of PageRank run using damping factor 0.55 on G1.
9. Task2ciG2.txt        - Results of PageRank run using damping factor 0.55 on G2.
10. Task2ciiG1.txt      - Results of PageRank run for exactly 4 iterations on G1.
11. Task2ciiG2.txt      - Results of PageRank run for exactly 4 iterations on G2.
12. Task2ciiiG1.txt     - Results of sorting documents in G1 based on raw in-link count.
13. Task2ciiiG2.txt     - Results of sorting documents in G2 based on raw in-link count.
```


## External Libraries Referenced

The following external library might need to be referenced to the build path, or via using a Maven dependency.

1. JSoup

```
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.9.2</version>
</dependency>
```


The above library has been included in the ./externalLibrary/ folder

