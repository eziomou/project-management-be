package io.github.eziomou.pm.endpoint;

import io.quarkus.test.oidc.server.OidcWiremockTestResource;

import java.util.Arrays;
import java.util.HashSet;

abstract class BaseEndpointTest {

    protected String getAccessToken(String userName, String... groups) {
        return OidcWiremockTestResource.getAccessToken(userName, new HashSet<>(Arrays.asList(groups)));
    }
}
