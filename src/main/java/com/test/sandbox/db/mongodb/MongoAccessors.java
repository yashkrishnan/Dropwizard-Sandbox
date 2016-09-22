package com.test.sandbox.db.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.test.sandbox.constants.DatabaseConstants;
import com.test.sandbox.constants.TextConstants;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Accessor class to wrap mongo operations
 */
public class MongoAccessors extends AbstractDBService {

    private static final Logger logger = LoggerFactory.getLogger(MongoAccessors.class);
    private static MongoDatabase mongoDatabase = null;

    public static Long checkDocumentCount(String collectionName, BasicDBObject queryObject) {
        long count = 0;
        if (collectionName != null) {
            MongoCollection<Document> collection = getMongoCollection(collectionName);
            count = collection.count(queryObject);
        }
        return count;
    }

    public static ObjectId insertDocumentToCollection(String collectionName, Document newObject) {
        if (collectionName != null) {
            MongoCollection<Document> collection = getMongoCollection(collectionName);
            collection.insertOne(newObject);
            return newObject.getObjectId(DatabaseConstants._ID);
        }
        return null;
    }

    private static MongoCollection<Document> getMongoCollection(String collectionName) {
        MongoCollection<Document> collection = null;
        try {

            if (mongoDatabase == null) {
                mongoDatabase = MongoService.getDB();
            }
            if (collectionName != null) {
                collection = mongoDatabase.getCollection(collectionName);
            }
        } catch (MongoException e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
        return collection;
    }

    public static Boolean checkDocumentFromCollection(String collectionName, BasicDBObject queryObject) {
        boolean status = false;
        if (collectionName != null) {
            MongoCollection<Document> collection = getMongoCollection(collectionName);
            FindIterable<Document> findIterable = collection.find(queryObject);
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            status = mongoCursor.hasNext();
            mongoCursor.close();
        }
        return status;
    }

    public static Boolean checkDocumentFromCollection(String collectionName, BasicDBObject queryObject, BasicDBObject sortObject) {
        boolean status = false;
        if (collectionName != null) {
            MongoCollection<Document> collection = getMongoCollection(collectionName);
            FindIterable<Document> findIterable = collection.find(queryObject).sort(sortObject);
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            status = mongoCursor.hasNext();
            mongoCursor.close();
        }
        return status;
    }

    public static Document fetchDocumentFromCollection(String collectionName, BasicDBObject queryObject) {
        if (collectionName != null) {
            MongoCollection<Document> collection = getMongoCollection(collectionName);
            FindIterable<Document> findIterable = collection.find(queryObject);
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            if (mongoCursor.hasNext()) {
                return mongoCursor.next();
            }
            mongoCursor.close();
        }
        return null;
    }

    public static Document fetchDocumentFromCollection(String collectionName, BasicDBObject queryObject, BasicDBObject projectionFields) {
        if (collectionName != null) {
            MongoCollection<Document> collection = getMongoCollection(collectionName);
            FindIterable<Document> findIterable = collection.find(queryObject).projection(projectionFields);
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            if (mongoCursor.hasNext()) {
                return mongoCursor.next();
            }
            mongoCursor.close();
        }
        return null;
    }

    public static Document fetchDocumentFromCollection(String collectionName, BasicDBObject queryObject, BasicDBObject sortObject, BasicDBObject projectionFields) {
        if (collectionName != null) {
            MongoCollection<Document> collection = getMongoCollection(collectionName);
            FindIterable<Document> findIterable = collection.find(queryObject);

            if (sortObject != null) {
                findIterable.sort(sortObject);
            }
            if (projectionFields != null) {
                findIterable.projection(projectionFields);
            }
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            if (mongoCursor.hasNext()) {
                return mongoCursor.next();
            }
            mongoCursor.close();
        }
        return null;
    }

    public static List<Document> fetchDocumentsFromCollection(String collectionName, BasicDBObject queryObject) {
        List<Document> documentList = new ArrayList<>();
        if (collectionName != null) {
            try {
                MongoCollection<Document> collection = getMongoCollection(collectionName);
                FindIterable<Document> findIterable = collection.find(queryObject);
                MongoCursor<Document> mongoCursor = findIterable.iterator();
                while (mongoCursor.hasNext()) {
                    documentList.add(mongoCursor.next());
                }
                mongoCursor.close();
            } catch (MongoException e) {
                logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
            }
        }
        return documentList;
    }

    public static List<Document> fetchDocumentsFromCollection(String collectionName, BasicDBObject queryObject, BasicDBObject sortFields) {
        List<Document> documentList = new ArrayList<>();
        if (collectionName != null) {
            try {
                MongoCollection<Document> collection = getMongoCollection(collectionName);
                FindIterable<Document> findIterable = collection.find(queryObject);
                if (sortFields != null) {
                    findIterable.sort(sortFields);
                }
                MongoCursor<Document> mongoCursor = findIterable.iterator();
                while (mongoCursor.hasNext()) {
                    documentList.add(mongoCursor.next());
                }
                mongoCursor.close();
            } catch (MongoException e) {
                logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
            }
        }
        return documentList;
    }

    public static List<Document> fetchDocumentsFromCollection(String collectionName, BasicDBObject queryObject, BasicDBObject sortFields, BasicDBObject projectionFields, Integer skipCount, Integer fetchLimit) {
        List<Document> documentList = new ArrayList<>();
        if (collectionName != null) {
            try {
                MongoCollection<Document> collection = getMongoCollection(collectionName);
                FindIterable<Document> findIterable = collection.find(queryObject);
                if (sortFields != null) {
                    findIterable.sort(sortFields);
                }
                if (projectionFields != null) {
                    findIterable.projection(projectionFields);
                }
                if (skipCount != null) {
                    findIterable.skip(skipCount);
                }
                if (fetchLimit != null) {
                    findIterable.limit(fetchLimit);
                }
                MongoCursor<Document> mongoCursor = findIterable.iterator();
                while (mongoCursor.hasNext()) {
                    documentList.add(mongoCursor.next());
                }
                mongoCursor.close();
            } catch (MongoException e) {
                logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
            }
        }
        return documentList;
    }

    public static List<Document> fetchDocumentsFromCollection(String collectionName, BasicDBObject queryObject, BasicDBObject sortFields, Integer skipCount, Integer fetchLimit) {
        List<Document> documentList = new ArrayList<>();
        if (collectionName != null) {
            MongoCollection<Document> collection = getMongoCollection(collectionName);
            FindIterable<Document> findIterable = collection.find(queryObject).sort(sortFields).skip(skipCount).limit(fetchLimit);
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            while (mongoCursor.hasNext()) {
                documentList.add(mongoCursor.next());
            }
            mongoCursor.close();
        }
        return documentList;
    }

    public static long updateDocumentInCollection(String collectionName, BasicDBObject queryObject, BasicDBObject replaceObject) {
        UpdateResult updateResult = UpdateResult.unacknowledged();
        if (collectionName != null) {
            MongoCollection<Document> collection = getMongoCollection(collectionName);
            updateResult = collection.updateOne(queryObject, replaceObject);
        }
        return updateResult.getModifiedCount();
    }

    public static long updateDocumentsInCollection(String collectionName, BasicDBObject queryObject, BasicDBObject replaceObject) {
        UpdateResult updateResult = UpdateResult.unacknowledged();
        if (collectionName != null) {
            MongoCollection<Document> collection = getMongoCollection(collectionName);
            updateResult = collection.updateMany(queryObject, replaceObject);
        }
        return updateResult.getModifiedCount();
    }

    public static String getLastIssuedEntityId(String collectionName, String idType, String sortKey) {
        String lastEntityId = TextConstants.ALPHA_NUMERIC_FIRST_ID;
        if (collectionName != null) {
            List<Document> documentList = fetchDocumentsFromCollection(collectionName, new BasicDBObject(), new BasicDBObject(sortKey, -1), 0, 1);
            Document document;
            if (!documentList.isEmpty()) {
                document = documentList.get(0);
                lastEntityId = document.getString(idType);
            }
        }
        return lastEntityId;
    }

    public static long removeDocumentInCollection(String collectionName, BasicDBObject queryObject) {
        DeleteResult deleteResult = DeleteResult.unacknowledged();
        if (collectionName != null) {
            try {
                MongoCollection<Document> collection = getMongoCollection(collectionName);
                deleteResult = collection.deleteOne(queryObject);
            } catch (MongoException e) {
                logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
            }
        }
        return deleteResult.getDeletedCount();
    }

    public static long removeDocumentsInCollection(String collectionName, BasicDBObject queryObject) {
        DeleteResult deleteResult = DeleteResult.unacknowledged();
        if (collectionName != null) {
            try {
                MongoCollection<Document> collection = getMongoCollection(collectionName);
                deleteResult = collection.deleteMany(queryObject);
            } catch (MongoException e) {
                logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
            }
        }
        return deleteResult.getDeletedCount();
    }

    public static Boolean ensureIndexInCollection(String collectionName, String indexName) {
        if (collectionName != null) {
            try {
                MongoCollection<Document> collection = getMongoCollection(collectionName);
                ListIndexesIterable<Document> indexes = collection.listIndexes(Document.class);
                for (Document document : indexes) {
                    if (document.containsValue(indexName)) {
                        return true;
                    }
                }
            } catch (MongoException e) {
                logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
            }
        }
        return false;
    }

    public static String createIndexInCollection(String collectionName, BasicDBObject indexObject) {
        String indexResult = null;
        if (collectionName != null) {
            try {
                MongoCollection<Document> collection = getMongoCollection(collectionName);
                indexResult = collection.createIndex(indexObject);
            } catch (MongoException e) {
                logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
            }
        }
        return indexResult;
    }

    public static List<Document> aggregateDocumentsFromCollection(String collectionName, List<BasicDBObject> pipeline) {
        List<Document> documentList = new ArrayList<>();
        if (collectionName != null) {
            try {
                MongoCollection<Document> collection = getMongoCollection(collectionName);
                AggregateIterable<Document> aggregateIterable = collection.aggregate(pipeline);
                MongoCursor<Document> mongoCursor = aggregateIterable.iterator();
                while (mongoCursor.hasNext()) {
                    documentList.add(mongoCursor.next());
                }
                mongoCursor.close();
            } catch (MongoException e) {
                logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
            }
        }
        return documentList;
    }

    @Override
    public Object create(Map params) {
        return null;
    }

    @Override
    public List<Document> read(Map params) {
        return null;
    }

    @Override
    public Object update(Map params) {
        return null;
    }

    @Override
    public Object delete(Map params) {
        return null;
    }

    @Override
    public Boolean exists(Map params) {
        return null;
    }

    @Override
    public Long count(Map params) {
        return null;
    }
}