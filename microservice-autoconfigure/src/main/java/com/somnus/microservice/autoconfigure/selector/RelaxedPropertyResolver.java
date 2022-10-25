package com.somnus.microservice.autoconfigure.selector;

import java.util.Map;
import java.util.Objects;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
/**
 * @author Kevin
 * @date 2019/6/14 10:01
 */
public class RelaxedPropertyResolver implements PropertyResolver {

    private final PropertyResolver resolver;

    private final String prefix;

    public RelaxedPropertyResolver(PropertyResolver resolver) {
        this(resolver, null);
    }

    public RelaxedPropertyResolver(PropertyResolver resolver, String prefix) {
        Assert.notNull(resolver, "PropertyResolver must not be null");
        this.resolver = resolver;
        this.prefix = (prefix != null ? prefix : "");
    }

    @NonNull
    @Override
    public String getRequiredProperty(@NonNull String key) throws IllegalStateException {
        return getRequiredProperty(key, String.class);
    }

    @NonNull
    @Override
    public <T> T getRequiredProperty(@NonNull String key, @NonNull Class<T> targetType) throws IllegalStateException {
        T value = getProperty(key, targetType);
        Assert.state(value != null, String.format("required key [%s] not found", key));
        return value;
    }

    @Nullable
    @Override
    public String getProperty(@NonNull String key) {
        return getProperty(key, String.class, null);
    }

    @NonNull
    @Override
    public String getProperty(@NonNull String key, @NonNull String defaultValue) {
        return getProperty(key, String.class, defaultValue);
    }

    @Nullable
    @Override
    public <T> T getProperty(@NonNull String key, @NonNull Class<T> targetType) {
        return getProperty(key, targetType, null);
    }

    @NonNull
    @Override
    public <T> T getProperty(@NonNull String key, @NonNull Class<T> targetType, @NonNull T defaultValue) {
        RelaxedNames prefixes = new RelaxedNames(this.prefix);
        RelaxedNames keys = new RelaxedNames(key);
        for (String prefix : prefixes) {
            for (String relaxedKey : keys) {
                if (this.resolver.containsProperty(prefix + relaxedKey)) {
                    return Objects.requireNonNull(this.resolver.getProperty(prefix + relaxedKey, targetType));
                }
            }
        }
        return defaultValue;
    }

    @Override
    public boolean containsProperty(@NonNull String key) {
        RelaxedNames prefixes = new RelaxedNames(this.prefix);
        RelaxedNames keys = new RelaxedNames(key);
        for (String prefix : prefixes) {
            for (String relaxedKey : keys) {
                if (this.resolver.containsProperty(prefix + relaxedKey)) {
                    return true;
                }
            }
        }
        return false;
    }

    @NonNull
    @Override
    public String resolvePlaceholders(@NonNull String text) {
        throw new UnsupportedOperationException(
                "Unable to resolve placeholders with relaxed properties");
    }

    @NonNull
    @Override
    public String resolveRequiredPlaceholders(@NonNull String text) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Unable to resolve placeholders with relaxed properties");
    }

    /**
     * Return a Map of all values from all underlying properties that start with the
     * specified key. NOTE: this method can only be used if the underlying resolver is a
     * {@link ConfigurableEnvironment}.
     * @param keyPrefix the key prefix used to filter results
     * @return a map of all sub properties starting with the specified key prefix.
     * @see PropertySourceUtils#getSubProperties
     */
    public Map<String, Object> getSubProperties(String keyPrefix) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, this.resolver, "SubProperties not available.");
        ConfigurableEnvironment env = (ConfigurableEnvironment) this.resolver;
        return PropertySourceUtils.getSubProperties(env.getPropertySources(), this.prefix, keyPrefix);
    }

    /**
     * Return a property resolver for the environment, preferring one that ignores
     * unresolvable nested placeholders.
     * @param environment the source environment
     * @param prefix the prefix
     * @return a property resolver for the environment
     * @since 1.4.3
     */
    public static RelaxedPropertyResolver ignoringUnresolvableNestedPlaceholders(Environment environment, String prefix) {
        Assert.notNull(environment, "Environment must not be null");
        PropertyResolver resolver = environment;
        if (environment instanceof ConfigurableEnvironment) {
            resolver = new PropertySourcesPropertyResolver(
                    ((ConfigurableEnvironment) environment).getPropertySources());
            ((PropertySourcesPropertyResolver) resolver).setIgnoreUnresolvableNestedPlaceholders(true);
        }
        return new RelaxedPropertyResolver(resolver, prefix);
    }
}