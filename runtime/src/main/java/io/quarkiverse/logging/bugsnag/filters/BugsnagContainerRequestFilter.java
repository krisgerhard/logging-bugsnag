package io.quarkiverse.logging.bugsnag.filters;

import com.bugsnag.Bugsnag;
import org.jboss.resteasy.core.interception.jaxrs.PreMatchContainerRequestContext;
import org.jboss.resteasy.spi.HttpRequest;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.Priorities.AUTHENTICATION;

@Provider
@PreMatching
@Priority(AUTHENTICATION)
public class BugsnagContainerRequestFilter implements ContainerRequestFilter {

    private static final ThreadLocal<HttpRequest> HTTP_REQUEST = new ThreadLocal<>();

    public static HttpRequest getRequest() {
        return HTTP_REQUEST.get();
    }

    public static void clearRequest() {
        HTTP_REQUEST.remove();
        Bugsnag.clearThreadMetaData();
    }

    private void trackServletSession() {
        for (Bugsnag bugsnag : Bugsnag.uncaughtExceptionClients()) {
            if (bugsnag.shouldAutoCaptureSessions()) {
                bugsnag.startSession();
            }
        }
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        trackServletSession();
        if (containerRequestContext instanceof PreMatchContainerRequestContext) {
            HTTP_REQUEST.set(((PreMatchContainerRequestContext) containerRequestContext).getHttpRequest());
        }
    }
}
