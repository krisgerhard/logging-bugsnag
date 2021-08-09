package io.quarkiverse.logging.bugsnag.deployment;

import com.bugsnag.Configuration;
import com.bugsnag.Report;
import com.bugsnag.Severity;
import com.bugsnag.callbacks.AppCallback;
import com.bugsnag.callbacks.Callback;
import com.bugsnag.callbacks.DeviceCallback;
import com.bugsnag.callbacks.ServletCallback;
import com.bugsnag.delivery.*;
import com.bugsnag.logback.*;
import com.bugsnag.serialization.SerializationException;
import com.bugsnag.serialization.Serializer;
import com.bugsnag.util.DaemonThreadFactory;
import com.bugsnag.util.FilteredMap;
import io.quarkiverse.logging.bugsnag.BugsnagConfig;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.ExtensionSslNativeSupportBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LogHandlerBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkiverse.logging.bugsnag.BugsnagHandlerValueFactory;
import io.quarkiverse.logging.bugsnag.filters.BugsnagContainerRequestFilter;
import io.quarkus.resteasy.common.spi.ResteasyJaxrsProviderBuildItem;

class LoggingBugsnagProcessor {

    private static final String FEATURE = "bugsnag";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    LogHandlerBuildItem addBugsnagLogHandler(final BugsnagConfig config, final BugsnagHandlerValueFactory bugsnagHandlerValueFactory) {
        return new LogHandlerBuildItem(bugsnagHandlerValueFactory.create(config));
    }

    @BuildStep
    ExtensionSslNativeSupportBuildItem activateSslNativeSupport() {
        return new ExtensionSslNativeSupportBuildItem(FEATURE);
    }

    @BuildStep
    ResteasyJaxrsProviderBuildItem createFilterBuildItem() {
        return new ResteasyJaxrsProviderBuildItem(BugsnagContainerRequestFilter.class.getName());
    }

    @BuildStep
    ReflectiveClassBuildItem addReflection() {
        return new ReflectiveClassBuildItem(true, true,
                Report.class.getName(),
                Configuration.class.getName(),
                Severity.class.getName(),
                Callback.class.getName(),
                AppCallback.class.getName(),
                DeviceCallback.class.getName(),
                ServletCallback.class.getName(),
                Delivery.class.getName(),
                HttpDelivery.class.getName(),
                SyncHttpDelivery.class.getName(),
                AsyncHttpDelivery.class.getName(),
                OutputStreamDelivery.class.getName(),
                BugsnagMarker.class.getName(),
                LogbackMetaData.class.getName(),
                LogbackMetaDataKey.class.getName(),
                LogbackMetaDataTab.class.getName(),
                ProxyConfiguration.class.getName(),
                Serializer.class.getName(),
                SerializationException.class.getName(),
                DaemonThreadFactory.class.getName(),
                FilteredMap.class.getName(),
                "com.bugsnag.BeforeSendSession",
                "com.bugsnag.DateUtils",
                "com.bugsnag.Diagnostics",
                "com.bugsnag.Exception",
                "com.bugsnag.ExceptionHandler",
                "com.bugsnag.HandledState",
                "com.bugsnag.MetaData",
                "com.bugsnag.Notification",
                "com.bugsnag.Notifier",
                "com.bugsnag.NotifierUtils",
                "com.bugsnag.Session",
                "com.bugsnag.SessionCount",
                "com.bugsnag.SessionPayload",
                "com.bugsnag.SessionCount",
                "com.bugsnag.SessionTracker",
                "com.bugsnag.Stackframe",
                "com.bugsnag.ThreadState"
        );
    }
}
