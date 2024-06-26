package vicmicroservices.payvault;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

// In our controller, we can implement routing Logic for endpoints
@RestController
@RequestMapping("/api/v1/paycards")
class PayVaultController {
    // Addition of the Repository here is used for dependency injection.
    @Autowired
    private final PayVaultRepository payVaultRepository;

    private PayVaultController(PayVaultRepository payVaultRepository) {

        this.payVaultRepository = payVaultRepository;
    }
    //    for GET Requests


//    @GetMapping("/home")
//    private ResponseEntity<PayCard> findByIds(@PathVariable Long vaultId){
//        PayCard payCard = new PayCard(5L,670.6);
//        return ResponseEntity.ok(payCard);
//    };

    //    this is a handler for requests that come to api/v1/paycards/xx
    @GetMapping("/{payCardId}")
    private ResponseEntity<PayCard> getPayCard(@PathVariable Long payCardId, Principal principal) {
        Optional<PayCard> optionalPayCard = Optional
                        .ofNullable(payVaultRepository.findByIdAndCustomer(payCardId, principal.getName()));
        // Use functional style expression on one line
        return optionalPayCard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

//        if(optionalPayCard.isPresent()){
//            return ResponseEntity.ok(optionalPayCard.get());
//        }else{
//            return ResponseEntity.notFound().build();
//        }
// The logic below can be abstracted to be handled by the Repository to ensure the Single Responsibility Principle.
//        if (payCardId < 10000L) {
//            PayCard payCard = new PayCard(9L, 780.5);
//            System.out.println("Valid PayCard ID in db.");
//            return ResponseEntity.ok(payCard);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
    }

    @PostMapping(path = "/create")
    private ResponseEntity<Void> createPayCard(@RequestBody PayCard newPayCardInfo, UriComponentsBuilder ucb, Principal principal) {
        // when requests are sent to the /create endpoint, we get the principal who made the request, save it and return the location
        PayCard customerPayCard = new PayCard(null,newPayCardInfo.balance(),principal.getName());
        PayCard savedPayCard = payVaultRepository.save(customerPayCard);
        URI locationOfSavedPayCard = ucb.path("/api/v1/paycards/{id}")
                .buildAndExpand(savedPayCard.id())
                .toUri();
        return ResponseEntity.created(locationOfSavedPayCard).build();
        //to create the URI from a sting use the URI.create method
//        return ResponseEntity.created(URI.create("/paycards/id?")).build();

    }

//    @GetMapping("/list_paycards")
//    private ResponseEntity<Iterable<PayCard>> findAll() {
//        return ResponseEntity.ok(payVaultRepository.findAll());
//    }

    // Users should be able to have multiple virtualCards
    @GetMapping("/list_paycards")
    private ResponseEntity<List<PayCard>> getAllPayCards(Pageable pageable, Principal principal){
        Page<PayCard> page = payVaultRepository.findByCustomer(
                principal.getName(),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.DESC, "balance")))
                );
        return ResponseEntity.ok(page.getContent());
    }

}