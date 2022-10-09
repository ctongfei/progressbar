package me.tongfei.progressbar;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.BeforeAllCallback;


// Credit to https://stackoverflow.com/a/64070019/2990673
public class StreamSetupExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    @Override
    public void beforeAll(ExtensionContext context) {
        // We need to use a unique key here, across all usages of this particular extension.
        String uniqueKey = this.getClass().getName();
        Object value = context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).get(uniqueKey);
        if (value == null) {
            // First test container invocation.
            context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).put(uniqueKey, this);
            setup();
        }
    }

    void setup() {
        // autoFlush = true for stdout/stderr streams
        System.setOut(new PrintStream(System.out, true));
        System.setErr(new PrintStream(System.err, true));
        System.out.println();
    }

    @Override
    public void close() throws Throwable {
        // do nothing
    }
}
