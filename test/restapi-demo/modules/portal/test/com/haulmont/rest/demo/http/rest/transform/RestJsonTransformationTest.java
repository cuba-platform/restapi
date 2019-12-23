/*
 * Copyright (c) 2008-2017 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.http.rest.transform;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.addon.restapi.api.config.RestJsonTransformations;
import com.haulmont.addon.restapi.api.transform.AbstractEntityJsonTransformer;
import com.haulmont.addon.restapi.api.transform.StandardEntityJsonTransformer;
import com.haulmont.addon.restapi.api.transform.JsonTransformationDirection;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import mockit.Expectations;
import mockit.Mocked;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 */
public class RestJsonTransformationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mocked
    protected AppBeans appBeans;

    @Mocked
    protected Scripting scripting;

    @Mocked
    protected RestJsonTransformations restJsonTransformations;

    @Test
    public void renameEntityAndAttributes() throws Exception {

        Map<String, String> carAttributesToRename = new HashMap<>();
        carAttributesToRename.put("oldVin", "vin");
        StandardEntityJsonTransformer carTransformer = new StandardEntityJsonTransformer("ref$OldCar", "ref_Car", "1.0", JsonTransformationDirection.FROM_VERSION);
        carTransformer.setAttributesToRename(carAttributesToRename);
        initStandardTransformer(carTransformer);


        Map<String, String> modelAttributesToRename = new HashMap<>();
        modelAttributesToRename.put("oldName", "name");
        StandardEntityJsonTransformer modelTransformer = new StandardEntityJsonTransformer("ref$OldModel", "ref$Model", "1.0", JsonTransformationDirection.FROM_VERSION);
        modelTransformer.setAttributesToRename(modelAttributesToRename);
        initStandardTransformer(modelTransformer);

        new Expectations() {{
            restJsonTransformations.getTransformer("ref$OldCar", "1.0", JsonTransformationDirection.FROM_VERSION); result = carTransformer; minTimes = 0;
            restJsonTransformations.getTransformer("ref$OldModel", "1.0", JsonTransformationDirection.FROM_VERSION); result = modelTransformer; minTimes = 0;
        }};

        assertEquals("ref_Car", carTransformer.getTransformedEntityName());

        String srcJson = getFileContent("renameEntityAndAttributes.json");
        String resultJson = carTransformer.transformJson(srcJson);

        DocumentContext context = JsonPath.parse(resultJson);
        assertEquals("ref_Car", context.read("$._entityName"));
        assertEquals("VIN-01", context.read("$.vin"));

        thrown.expect(PathNotFoundException.class);
        context.read("$.oldVin");

        assertEquals("ref$Model", context.read("$.model._entityName"));
        assertEquals("Audi", context.read("$.model.name"));
        assertEquals("Audi Manufacturer", context.read("$.model.manufacturer"));

        thrown.expect(PathNotFoundException.class);
        context.read("$.model.oldName");
    }

    @Test
    public void renameEntityAndAttributeInArray() throws Exception {
        Map<String, String> attributesToRename = new HashMap<>();
        attributesToRename.put("lastName", "familyName");
        StandardEntityJsonTransformer standardEntityJsonTransformer = new StandardEntityJsonTransformer("app$OldEntity", "app$NewEntity", "1.0", JsonTransformationDirection.TO_VERSION);
        standardEntityJsonTransformer.setAttributesToRename(attributesToRename);

        assertEquals("app$NewEntity", standardEntityJsonTransformer.getTransformedEntityName());

        String srcJson = getFileContent("renameEntityAndAttributeInArray.json");
        String resultJson = standardEntityJsonTransformer.transformJson(srcJson);

        DocumentContext context = JsonPath.parse(resultJson);
        assertEquals("app$NewEntity", context.read("$.[0]._entityName"));
        assertEquals("Bob", context.read("$.[0].firstName"));
        assertEquals("Smith", context.read("$.[0].familyName"));

        assertEquals("app$NewEntity", context.read("$.[1]._entityName"));
        assertEquals("Jack", context.read("$.[1].firstName"));
        assertEquals("Daniels", context.read("$.[1].familyName"));

        thrown.expect(PathNotFoundException.class);
        context.read("$.[0].lastName");

        thrown.expect(PathNotFoundException.class);
        context.read("$.[1].lastName");
    }

    @Test
    public void transformCompositionAttribute() throws Exception {
        Map<String, String> attributesToRename = new HashMap<>();
        attributesToRename.put("oldDescription", "description");
        StandardEntityJsonTransformer repairTransformer = new StandardEntityJsonTransformer("ref$OldRepair", "ref$Repair", "1.0", JsonTransformationDirection.FROM_VERSION);
        repairTransformer.setAttributesToRename(attributesToRename);
        initStandardTransformer(repairTransformer);

        new Expectations() {{
            AppBeans.get(RestJsonTransformations.class); result = restJsonTransformations; minTimes = 0;
            AppBeans.get("cuba_RestJsonTransformations"); result = restJsonTransformations; minTimes = 0;
            restJsonTransformations.getTransformer("ref$OldRepair", "1.0", JsonTransformationDirection.FROM_VERSION); result = repairTransformer; minTimes = 0;
        }};

        String srcJson = getFileContent("transformCompositionAttribute.json");
        String resultJson = repairTransformer.transformJson(srcJson);

        DocumentContext context = JsonPath.parse(resultJson);
        assertEquals("ref_Car", context.read("$._entityName"));
        assertEquals("VIN-01", context.read("$.vin"));

        assertEquals("ref$Repair", context.read("$.repairs.[0]_entityName"));
        assertEquals("Repair 1", context.read("$.repairs.[0].description"));

        thrown.expect(PathNotFoundException.class);
        context.read("$.repairs.[0].oldDescription");
    }

    private void initStandardTransformer(StandardEntityJsonTransformer transformer) throws NoSuchFieldException, IllegalAccessException {
        Field jsonTransformationsField = AbstractEntityJsonTransformer.class.getDeclaredField("jsonTransformations");
        jsonTransformationsField.setAccessible(true);
        jsonTransformationsField.set(transformer, restJsonTransformations);
    }

    @Test
    public void removeAttributes() throws Exception {
        Set<String> attributesToRemove = new HashSet<>();
        attributesToRemove.add("description");
        attributesToRemove.add("model");
        StandardEntityJsonTransformer carTransformer = new StandardEntityJsonTransformer("ref_Car", "ref_Car", "1.0", JsonTransformationDirection.FROM_VERSION);
        carTransformer.setAttributesToRemove(attributesToRemove);
        initStandardTransformer(carTransformer);

        String srcJson = getFileContent("removeAttributes.json");
        String resultJson = carTransformer.transformJson(srcJson);

        DocumentContext context = JsonPath.parse(resultJson);
        assertEquals("ref_Car", context.read("$._entityName"));
        assertEquals("VIN-01", context.read("$.vin"));

        thrown.expect(PathNotFoundException.class);
        context.read("$.model");

        thrown.expect(PathNotFoundException.class);
        context.read("$.description");
    }

    protected String getFileContent(String fileName) throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream("data/" + fileName));
    }
}
