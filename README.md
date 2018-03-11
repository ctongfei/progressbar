# progressbar [![Maven Central](https://img.shields.io/maven-central/v/me.tongfei/progressbar.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/me.tongfei/progressbar)

A simple console progress bar. Progress bar writing now runs on another thread.

<img src="https://i.gyazo.com/1c02d51927e769cf245a108f5a8dfaf5.gif" width="600"/>

Menlo, Fira Mono, Source Code Pro or SF Mono are recommended for optimal visual effects.

For Consolas or Andale Mono fonts, use `ProgressBarStyle.ASCII` (see below) because the box-drawing glyphs are not aligned properly in these fonts.

<img src="https://i.gyazo.com/e01943454443f90c9499c00a6c197a41.gif" width="600"/>

Documentation:
 - [Javadoc](https://javadoc.io/doc/me.tongfei/progressbar/0.6.0)

Maven:
```xml
    <dependency>
      <groupId>me.tongfei</groupId>
      <artifactId>progressbar</artifactId>
      <version>0.6.0</version>
    </dependency>
```

Declarative usage (from 0.6.0):
```java
// Looping over a collection:
for (T x : ProgressBar.wrap(collection, "TaskName")) {
    ...
    // Progress will be automatically monitored by a progress bar
}
```

Imperative usage:

```java
ProgressBar pb = new ProgressBar("Test", 100); // name, initial max
 // Use ProgressBar("Test", 100, ProgressBarStyle.ASCII) if you want ASCII output style
pb.start(); // the progress bar starts timing
// Or you could combine these two lines like this:
//   ProgressBar pb = new ProgressBar("Test", 100).start();
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
  // Can set n to be less than zero: this means that this progress bar would become
  // indefinite: the max would be unknown.
  ...
  pb.setExtraMessage("Reading..."); // Set extra message to display at the end of the bar
}
pb.stop() // stops the progress bar
```

#### Changelog
[CHANGELOG](https://github.com/ctongfei/progressbar/blob/master/CHANGELOG.md)
