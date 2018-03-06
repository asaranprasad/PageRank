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
454.39279056114674
400.977661730294
356.99275038673625
332.53072946352955
319.50741906314965
312.47231362023115
308.73711686986746
306.74071276630764
305.69327453518275
305.14515283875386
304.8624537851347
304.71792034820567
Sources #: 0
Sinks #: 7

DFS Graph2
541.5159416673431
530.2111517490546
513.4959020482503
509.2498346140174
506.96848884242996
506.5044612360242
506.32743237789174
506.3161968405469
Sources #: 0
Sinks #: 7
- - - - - - - - - - - - - - - - - - - - - - - - - - - -

BFS Graph1 - Damping Factor - 0.55
680.0893660487912
654.8065891828851
638.9045198085157
632.9387732778513
630.8062126055536
630.0489059924884
629.784100475113
629.6917720702307

DFS Graph2 - Damping Factor - 0.55
748.3285136050742
748.3149896641073
744.066464191493
743.495133335067
743.3346463626146
743.3326540140802

BFS Graph1 - Page Rank 4 iterations
454.39279056114674
400.977661730294
356.99275038673625
332.53072946352955

DFS Graph2 - Page Rank 4 iterations
541.5159416673431
530.2111517490546
513.4959020482503
509.2498346140174


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
