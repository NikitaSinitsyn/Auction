package ru.skypro.couresework.auction.DTO;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.skypro.couresework.auction.Entity.LotStatus;
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class LotDTO {
    private Long id;
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;
    private LotStatus status;
    private Integer currentPrice;
    private BidDTO lastBid;

    public LotDTO() {

    }
}
