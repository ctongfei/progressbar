# progressbar [![Maven Central](https://img.shields.io/maven-central/v/me.tongfei/progressbar.svg)](https://maven-badges.herokuapp.com/maven-central/me.tongfei/progressbar)
A simple console progress bar.

Maven:
```xml
    <dependency>
      <groupId>me.tongfei</groupId>
      <artifactId>progressbar</artifactId>
      <version>0.4.2</version>
    </dependency>
```

Usage:

```java
ProgressBar pb = new ProgressBar("Test", 100);
pb.start(); // the progress bar starts timing
some loop {
  ...
  pb.step(); // step by 1
  pb.stepBy(n); // step by n
  ...
  pb.stepTo(n); // step directly to n
  ...
  pb.maxHint(n);
  // reset the max of this progress bar as n. This may be useful when the program
  // gets new information about the current progress.
  ...
  pb.setExtraMessage("Reading..."); // Set extra message to display at the end of the bar
}
pb.stop() // stops the progress bar
```

Output style:
```
Test 62% [================         ] 621/1000 (0:00:12 / 0:00:07) Reading...
```

#### Changelog

 - 0.4.2: Default output stream is changed to `System.err`; can be customized in constructor.
 - 0.4.1: Added a `stepTo` method to `ProgressBar`s.
 - 0.4.0: Migrated from Scala to Java: less dependencies.
