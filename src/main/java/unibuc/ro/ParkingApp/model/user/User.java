package unibuc.ro.ParkingApp.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import unibuc.ro.ParkingApp.model.chat.Chat;
import unibuc.ro.ParkingApp.model.feedback.Feedback;
import unibuc.ro.ParkingApp.model.listing.Listing;

import java.text.DecimalFormat;
import java.util.*;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID userUUID;
    String username;
    String email;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Listing> listings = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbackList = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "user_chats_mapping", joinColumns = @JoinColumn(name = "userUUID"))
    @MapKeyColumn(name = "chatKey")
    @Column(name = "chatValue")
    private Map<UUID, UUID> chats;
    @ElementCollection(targetClass = UUID.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "unread_chats", joinColumns = @JoinColumn(name = "userUUID"))
    @Column(name = "unread_chat")
    @JsonIgnore
    private Set<UUID> unreadChats = new HashSet<>();

    private boolean isTrusted;
    private double rating;
    private boolean hasProfilePicture ;
    private String profilePicturePath;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String sex;
    private int age;




    public void computeNewRating() {
        double averageRating = feedbackList.stream()
                .mapToInt(Feedback::getRatingGiven)
                .average()
                .orElse(0);
        DecimalFormat df = new DecimalFormat("#0.00");
        String formattedRating = df.format(averageRating);

        this.rating = Double.parseDouble(formattedRating);

    }
    public void addChat(UUID userUUID, UUID chatUUID) {
        this.chats.put(userUUID, chatUUID);
    }
    public void addUnreadMessage(UUID chatUUID) {
        this.unreadChats.add(chatUUID);
    }
    public void removeUnreadMessage(UUID chatUUID) {
        this.unreadChats.remove(chatUUID);
    }

}