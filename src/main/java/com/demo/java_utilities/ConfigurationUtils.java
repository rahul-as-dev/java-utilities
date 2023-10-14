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

    public static void checkFilesExist(
        HierarchicalConfiguration<ImmutableNode> configuration,
        String... parameters
    ) throws ConfigurationException {
        checkParameters(configuration, parameters);
        List<String> parameterNotFound = new ArrayList<>();
        for (String parameter : parameters) {
            if (
                !new File(configuration.getString(parameter)).exists()
            ) parameterNotFound.add(parameter);
        }
        if (!parameterNotFound.isEmpty()) throw new ConfigurationException(
            "The following required files given by the configuration do not exist: " +
            LS +
            parameterNotFound.stream().collect(joining(LS))
        );
    }

    public static XMLConfiguration loadXmlConfiguration(File configurationFile)
        throws ConfigurationException {
        try {
            Parameters params = new Parameters();
            FileBasedConfigurationBuilder<XMLConfiguration> configBuilder =
                new FileBasedConfigurationBuilder<>(
                    XMLConfiguration.class
                ).configure(
                    params
                        .xml()
                        .setExpressionEngine(new XPathExpressionEngine())
                        .setEncoding(StandardCharsets.UTF_8.name())
                        .setFile(configurationFile)
                );
            return configBuilder.getConfiguration();
        } catch (ConfigurationException e) {
            throw new ConfigurationException(e);
        }
    }

    public static HierarchicalConfiguration<
        ImmutableNode
    > createEmptyConfiguration() throws ConfigurationException {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<XMLConfiguration> builder =
            new FileBasedConfigurationBuilder<>(
                XMLConfiguration.class
            ).configure(
                params
                    .xml()
                    .setExpressionEngine(new XPathExpressionEngine())
                    .setEncoding(StandardCharsets.UTF_8.name())
            );
        XMLConfiguration c;
        try {
            c = builder.getConfiguration();
        } catch (ConfigurationException e) {
            throw new ConfigurationException();
        }
        return c;
    }

    public static void writeConfiguration(
        HierarchicalConfiguration<ImmutableNode> configuration,
        File destination
    ) throws ConfigurationException {
        try {
            if (
                !(configuration instanceof FileBased)
            ) throw new ConfigurationException(
                "The created configuration cannot be stored to file " +
                "because the chosen configuration implementation " +
                configuration.getClass().getCanonicalName() +
                " " +
                "does not implement the " +
                FileBased.class.getCanonicalName() +
                " interface"
            );
            FileHandler fh = new FileHandler((FileBased) configuration);
            fh.save(destination);
        } catch (ConfigurationException e) {
            throw new ConfigurationException();
        }
    }

    public static String dot(String... keys) {
        return Stream.of(keys).collect(joining("."));
    }

    /**
     * Convenience method for quick concatenation of hierarchical configuration keys into an XPath expression.
     *
     * @param keys Configuration keys to concatenate into a single hierarchical key.
     * @return The input keys joined with slashes for xpath expressions.
     */
    public static String slash(String... keys) {
        return Stream.of(keys).collect(joining("/"));
    }

    public static String ws(String baseElement, String newElement) {
        return baseElement + " " + newElement;
    }

    public static String last(String path) {
        return path + "[last()]";
    }

    public static String attrEqPred(String attribute, String value) {
        return attrEqMultiPred("", attribute, value);
    }

    public static String elementEqMultiPred(
        String element,
        String... attributesAndValues
    ) {
        return element + attrEqMultiPred("", attributesAndValues);
    }

    public static String elementEqPred(
        String element,
        String attribute,
        String value
    ) {
        return element + attrEqMultiPred("", attribute, value);
    }

    public static String attrEqMultiPred(
        String operator,
        String... attributesAndValues
    ) {
        if (
            attributesAndValues.length % 2 == 1
        ) throw new IllegalStateException(
            "There is an uneven number of arguments."
        );
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < attributesAndValues.length; i++) {
            String attributesAndValue = attributesAndValues[i];
            if (i % 2 == 0) {
                sb.append(
                    attrEq(attributesAndValue, attributesAndValues[i + 1])
                );
                if (i + 2 < attributesAndValues.length) sb
                    .append(" ")
                    .append(operator)
                    .append(" ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static String attrEq(String attribute, String value) {
        return "@" + attribute + "='" + value + "'";
    }
}
