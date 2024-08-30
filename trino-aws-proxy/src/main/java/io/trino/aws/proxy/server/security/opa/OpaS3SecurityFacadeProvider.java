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
package io.trino.aws.proxy.server.security.opa;

import com.google.inject.Inject;
import io.trino.aws.proxy.spi.credentials.Identity;
import io.trino.aws.proxy.spi.rest.ParsedS3Request;
import io.trino.aws.proxy.spi.security.S3SecurityFacade;
import io.trino.aws.proxy.spi.security.S3SecurityFacadeProvider;
import io.trino.aws.proxy.spi.security.opa.OpaS3SecurityMapper;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class OpaS3SecurityFacadeProvider
        implements S3SecurityFacadeProvider
{
    private final URI opaServerUri;
    private final OpaS3SecurityMapper opaS3SecurityMapper;

    @Inject
    public OpaS3SecurityFacadeProvider(OpaS3SecurityMapper opaS3SecurityMapper, OpaS3SecurityConfig config)
    {
        this.opaS3SecurityMapper = requireNonNull(opaS3SecurityMapper, "opaS3SecurityMapper is null");
        opaServerUri = UriBuilder.fromUri(config.getOpaServerBaseUri()).build();
    }

    @Override
    public S3SecurityFacade securityFacadeForRequest(ParsedS3Request request, Optional<Identity> identity)
            throws WebApplicationException
    {
        return lowercaseAction -> opaS3SecurityMapper.apply(request, lowercaseAction, opaServerUri, identity);
    }
}
