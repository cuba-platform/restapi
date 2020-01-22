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
@Role(name = RestFullAccessRole.NAME, securityScope = "REST")
public class RestFullAccessRole extends AnnotatedRoleDefinition {

    public static final String NAME = "rest-full-access";

    @Override
    @DefaultEntityAccess(allow = {EntityOp.CREATE, EntityOp.READ, EntityOp.UPDATE, EntityOp.DELETE})
    public EntityPermissionsContainer entityPermissions() {
        return super.entityPermissions();
    }

    @Override
    @DefaultEntityAttributeAccess(EntityAttrAccess.MODIFY)
    public EntityAttributePermissionsContainer entityAttributePermissions() {
        return super.entityAttributePermissions();
    }

    @Override
    @DefaultSpecificAccess(Access.ALLOW)
    public SpecificPermissionsContainer specificPermissions() {
        return super.specificPermissions();
    }

    @Override
    @DefaultScreenAccess(Access.ALLOW)
    public ScreenPermissionsContainer screenPermissions() {
        return super.screenPermissions();
    }

    @Override
    public ScreenComponentPermissionsContainer screenComponentPermissions() {
        return super.screenComponentPermissions();
    }

}
