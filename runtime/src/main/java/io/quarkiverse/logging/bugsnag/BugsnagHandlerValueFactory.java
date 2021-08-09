package io.quarkiverse.logging.bugsnag;

import com.bugsnag.Bugsnag;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import io.quarkus.runtime.configuration.ConfigurationException;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import java.util.Optional;
import java.util.logging.Handler;

@Recorder
public class BugsnagHandlerValueFactory {

    public RuntimeValue<Optional<Handler>> create(final BugsnagConfig config) {
        if (!config.enable) {
            return new RuntimeValue<>(Optional.empty());
        }

        if (!config.apiKey.isPresent()) {
            throw new ConfigurationException("Configuration key \"quarkus.log.bugsnag.api-key\" is required when Bugsnag is enabled, but its value is empty/missing");
        }

        Bugsnag bugsnag = new Bugsnag(config.apiKey.get());
        Config appConfig = ConfigProvider.getConfig();
        Optional<String> appVersion = Optional.of(appConfig.getValue("quarkus.application.version", String.class));
        appVersion.ifPresent(bugsnag::setAppVersion);
        config.notifyReleaseStage.ifPresent((notifyReleaseStage) -> bugsnag.setNotifyReleaseStages(notifyReleaseStage.toArray(new String[0])));
        config.filters.ifPresent((filters) -> bugsnag.setFilters(filters.toArray(new String[0])));
        config.ignoredClasses.ifPresent((ignoredClasses) -> bugsnag.setIgnoreClasses(ignoredClasses.toArray(new String[0])));
        config.projectPackages.ifPresent((projectPackages) -> bugsnag.setProjectPackages(projectPackages.toArray(new String[0])));
        config.releaseStage.ifPresent(bugsnag::setReleaseStage);
        bugsnag.setSendThreads(config.sendThreads);
        config.timeout.ifPresent(bugsnag::setTimeout);

        return new RuntimeValue<>(Optional.of(new BugsnagHandler(bugsnag, config)));
    }
}

