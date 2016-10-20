# progressbar [![Maven Central](https://img.shields.io/maven-central/v/me.tongfei/progressbar.svg)](https://maven-badges.herokuapp.com/maven-central/me.tongfei/progressbar)
A simple console progress bar. Progress bar writing now runs on another thread.

```
Reading  44% │█████████████▎                │  4434/10000 (0:00:18 / 0:00:23)
```

Do not use Consolas or Andale Mono for terminal font because the box-drawing glyphs
in these fonts are not aligned properly. Menlo, Fira Mono, Source Code Pro or SF Mono
are recommended for optimal visual effects.

Maven:
```xml
    <dependency>
      <groupId>me.tongfei</groupId>
      <artifactId>progressbar</artifactId>
      <version>0.5.0</version>
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

#### Changelog

 - 0.5.0: Separated the progress bar thread from the main thread for better performance. Fixed the character offset issue. Thanks @rualpe !
 - 0.4.3: Changed the symbols to box-drawing characters; more fine-grained display. Thanks @hrj !
 - 0.4.2: Default output stream is changed to `System.err`; can be customized in constructor. Thanks @AluisioASG !
 - 0.4.1: Added a `stepTo` method to `ProgressBar`s. Thanks @svenmauer !
 - 0.4.0: Migrated from Scala to Java: less dependencies.
