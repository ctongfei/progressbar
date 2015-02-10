# progressbar
A simple console progress bar.

SBT:
```scala
libraryDependencies += "me.tongfei" % "progressbar_2.11" % "0.1.1"
```

Usage:

```scala
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
Test [===================================               ] 70% (Elapsed: 0:00:14 Remaining: 0:00:06)
```
