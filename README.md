# progressbar
A simple console progress bar

Usage:

```
val pb = new ProgressBar("Test", 100)
pb.start() // the progress bar starts timing
some loop {
  ...
  pb.step() // step by 1
  pb.stepBy(n) // step by n
  ...
}

pb.stop() // stops the progress bar
```

Output style:
```
Test [==================================================] 100% (Elapsed: PT5.431S Remaining: PT0S)
```
