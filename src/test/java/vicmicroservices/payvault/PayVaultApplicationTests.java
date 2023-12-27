package vicmicroservices.payvault;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PayVaultApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;
//    this rest template helps us make mock API requests to the locally running application for the sake of our unit tests

    @Test
    void shouldReturnAPayCardWhenDataIsSaved(){
//        get a response entity from the specified url
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/paycards/100",String.class);
//        what do we want to assert, that we have a successful http request , there are multiple ways to check this
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        // here we parse the response as a JSON object , in this test, responses must not be null in the id fields of the JSON
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.Id");
        assertThat(id).isEqualTo(100);
        System.out.println( "OUTPUT IS :"+ response.getBody());
        // test the amount too
        Double vaultBalance = documentContext.read("$.balance");
        assertThat(vaultBalance).isGreaterThan(200);


    }

    @Test
    void shouldNotReturnAPayCardWithInvalidId(){
        // we are testing to see that the application Id's are never > 10000
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/paycards/10005",String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();

    }

    @Test
    void shouldCreateANewPayCard(){
        Double dummyBalance = 500.4;
        PayCard payCard = new PayCard(null,dummyBalance);
        ResponseEntity<Void> voidResponseEntity = restTemplate.postForEntity("/api/v1/paycards/create",payCard, Void.class);
        // if creation is successful we expect a 201(CREATED) response
        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Check the Location URI from the response headers
        URI newPayCardLocation = voidResponseEntity.getHeaders().getLocation();
        // make a get request to that uri
        ResponseEntity<String> getResponse = restTemplate.getForEntity(newPayCardLocation,String.class);
        System.out.println(newPayCardLocation);
        System.out.println(getResponse.getBody());
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        // test the value posted after retrieval
        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Double cardBalance = documentContext.read("$.balance");
        assertThat(cardBalance).isEqualTo(dummyBalance);

    }


}
