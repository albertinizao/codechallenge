package com.opipo.codechallenge.it;

import static java.util.Locale.ENGLISH;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Configurable;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.opipo.web.api.model.TransactionTesting;

import io.cucumber.core.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterByTypeTransformer;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableCellByTypeTransformer;
import io.cucumber.datatable.TableEntryByTypeTransformer;

/**
 * Class used to convert DataTable to Java Object using Jackson (Json library).
 * <p>
 * <b>NOTE:</b> This class assumes that you will use field names match the data table column headers.
 * </p>
 */
@Configurable
public class CucumberTypeRegistryConfigurer implements TypeRegistryConfigurer {

    private ObjectMapper mapper;

    @Override
    public Locale locale() {
        return ENGLISH;
    }

    @Override
    public void configureTypeRegistry(io.cucumber.core.api.TypeRegistry typeRegistry) {
        Transformer transformer = new Transformer();
        typeRegistry.defineDataTableType(DataTableType.entry(TransactionTesting.class));
        typeRegistry.setDefaultDataTableCellTransformer(transformer);
        typeRegistry.setDefaultDataTableEntryTransformer(transformer);
        typeRegistry.setDefaultParameterTransformer(transformer);
    }

    private class Transformer
            implements ParameterByTypeTransformer, TableEntryByTypeTransformer, TableCellByTypeTransformer {

        @Override
        public Object transform(String s, Type type) {
            return mapper.convertValue(s, mapper.constructType(type));
        }

        @Override
        public <T> T transform(Map<String, String> map, Class<T> aClass,
                TableCellByTypeTransformer tableCellByTypeTransformer) {

            return mapper.convertValue(map, aClass);
        }

        @Override
        public <T> T transform(String s, Class<T> aClass) {
            return mapper.convertValue(s, aClass);
        }
    }

    public class CustomSerializer extends JsonSerializer<OffsetDateTime> {

        private DateTimeFormatter formatter;

        public CustomSerializer(DateTimeFormatter formatter) {
            this.formatter = formatter;
        }

        @Override
        public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            gen.writeString(value.format(this.formatter));
        }
    }

    public class CustomDeserializer extends JsonDeserializer<OffsetDateTime> {

        private DateTimeFormatter formatter;

        public CustomDeserializer(DateTimeFormatter formatter) {
            this.formatter = formatter;
        }

        @Override
        public OffsetDateTime deserialize(JsonParser parser, DeserializationContext context)
                throws IOException, JsonProcessingException {
            return OffsetDateTime.parse(parser.getText(), this.formatter);
        }
    }

}