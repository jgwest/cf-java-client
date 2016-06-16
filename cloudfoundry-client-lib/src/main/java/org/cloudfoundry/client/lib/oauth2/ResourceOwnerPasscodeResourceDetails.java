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

import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;

/**
 * Resource details for passcode auth.
 *
 * @see ResourceOwnerPasscodeAccessTokenProvider
 *
 * @author Matthias Winzeler <matthias.winzeler@gmail.com>
 */
public class ResourceOwnerPasscodeResourceDetails extends BaseOAuth2ProtectedResourceDetails {
    private String passcode;

    public ResourceOwnerPasscodeResourceDetails() {
        setGrantType("password");
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }
}
