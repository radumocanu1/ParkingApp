package unibuc.ro.ParkingApp.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.hibernate.NonUniqueResultException;
import org.jboss.resteasy.spi.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import unibuc.ro.ParkingApp.configuration.ApplicationConstants;
import unibuc.ro.ParkingApp.exception.PendingPaymentNotFound;
import unibuc.ro.ParkingApp.model.PendingPayment;
import unibuc.ro.ParkingApp.model.listing.Listing;
import unibuc.ro.ParkingApp.model.listing.ListingPaymentRequest;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.repository.PendingPaymentsRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log
public class PaymentService {

    @Value("${stripe.api.key}")
    private String apiKey;
    @Value("${frontend_client_base_url}")
    private String baseUrl;
    private final PendingPaymentsRepository pendingPaymentsRepository;
    private final OIDCUserMappingService oidcUserMappingService;
    private final ChatService chatService;
    private final RentingService rentingService;
    private final ListingService listingService;

    @PostConstruct
    public void init() {

        Stripe.apiKey = System.getenv(apiKey);
    }

    public Session createCheckoutSession(String tokenSubClaim,ListingPaymentRequest listingPaymentRequest) throws StripeException {
        addPaymentRequestToDB(tokenSubClaim,listingPaymentRequest);
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(baseUrl + "/payment/success")
                        .setCancelUrl(baseUrl + "/payment/cancel")
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
    private void addPaymentRequestToDB( String tokenSubClaim, ListingPaymentRequest listingPaymentRequest){
        PendingPayment pendingPayment = new PendingPayment();
        pendingPayment.setListingUUID(listingPaymentRequest.getListingUUID());
        pendingPayment.setStartDate(listingPaymentRequest.getStartDate());
        pendingPayment.setEndDate(listingPaymentRequest.getEndDate());
        pendingPayment.setCarNumber(listingPaymentRequest.getCarNumber());
        pendingPayment.setUserUUID(oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser().getUserUUID());
        pendingPaymentsRepository.save(pendingPayment);
    }
    public void acceptPayment(String tokenSubClaim){
        log.info("Payment accepted");
        UUID userUUID = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser().getUserUUID();
        Optional<PendingPayment> pendingPaymentOptional;
        try {
            pendingPaymentOptional = pendingPaymentsRepository.findByUserUUID(userUUID);
        }catch (Exception e){
            pendingPaymentsRepository.deleteAllByUserUUID(userUUID);
            throw new RuntimeException();
        }
        if(pendingPaymentOptional.isEmpty())
            throw new PendingPaymentNotFound();
        PendingPayment pendingPayment = pendingPaymentOptional.get();
        rentingService.rent(pendingPayment.getUserUUID(), pendingPayment.getListingUUID(),pendingPayment.getStartDate(), pendingPayment.getEndDate(), pendingPayment.getCarNumber() );
        chatService.sendAdminMessage(userUUID, ApplicationConstants.PAYMENT_ACCEPTED);
        Listing listing = listingService.getListing(pendingPayment.getListingUUID());
        User publishingUser = listing.getUser();
        chatService.sendAdminMessage(publishingUser.getUserUUID(), String.format(ApplicationConstants.PARKING_SPOT_RENTED,publishingUser.getUsername(), listing.getTitle(), pendingPayment.getStartDate(), pendingPayment.getEndDate(), pendingPayment.getCarNumber()));
        pendingPaymentsRepository.deleteByUserUUID(userUUID);
        Optional<PendingPayment> remainingPayments = pendingPaymentsRepository.findByUserUUID(userUUID);
        if (remainingPayments.isEmpty()) {
            System.out.println("Pending payments successfully deleted.");
        } else {
            System.out.println("Failed to delete pending payments: " + remainingPayments);
        }
    }
    @Transactional
    public void rejectPayment(String tokenSubClaim){
        log.info("Payment rejected");
        UUID userUUID = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser().getUserUUID();
        if(pendingPaymentsRepository.findByUserUUID(userUUID).isEmpty())
            throw new PendingPaymentNotFound();
        chatService.sendAdminMessage(userUUID, ApplicationConstants.PAYMENT_REJECTED);
        pendingPaymentsRepository.deleteByUserUUID(userUUID);
    }

}