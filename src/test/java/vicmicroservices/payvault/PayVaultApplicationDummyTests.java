package vicmicroservices.payvault;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
class PayVaultApplicationDummyTests {

	@Test
	void contextLoads() {
	}

	@Test
	void dummyTest() {
		assertThat(42).isEqualTo(42);
	}
}

