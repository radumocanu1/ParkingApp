package unibuc.ro.ParkingApp.controller;


import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import unibuc.ro.ParkingApp.model.listing.ListingPaymentRequest;
import unibuc.ro.ParkingApp.service.PaymentService;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService stripeService;

    @PostMapping("/create-checkout-session")
    public String createCheckoutSession(@RequestBody ListingPaymentRequest listingPaymentRequest) throws StripeException {
        Session session = stripeService.createCheckoutSession(listingPaymentRequest);
        return session.getId();
    }
}