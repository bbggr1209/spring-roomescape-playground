package roomescape.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.ReservationReqDto;
import roomescape.dto.ReservationResDto;
import roomescape.model.Reservation;
import roomescape.template.ApiResponseTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Controller
public class ReservationController {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/reservations")
    @ResponseBody
    public ApiResponseTemplate<List<ReservationResDto>> getReservations() {
        List<ReservationResDto> dtoList = reservations.stream()
                .map(reservation -> new ReservationResDto(
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getDate(),
                        reservation.getTime()))
                .collect(Collectors.toList());

        return new ApiResponseTemplate<>("success", dtoList);
    }

    @PostMapping("/reservations")
    @ResponseBody
    public ResponseEntity<ApiResponseTemplate<ReservationResDto>> addReservation(@RequestBody ReservationReqDto reservationReqDto) {
        Reservation newReservation = new Reservation(
                index.getAndIncrement(),
                reservationReqDto.getName(),
                reservationReqDto.getDate(),
                reservationReqDto.getTime()
        );
        reservations.add(newReservation);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/reservations/" + newReservation.getId());

        ReservationResDto newReservationResDto = new ReservationResDto(
                newReservation.getId(),
                newReservation.getName(),
                newReservation.getDate(),
                newReservation.getTime()
        );

        return new ResponseEntity<>(
                new ApiResponseTemplate<>("success", newReservationResDto),
                headers,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/reservations/{id}")
    @ResponseBody
    public ResponseEntity<ApiResponseTemplate<String>> deleteReservation(@PathVariable Long id) {
        boolean removed = reservations.removeIf(reservation -> reservation.getId().equals(id));
        return removed ? createSuccessDeleteResponse() : createNotFoundDeleteResponse();
    }

    private ResponseEntity<ApiResponseTemplate<String>> createSuccessDeleteResponse() {
        return new ResponseEntity<>(new ApiResponseTemplate<>("success", "예약 삭제 성공"), HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<ApiResponseTemplate<String>> createNotFoundDeleteResponse() {
        return new ResponseEntity<>(new ApiResponseTemplate<>("error", "예약을 삭제 실패"), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/reservations/reset")
    @ResponseBody
    public ResponseEntity<Void> resetReservations() {
        index.set(1);
        reservations.clear();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
