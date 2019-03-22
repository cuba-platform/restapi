[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![Build Status](https://travis-ci.org/cuba-platform/restapi.svg?branch=master)](https://travis-ci.org/cuba-platform/restapi)
[![Documentation](https://img.shields.io/badge/documentation-online-03a9f4.svg)](https://github.com/cuba-platform/restapi/wiki)

# Overview

The universal REST v2 API of the platform enables working with the platform by sending simple HTTP-request.

The universal REST API provides the following functionality:
- CRUD operations on entities.
- Execution of predefined JPQL queries.
- Execution of service methods.
- Getting metadata (entities, views, enumerations, datatypes).
- Getting current user permissions (access to entities, attributes, specific permissions).
- Getting current user information (name, language, time zone, etc.).
- Uploading and downloading files.

REST API uses the OAuth2 protocol for authentication and supports anonymous access.

# Installation

To install the component in your project, do the following steps:

1. Open your application in CUBA Studio.

2. Open **Project -> Properties** in the project tree.

3. On the **App components** pane click the **Plus** button next to **Custom components**.

4. Paste the add-on coordinates in the corresponding field as follows: `group:name:version`.
    
    - Artifact group: `com.haulmont.addon.restapi`;
    - Artifact name: `restapi-global`;
    - Version: `add-on version`.
      
    Specify the add-on version compatible with the used version of the CUBA platform.

    | Platform Version | Add-on Version |
    |------------------|----------------|
    | 7.1-SNAPSHOT     | 0.1-SNAPSHOT   |
    
    Example: `com.haulmont.addon.restapi:restapi-global:0.1-SNAPSHOT`

5. Click **OK** to save the project properties.

After that the REST v2 API functions will be available at the `{host:port}/{module prefix}/rest/v2/*` URL.
