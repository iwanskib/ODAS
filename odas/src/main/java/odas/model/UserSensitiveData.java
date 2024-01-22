package odas.model;

import jakarta.persistence.*;
import odas.service.EncryptionService;

@Entity
@Table(name = "user_sensitive_data")
public class UserSensitiveData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "encrypted_card_number")
    private String encryptedCardNumber;

    @Column(name = "encrypted_id_card_number")
    private String encryptedIdCardNumber;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public UserSensitiveData() {
    }
    public UserSensitiveData(String cardNumber, String idCardNumber, EncryptionService encryptionService, String encryptionKey) {
        this.encryptedCardNumber = encryptionService.encrypt(cardNumber, encryptionKey);
        this.encryptedIdCardNumber = encryptionService.encrypt(idCardNumber, encryptionKey);
    }

    public String getEncryptedCardNumber() {
        return encryptedCardNumber;
    }
    public String getEncryptedIdCardNumber() {
        return encryptedIdCardNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}