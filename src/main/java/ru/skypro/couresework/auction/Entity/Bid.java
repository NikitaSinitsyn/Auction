package ru.skypro.couresework.auction.Entity;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "bid")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String bidderName;

    @NotNull
    private LocalDateTime bidDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private Lot lot;

    public Bid(String bidderName, LocalDateTime bidDate) {
        this.bidderName = bidderName;
        this.bidDate = bidDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return Objects.equals(id, bid.id) && Objects.equals(bidderName, bid.bidderName) && Objects.equals(bidDate, bid.bidDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bidderName, bidDate);
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", bidderName='" + bidderName + '\'' +
                ", bidDate=" + bidDate +
                '}';
    }
}
