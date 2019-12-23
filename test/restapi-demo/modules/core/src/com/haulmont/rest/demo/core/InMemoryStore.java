/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core;

import com.haulmont.cuba.core.app.DataStore;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.ValueLoadContext;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component(InMemoryStore.NAME)
public class InMemoryStore implements DataStore {

    public static final String NAME = "refapp_InMemoryStore";

    @Inject
    private Metadata metadata;

    private Map<String, Map<Object, Entity>> entities = new ConcurrentHashMap<>();

    @Nullable
    @Override
    public <E extends Entity> E load(LoadContext<E> context) {
        Map<Object, Entity> instances = entities.get(context.getMetaClass());
        if (instances == null)
            return null;
        else
            return (E) instances.get(context.getId());
    }

    @Override
    public <E extends Entity> List<E> loadList(LoadContext<E> context) {
        Map<Object, Entity> instances = entities.get(context.getMetaClass());
        if (instances == null)
            return Collections.emptyList();
        else
            return new ArrayList(instances.values());
    }

    @Override
    public long getCount(LoadContext<? extends Entity> context) {
        Map<Object, Entity> instances = entities.get(context.getMetaClass());
        if (instances == null)
            return 0;
        else
            return instances.size();
    }

    @Override
    public Set<Entity> commit(CommitContext context) {
        Set<Entity> result = new HashSet<>();

        for (Entity entity : context.getCommitInstances()) {
            String metaClassName = metadata.getClassNN(entity.getClass()).getName();
            Map<Object, Entity> instances = entities.get(metaClassName);
            if (instances == null) {
                instances = new ConcurrentHashMap<>();
                entities.put(metaClassName, instances);
            }
            instances.put(entity.getId(), entity);
            result.add(entity);
        }
        for (Entity entity : context.getRemoveInstances()) {
            String metaClassName = metadata.getClassNN(entity.getClass()).getName();
            Map<Object, Entity> instances = entities.get(metaClassName);
            if (instances != null) {
                instances.remove(entity.getId());
            }
            result.add(entity);
        }

        return result;
    }

    @Override
    public List<KeyValueEntity> loadValues(ValueLoadContext context) {
        return new ArrayList<>();
    }

    public void clear() {
        entities.clear();
    }
}
