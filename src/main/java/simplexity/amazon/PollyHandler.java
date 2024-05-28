package simplexity.amazon;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClient;

public class PollyHandler {

    AmazonPolly polly;

    public PollyHandler(String accessID, String accessSecret, Region awsRegion){
        polly = new AmazonPollyClient(new BasicAWSCredentials(accessID, accessSecret), new ClientConfiguration());
        polly.setRegion(awsRegion);
    }

    public AmazonPolly getPolly() {
        return polly;
    }
}
