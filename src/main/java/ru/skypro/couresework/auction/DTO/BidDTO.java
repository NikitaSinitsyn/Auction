package ru.skypro.couresework.auction.DTO;

import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor

@Getter
@Setter
@EqualsAndHashCode
public class BidDTO {
    private String bidderName;
    private LocalDateTime bidDate;
    public BidDTO() {
    }
}
