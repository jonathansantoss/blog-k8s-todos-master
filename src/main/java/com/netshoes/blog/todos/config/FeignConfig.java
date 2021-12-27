package com.netshoes.blog.todos.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.netshoes.blog.todos.gateways.TodoUsersClient;
import feign.Feign;
import feign.FeignException;
import feign.Logger;
import feign.Response;
import feign.codec.Decoder;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Optional;


@Configuration
public class FeignConfig {

    @Autowired
    private FeignProperties properties;

    @Bean
    public TodoUsersClient todoUsersClient() {
        return createClient(TodoUsersClient.class, properties.getUsers());
    }

    public static <T> T createClient(Class<T> type, String uri) {
        final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return Feign.builder()
            .decode404()
            .client(new ApacheHttpClient())
            .encoder(new JacksonEncoder(mapper))
            .decoder(new OptionalAwareDecoder(mapper))
            .logger(new Slf4jLogger(type))
            .logLevel(Logger.Level.FULL)
            .target(type, uri);
    }

    public static final class OptionalAwareDecoder implements Decoder {

        private final ObjectMapper om;

        public OptionalAwareDecoder(final ObjectMapper om) {
            this.om = om;
        }

        @Override
        public Object decode(Response response, Type type) throws IOException, FeignException {
            if (response.status() == 404 || response.body() == null) return Optional.empty();
            Reader reader = response.body().asReader();
            if (!reader.markSupported()) {
                reader = new BufferedReader(reader, 1);
            }
            try {
                reader.mark(1);
                if (reader.read() == -1) {
                    return null;
                }
                reader.reset();
                return Optional.of(om.readValue(reader, om.constructType(type)));
            } catch (RuntimeJsonMappingException e) {
                if (e.getCause() != null && e.getCause() instanceof IOException) {
                    throw IOException.class.cast(e.getCause());
                }
                throw e;
            }
        }
    }

}
