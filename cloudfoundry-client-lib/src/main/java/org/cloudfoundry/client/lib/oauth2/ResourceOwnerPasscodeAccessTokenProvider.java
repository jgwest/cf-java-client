/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cloudfoundry.client.lib.oauth2;

import java.util.Iterator;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * This class extends the Spring Security OAuth ResourceOwnerPasswordAccessTokenProvider
 * to pass a passcode param instead of a username and password param.
 *
 * This is needed because CloudFoundry uses this passcode submission which is not part of the OAuth2 standard
 * and thus it's not provided by Spring Security Oauth.
 *
 * It is used in SSO scenarios, similar to what the CF CLI with <code>cf login --sso</code> does.
 *
 * @author Matthias Winzeler <matthias.winzeler@gmail.com>
 */

public class ResourceOwnerPasscodeAccessTokenProvider extends ResourceOwnerPasswordAccessTokenProvider {
    // copied from ResourceOwnerPasswordAccessTokenProvider and modified to use
    // ResourceOwnerPasscodeResourceDetails
    @Override
    public boolean supportsResource(OAuth2ProtectedResourceDetails resource) {
        return resource instanceof ResourceOwnerPasscodeResourceDetails && "password".equals(resource.getGrantType());
    }

    @Override
    public OAuth2AccessToken obtainAccessToken(OAuth2ProtectedResourceDetails details, AccessTokenRequest request)
            throws UserRedirectRequiredException, AccessDeniedException, OAuth2AccessDeniedException {

        ResourceOwnerPasscodeResourceDetails resource = (ResourceOwnerPasscodeResourceDetails) details;
        return retrieveToken(request, resource, getParametersForTokenRequest(resource, request), new HttpHeaders());
    }

    // copied from ResourceOwnerPasswordAccessTokenProvider and modified to send passcode instead of user/pw
    private MultiValueMap<String, String> getParametersForTokenRequest(ResourceOwnerPasscodeResourceDetails resource, AccessTokenRequest request) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.set("grant_type", "password");

        form.set("passcode", resource.getPasscode());
        form.putAll(request);

        if (resource.isScoped()) {

            StringBuilder builder = new StringBuilder();
            List<String> scope = resource.getScope();

            if (scope != null) {
                Iterator<String> scopeIt = scope.iterator();
                while (scopeIt.hasNext()) {
                    builder.append(scopeIt.next());
                    if (scopeIt.hasNext()) {
                        builder.append(' ');
                    }
                }
            }

            form.set("scope", builder.toString());
        }

        return form;

    }
}
