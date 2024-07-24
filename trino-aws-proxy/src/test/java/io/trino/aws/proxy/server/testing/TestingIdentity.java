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
package io.trino.aws.proxy.server.testing;

import io.trino.aws.proxy.spi.credentials.Identity;

import static java.util.Objects.requireNonNull;

public record TestingIdentity(String user)
        implements Identity
{
    public TestingIdentity
    {
        requireNonNull(user, "username is null");
    }

    @Override
    public String user()
    {
        return user;
    }
}