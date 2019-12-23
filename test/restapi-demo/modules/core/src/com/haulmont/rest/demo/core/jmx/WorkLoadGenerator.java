/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.jmx;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.UuidSource;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.rest.demo.core.entity.Currency;
import com.haulmont.rest.demo.core.entity.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.*;

@Component("refapp_WorkLoadGeneratorMBean")
public class WorkLoadGenerator implements WorkLoadGeneratorMBean {

    protected static String BASE_PATH = "com/haulmont/refapp/core/sampledata/";

    @Inject
    protected Resources resources;

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected UuidSource uuidSource;

    @Override
    public String generateUsers(Integer count) {
        try {
            validateCount(count);

            List<String> names = getList(BASE_PATH + "names");
            List<String> surnames = getList(BASE_PATH + "surnames");

            try (Transaction transaction = persistence.createTransaction()) {

                EntityManager entityManager = persistence.getEntityManager();

                Group group = getGroup(entityManager);

                for (int i = 0; i < count; i++) {
                    User user = metadata.create(User.class);

                    String firstName = (String) getRandomValue(names);
                    user.setFirstName(firstName);
                    String login = firstName + firstName.hashCode() + i;
                    user.setLogin(login);

                    user.setPassword("cuba123");

                    String surname = (String) getRandomValue(surnames);
                    user.setLastName(surname);

                    String name = firstName + " " + surname;
                    user.setName(name);

                    user.setGroup(group);

                    entityManager.persist(user);
                }

                transaction.commit();
            }

            return "Done";

        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    protected void validateCount(Integer count) {
        Preconditions.checkNotNullArgument(count, "Entities not generated because the count is empty!");
        if (count <= 0) {
            throw new IllegalArgumentException("Entities not generated because the count must be positive!");
        }
    }

    protected Group getGroup(EntityManager entityManager) {
        TypedQuery<Group> query = entityManager.createQuery("select g from sec$Group g", Group.class);
        return query.getFirstResult();
    }

    @Override
    public String generateCars(Integer count) {
        try {
            validateCount(count);

            try (Transaction transaction = persistence.createTransaction()) {

                EntityManager entityManager = persistence.getEntityManager();

                List<Model> models = getModelsNE(entityManager);
                List<Colour> colours = getColoursNE(entityManager);
                List<Driver> drivers = getDriversNE(entityManager);
                List<Currency> currencies = getCurrencies(entityManager);
                List<Seller> sellers = getSellersNE(entityManager);

                for (int i = 0; i < count; i++) {
                    Car car = metadata.create(Car.class);

                    String vinRandom = String.valueOf(RandomUtils.nextInt(0, 10000000) + 100000);
                    car.setVin(vinRandom);

                    Model model = (Model) getRandomValue(models);
                    car.setModel(model);

                    Colour colour = (Colour) getRandomValue(colours);
                    car.setColour(colour);

                    setDriverAllocation(entityManager, drivers, car);

                    Seller seller = (Seller) getRandomValue(sellers);
                    car.setSeller(seller);

                    Currency currency = (Currency) getRandomValue(currencies);
                    car.setCurrency(currency);

                    CarDocumentation carDocumentation = createCarDocumentation(model, vinRandom, entityManager);
                    car.setCarDocumentation(carDocumentation);

                    CarDetails carDetails = createCarDetails(entityManager, car);
                    car.setDetails(carDetails);

                    entityManager.persist(car);
                }

                transaction.commit();

            }
            return "Done";

        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    protected CarDetails createCarDetails(EntityManager entityManager, Car car) {
        TypedQuery<CarDetails> query = entityManager.createQuery("select g from ref$CarDetails g", CarDetails.class);
        CarDetails carDetails = query.getFirstResult();

        if (carDetails != null) {
            return carDetails;
        }

        carDetails = metadata.create(CarDetails.class);
        carDetails.setCar(car);
        carDetails.setDetails("Default details");

        List<CarDetailsItem> carDetailsItems = createCarDetailsItems(entityManager, carDetails);
        carDetails.setItems(carDetailsItems);

        entityManager.persist(carDetails);
        return carDetails;
    }

    protected List<CarDetailsItem> createCarDetailsItems(EntityManager entityManager, CarDetails carDetails) {
        TypedQuery<CarDetailsItem> query = entityManager.createQuery("select g from ref$CarDetailsItem g",
                CarDetailsItem.class);
        List<CarDetailsItem> carDetailsItems = query.getResultList();

        if (carDetailsItems != null) {
            return carDetailsItems;
        }

        return Arrays.asList(
                createCarDetailsItem("4 wheels", carDetails, entityManager),
                createCarDetailsItem("Body", carDetails, entityManager),
                createCarDetailsItem("Headlights", carDetails, entityManager)
        );
    }

    protected CarDetailsItem createCarDetailsItem(String info, CarDetails carDetails, EntityManager entityManager) {
        CarDetailsItem carDetailsItem = metadata.create(CarDetailsItem.class);

        carDetailsItem.setInfo(info);
        carDetailsItem.setCarDetails(carDetails);

        entityManager.persist(carDetailsItem);
        return carDetailsItem;
    }

    protected CarDocumentation createCarDocumentation(Model model, String vinRandom, EntityManager entityManager) {
        CarDocumentation carDocumentation = metadata.create(CarDocumentation.class);

        String title = "Documentation for " + model.getName() + vinRandom;
        carDocumentation.setTitle(title);

        entityManager.persist(carDocumentation);
        return carDocumentation;
    }

    protected List<Seller> getSellersNE(EntityManager entityManager) {
        TypedQuery<Seller> querySeller = entityManager.createQuery("select g from ref$Seller g", Seller.class);
        List<Seller> sellers = querySeller.getResultList();

        if (sellers.size() == 0) {
            throw new IllegalStateException("Entities not generated because a list of sellers is empty!");
        }

        return sellers;
    }

    protected List<Currency> getCurrencies(EntityManager entityManager) {
        TypedQuery<Currency> queryCurrency = entityManager.createQuery("select g from ref$Currency g", Currency.class);
        List<Currency> currencies = queryCurrency.getResultList();

        if (currencies.size() == 0) {
            createCurrencies(entityManager, currencies);
        }

        return currencies;
    }

    protected List<Driver> getDriversNE(EntityManager entityManager) {
        TypedQuery<Driver> queryDriver = entityManager.createQuery("select g from ref$Driver g", Driver.class);
        List<Driver> drivers = queryDriver.getResultList();

        if (drivers.size() == 0) {
            throw new IllegalStateException("Entities not generated because a list of drivers is empty!");
        }

        return drivers;
    }

    protected List<Colour> getColoursNE(EntityManager entityManager) {
        TypedQuery<Colour> queryColour = entityManager.createQuery("select g from ref$Colour g", Colour.class);
        List<Colour> colours = queryColour.getResultList();

        if (colours.size() == 0) {
            throw new IllegalStateException("Entities not generated because a list of colours is empty!");
        }

        return colours;
    }

    protected List<Model> getModelsNE(EntityManager entityManager) {
        TypedQuery<Model> query = entityManager.createQuery("select g from ref$Model g", Model.class);
        List<Model> models = query.getResultList();

        if (models.size() == 0) {
            throw new IllegalStateException("Entities not generated because a list of models is empty!");
        }

        return models;
    }

    protected void setDriverAllocation(EntityManager entityManager, List<Driver> drivers, Car car) {
        DriverAllocation driverAllocation = metadata.create(DriverAllocation.class);

        driverAllocation.setCar(car);

        Driver driver = (Driver) getRandomValue(drivers);
        driverAllocation.setDriver(driver);

        entityManager.persist(driverAllocation);

        HashSet<DriverAllocation> driverAllocations = new HashSet<>();
        driverAllocations.add(driverAllocation);

        car.setDriverAllocations(driverAllocations);
    }

    protected void createCurrencies(EntityManager entityManager, List<Currency> currencies) {
        String[] names = {"Russian ruble", "British pound", "Euro", "United States dollar"};
        String[] codes = {"RUB", "GBP", "EUR", "USD"};

        for (int i = 0; i < names.length; i++) {
            Currency currency = metadata.create(Currency.class);
            currency.setName(names[i]);
            currency.setCode(codes[i]);
            entityManager.persist(currency);
            currencies.add(currency);
        }

    }

    @Override
    public String generateSellers(Integer count) {
        try {
            validateCount(count);

            List<String> names = getList(BASE_PATH + "names");
            List<String> surnames = getList(BASE_PATH + "surnames");

            try (Transaction transaction = persistence.createTransaction()) {

                EntityManager entityManager = persistence.getEntityManager();

                for (int i = 0; i < count; i++) {
                    Seller seller = metadata.create(Seller.class);

                    String nameRandom = (String) getRandomValue(names);
                    String surnameRandom = (String) getRandomValue(surnames);
                    String name = nameRandom + " " + surnameRandom;
                    seller.setName(name);

                    entityManager.persist(seller);
                }

                transaction.commit();
            }
            return "Done";
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    @Override
    public String generateColours(Integer count) {
        try {
            validateCount(count);

            List<String> colours = getList(BASE_PATH + "colours");

            try (Transaction transaction = persistence.createTransaction()) {

                EntityManager entityManager = persistence.getEntityManager();

                for (int i = 0; i < count; i++) {
                    Colour colour = metadata.create(Colour.class);
                    String name = (String) getRandomValue(colours);
                    colour.setName(name);

                    entityManager.persist(colour);
                }

                transaction.commit();
            }
            return "Done";
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    @Override
    public String generatePlants(Integer count) {
        try {
            validateCount(count);

            List<String> plants = getList(BASE_PATH + "plants");

            try (Transaction transaction = persistence.createTransaction()) {

                EntityManager entityManager = persistence.getEntityManager();

                List<Model> models = getModelsNE(entityManager);

                for (int i = 0; i < count; i++) {
                    CustomExtPlant plant = metadata.create(CustomExtPlant.class);

                    String name = (String) getRandomValue(plants);
                    plant.setName(name);

                    Set<Model> modelSet = generateModelSet(models);
                    plant.setModels(modelSet);

                    entityManager.persist(plant);
                }

                transaction.commit();
            }

            return "Done";

        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    // Generates a set of models for a plant. The maximum allowed count of models is half of the existing models count.
    // One model can be associated with many plants.
    protected Set<Model> generateModelSet(List<Model> models) {
        Set<Model> modelSet = new HashSet<>();
        int countRandom = RandomUtils.nextInt(0, models.size());
        for (int j = 0; j < countRandom / 2; j++) {
            Model model = (Model) getRandomValue(models);
            modelSet.add(model);
        }
        return modelSet;
    }

    @Override
    public String generateRepairs(Integer count) {
        try {
            validateCount(count);

            List<String> repairs = getList(BASE_PATH + "repairs");

            try (Transaction transaction = persistence.createTransaction()) {

                EntityManager entityManager = persistence.getEntityManager();

                List<Car> cars = getCarsNE(entityManager);

                for (int i = 0; i < count; i++) {
                    Repair repair = metadata.create(Repair.class);

                    Car car = (Car) getRandomValue(cars);
                    repair.setCar(car);

                    String description = (String) getRandomValue(repairs);
                    repair.setDescription(description);

                    repair.setDate(new Date(timeSource.currentTimeMillis()));

                    Set<CarToken> carTokens = generateCarTokens(entityManager, repair);
                    repair.setCarTokens(carTokens);

                    entityManager.persist(repair);
                }

                transaction.commit();
            }

            return "Done";

        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    protected Set<CarToken> generateCarTokens(EntityManager entityManager, Repair repair) {
        Set<CarToken> carTokens = new HashSet<>();
        int count = RandomUtils.nextInt(0, 2) + 1;
        for (int i = 0; i < count; i++) {
            CarToken carToken = metadata.create(CarToken.class);

            carToken.setRepair(repair);

            String token = uuidSource.createUuid().toString();
            carToken.setToken(token);

            CarGarageToken carGarageToken = createCarGarageToken(entityManager);
            carToken.setGarageToken(carGarageToken);

            entityManager.persist(carToken);
            carTokens.add(carToken);
        }
        return carTokens;
    }

    protected CarGarageToken createCarGarageToken(EntityManager entityManager) {
        CarGarageToken carGarageToken = metadata.create(CarGarageToken.class);

        String garageToken = uuidSource.createUuid().toString().substring(0, 19);
        carGarageToken.setToken(garageToken);
        carGarageToken.setTitle(garageToken);

        carGarageToken.setDescription("Custom garage token");
        carGarageToken.setLastUsage(timeSource.currentTimestamp());

        entityManager.persist(carGarageToken);

        return carGarageToken;
    }

    protected List<Car> getCarsNE(EntityManager entityManager) {
        TypedQuery<Car> query = entityManager.createQuery("select g from ref_Car g", Car.class);
        List<Car> cars = query.getResultList();

        if (cars.size() == 0) {
            throw new IllegalStateException("Entities not generated because a list of cars is empty!");
        }

        return cars;
    }

    @Override
    public String generateDrivers(Integer count) {
        try {
            validateCount(count);

            List<String> names = getList(BASE_PATH + "names");
            List<String> cities = getList(BASE_PATH + "cities");
            List<String> streets = getList(BASE_PATH + "streets");
            List<String> callsigns = getList(BASE_PATH + "callsigns");

            try (Transaction transaction = persistence.createTransaction()) {

                EntityManager entityManager = persistence.getEntityManager();

                DriverGroup driverGroup = createDriverGroup(entityManager);

                for (int i = 0; i < count; i++) {
                    ExtDriver driver = metadata.create(ExtDriver.class);

                    DriverCallsign driverCallsign = createCallsign(callsigns, entityManager, i);

                    driver.setCallsign(driverCallsign);

                    String name = (String) getRandomValue(names);
                    driver.setName(name);

                    driver.setDriverGroup(driverGroup);

                    driver.setStatus(DriverStatus.ACTIVE);

                    Address address = generateAddress(cities, streets);
                    driver.setAddress(address);

                    entityManager.persist(driver);
                }

                transaction.commit();
            }
            return "Done";
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    protected DriverCallsign createCallsign(List<String> callsigns, EntityManager entityManager, int i) {
        DriverCallsign driverCallsign = metadata.create(DriverCallsign.class);

        String callsignRandom = (String) getRandomValue(callsigns);
        String callsign = callsignRandom + timeSource.currentTimeMillis() + i;
        driverCallsign.setCallsign(callsign);

        entityManager.persist(driverCallsign);

        return driverCallsign;
    }

    protected DriverGroup createDriverGroup(EntityManager entityManager) {
        DriverGroup driverGroup = metadata.create(DriverGroup.class);

        String name = String.valueOf(RandomUtils.nextInt(0, 300));
        driverGroup.setName(name);

        entityManager.persist(driverGroup);

        return driverGroup;
    }

    protected Object getRandomValue(List<?> array) {
        int randomIndex = RandomUtils.nextInt(0, array.size());
        return array.get(randomIndex);
    }

    protected Address generateAddress(List<String> cities, List<String> streets) {

        Address address = metadata.create(Address.class);

        address.setCountry("England");

        String city = (String) getRandomValue(cities);
        address.setCity(city);

        int houseRandom = RandomUtils.nextInt(0, 200);
        address.setHouseNumber(String.valueOf(houseRandom));

        int flatRandom = RandomUtils.nextInt(0, 200);
        address.setFlatNumber(String.valueOf(flatRandom));

        String street = (String) getRandomValue(streets);
        address.setStreet(street);

        int zipCode = RandomUtils.nextInt(0, 900000) + 100000;
        address.setZip(String.valueOf(zipCode));

        address.setSince(new Date(timeSource.currentTimeMillis()));

        return address;
    }

    @Override
    public String generatePricingRegions(Integer count) {
        try {
            validateCount(count);

            List<String> regions = getList(BASE_PATH + "regions");

            try (Transaction transaction = persistence.createTransaction()) {

                EntityManager entityManager = persistence.getEntityManager();

                List<PricingRegion> generatedRegions = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    PricingRegion pricingRegion = metadata.create(PricingRegion.class);

                    String name = (String) getRandomValue(regions);
                    pricingRegion.setName(name);

                    if (!generatedRegions.isEmpty()) {
                        PricingRegion parent = (PricingRegion) getRandomValue(generatedRegions);
                        pricingRegion.setParent(parent);
                    }

                    entityManager.persist(pricingRegion);
                    generatedRegions.add(pricingRegion);
                }

                transaction.commit();
            }

            return "Done";

        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    @Override
    public String generateModels(Integer count) {
        try {
            validateCount(count);

            String path = BASE_PATH + "carModels";
            String json = resources.getResourceAsString(path);
            ArrayList models = new Gson().fromJson(json, ArrayList.class);

            try (Transaction transaction = persistence.createTransaction()) {

                EntityManager entityManager = persistence.getEntityManager();

                int[] numbersOfSeats = {2, 4, 5};

                for (int i = 0; i < count; i++) {
                    LinkedTreeMap carModel = (LinkedTreeMap) getRandomValue(models);

                    ExtModel model = metadata.create(ExtModel.class);

                    ArrayList names = (ArrayList) carModel.get("names");
                    String name = (String) getRandomValue(names);
                    model.setName(name);

                    String manufacturer = (String) carModel.get("manufacturer");
                    model.setManufacturer(manufacturer);

                    int seatRandomIndex = RandomUtils.nextInt(0, numbersOfSeats.length);
                    model.setNumberOfSeats(numbersOfSeats[seatRandomIndex]);

                    int launchYear = RandomUtils.nextInt(0, 28) + 1990;
                    model.setLaunchYear(launchYear);

                    entityManager.persist(model);
                }

                transaction.commit();
            }

            return "Done";

        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    protected List<String> getList(String path) throws IOException {
        InputStream inputStream = resources.getResourceAsStream(path);
        if (inputStream != null) {
            return IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
        }
        return null;
    }

}
