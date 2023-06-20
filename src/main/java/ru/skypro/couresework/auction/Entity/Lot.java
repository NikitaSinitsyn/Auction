package ru.skypro.couresework.auction.Entity;
import jakarta.persistence.*;
import lombok.*;


import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "lot")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 64)
    private String title;

    @NotBlank
    @Size(min = 1, max = 4096)
    private String description;

    @NotNull
    @Min(value = 1)
    private Integer startPrice;

    @NotNull
    @Min(value = 1)
    private Integer bidPrice;

    @Enumerated(EnumType.STRING)
    private LotStatus status;
    private String lastBidder;
    private BigDecimal currentPrice;

    @OneToMany(mappedBy = "lot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bid> bids;

    public Lot(String title, String description, Integer startPrice, Integer bidPrice) {
        this.title = title;
        this.description = description;
        this.startPrice = startPrice;
        this.bidPrice = bidPrice;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lot lot = (Lot) o;
        return Objects.equals(id, lot.id) && Objects.equals(title, lot.title) && Objects.equals(description, lot.description) && Objects.equals(startPrice, lot.startPrice) && Objects.equals(bidPrice, lot.bidPrice) && status == lot.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, startPrice, bidPrice, status);
    }

    @Override
    public String toString() {
        return "Lot{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startPrice=" + startPrice +
                ", bidPrice=" + bidPrice +
                ", status=" + status +
                '}';
    }
}
