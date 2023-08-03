package ru.skypro.couresework.auction.Service;

import ru.skypro.couresework.auction.DTO.BidDTO;
import ru.skypro.couresework.auction.DTO.CreateLotDTO;
import ru.skypro.couresework.auction.DTO.LotDTO;
import ru.skypro.couresework.auction.Entity.Lot;
import ru.skypro.couresework.auction.Entity.LotStatus;

import java.util.List;

public interface LotService {
    LotDTO createLot(CreateLotDTO createLotDTO);
    void startAuction(Long lotId);
    void placeBid(Long lotId, BidDTO bidDTO);
    void stopAuction(Long lotId);
    LotDTO getLotById(Long lotId);
    BidDTO getFirstBidder(Long lotId);
    BidDTO getMostFrequentBidder(Long lotId);
    List<LotDTO> getLotsByStatusAndPage(LotStatus status, int page);

    List<Lot> getAllLots();
}
