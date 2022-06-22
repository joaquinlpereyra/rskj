/*
 * This file is part of RskJ
 * Copyright (C) 2018 RSK Labs Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package co.rsk.rpc.netty.rest;

import co.rsk.rpc.netty.rest.modules.RestModule;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RestServerDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(RestServerDispatcher.class);

    private final List<RestModule> moduleList;

    public RestServerDispatcher(List<RestModule> moduleList) {
        Objects.requireNonNull(moduleList, "Module List can not be null");
        this.moduleList = Collections.unmodifiableList(moduleList);
    }

    public DefaultFullHttpResponse dispatch(HttpRequest request) throws URISyntaxException {

        String uri = new URI(request.getUri()).getPath();

        RestModule restModule = moduleList.stream()
                .filter(module -> module.getUri().startsWith(uri)).findFirst().orElse(null);

        if (restModule == null) {
            logger.info("Handler Not Found.");
            return RestUtils.createResponse("Not Found", HttpResponseStatus.NOT_FOUND);
        }

        if (restModule.isActive()) {
            logger.info("Dispatching request.");
            return restModule.processRequest(uri, request.getMethod());
        }

        logger.info("Request received but module is disabled.");
        return RestUtils.createResponse("Not Found", HttpResponseStatus.NOT_FOUND);

    }

}
