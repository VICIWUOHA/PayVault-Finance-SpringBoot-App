package vicmicroservices.payvault;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class PayCardJsonTest {

    @Autowired
    private JacksonTester<PayCard> json;

    @Autowired
    private JacksonTester<PayCard[]> jsonList;

    private PayCard[] payCards;

    @BeforeEach
    void setUp() {
        payCards = Arrays.array(
                new PayCard(99L, 123.45, "VictorI"),
                new PayCard(100L, 100.00, "VictorI"),
                new PayCard(101L, 150.00, "VictorI"));
    }
    @Test
    void cashCardListSerializationTest() throws IOException {
        assertThat(jsonList.write(payCards)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void payCardSerializationTest() throws IOException {
        PayCard payCard = payCards[0];
        assertThat(json.write(payCard)).isStrictlyEqualToJson("single.json");
        assertThat(json.write(payCard)).hasJsonPathNumberValue("@.Id");
        assertThat(json.write(payCard)).extractingJsonPathNumberValue("@.Id")
                .isEqualTo(99);
        assertThat(json.write(payCard)).hasJsonPathNumberValue("@.balance");
        assertThat(json.write(payCard)).extractingJsonPathNumberValue("@.balance")
                .isEqualTo(123.45);
        assertThat(json.write(payCard)).extractingJsonPathStringValue("@.customer")
                .isEqualTo("VictorI");
    }

    @Test
    void payCardDeserializationTest() throws IOException {
        String expected = """
                {
                    "Id": 99,
                    "balance": 123.45,
                    "customer": "VictorI"
                }
                """;
        assertThat(json.parse(expected))
                .isEqualTo(new PayCard(99L, 123.45,"VictorI"));
        assertThat(json.parseObject(expected).Id()).isEqualTo(99);
        assertThat(json.parseObject(expected).balance()).isEqualTo(123.45);
    }
}
