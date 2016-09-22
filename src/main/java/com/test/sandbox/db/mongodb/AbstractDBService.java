package com.test.sandbox.db.mongodb;

import org.bson.Document;

import java.util.List;
import java.util.Map;

/**
 * Base service class for all DB calls
 */
public abstract class AbstractDBService<T> {

    public abstract Object create(Map<String, Object> params);

    public abstract List<Document> read(Map<String, Object> params);

    public abstract Object update(Map<String, Object> params);

    public abstract Object delete(Map<String, Object> params);

    public abstract Boolean exists(Map<String, Object> params);

    public abstract Long count(Map<String, Object> params);

}