package ru.skypro.couresework.auction.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.couresework.auction.Entity.Lot;
import ru.skypro.couresework.auction.Entity.LotStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LotRepository extends JpaRepository<Lot, Long> {
    Page<Lot> findByStatus(LotStatus status, Pageable pageable);

}
