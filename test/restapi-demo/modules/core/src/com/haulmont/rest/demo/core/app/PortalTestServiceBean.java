/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.app;

import com.haulmont.bpm.entity.stencil.Stencil;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.validation.CustomValidationException;
import com.haulmont.rest.demo.core.entity.Car;
import com.haulmont.rest.demo.core.entity.RefappNotPersistentStringIdEntity;
import com.haulmont.rest.demo.core.entity.TransientDriver;
import com.haulmont.rest.demo.core.exception.CustomHttpClientErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service(PortalTestService.NAME)
public class PortalTestServiceBean implements PortalTestService {

    @Inject
    protected Metadata metadata;

    @Inject
    protected Persistence persistence;

    @Inject
    protected DataManager dataManager;

    @Override
    public void emptyMethod() {}

    @Override
    public void notPermittedMethod() {}

    @Override
    public Integer sum(int number1, String number2) {
        return number1 + Integer.valueOf(number2);
    }

    @Override
    public void methodWithCustomException() {
        throw new CustomHttpClientErrorException(HttpStatus.I_AM_A_TEAPOT, "Server is not a coffee machine");
    }

    @Override
    public void methodWithException() {
        throw new RuntimeException("Error!");
    }

    @Override
    public String overloadedMethod(int param) {
        return "int";
    }

    @Override
    public String overloadedMethod(String param) {
        return "String";
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Override
    public Car findCar(UUID carId, String viewName) {
        Car result;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            result = em.find(Car.class, carId, viewName);
            tx.commit();
        } finally {
            tx.end();
        }
        return result;
    }

    @Override
    public List<Car> findAllCars(String viewName) {
        List<Car> result;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            result = em.createQuery("select c from ref_Car c", Car.class).setViewName(viewName).getResultList();
            tx.commit();
        } finally {
            tx.end();
        }
        return result;
    }

    @Override
    public Car updateCarVin(Car car, String vin) {
        car.setVin(vin);
        return car;
    }

    @Override
    public String concatCarVins(List<Car> cars) {
        StringBuilder sb = new StringBuilder();
        cars.forEach(car -> sb.append(car.getVin()));
        return sb.toString();
    }

    @Override
    public List<Car> updateCarVins(List<Car> cars, String vin) {
        for (Car car : cars) {
            car.setVin(vin);
        }
        return cars;
    }

    @Override
    public String testNullParam(UUID uuid) {
        return uuid == null ? "true" : "false";
    }

    @Override
    public Date testDateParam(Date param) {
        return param;
    }

    @Override
    public BigDecimal testBigDecimalParam(BigDecimal param) {
        return param;
    }

    @Override
    public TestPojo getPojo() throws Exception {
        TestPojo.TestNestedPojo testNestedPojo = new TestPojo.TestNestedPojo();
        testNestedPojo.setNestedField(2);
        TestPojo testPojo = new TestPojo();
        testPojo.setField1("field1 value");
        testPojo.setNestedPojo(testNestedPojo);
        testPojo.setDateField(sdf.parse("15.01.2017 17:56:00"));
        return testPojo;
    }

    @Override
    public List<TestPojo> getPojoList() throws ParseException {
        TestPojo pojo1 = new TestPojo();
        pojo1.setField1("pojo1");

        TestPojo.TestNestedPojo testNestedPojo = new TestPojo.TestNestedPojo();
        testNestedPojo.setNestedField(1);
        pojo1.setNestedPojo(testNestedPojo);
        pojo1.setDateField(sdf.parse("15.01.2017 17:56:00"));

        TestPojo pojo2 = new TestPojo();
        pojo2.setField1("pojo2");

        return Arrays.asList(pojo1, pojo2);
    }

    @Override
    public int methodWithPojoParameter(TestPojo pojo) {
        return pojo.getNestedPojo().getNestedField();
    }

    @Override
    public int methodWithPojoCollectionParameter(List<TestPojo> pojoCollection) {
        return pojoCollection.size();
    }

    @Override
    public String methodWithPojoCollectionParameter2(List<TestPojo> pojoCollection) {
        TestPojo testPojo = pojoCollection.get(0);
        return testPojo.getField1();
    }

    @Override
    public int validatedMethod(@Pattern(regexp = "\\d+") String code) {
        return 0;
    }

    @Nonnull
    @Override
    public String validatedMethodResult(@Pattern(regexp = "\\d+") String code) {
        if ("100".equals(code)) {
            throw new CustomValidationException("Epic fail!");
        }
        return null;
    }

    @Override
    public Stencil notPersistedEntity() {
        Stencil stencil = metadata.create(Stencil.class);
        stencil.setTitle("stencil 1");
        return stencil;
    }

    @Override
    public List<String> methodWithPrimitiveListArguments(List<String> stringList, int[] intArray, int intArgument) {
        return stringList;
    }

    @Override
    public List<PojoWithNestedEntity> getPojosWithNestedEntity() {
        LoadContext<Car> ctx = LoadContext.create(Car.class).
                setView(View.LOCAL)
                .setQuery(LoadContext.createQuery("select c from ref_Car c order by c.vin"));
        List<Car> cars = dataManager.loadList(ctx);
        final int[] counter = {1};
        return cars.stream()
                .map(car -> new PojoWithNestedEntity(car, counter[0]++))
                .collect(Collectors.toList());
    }

    @Override
    public List<PojoWithNestedEntity> getPojosWithNestedEntityWithView() {
        LoadContext<Car> ctx = LoadContext.create(Car.class).
                setView("car-with-colour")
                .setQuery(LoadContext.createQuery("select c from ref_Car c order by c.vin"));
        List<Car> cars = dataManager.loadList(ctx);
        final int[] counter = {1};
        return cars.stream()
                .map(car -> new PojoWithNestedEntity(car, counter[0]++))
                .collect(Collectors.toList());
    }

    @Override
    public int methodWithListOfMapParam(List<Map<String, BigDecimal>> param) {
        return param.stream()
                .mapToInt(Map::size)
                .sum();
    }

    @Override
    public List<Map<String, Object>> methodReturnsListOfMap() {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("key1", 1);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("key2", 2);
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(map1);
        list.add(map2);
        return list;
    }

    @Override
    public TransientDriver getTransientDriver() {
        TransientDriver transientDriver = metadata.create(TransientDriver.class);
        transientDriver.setName("Bob");
        transientDriver.setGroupName("Group1");
        return transientDriver;
    }

    @Override
    public RefappNotPersistentStringIdEntity getRefappNotPersistentStringIdEntity() {
        RefappNotPersistentStringIdEntity transientDriver = metadata.create(RefappNotPersistentStringIdEntity.class);
        transientDriver.setId("1");
        transientDriver.setName("Bob");
        return transientDriver;
    }
}