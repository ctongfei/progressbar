Currently `progressbar` supports three visual style sets:

 - `COLORFUL_UNICODE_BLOCK` (default): Rendered using Unicode box drawing symbols with ANSI colors. Good if your font is among Menlo, Fira Mono, Source Code Pro or SF Mono; and your terminal supports ANSI colors.

 - `UNICODE_BLOCK`: Rendered using Unicode box drawing symbols. 

 - `ASCII`: Rendered using pure ASCII symbols. This is preferred if your terminal's font is either Consolas or Andale Mono.

To set these, using a progress bar builder with the `setStyle` method, passing in one of the enum values above.
``` java
ProgressBarBuilder pbb = new ProgressBarBuilder()
    .setStyle(ProgressBarStyle.<STYLE>)
```
