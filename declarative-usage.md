Since Progressbar `0.6.0`, declarative usage is the preferred way of using a progress bar.

Basically, one can wrap a stream/collection with `ProgressBar.wrap(...)` so that when iterating/reading/writing over it, a progress bar automatically tracks its progress. The type of your collection/stream does not change after wrapped with a progress bar.
The collection/stream types supported are:

 - `T[]`;
 - `java.lang.Iterable<T>`;
 - `java.util.Iterator<T>`;
 - `java.io.InputStream` (can be regarded as an `Iterator<Byte>`);
 - `java.io.Reader` (can be regarded as an `Iterator<Char>`);
 - `java.io.OutputStream` (dual of `InputStream`);
 - `java.io.Writer` (dual of `Reader`);
 - `java.util.Spliterator<T>`;
 - `java.util.Stream<T>` (actually any `S` such that `S extends BaseStream<T, S>`. When wrapping over a primitive stream, boxing overhead may be incurred).

The syntax for the method call is
```java
ProgressBar.wrap(collection, taskName)
```
Or, if you want to use the [builder pattern](builder.md) to customize the progress bar, use 
``` java
ProgressBarBuilder pbb = new ProgressBarBuilder()
    .setXXX().setYYY();  // setting the builder
ProgressBar.wrap(iterable, pbb)
```

Examples:

##### Example 1: Traverses through a Java collection

If the size of the collection is known, the progress bar's max will be automatically set as the size of the collection; otherwise the progress bar will be indefinite.

``` java
for (T x : ProgressBar.wrap(collection, "Traversing")) {
    ...
}
```

##### Example 2: Loops over an integer range

Since `0.7.2`, tracking the progress of sequential or parallel Java streams is supported. 
``` java
ProgressBar.wrap(IntStream.range(left, right).parallel(), "Task").forEach(i -> {
        ...
    });
```

##### Example 3: Reads a large file lazily

When wrapping around a `java.io.InputStream`, whether it is a `java.io.FileInputStream` will be detected. If successful, the file's full size in byte will be retrieved and set as the progress bar's max; otherwise, the progress bar will be indefinite.

``` java
ProgressBarBuilder pbb = new ProgressBarBuilder()
    .setTaskName("Reading")
    .setUnit("MiB", 1048576); // setting the progress bar to use MiB as the unit

try (Reader reader = new BufferedReader(new InputStreamReader(
        ProgressBar.wrap(new FileInputStream(f), pbb)
    ))) 
{
    ...
}
```
