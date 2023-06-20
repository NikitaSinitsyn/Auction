package ru.skypro.couresework.auction.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.couresework.auction.DTO.BidDTO;
import ru.skypro.couresework.auction.DTO.CreateLotDTO;
import ru.skypro.couresework.auction.DTO.LotDTO;
import ru.skypro.couresework.auction.Entity.Lot;
import ru.skypro.couresework.auction.Entity.LotStatus;
import ru.skypro.couresework.auction.Service.LotService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/lot")
@RequiredArgsConstructor
public class LotController {
    private final LotService lotService;

    @PostMapping
    public ResponseEntity<LotDTO> createLot(@Valid @RequestBody CreateLotDTO createLotDTO) {
        LotDTO createdLot = lotService.createLot(createLotDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLot);
    }

    @PostMapping("/{lotId}/start")
    public ResponseEntity<Void> startAuction(@PathVariable Long lotId) {
        lotService.startAuction(lotId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{lotId}/bids")
    public ResponseEntity<Void> placeBid(@PathVariable Long lotId, @Valid @RequestBody BidDTO bidDTO) {
        lotService.placeBid(lotId, bidDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{lotId}/stop")
    public ResponseEntity<Void> stopAuction(@PathVariable Long lotId) {
        lotService.stopAuction(lotId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{lotId}")
    public ResponseEntity<LotDTO> getLotById(@PathVariable Long lotId) {
        LotDTO lot = lotService.getLotById(lotId);
        return ResponseEntity.ok(lot);
    }

    @GetMapping("/{lotId}/firstBidder")
    public ResponseEntity<BidDTO> getFirstBidder(@PathVariable Long lotId) {
        BidDTO firstBidder = lotService.getFirstBidder(lotId);
        return ResponseEntity.ok(firstBidder);
    }

    @GetMapping("/{lotId}/mostFrequentBidder")
    public ResponseEntity<BidDTO> getMostFrequentBidder(@PathVariable Long lotId) {
        BidDTO mostFrequentBidder = lotService.getMostFrequentBidder(lotId);
        return ResponseEntity.ok(mostFrequentBidder);
    }

    @GetMapping
    public ResponseEntity<List<LotDTO>> getLotsByStatusAndPage(@RequestParam LotStatus status, @RequestParam int page) {
        List<LotDTO> lots = lotService.getLotsByStatusAndPage(status, page);
        return ResponseEntity.ok(lots);
    }

    @GetMapping("/lot/export")
    public ResponseEntity<byte[]> exportLotsToCSV() {
        try {
            // Создание временного файла CSV в памяти
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outputStream), CSVFormat.DEFAULT);

            // Запись заголовка
            csvPrinter.printRecord("id", "title", "status", "lastBidder", "currentPrice");

            // Запись данных лотов
            List<Lot> lots = lotService.getAllLots();
            for (Lot lot : lots) {
                csvPrinter.printRecord(
                        lot.getId(),
                        lot.getTitle(),
                        lot.getStatus(),
                        lot.getLastBidder(),
                        lot.getCurrentPrice()
                );
            }

            // Закрытие CSV принтера
            csvPrinter.close();

            // Получение массива байт из временного файла CSV
            byte[] csvBytes = outputStream.toByteArray();

            // Установка заголовков HTTP для отправки файла
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "lots.csv");
            headers.setContentLength(csvBytes.length);

            // Возврат CSV файла как массива байт
            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            // Обработка исключения IOException
            e.printStackTrace(); // Или любая другая обработка ошибки
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Возврат ошибки сервера
        }
    }

}
