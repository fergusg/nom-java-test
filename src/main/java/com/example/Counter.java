package com.example;

public interface Counter {

    /**
     * This method should read the complete data set from all producers and count the occurrences of each unique value
     * across all producers.
     * It should return a String listing unique values given by the producers along with the count for how many times
     * that value was seen.
     * In the case where the number of unique values is larger than the limit given, then the number of values returned
     * should be equal to the limit and these much be the values with the highest counts.
     *
     * @param limit the maximum number of values to return.
     * @param producers the producers to read values from.
     * @return a list of unique values and their count.
     */
    String getTop(int limit, Producer... producers);

}
