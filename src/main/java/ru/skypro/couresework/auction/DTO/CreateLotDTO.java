package ru.skypro.couresework.auction.DTO;
import lombok.*;

import javax.validation.constraints.*;


@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CreateLotDTO {
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
}
