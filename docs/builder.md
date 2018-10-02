# Progress bar builders

Since `0.7.0`, apart from standard constructors, you can also use the so-called builder pattern to customize a progress bar.

All `setXXX()` (also `showSpeed`) clauses belwo are optional. 

``` java
ProgressBarBuilder pbb = new ProgressBarBuilder()
    .setInitialMax(<initial max>)
    .setStyle(ProgressBarStyle.<style>)
    .setTaskName(<task name>)
    .setUnit(<unit name>, <unit size>)
    .setUpdateIntervalMillis(<update interval>)
    .showSpeed();

for (T x : ProgressBar.wrap(collection, pbb)) {
    ...
}
```
