package unibuc.ro.ParkingApp.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import unibuc.ro.ParkingApp.model.listing.ListingPaymentRequest;

@Service
public class PaymentService {

    @Value("${stripe.api.key}")
    private String apiKey;
    @Value("${frontend_client_base_url}")
    private String baseUrl;

    @PostConstruct
    public void init() {

        Stripe.apiKey = System.getenv(apiKey);
    }

    public Session createCheckoutSession(ListingPaymentRequest listingPaymentRequest) throws StripeException {
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(baseUrl + "/success")
                        .setCancelUrl(baseUrl + "/cancel")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency("ron")
                                                        .setUnitAmount(listingPaymentRequest.getPrice() * 100L)
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName(listingPaymentRequest.getTitle())
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();

        return Session.create(params);
    }
}