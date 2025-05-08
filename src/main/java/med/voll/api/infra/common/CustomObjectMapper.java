package med.voll.api.infra.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class CustomObjectMapper {

    @Bean
    public ObjectMapper getInstance() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule())
              .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
              .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
              .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
              .setSerializationInclusion(JsonInclude.Include.NON_NULL)// Só serializa campos não nulos
              .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
