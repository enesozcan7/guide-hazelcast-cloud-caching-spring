package guides.hazelcast.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.spi.impl.discovery.HazelcastCloudDiscovery;
import com.hazelcast.client.spi.properties.ClientProperty;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.config.JoinConfig;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RestController
public class Application {

    private String CLUSTER_NAME = "null";
    private String CLUSTER_PW = "null";
    private String DISCOVERY_TOKEN = "null";
    private String BASE_PROPERTY = "https://coordinator.hazelcast.cloud";

    private static ClientConfig config;

    @Bean
    public ClientConfig hazelcastConfig() {
        ClientConfig config = new ClientConfig();
        config.setGroupConfig(new GroupConfig(CLUSTER_NAME, CLUSTER_PW));
        config.setProperty("hazelcast.client.statistics.enabled","true");
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), DISCOVERY_TOKEN);
        config.setProperty(HazelcastCloudDiscovery.CLOUD_URL_BASE_PROPERTY.getName(), BASE_PROPERTY);
        return config;
    }

    @RequestMapping("/")
    public String homepage(){
	return "Homepage\n";
    }
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
