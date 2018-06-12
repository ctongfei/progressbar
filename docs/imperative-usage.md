Since Progressbar `0.7.0`, Java try-with-resource pattern is used to ensure safe termination of progress bar threads.

To use progress bars imperatively to support mutation to the progress bar in the progress (e.g. manually moving the cursor), use the following syntax:

``` java
try (ProgressBar pb = new ProgressBar("Test", 100)) { // name, initial max
 // Use ProgressBar("Test", 100, ProgressBarStyle.ASCII) if you want ASCII output style
  for (T x : ProgressBar.wrap(collection, "TaskName")) {
    ...
    pb.step(); // step by 1
    pb.stepBy(n); // step by n
    ...
    pb.stepTo(n); // step directly to n
    ...
    pb.maxHint(n);
    // reset the max of this progress bar as n. This may be useful when the program
    // gets new information about the current progress.
    // Can set n to be less than zero: this means that this progress bar would become
    // indefinite: the max would be unknown.
    ...
    pb.setExtraMessage("Reading..."); // Set extra message to display at the end of the bar
  }
} // progress bar stops automatically after completion of try-with-resource block
```
