package vicmicroservices.payvault;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PayVaultApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;
//    this rest template helps us make mock API requests to the locally running application for the sake of our unit tests

    @Test
    void shouldReturnAPayCardWhenDataIsSaved(){
//        get a response entity from the specified url
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("VictorI","123abcxyz")
                .getForEntity("/api/v1/paycards/100",String.class);
//        what do we want to assert, that we have a successful http request , there are multiple ways to check this
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        // here we parse the response as a JSON object , in this test, responses must not be null in the id fields of the JSON
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(100);
        System.out.println( "OUTPUT IS :"+ response.getBody());
        // test the amount too
        Double vaultBalance = documentContext.read("$.balance");
//        assertThat(vaultBalance).isGreaterThan(200);
        assertThat(vaultBalance).isEqualTo(1.00);


    }

    @Test
    void shouldNotReturnAPayCardWithInvalidId(){
        // we are testing to see that the application Id's are never > 10000
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("VictorI", "123abcxyz")
                .getForEntity("/api/v1/paycards/10005", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();

    }

    @Test
    void shouldCreateANewPayCard(){
        Double dummyBalance = 500.4;
        PayCard payCard = new PayCard(null,dummyBalance,"testUser1");
        ResponseEntity<Void> voidResponseEntity = restTemplate
                .withBasicAuth("VictorI","123abcxyz")
                .postForEntity("/api/v1/paycards/create",payCard, Void.class);
        // if creation is successful we expect a 201(CREATED) response
        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Check the Location URI from the response headers
        URI newPayCardLocation = voidResponseEntity.getHeaders().getLocation();
        // make a get request to that uri
        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("VictorI","123abcxyz")
                .getForEntity(newPayCardLocation,String.class);
        System.out.println(newPayCardLocation);
        System.out.println(getResponse.getBody());
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        // test the value posted after retrieval
        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        Double cardBalance = documentContext.read("$.balance");
        assertThat(cardBalance).isEqualTo(dummyBalance);

    }

    @Test
    void shouldReturnAllPayCards(){
        //make a new request
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("VictorI","123abcxyz")
                .getForEntity("/api/v1/paycards/list_paycards", String.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

//    Tests for Pagination and Sorting

    @Test
    void shouldReturnAllCashCardsWhenListIsRequested() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("VictorI","123abcxyz")
                .getForEntity("/api/v1/paycards/list_paycards", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int cashCardCount = documentContext.read("$.length()");
        assertThat(cashCardCount).isEqualTo(3);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(99, 100, 101);

        JSONArray balances = documentContext.read("$..balance");
        assertThat(balances).containsExactlyInAnyOrder(123.45, 1.00, 150.00);
    }

//    test for bad auth
    @Test
    void shouldNotReturnAPayCardWhenUsingBadCredentials() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("unknown-USER", "123abcxyz")
                .getForEntity("/api/v1/paycards/100", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        response = restTemplate
                .withBasicAuth("VictorI", "BAD-PASSWORD")
                .getForEntity("/api/v1/paycards/99", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
    @Test
    void shouldRejectUsersWhoAreNotCardOwners() {
//        this should enforce RBAC such that if a user doesn't have the PAYCARD-OWNER Role, they should get a 403 forbidden response.
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("stanley-owns-no-cards", "qrs456")
                .getForEntity("/api/v1/paycards/99", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shoulNotAllowUsersAccessPayCardsTheyDoNotOwn(){

        ResponseEntity<String> response = restTemplate
                .withBasicAuth("VictorI","123abcxyz")
                .getForEntity("/api/v1/paycards/102",String.class); //102 belongs to Ben

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }
}
