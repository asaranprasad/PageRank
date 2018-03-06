Added inLinks functionality






T2ci - Damping factor reduced from 0.85 to 0.55 causes convergence to happen sooner.
T2cii - Top ranked results from Task 2 with unlimited convergence iterations seem to maintain same spot. Lower ranked items have changes in their ordering.
T2ciii - 	2 elements from the top 10 of Task-2C-iii-Graph1 occur in the top 10 of Task-2B-Graph1
			4 elements from the top 10 of Task-2C-iii-Graph2 occur in the top 10 of Task-2B-Graph2
			Pros: 
				1.
				2.
			Cons:
				1.
				2.


BASE LINE - - - - - - - - - - - - - - - - - - - - - - -

BFS Graph1
636.7395227335555
563.6124790598514
551.3122596544034
555.9907390609376
557.9627436716559
560.6812649453698
562.027441392493
562.2282801303966
561.9815221021122
561.901372071453
Sources #: 1
Sinks #: 116

DFS Graph2
604.8511849845348
515.6509334511012
463.5951733825911
431.805855615563
423.00647742903897
434.63359287090896
447.59762502774277
453.9737228929998
455.21789948496223
454.08354745526515
452.63143122728576
451.74891111225315
451.4975088170742
451.5933006511438
Sources #: 1
Sinks #: 107
- - - - - - - - - - - - - - - - - - - - - - - - - - - -

BFS Graph1 - Damping Factor - 0.55
816.0455063428032
792.5752652485813
790.5294867316087
791.4222977887431
791.593273408057
791.7421391238349

DFS Graph2 - Damping Factor - 0.55
793.8977466236977
760.6880565991598
747.5455701257093
742.4604254389111
742.2619564711588
743.4857543349355
744.1190490013552
744.2772411299045
744.2807166974015

BFS Graph1 - Page Rank 4 iterations
636.7395227335555
563.6124790598514
551.3122596544034
555.9907390609376

DFS Graph2 - Page Rank 4 iterations
604.8511849845348
515.6509334511012
463.5951733825911
431.805855615563


- - - - - - - - - - - - - - - - - - - - - - - - - - - - -






# Web Crawler - Focused Crawling - Java Implementation

Task1: Crawling the documents using both Breadth-First and Depth-First traversal techniques.
Task2: Crawling the documents with respect to a set of keywords and considering valid variations of the keywords.

## Setting up

The ./src/ folder consists of source code files
```
1. CrawlCaller.java - Consists of the Main Method to kick start the program.
2. CrawlConfig.java - Contains configurable parameters for the call.
3. Crawler.java     - Has the crawler logic implemented.
4. FileUtility.java - Utility file for accessing flat files.
5. Dictionary.java  - Accesses and maintains a dictionary of English words.
6. Tests.java       - Consists of tests to validate the crawler ouputs.
```

The ./output/ folder consists of sample outputs of the call
```
1. crawlBreadthFirst.txt - First 1000 urls from breadth first traversal.
2. crawlDepthFirst.txt   - First 1000 urls from depth first traversal.
3. crawlFocused.txt      - First 1000 urls from focused crawling.
```

### External Libraries Referenced

The following external library might need to be referenced to the build path, or via using a Maven dependency.

1. JSoup

```
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.9.2</version>
</dependency>
```


2. English Words Collection - words.txt

```
Citation: https://github.com/dwyl/english-words/blob/master/words.txt
```

The above libraries have been included in the ./externalLibrary/ folder

### Maximum Depth Reached

Task 1:
```
DFS         : 6
BFS         : 2
```

Task 2:
```
FocusedCrawl: 4
```
