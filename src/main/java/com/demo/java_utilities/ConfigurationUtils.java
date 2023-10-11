package com.demo.java_utilities;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileBased;
import org.apache.commons.configuration2.io.FileHandler;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.commons.configuration2.tree.xpath.XPathExpressionEngine;

// Wrapper for apache common and other configurations
public class ConfigurationUtils {

    public static final String LS = System.getProperty("line.separator");

    public static <T> T requirePresent(String key, Function<String, T> f)
        throws ConfigurationException {
        T value = f.apply(key);
        if (value == null) throw new ConfigurationException(
            "The passed configuration does not have a value for key " +
            key +
            "."
        );
        return value;
    }

    public static void checkParameters(
        HierarchicalConfiguration<ImmutableNode> configuration,
        String... parameters
    ) throws ConfigurationException {
        List<String> parameterNotFound = new ArrayList<>();
        for (String parameter : parameters) {
            if (
                configuration.getProperty(parameter) == null
            ) parameterNotFound.add(parameter);
        }
        if (!parameterNotFound.isEmpty()) throw new ConfigurationException(
            "The following required parameters are not set in the configuration:" +
            LS +
            parameterNotFound.stream().collect(joining(LS))
        );
    }
}
