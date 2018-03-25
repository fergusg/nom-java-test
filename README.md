# Java Tests

Assumes `Java 8` (developed with `OpenJDK`) and recent `maven`, on `PATH`

## Run

```bash
    mvn install -DskipTests
    java -cp target/my-app-1.0-SNAPSHOT.jar com.example.Main
```

## Tests

None yet.  Give me a break.

Nor any proper benchmarking.  Ran out of time.  On my PC, 1 Producer ~ 22s, all 4 ~32s.

## Time Taken

~ 3.5hrs

Initially I set myself a ~1hr limit, but being very rusty and out-of-date in Java (e.g., I've never setup maven before.)  In 2hrs I was able to get the pipeline working for a single producer.  It took me another hour to add multi-threading, plus an extra 30mins to tidy up, etc.

## Comments

1. I wasted lots of time trying to optimized the final merge, not properly appreciating that the producer step is the slow bit.

1. Most glaring inefficiency is that we wait for **all** threads to finish before starting the merge.  In this case it's not too bad since all threads execute in about the same time. Also, the merge step is *relatively* quick.

1. We could seek to remove the second `stream` in `bin()`.  This is more for code neatness as I strongly suspect the performance overhead is relatively small.

1. In this CPU-limited application, it seems probable that this works best when the number of threads isn't greater than the number of (idle) cores, so that at most 1 thread executes in each core (to minimise context switching).  At scale, we might use a more sophisticated strategy such as those provided in `java.util.concurrent`.  If I/O were to be a limit (even partially), all bets are off.