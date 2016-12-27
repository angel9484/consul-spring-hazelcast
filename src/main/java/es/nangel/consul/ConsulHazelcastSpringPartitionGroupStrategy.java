package es.nangel.consul;

import com.hazelcast.partition.membergroup.MemberGroup;
import com.hazelcast.spi.partitiongroup.PartitionGroupStrategy;

/**
 * Created by angelopez on 27/12/2016.
 */
public class ConsulHazelcastSpringPartitionGroupStrategy implements PartitionGroupStrategy {
    public Iterable<MemberGroup> getMemberGroups() {
        return null;
    }
}
