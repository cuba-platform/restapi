/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.rest.demo.core.validation;

import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.rest.demo.core.entity.Currency;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TestCurrencyValidator implements ConstraintValidator<TestCurrencyClassConstraint, Currency> {
    @Override
    public void initialize(TestCurrencyClassConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(Currency currency, ConstraintValidatorContext context) {
        if (PersistenceHelper.isLoaded(currency, "code")) {
            if ("BAN".equals(currency.getCode())) {
                return false;
            }
        }
        return true;
    }
}