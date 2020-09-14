Integrating with loggers (e.g. `slf4j`) requires changes to how the progress bar is handled.

To do this, a specific `DelegatingProgressBarConsumer` is required, with `logger::info` (or other logger levels) passed in as a lambda expression:

```java
    // create logger using slf4j
    final Logger logger = LoggerFactory.getLogger("Test");

    try (ProgressBar pb = new ProgressBarBuilder()
            .setInitialMax(100)
            .setTaskName("Test")
            .setConsumer(new DelegatingProgressBarConsumer(logger::info))
            .build()) {
        // your taskName here
    }
```
