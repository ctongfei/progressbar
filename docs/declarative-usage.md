Since Progressbar `0.6.0`, declarative usage is the preferred way of using a progress bar.

Basically, you wrap an `Iterable`, `Iterator`, `InputStream` (can be seen as an `Iterator<Byte>`), 
`Spliterator` or `Stream`, so that when iterating over it, a progress bar automatically tracks its progress. The type of your collection does not change after wrapped with a progress bar.

This is done by using the static method `ProgressBar.wrap(...)`. The syntax is
``` java
ProgressBar.wrap(collection, <taskName name>)
```
Or, if you want to use the [builder pattern](builder.md) to customize the progress bar, use 
``` java
ProgressBarBuilder pbb = new ProgressBarBuilder()
// setting the builder
ProgressBar.wrap(iterable, pbb)
```

Examples:

##### Example: Traverses through a Java collection

If the size of the collection is known, the progress bar's max will be automatically set as the size of the collection; otherwise the progress bar will be indefinite.

``` java
for (T x : ProgressBar.wrap(collection, "Traversing")) {
    ...
}
```

##### Example: Loops over an integer range

Since `0.7.2`, tracking the progress of sequential or parallel Java streams is supported. 
``` java
ProgressBar.wrap(IntStream.range(left, right).parallel(), "Task").forEach(i -> {
        ...
    });
```

##### Example: Reads a large file lazily

When wrapping around a `java.io.InputStream`, whether it is a `java.io.FileInputStream` will be detected. If successful, the file's full size in byte will be retrieved and set as the progress bar's max; otherwise, the progress bar will be indefinite.

``` java
ProgressBarBuilder pbb = new ProgressBarBuilder()
    .setTaskName("Reading")
    .setUnit("MB", 1048576); // setting the progress bar to use MB as the unit

try (Reader reader = new BufferedReader(new InputStreamReader(
        ProgressBar.wrap(new FileInputStream(f), pbb)
    ))) 
{
    ...
}
```
