package io.quarkiverse.logging.bugsnag;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

import static io.quarkus.runtime.annotations.ConfigPhase.RUN_TIME;

/**
 * Configuration for Bugsnag logging.
 */
@ConfigRoot(phase = RUN_TIME, name = "log.bugsnag")
public class BugsnagConfig {

    /**
     * Determine whether to enable the Bugsnag logging extension.
     */
    @ConfigItem
    boolean enable;

    /**
     * Bugsnag API key
     */
    @ConfigItem
    public Optional<String> apiKey;

    /**
     * The minimum event level.
     *
     * Every log statement that is greater than minimum event level is sent to Bugsnag.
     */
    @ConfigItem(defaultValue = "WARN")
    public Level minimumEventLevel;

    /**
     * Set which keys should be filtered when sending metaData to Bugsnag.
     * Use this when you want to ensure sensitive information, such as passwords
     * or credit card information is stripped from metaData you send to Bugsnag.
     * Any keys in metaData which contain these strings will be marked as
     * [FILTERED] when send to Bugsnag.
     */
    @ConfigItem
    public Optional<Set<String>> filters;

    /**
     * Set which exception classes should be ignored (not sent) by Bugsnag.
     */
    @ConfigItem
    public Optional<Set<String>> ignoredClasses;

    /**
     * Set for which releaseStages errors should be sent to Bugsnag.
     * Use this to stop errors from development builds being sent.
     */
    @ConfigItem
    public Optional<Set<String>> notifyReleaseStage;

    /**
     * Set which packages should be considered part of your application.
     * Bugsnag uses this to help with error grouping, and stacktrace display.
     */
    @ConfigItem
    public Optional<Set<String>> projectPackages;

    /**
     * Set the current "release stage" of your application.
     */
    @ConfigItem
    public Optional<String> releaseStage;

    /**
     * Set whether Bugsnag should capture and report thread-state for all
     * running threads. This is often not useful for Java web apps, since
     * there could be thousands of active threads depending on your
     * environment.
     */
    @ConfigItem(defaultValue = "false")
    public boolean sendThreads;

    /**
     * Set a timeout (in ms) to use when delivering Bugsnag error reports and sessions.
     */
    @ConfigItem
    public Optional<Integer> timeout;
}