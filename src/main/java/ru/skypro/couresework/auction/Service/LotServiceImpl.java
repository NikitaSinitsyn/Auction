package ru.skypro.couresework.auction.Service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.skypro.couresework.auction.CustomExceptionHandler.InvalidStatusException;
import ru.skypro.couresework.auction.DTO.BidDTO;
import ru.skypro.couresework.auction.DTO.CreateLotDTO;
import ru.skypro.couresework.auction.DTO.LotDTO;
import ru.skypro.couresework.auction.Entity.Bid;
import ru.skypro.couresework.auction.Entity.Lot;
import ru.skypro.couresework.auction.Entity.LotStatus;
import ru.skypro.couresework.auction.Repository.BidRepository;
import ru.skypro.couresework.auction.Repository.LotRepository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.awt.print.Pageable;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class LotServiceImpl implements LotService{
    private final LotRepository lotRepository;
    private final BidRepository bidRepository;
    private static final Logger logger = LoggerFactory.getLogger(LotServiceImpl.class);


    @Override
    public LotDTO createLot(@Valid CreateLotDTO createLotDTO) {
        // Валидация createLotDTO уже происходит благодаря аннотации @Valid

        // Логирование информации о создании лота
        logger.info("Creating a new lot: {}", createLotDTO);

        // Создание нового лота и сохранение в базе данных
        Lot lot = new Lot(createLotDTO.getTitle(), createLotDTO.getDescription(), createLotDTO.getStartPrice(), createLotDTO.getBidPrice());
        Lot savedLot = lotRepository.save(lot);

        // Логирование информации о сохраненном лоте
        logger.info("Lot created: {}", savedLot);

        return mapToLotDTO(savedLot);
    }

    @Override
    public void startAuction(Long lotId) {
        // Поиск лота по ID
        Lot lot = lotRepository.findById(lotId).orElseThrow(() -> new NotFoundException("Lot not found"));

        // Логирование информации о начале аукциона
        logger.info("Starting auction for lot: {}", lot);

        // Проверка статуса лота
        if (lot.getStatus() == LotStatus.CREATED) {
            // Изменение статуса лота на STARTED и сохранение в базе данных
            lot.setStatus(LotStatus.STARTED);
            lotRepository.save(lot);

            // Логирование информации об успешном начале аукциона
            logger.info("Auction started for lot: {}", lot);
        } else {
            // Логирование информации о невозможности начать аукцион
            logger.warn("Cannot start auction for lot {}. Invalid lot status: {}", lot.getId(), lot.getStatus());
        }
    }

    @Override
    public void placeBid(Long lotId, BidDTO bidDTO) {
        // Поиск лота по ID
        Lot lot = lotRepository.findById(lotId).orElseThrow(() -> new NotFoundException("Lot not found"));

        // Проверка статуса лота
        if (lot.getStatus() != LotStatus.STARTED) {
            throw new InvalidStatusException("Cannot place bid on lot in the current status");
        }

        // Валидация bidDTO

        // Создание новой ставки и сохранение в базе данных
        Bid bid = new Bid(bidDTO.getBidderName(), LocalDateTime.now());
        bid.setLot(lot);
        bidRepository.save(bid);
    }

    @Override
    public void stopAuction(Long lotId) {
        // Логирование информации о запросе на остановку аукциона для лота с указанным ID
        logger.info("Stopping auction for lotId: {}", lotId);

        // Поиск лота по ID
        Lot lot = lotRepository.findById(lotId).orElseThrow(() -> {
            // Логирование информации об ошибке: лот не найден
            logger.error("Lot not found with ID: {}", lotId);
            return new NotFoundException("Lot not found");
        });

        // Проверка статуса лота
        if (lot.getStatus() == LotStatus.STARTED) {
            // Изменение статуса лота на STOPPED и сохранение в базе данных
            lot.setStatus(LotStatus.STOPPED);
            lotRepository.save(lot);

            // Логирование информации об успешной остановке аукциона
            logger.info("Auction stopped for lot: {}", lot);
        } else {
            // Логирование информации о невозможности остановить аукцион
            logger.warn("Cannot stop auction for lot {}. Invalid lot status: {}", lot.getId(), lot.getStatus());
        }
    }

    @Override
    public LotDTO getLotById(Long lotId) {
        // Логирование информации о запросе на получение лота по ID
        logger.info("Getting lot by ID: {}", lotId);

        // Поиск лота по ID
        Lot lot = lotRepository.findById(lotId).orElseThrow(() -> {
            // Логирование информации об ошибке: лот не найден
            logger.error("Lot not found with ID: {}", lotId);
            return new NotFoundException("Lot not found");
        });

        // Логирование информации о полученном лоте
        logger.info("Lot found: {}", lot);

        return mapToLotDTO(lot);
    }

    @Override
    public BidDTO getFirstBidder(Long lotId) {
        // Логирование информации о запросе на получение первой ставки по лоту
        logger.info("Getting first bidder for lot with ID: {}", lotId);

        // Поиск лота по ID
        Lot lot = lotRepository.findById(lotId).orElseThrow(() -> {
            // Логирование информации об ошибке: лот не найден
            logger.error("Lot not found with ID: {}", lotId);
            return new NotFoundException("Lot not found");
        });

        // Получение первой ставки
        Optional<Bid> firstBid = bidRepository.findFirstByLotOrderByBidDateAsc(lot);

        if (firstBid.isEmpty()) {
            // Логирование информации: ставки не найдены для данного лота
            logger.warn("No bids found for lot with ID: {}", lotId);
            throw new NotFoundException("No bids found for this lot");
        }

        // Логирование информации о найденной первой ставке
        logger.info("First bid found for lot with ID: {}, Bid: {}", lotId, firstBid.get());

        return mapToBidDTO(firstBid.get());
    }

    @Override
    public BidDTO getMostFrequentBidder(Long lotId) {
        // Логирование информации о запросе на получение наиболее частого участника торгов
        logger.info("Getting most frequent bidder for lot with ID: {}", lotId);

        // Поиск лота по ID
        Lot lot = lotRepository.findById(lotId).orElseThrow(() -> {
            // Логирование информации об ошибке: лот не найден
            logger.error("Lot not found with ID: {}", lotId);
            return new NotFoundException("Lot not found");
        });

        // Получение наиболее частого участника торгов
        String mostFrequentBidder = bidRepository.findMostFrequentBidderByLot(lot);

        if (mostFrequentBidder == null) {
            // Логирование информации: ставки не найдены для данного лота
            logger.warn("No bids found for lot with ID: {}", lotId);
            throw new NotFoundException("No bids found for this lot");
        }

        // Логирование информации о наиболее частом участнике торгов
        logger.info("Most frequent bidder found for lot with ID: {}, Bidder Name: {}", lotId, mostFrequentBidder);

        BidDTO bidDTO = new BidDTO();
        bidDTO.setBidderName(mostFrequentBidder);

        return bidDTO;
    }

    @Override
    public List<LotDTO> getLotsByStatusAndPage(LotStatus status, int page) {
        logger.info("Getting lots by status and page. Status: {}, Page: {}", status, page);

        // Pagination
        Pageable pageable = (Pageable) PageRequest.of(page, 10);

        // Getting the list of lots by status
        Page<Lot> lotPage = lotRepository.findByStatus(status,(org.springframework.data.domain.Pageable) pageable);

        // Converting Page<Lot> to List<LotDTO>
        List<LotDTO> lotDTOs = lotPage.getContent().stream().map(this::mapToLotDTO).collect(Collectors.toList());

        logger.info("Retrieved {} lots.", lotDTOs.size());

        return lotDTOs;
    }

    // Преобразование Lot в LotDTO
    private LotDTO mapToLotDTO(Lot lot) {
        LotDTO lotDTO = new LotDTO();
        lotDTO.setId(lot.getId());
        lotDTO.setTitle(lot.getTitle());
        lotDTO.setDescription(lot.getDescription());
        lotDTO.setStartPrice(lot.getStartPrice());
        lotDTO.setBidPrice(lot.getBidPrice());
        lotDTO.setStatus(lot.getStatus());
        lotDTO.setCurrentPrice(calculateCurrentPrice(lot));
        lotDTO.setLastBid(getLastBidDTO(lot));

        return lotDTO;
    }

    // Вычисление текущей цены лота
    private Integer calculateCurrentPrice(Lot lot) {
        int bidCount = bidRepository.countByLot(lot);
        return bidCount * lot.getBidPrice() + lot.getStartPrice();
    }

    // Получение последней ставки по лоту
    private BidDTO getLastBidDTO(Lot lot) {
        Bid lastBid = bidRepository.findTopByLotOrderByBidDateDesc(lot);

        if (lastBid == null) {
            return null;
        }

        return mapToBidDTO(lastBid);
    }

    // Преобразование Bid в BidDTO
    private BidDTO mapToBidDTO(Bid bid) {
        BidDTO bidDTO = new BidDTO();
        bidDTO.setBidderName(bid.getBidderName());
        bidDTO.setBidDate(bid.getBidDate());

        return bidDTO;
    }
    public List<Lot> getAllLots() {
        return lotRepository.findAll();
    }
}

