# progressbar
[![Maven Central](https://img.shields.io/maven-central/v/me.tongfei/progressbar.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/me.tongfei/progressbar)

A console progress bar for JVM with minimal runtime overhead.

<img src="https://i.imgur.com/E4mvuWh.gif" width="600"/>

Menlo, 
[Fira Mono](https://github.com/mozilla/Fira), 
[Source Code Pro](https://github.com/adobe-fonts/source-code-pro) or 
[SF Mono](https://developer.apple.com/fonts/) are recommended for optimal visual effects.

For Consolas or Andale Mono fonts, use `ProgressBarStyle.ASCII` because the box-drawing glyphs are not aligned properly in these fonts.

<img src="https://i.gyazo.com/e01943454443f90c9499c00a6c197a41.gif" width="600"/>

##### Documentation
 - [Javadoc](https://javadoc.io/doc/me.tongfei/progressbar/0.7.0)
 

##### Installation

Maven:

```xml
  <dependency>
      <groupId>me.tongfei</groupId>
      <artifactId>progressbar</artifactId>
      <version>0.7.0</version>
  </dependency>
```

##### Usage
Declarative usage (since `0.6.0`):
```java
// Looping over a collection:
for (T x in ProgressBar.wrap(collection, "TaskName")) {
    ...
    // Progress will automatically monitored by a progress bar
}
```

Imperative usage (since `0.7.0` switched to Java's try-with-resource pattern):

```java
// try-with-resource block
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
