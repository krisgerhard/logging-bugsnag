package io.quarkiverse.logging.bugsnag;

import com.bugsnag.Bugsnag;
import io.quarkiverse.logging.bugsnag.callbacks.JaxrsCallback;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static java.util.logging.ErrorManager.CLOSE_FAILURE;
import static java.util.logging.ErrorManager.WRITE_FAILURE;

public class BugsnagHandler extends Handler {

    private Bugsnag bugsnag;
    private BugsnagConfig config;

    public BugsnagHandler() {
    }

    public BugsnagHandler(final Bugsnag bugsnag, final BugsnagConfig config) {
        this.bugsnag = bugsnag;
        this.config = config;
        if (JaxrsCallback.isAvailable()) {
            bugsnag.addCallback(new JaxrsCallback());
        }
    }

    @Override
    public void publish(final LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        if (record.getLevel().intValue() < config.minimumEventLevel.intValue()) {
            return;
        }
        try {
            bugsnag.notify(record.getThrown());
        } catch (RuntimeException e) {
            reportError(
                    "An exception occurred while creating a new event in Bugsnag",
                    e,
                    WRITE_FAILURE
            );
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
        try {
            bugsnag.close();
        } catch (RuntimeException e) {
            reportError(
                    "An exception occurred while closing the Bugsnag connection",
                    e,
                    CLOSE_FAILURE
            );
        }
    }
}
