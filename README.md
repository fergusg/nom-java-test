# Java Tests

Assumes `Java 8` (developed with `OpenJDK`) and recent `maven`, on `PATH`

## Run

```bash
    mvn install -DskipTests
    java -cp target/my-app-1.0-SNAPSHOT.jar com.example.Main
```

## Tests

None yet.  Give me a break.

Nor any benchmarking.  Ran out of time.

## Comments

1. I've never setup maven before.  That took a while.
1. Wasted lots of time trying to optimized the final merge, not properly appreciating that the producer step is the slow bit.