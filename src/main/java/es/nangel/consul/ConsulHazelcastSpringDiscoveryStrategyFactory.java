package es.nangel.consul;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import com.hazelcast.config.properties.PropertyDefinition;
import com.hazelcast.logging.ILogger;
import com.hazelcast.spi.discovery.DiscoveryNode;
import com.hazelcast.spi.discovery.DiscoveryStrategy;
import com.hazelcast.spi.discovery.DiscoveryStrategyFactory;

/**
 * Created by angelopez on 27/12/2016.
 */
public class ConsulHazelcastSpringDiscoveryStrategyFactory implements DiscoveryStrategyFactory {
    @Override
    public Class<? extends DiscoveryStrategy> getDiscoveryStrategyType() {
        return ConsulHazelcastSpringDiscoveryStrategy.class;
    }

    @Override
    public DiscoveryStrategy newDiscoveryStrategy(DiscoveryNode discoveryNode, ILogger iLogger, Map<String, Comparable> properties) {
        try {
            return new ConsulHazelcastSpringDiscoveryStrategy(discoveryNode, iLogger, properties, HazelcastSpringConsul.getInstance());
        } catch (NoSuchMethodException | InvocationTargetException |ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public Collection<PropertyDefinition> getConfigurationProperties() {
        return null;
    }
}
