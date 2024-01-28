package vicmicroservices.payvault;

import org.springframework.data.repository.CrudRepository;

// This Repository handles access to our data persistence layer.
// it would be injected into our Controller Layer using dependency injection
// The Long specifies the datatype for our PayCard Id field
//@Repository
interface PayVaultRepository extends CrudRepository<PayCard, Long> {

}
