package com.opipo.codechallenge.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.opipo.codechallenge.repository.model.TransactionEntity;
import com.opipo.web.api.model.Transaction;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ComponentScan(value = {"com.opipo.web.api", "io.swagger.configuration"})
@EnableSwagger2
public class WebApiAutoconfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<Transaction, TransactionEntity>() {
            @Override
            protected void configure() {
            }
        });
        modelMapper.getTypeMap(Transaction.class, TransactionEntity.class).setPostConverter(context -> {
            Transaction origin = context.getSource();
            TransactionEntity target = context.getDestination();
            target.setReference(
                    origin.getReference() == null ? Integer.toString(origin.hashCode()) : origin.getReference());
            return target;
        });
        return modelMapper;
    }
}
