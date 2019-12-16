package com.opipo.codechallenge.it;

import static java.util.Locale.ENGLISH;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Configurable;

import com.opipo.web.api.model.TransactionTesting;

import io.cucumber.core.api.TypeRegistryConfigurer;
import io.cucumber.datatable.DataTableType;

/**
 * Class used to convert DataTable to Java Object using Jackson (Json library).
 * <p>
 * <b>NOTE:</b> This class assumes that you will use field names match the data table column headers.
 * </p>
 */
@Configurable
public class CucumberTypeRegistryConfigurer implements TypeRegistryConfigurer {

    @Override
    public Locale locale() {
        return ENGLISH;
    }

    @Override
    public void configureTypeRegistry(io.cucumber.core.api.TypeRegistry typeRegistry) {
        typeRegistry.defineDataTableType(DataTableType.entry(TransactionTesting.class));
    }

}