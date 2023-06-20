package ru.skypro.couresework.auction.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skypro.couresework.auction.Entity.Bid;
import ru.skypro.couresework.auction.Entity.Lot;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
    @Query("SELECT b FROM Bid b WHERE b.lot = ?1 ORDER BY b.bidDate ASC")
    Optional<Bid> findFirstByLotOrderByBidDateAsc(Lot lot);

    @Query("SELECT b.bidderName FROM Bid b WHERE b.lot = :lot GROUP BY b.bidderName ORDER BY COUNT(b.bidderName) DESC")
    String findMostFrequentBidderByLot(@Param("lot") Lot lot);

    @Query("SELECT COUNT(b) FROM Bid b WHERE b.lot = :lot")
    int countByLot(Lot lot);

    Bid findTopByLotOrderByBidDateDesc(Lot lot);

}
