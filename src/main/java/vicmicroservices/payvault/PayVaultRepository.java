package vicmicroservices.payvault;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
//import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

// This Repository handles access to our data persistence layer.
// it would be injected into our Controller Layer using dependency injection
// The Long specifies the datatype for our PayCard Id field
@Repository
interface PayVaultRepository extends CrudRepository<PayCard, Long>, PagingAndSortingRepository<PayCard, Long> {

//    @Query("SELECT p FROM pay_card p WHERE p.Id = :id and customer = :customer")
//    manually specify query this way . eg; Java expects id cols to be lower case not Id.
    PayCard findByIdAndCustomer(Long id, String customer);

    Page<PayCard> findByCustomer(String customer, PageRequest pageRequest);

    //method Names need to correspond to the Model eg; Customer cannot be User, since our PayCard class has no User atttribute.
    boolean existsByIdAndCustomer(Long id, String customer);
}
