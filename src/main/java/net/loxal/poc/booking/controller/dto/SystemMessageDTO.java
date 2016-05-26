/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>
 *
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

package net.loxal.poc.booking.controller.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class SystemMessageDTO implements JsonSerializable {

    private HttpStatus status;

    private String error;

    private String message;

    public SystemMessageDTO(final HttpStatus status, final String error, final String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(final HttpStatus status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    @Override
    public void serialize(final JsonGenerator jgen, final SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("status", status.value());
        jgen.writeStringField("error", error);
        jgen.writeStringField("message", message);
        jgen.writeEndObject();
        jgen.close();
    }

    @Override
    public void serializeWithType(final JsonGenerator jgen, final SerializerProvider provider, final TypeSerializer typeSer) throws IOException {
        serialize(jgen, provider);
    }
}
