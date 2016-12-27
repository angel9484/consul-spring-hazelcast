package es.nangel.consul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

/**
 * Created by angelopez on 27/12/2016.
 */
@Service
public class HazelcastSpringConsul {
    private static HazelcastSpringConsul instance;
    @Autowired
    private DiscoveryClient discoveryClient;

    public HazelcastSpringConsul() {
        instance = this;
    }

    public static HazelcastSpringConsul getInstance() {
        return instance;
    }

    public DiscoveryClient getDiscoveryClient() {
        return discoveryClient;
    }
}
