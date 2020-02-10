package com.haulmont.rest.demo.core.roles;

import com.haulmont.cuba.security.app.role.AnnotatedRoleDefinition;
import com.haulmont.cuba.security.app.role.annotation.*;
import com.haulmont.cuba.security.entity.Access;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.role.*;

/**
 * Role grants full access for REST API
 */
@Role(name = RestFullAccessRole.NAME, securityScope = "REST", isSuper = true)
public class RestFullAccessRole extends AnnotatedRoleDefinition {

    public static final String NAME = "rest-full-access";

    @Override
    public EntityPermissionsContainer entityPermissions() {
        return super.entityPermissions();
    }

    @Override
    public EntityAttributePermissionsContainer entityAttributePermissions() {
        return super.entityAttributePermissions();
    }

    @Override
    public SpecificPermissionsContainer specificPermissions() {
        return super.specificPermissions();
    }

    @Override
    public ScreenPermissionsContainer screenPermissions() {
        return super.screenPermissions();
    }

    @Override
    public ScreenComponentPermissionsContainer screenComponentPermissions() {
        return super.screenComponentPermissions();
    }

}
