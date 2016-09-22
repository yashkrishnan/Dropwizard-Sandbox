package com.test.sandbox.applications;

import com.test.sandbox.configurations.SandboxConfiguration;
import com.test.sandbox.constants.GenericConstants;
import com.test.sandbox.constants.PathConstants;
import com.test.sandbox.daos.CredentialDAO;
import com.test.sandbox.daos.UserDAOImpl;
import com.test.sandbox.db.mongodb.MongoService;
import com.test.sandbox.email.EmailPoolManager;
import com.test.sandbox.filters.CORSFilter;
import com.test.sandbox.filters.RequestFilter;
import com.test.sandbox.filters.SessionFilter;
import com.test.sandbox.healthchecks.GenericHealthCheck;
import com.test.sandbox.io.FilePoolManager;
import com.test.sandbox.monitors.ApplicationEventsListener;
import com.test.sandbox.resources.*;
import com.test.sandbox.resources.resourcehelpers.AssetResourceHelper;
import com.test.sandbox.resources.resourcehelpers.CredentialResourceHelper;
import com.test.sandbox.resources.resourcehelpers.UserResourceHelper;
import com.test.sandbox.resources.resourcehelpers.ViewResourceHelper;
import com.test.sandbox.utilities.SessionUtil;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jersey.sessions.SessionFactoryProvider;
import io.dropwizard.lifecycle.ServerLifecycleListener;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.session.SessionHandler;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class SandboxApplication extends Application<SandboxConfiguration> {

    private final Logger logger = LoggerFactory.getLogger(SandboxApplication.class);
    private AssetResourceHelper assetResourceHelper;
    private CredentialResourceHelper credentialResourceHelper;
    private UserResourceHelper userResourceHelper;
    private ViewResourceHelper viewResourceHelper;
    private SessionUtil sessionUtil;

    public SandboxApplication(AssetResourceHelper assetResourceHelper, CredentialResourceHelper credentialResourceHelper, UserResourceHelper userResourceHelper, ViewResourceHelper viewResourceHelper, SessionUtil sessionUtil) {
        this.assetResourceHelper = assetResourceHelper;
        this.credentialResourceHelper = credentialResourceHelper;
        this.userResourceHelper = userResourceHelper;
        this.viewResourceHelper = viewResourceHelper;
        this.sessionUtil = sessionUtil;
    }

    public static void main(String[] args) {
        AssetResourceHelper assetResourceHelper = new AssetResourceHelper();
        CredentialResourceHelper credentialResourceHelper = new CredentialResourceHelper(new CredentialDAO());
        UserResourceHelper userResourceHelper = new UserResourceHelper(new UserDAOImpl());
        ViewResourceHelper viewResourceHelper = new ViewResourceHelper();

        SessionUtil sessionUtil = new SessionUtil();

        try {
            new SandboxApplication(assetResourceHelper, credentialResourceHelper, userResourceHelper, viewResourceHelper, sessionUtil).run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void run(SandboxConfiguration sandboxConfiguration, Environment environment) throws Exception {

        // Test resource class
        environment.jersey().register(new TestResource());

        // Asset resource class
        environment.jersey().register(new AssetResource(SandboxConfiguration.getWebDirectory(), SandboxConfiguration.getResourceDirectory(), assetResourceHelper, sessionUtil));

        // View resource class
        environment.jersey().register(new ViewResource(viewResourceHelper, sessionUtil));

        // User resource class
        environment.jersey().register(new UserResource(userResourceHelper, sessionUtil));

        // Credential resource class
        environment.jersey().register(new CredentialResource(credentialResourceHelper, sessionUtil));

        // Optional Health Check
        environment.healthChecks().register(GenericConstants.GENERIC_HEALTH_CHECK, new GenericHealthCheck());

        // Setting session handler
        environment.jersey().register(SessionFactoryProvider.class);
        environment.servlets().setSessionHandler(new SessionHandler());

        // Setting resource filter
        //environment.servlets().addFilter(GenericConstants.RESOURCE_FILTER, new ResourceFilter()).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, PathConstants.PATH_USER_ROOT);

        // Setting session filter
        environment.servlets().addFilter(GenericConstants.SESSION_FILTER, new SessionFilter()).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, PathConstants.PATH_USER_ROOT);

        // Setting CORS filter
        environment.servlets().addFilter(GenericConstants.CORS_FILTER, new CORSFilter()).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, PathConstants.PATH_FILTER_ROOT);

        // Setting request filter
        environment.jersey().register(new RequestFilter());

        // Custom exception mapper
        // environment.jersey().register(new RuntimeExceptionMapper());

        // Multi part feature
        environment.jersey().register(new MultiPartFeature());

        // Initialize managed Email pool
        environment.lifecycle().manage(new EmailPoolManager());

        // Initialize managed IO pool
        environment.lifecycle().manage(new FilePoolManager());

        environment.lifecycle().addServerLifecycleListener(serverLifecycleListener);

        // Initialize Mongo DB database
        // MongoService.initializeMongoDB();

        environment.lifecycle().manage(new MongoService());

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                logger.info("SERVER SHUTDOWN INITIATED");
                MongoService.deinitializeMongoDB();
            }
        });

        environment.jersey().getResourceConfig().setApplicationName(getName());

        environment.jersey().register(ApplicationEventsListener.class);
    }

    // Configure aspects of the application required before the application is run, like bundles, configuration source providers, etc
    @Override
    public void initialize(Bootstrap<SandboxConfiguration> sandboxConfigurationBootstrap) {
        sandboxConfigurationBootstrap.addBundle(new ViewBundle<>());
        sandboxConfigurationBootstrap.addBundle(new AssetsBundle("/assets/css", "/assets/css", null, "assets/css"));
        sandboxConfigurationBootstrap.addBundle(new AssetsBundle("/assets/img", "/assets/img", null, "assets/img"));
        sandboxConfigurationBootstrap.addBundle(new AssetsBundle("/assets/js", "/assets/js", null, "assets/js"));
        sandboxConfigurationBootstrap.addBundle(new AssetsBundle("/assets/lib", "/assets/lib", null, "assets/lib"));
        sandboxConfigurationBootstrap.addBundle(new AssetsBundle("/assets/plugins", "/assets/plugins", null, "assets/plugins"));
    }

    private final ServerLifecycleListener serverLifecycleListener = new ServerLifecycleListener() {
        @Override
        public void serverStarted(Server server) {
            logger.info("SERVER " + server.getState());
            String host = null;
            ServerConnector serverConnector;
            for (Connector connector : server.getConnectors()) {
                if (connector instanceof ServerConnector) {
                    serverConnector = (ServerConnector) connector;
                    host = serverConnector.getHost();
                    if (host == null) {
                        host = "localhost";
                    }
                    logger.info(serverConnector.getName().toUpperCase() + " PORT : " + serverConnector.getLocalPort());
                    // Do something useful with serverConnector.getLocalPort()
                }
            }
            logger.info("HOST : " + host);
            // 1 MiB = 1024 KiB = 1024 Bytes x 1024 Bytes
            logger.info("MEMORY INFO");
            logger.info("MAX MEMORY : " + Runtime.getRuntime().maxMemory() / (1024 * 1024) + " MiB");
            logger.info("FREE MEMORY : " + Runtime.getRuntime().freeMemory() / (1024 * 1024) + " MiB");
            logger.info("TOTAL MEMORY : " + Runtime.getRuntime().totalMemory() / (1024 * 1024) + " MiB \n");
        }
    };
}
