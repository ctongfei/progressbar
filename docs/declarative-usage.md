Since Progressbar `0.6.0`, declarative usage is the preferred way of using a progress bar.

Basically, you wrap an `Iterable`, `Iterator` or `InputStream` (can be seen as an `Iterator[Byte]`) so that when iterating over it, a progress bar automatically tracks its progress. 

This is done by using the static method `ProgressBar.wrap(...)`. The syntax is
``` java
ProgressBar.wrap(iterable, <task name>)
```
Or, if you want to use the builder pattern to customize the progress bar, use 
``` java
ProgressBarBuilder pbb = new ProgressBarBuilder()
// setting the builder
ProgressBar.wrap(iterable, pbb)
```

Examples:

##### Example: Traverses through a Java collection
``` java
for (T x : ProgressBar.wrap(collection, "Traversing")) {
    ...
}
```

##### Example: Reads a large file lazily
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