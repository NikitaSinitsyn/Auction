package ru.skypro.couresework.auction.Service;

import ru.skypro.couresework.auction.DTO.BidDTO;

public interface BidService {
    void placeBid(Long lotId, BidDTO bidDTO);
}
