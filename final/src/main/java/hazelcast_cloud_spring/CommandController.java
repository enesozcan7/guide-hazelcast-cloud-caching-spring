package hazelcast_cloud_spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
public class CommandController {



    @Value("#{environment.MY_POD_NAME}")
    private String podName;


    @Autowired
    HazelcastInstance hazelcastInstance;


    private Map<String,String> retrieveMap() {
        return hazelcastInstance.getMap("map");
    }

    @RequestMapping("/put")
    public CommandResponse put(@RequestParam(value = "key") String key, @RequestParam(value = "value") String value) {
        retrieveMap().put(key, value);
        return new CommandResponse(value,podName);
    }

    @RequestMapping("/get")
    public CommandResponse get(@RequestParam(value = "key") String key) {
        String value = retrieveMap().get(key);
        return new CommandResponse(value,podName);
    }


}
