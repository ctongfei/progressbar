#### Selecting a visual style

Currently `progressbar` supports three visual style sets:

 - `COLORFUL_UNICODE_BLOCK` (default): Rendered using Unicode box drawing symbols with ANSI colors. Good if your font is among Menlo, Fira Mono, Source Code Pro or SF Mono; and your terminal supports ANSI colors.

 - `UNICODE_BLOCK`: Rendered using Unicode box drawing symbols. 

 - `ASCII`: Rendered using pure ASCII symbols. This is preferred if your terminal's font is either Consolas or Andale Mono.

To set these, using a progress bar [builder](builder.md) with the `setStyle` method, passing in one of the enum values above.
``` java
ProgressBarBuilder pbb = new ProgressBarBuilder()
    .setStyle(ProgressBarStyle.<STYLE>);
```


Since `0.10.0` you can customize the progress bar style also with a builder:

``` java
ProgressBarBuilder pbb = ProgressBar.builder()
    // ...
    .setStyle(ProgressBarStyle.builder()
                      .colorCode((byte) 33)  // the ANSI color code
                      .leftBracket("{")
                      .rightBracket("}")
                      .block('-')
                      .rightSideFractionSymbol('+')
                      .build()
   )
   // ...
```
