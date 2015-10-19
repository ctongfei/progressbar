# progressbar [![Maven Central](https://img.shields.io/maven-central/v/me.tongfei/progressbar_2.11.svg)](https://maven-badges.herokuapp.com/maven-central/me.tongfei/progressbar_2.11)
A simple console progress bar.
Does not create too much output: Minimal update time is 1 second (virtually no overhead).

 - New: Scaladoc at http://ctongfei.github.io/progressbar/api/.
 - New in 0.3.2: Supports extra information at the end of the progress bar.
 - New in 0.3.1: Better display.
 - New in 0.3.0: Added `maxHint` method for progress bars. The maximum value for progress bars can be reset.

SBT:
```scala
libraryDependencies += "me.tongfei" %% "progressbar" % "0.3.2"
```

Maven:
```xml
    <dependency>
      <groupId>me.tongfei</groupId>
      <artifactId>progressbar_2.11</artifactId>
      <version>0.3.2</version>
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
  pb.maxHint(n)
  // reset the max of this progress bar as n. This may be useful when the program
  // gets new information about the current progress.
  ...
  pb.setExtraMessage("Reading...") // Set extra message to display at the end of the bar
}
pb.stop() // stops the progress bar
```

Output style:
```
Test 62% [================         ] 621/1000 (0:00:12 / 0:00:07) Reading...
```
