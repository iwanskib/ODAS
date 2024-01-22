package odas.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String targetAccountNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;

    public Transaction() {
        this.createdAt = new Date();
    }

    public Transaction(Account account, BigDecimal amount, String title, String targetAccountNumber) {
        this.account = account;
        this.amount = amount;
        this.title = title;
        this.targetAccountNumber = targetAccountNumber;
        this.createdAt = new Date();
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getTitle() {
        return title;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}

