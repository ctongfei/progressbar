# progressbar [![Maven Central](https://img.shields.io/maven-central/v/me.tongfei/progressbar.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/me.tongfei/progressbar)
A simple console progress bar. Progress bar writing now runs on another thread.

<img src="https://i.gyazo.com/1c02d51927e769cf245a108f5a8dfaf5.gif" width="600"/>

Menlo, Fira Mono, Source Code Pro or SF Mono are recommended for optimal visual effects.

For Consolas or Andale Mono fonts, use `ProgressBarStyle.ASCII` (see below) because the box-drawing glyphs are not aligned properly in these fonts.

<img src="https://i.gyazo.com/e01943454443f90c9499c00a6c197a41.gif" width="600"/>

Maven:
```xml
    <dependency>
      <groupId>me.tongfei</groupId>
      <artifactId>progressbar</artifactId>
      <version>0.5.1</version>
    </dependency>
```

Usage:

```java
ProgressBar pb = new ProgressBar("Test", 100); // name, initial max
 // Use ProgressBar("Test", 100, ProgressBarStyle.ASCII) if you want ASCII output style
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

 - 0.5.1: Fixed the refresh problem when progress ended. Added style (Unicode block characters / pure ASCII) support. Thanks @rualpe !
 - 0.5.0: Separated the progress bar thread from the main thread for better performance. Fixed the character offset issue. Thanks @rualpe !
 - 0.4.3: Changed the symbols to box-drawing characters; more fine-grained display. Thanks @hrj !
 - 0.4.2: Default output stream is changed to `System.err`; can be customized in constructor. Thanks @AluisioASG !
 - 0.4.1: Added a `stepTo` method to `ProgressBar`s. Thanks @svenmauer !
 - 0.4.0: Migrated from Scala to Java: less dependencies.
