/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.aws.proxy.server.rest;

import io.trino.aws.proxy.server.rest.ResourceSecurity.AccessType.Access.PublicAccess;
import io.trino.aws.proxy.server.rest.ResourceSecurity.AccessType.Access.SigV4Access;
import io.trino.aws.proxy.spi.signing.SigningServiceType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Objects.requireNonNull;

@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface ResourceSecurity
{
    enum AccessType
    {
        PUBLIC(new PublicAccess()),
        S3(new SigV4Access(SigningServiceType.S3)),
        STS(new SigV4Access(SigningServiceType.STS)),
        LOGS(new SigV4Access(SigningServiceType.LOGS));

        public sealed interface Access
        {
            record PublicAccess() implements Access {}

            record SigV4Access(SigningServiceType signingServiceType) implements Access {}
        }

        private final Access access;

        AccessType(Access access)
        {
            this.access = requireNonNull(access, "access is null");
        }

        public Access access()
        {
            return access;
        }
    }

    AccessType value();
}