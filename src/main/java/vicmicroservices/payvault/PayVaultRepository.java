package vicmicroservices.payvault;

import org.springframework.data.repository.CrudRepository;

// This Repository handles access to our data persistence layer.
// it would be injected into our Controller Layer
//@Repository
interface PayVaultRepository extends CrudRepository<PayCard, Long> {

}
