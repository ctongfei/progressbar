# progressbar
A simple console progress bar.
Does not create too much output: Minimal update time is 1 second.

SBT:
```scala
libraryDependencies += "me.tongfei" % "progressbar_2.11" % "0.2.0"
```

Maven:
```xml
    <dependency>
      <groupId>me.tongfei</groupId>
      <artifactId>progressbar_2.11</artifactId>
      <version>0.2.0</version>
    </dependency>
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
Test [=========================                         ] 49% (0:00:10 / 0:00:10)
```
