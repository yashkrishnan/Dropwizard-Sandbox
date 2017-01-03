package com.example.sandbox.db.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import com.example.sandbox.configurations.SandboxConfiguration;
import com.example.sandbox.constants.DatabaseConstants;
import com.example.sandbox.constants.TextConstants;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Util class for MongoDB initialization and MongoDB applications
 */
public class MongoService implements Managed {
    private static final Logger logger = LoggerFactory.getLogger(MongoService.class);
    private static MongoClient mongoClient = null;
    private static MongoDatabase mongoDatabase = null;

    public static void initializeMongoDB() {
        if (mongoClient == null) {
            // Try to connect to mongodb server
            try {
                mongoClient = new MongoClient(SandboxConfiguration.getMongoHost(), Integer.parseInt(SandboxConfiguration.getMongoPort()));
            } catch (MongoException e) {
                logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
                if (mongoClient != null) {
                    mongoClient.close();
                }
            }
        }
        // Get database. If database doesn't exists, mongodb will create it
        if (mongoClient != null) {
            mongoDatabase = mongoClient.getDatabase(DatabaseConstants.SANDBOX_DB);
        }
    }

    public static void deinitializeMongoDB() {
        if (mongoClient != null) {
            mongoDatabase = null;
            // Try to connect to mongodb server
            try {
                mongoClient.close();
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }

    // Return initialized DB object
    public static MongoDatabase getDB() {
        if (mongoDatabase == null) {
            initializeMongoDB();
        }
        return mongoDatabase;
    }

    @Override
    public void start() throws Exception {
        initializeMongoDB();
    }

    @Override
    public void stop() throws Exception {
        deinitializeMongoDB();
    }
}