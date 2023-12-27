package vicmicroservices.payvault;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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
    private ResponseEntity<PayCard> getPayCard(@PathVariable Long payCardId) {
        Optional<PayCard> optionalPayCard = payVaultRepository.findById(payCardId);
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
    private ResponseEntity<Void> createPayCard(@RequestBody PayCard newPayCardInfo, UriComponentsBuilder ucb){
         // when requests are sent to the /create endpoint, we save it and return the location

        PayCard savedPayCard = payVaultRepository.save(newPayCardInfo);
        URI locationOfSavedPayCard = ucb.path("/api/v1/paycards/{id}")
                                        .buildAndExpand(savedPayCard.Id())
                                        .toUri();
        return ResponseEntity.created(locationOfSavedPayCard).build();
            //to create the URI from a sting use the URI.create method
//        return ResponseEntity.created(URI.create("/paycards/id?")).build();

    }

    @GetMapping("")
    private ResponseEntity<Iterable<PayCard>> getMultiplePayCards(){
         return ResponseEntity.ok(payVaultRepository.findAll());
    }
}
