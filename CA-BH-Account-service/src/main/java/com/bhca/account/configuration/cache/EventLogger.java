package com.bhca.account.configuration.cache;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventLogger implements CacheEventListener<Object, Object> {

    private static final Logger logger = LoggerFactory.getLogger(EventLogger.class);

    @Override
    public void onEvent(CacheEvent<?, ?> event) {
        logger.info("Cache event {} - {} - {}", event.getKey(), event.getOldValue(), event.getNewValue());
    }
}