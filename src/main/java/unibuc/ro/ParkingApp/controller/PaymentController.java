package unibuc.ro.ParkingApp.controller;


import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import unibuc.ro.ParkingApp.model.listing.ListingPaymentRequest;
import unibuc.ro.ParkingApp.service.PaymentService;

import java.security.Principal;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService stripeService;

    @PostMapping("/create-checkout-session")
    public String createCheckoutSession(@RequestBody ListingPaymentRequest listingPaymentRequest, Principal principal) throws StripeException {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        Session session = stripeService.createCheckoutSession((String) token.getTokenAttributes().get("sub"),listingPaymentRequest);
        return session.getId();
    }
    @PostMapping("/payment/accept/")
    public ResponseEntity<Void> acceptPayment(Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        stripeService.acceptPayment((String) token.getTokenAttributes().get("sub"));
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/payment/reject/")
    public ResponseEntity<Void> rejectPayment(Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        stripeService.rejectPayment((String) token.getTokenAttributes().get("sub"));
        return ResponseEntity.noContent().build();
    }
}