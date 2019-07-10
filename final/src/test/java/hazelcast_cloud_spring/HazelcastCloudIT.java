package hazelcast_cloud_spring;


import hazelcast_cloud_spring.CommandResponse;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class HazelcastCloudIT {

    private static String clusterUrl;

    @BeforeClass
    public static void oneTimeSetup() {
        String clusterIp = "localhost";
        String nodePort = "31000";
        clusterUrl = "http://" + clusterIp + ":" + nodePort ;
    }


    @Test
    public void testCloudConnection(){
        String key = "connection_key";
        String value = "connection_value";


        String put_url = String.format(clusterUrl +"/put?key=%s&value=%s", key, value);
        RestTemplate rest = new RestTemplate();

        ResponseEntity<CommandResponse> putResponse = rest.getForEntity(put_url, CommandResponse.class);
        Assert.assertTrue(putResponse.getStatusCode() == HttpStatus.OK);

        String get_url = String.format(clusterUrl+"/get?key=%s", key);
        ResponseEntity<CommandResponse> getResponse = rest.getForEntity(get_url, CommandResponse.class);
        String response_value = getResponse.getBody().getValue();
        Assert.assertTrue(value.equals(response_value));
    }

    @Test(timeout = 60000)
    public void testHazelcastCache() throws Exception {

        String key = "cloud_key";
        String value = "cloud_value";

        String put_url = String.format(clusterUrl +"/put?key=%s&value=%s", key, value);
        RestTemplate rest = new RestTemplate();

        ResponseEntity<CommandResponse> putResponse = rest.getForEntity(put_url, CommandResponse.class);
        Assert.assertTrue(putResponse.getStatusCode() == HttpStatus.OK);

        String firstPod = putResponse.getBody().getPodName();

        System.out.println("[TEST_LOG] First pod : " + firstPod);
        System.out.println("[TEST_LOG] Key : " + key);
        System.out.println("[TEST_LOG] Value : " + value);

        String secondValue;

        //GET call to see data is coming from another pod
        String get_url = String.format(clusterUrl+"/get?key=%s", key);

        while (true) { // it will try every second until it timeouts in 60 seconds
            ResponseEntity<CommandResponse> getResponse = rest.getForEntity(get_url, CommandResponse.class);
            String secondPod = getResponse.getBody().getPodName();
            System.out.println("[TEST_LOG] Current pod : " + secondPod);
            System.out.println("[TEST_LOG] Current value : " + getResponse.getBody().getValue());
            if (!secondPod.equals(firstPod)){
                secondValue = getResponse.getBody().getValue();

                break; // we get the response from different pod so SUCCESS!!
            }
            Thread.sleep(1000);
        }
        Assert.assertTrue(secondValue.equals(value));
    }

}

