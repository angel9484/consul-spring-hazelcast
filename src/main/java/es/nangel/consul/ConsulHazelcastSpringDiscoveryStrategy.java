package es.nangel.consul;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import com.hazelcast.logging.ILogger;
import com.hazelcast.nio.Address;
import com.hazelcast.spi.discovery.DiscoveryNode;
import com.hazelcast.spi.discovery.DiscoveryStrategy;
import com.hazelcast.spi.discovery.SimpleDiscoveryNode;
import com.hazelcast.spi.partitiongroup.PartitionGroupStrategy;

/**
 * Created by angelopez on 27/12/2016.
 */
public class ConsulHazelcastSpringDiscoveryStrategy implements DiscoveryStrategy {
    //TODO Aqui hay que resolver que haya varias instancias de hz y que no sea un singleton a mano
    private final DiscoveryClient discoveryClient;
    private final Map<String, Comparable> properties;
    private final ILogger logger;
    private final DiscoveryNode discoveryNode;
    private final String serviceName;
    private final PartitionGroupStrategy partitionGroupStrategy;

    public ConsulHazelcastSpringDiscoveryStrategy(
            DiscoveryNode discoveryNode,
            ILogger iLogger,
            Map<String, Comparable> properties,
            HazelcastSpringConsul instance)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        this.discoveryNode = discoveryNode;
        this.logger = iLogger;
        this.properties = properties;
        this.discoveryClient = instance.getDiscoveryClient();
        this.serviceName = (String) properties.get("serviceName");
        Comparable partitionGroupStrategyClassName = properties.get("partition-group-strategy");
        if (partitionGroupStrategyClassName != null) {
            Class<?> partitionGroupStrategyClass = Class.forName((String) partitionGroupStrategyClassName);
            //TODO intentar no acoplar en el constructor esto
            this.partitionGroupStrategy =
                    (PartitionGroupStrategy) partitionGroupStrategyClass.getConstructor(HazelcastSpringConsul.class).newInstance(instance);
        } else {
            this.partitionGroupStrategy = null;
        }
    }

    @Override
    public void start() {
        logger.info("Initialized config with: " + discoveryClient);
        //TODO register service with spring boot
    }

    @Override
    public Iterable<DiscoveryNode> discoverNodes() {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (instances == null) {
            return null;
        }
        return instances.stream().map(i -> {
            try {
                Address address = new Address(i.getHost(), i.getPort());
                Address publicAddress = new Address(i.getUri().getHost(), i.getUri().getPort());
                return new SimpleDiscoveryNode(address, publicAddress);
            } catch (UnknownHostException e) {
                logger.severe("Problem with host on service: " + this, e);
                throw new UnsupportedOperationException(e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public void destroy() {
        logger.info("Shutting down config with: " + discoveryClient);
        //TODO unregister service with spring boot
    }

    @Override
    public PartitionGroupStrategy getPartitionGroupStrategy() {
        return partitionGroupStrategy;
    }

    @Override
    public Map<String, Object> discoverLocalMetadata() {
        return discoveryClient.getLocalServiceInstance().getMetadata().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                Map.Entry::getValue));
    }
}
