package ru.skypro.couresework.auction.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.skypro.couresework.auction.CustomExceptionHandler.InvalidStatusException;
import ru.skypro.couresework.auction.DTO.BidDTO;
import ru.skypro.couresework.auction.Entity.Bid;
import ru.skypro.couresework.auction.Entity.Lot;
import ru.skypro.couresework.auction.Entity.LotStatus;
import ru.skypro.couresework.auction.Repository.BidRepository;
import ru.skypro.couresework.auction.Repository.LotRepository;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService{
    private final LotRepository lotRepository;
    private final BidRepository bidRepository;

    private static final Logger logger = LoggerFactory.getLogger(BidServiceImpl.class);

    @Transactional
    public void placeBid(Long lotId, @Valid BidDTO bidDTO) {
        // Поиск лота по ID
        Lot lot = lotRepository.findById(lotId).orElseThrow(() -> new NotFoundException("Lot not found"));

        // Проверка статуса лота
        if (lot.getStatus() != LotStatus.STARTED) {
            throw new InvalidStatusException("Cannot place bid on lot in the current status");
        }

        // Логирование информации о ставке
        logger.info("Placing bid on lot with ID: {}", lotId);
        logger.info("Bid details: bidderName={}, bidTime={}", bidDTO.getBidderName(), LocalDateTime.now());

        // Создание новой ставки и сохранение в базе данных
        Bid bid = new Bid(bidDTO.getBidderName(), LocalDateTime.now());
        bid.setLot(lot);
        bidRepository.save(bid);
    }
}
