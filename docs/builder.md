# Progress bar builders

Since `0.7.0`, apart from standard constructors, you can also use the so-called builder pattern to customize a progress bar.

All `setXXX()` (also `showSpeed`) clauses below are optional. 

``` java
ProgressBarBuilder pbb = new ProgressBarBuilder()
    .setInitialMax(<initial max>)
    .setStyle(ProgressBarStyle.<style>)
    .setTaskName(<taskName name>)
    .setUnit(<unit name>, <unit size>)
    .setUpdateIntervalMillis(<update interval>)
    .setMaxRenderedLength(<max rendered length in terminal>)
    .showSpeed();
  // or .showSpeed(new DecimalFormat("#.##")) to customize speed display
    .setEtaFunction(state -> ...)
for (T x : ProgressBar.wrap(collection, pbb)) {
    ...
}
```
