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
package co.rsk.rest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

public class RestUtilsTest {

    @Test
    public void testCreateResponse_contentOnly_executesAsExpected() {
        // Given
        String content = "content.text";

        ByteBuf expectedBufContent = Unpooled.copiedBuffer(content, StandardCharsets.UTF_8);

        // When
        DefaultFullHttpResponse response = RestUtils.createResponse(content);

        // Then
        Assert.assertEquals(HttpResponseStatus.OK, response.getStatus());
        Assert.assertEquals(HttpVersion.HTTP_1_1, response.getProtocolVersion());
        Assert.assertEquals("text/plain;charset=UTF-8", response.headers().get(CONTENT_TYPE));
        Assert.assertEquals(expectedBufContent.readableBytes(), Integer.parseInt(response.headers().get(CONTENT_LENGTH)));
        Assert.assertEquals(expectedBufContent, response.content());
    }

    @Test
    public void testCreateResponse_contentAndStatus_executesAsExpected() {
        // Given
        String content = "content.text";
        HttpResponseStatus httpResponseStatus = HttpResponseStatus.NOT_ACCEPTABLE;

        ByteBuf expectedBufContent = Unpooled.copiedBuffer(content, StandardCharsets.UTF_8);

        // When
        DefaultFullHttpResponse response = RestUtils.createResponse(content, httpResponseStatus);

        // Then
        Assert.assertEquals(httpResponseStatus, response.getStatus());
        Assert.assertEquals(HttpVersion.HTTP_1_1, response.getProtocolVersion());
        Assert.assertEquals("text/plain;charset=UTF-8", response.headers().get(CONTENT_TYPE));
        Assert.assertEquals(expectedBufContent.readableBytes(), Integer.parseInt(response.headers().get(CONTENT_LENGTH)));
        Assert.assertEquals(expectedBufContent, response.content());
    }

    @Test
    public void testCreateResponse_customParameters_executesAsExpected() {
        // Given
        String content = "content.text";
        HttpResponseStatus httpResponseStatus = HttpResponseStatus.PAYMENT_REQUIRED;
        Charset charset = StandardCharsets.UTF_16;
        HttpVersion httpVersion = HttpVersion.HTTP_1_0;
        String contentType = "content.type";

        ByteBuf expectedBufContent = Unpooled.copiedBuffer(content, charset);

        // When
        DefaultFullHttpResponse response = RestUtils.createResponse(httpVersion, httpResponseStatus, content, charset, contentType);

        // Then
        Assert.assertEquals(httpResponseStatus, response.getStatus());
        Assert.assertEquals(httpVersion, response.getProtocolVersion());
        Assert.assertEquals(contentType, response.headers().get(CONTENT_TYPE));
        Assert.assertEquals(expectedBufContent.readableBytes(), Integer.parseInt(response.headers().get(CONTENT_LENGTH)));
        Assert.assertEquals(expectedBufContent, response.content());
    }

}
