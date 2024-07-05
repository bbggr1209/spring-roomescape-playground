package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.model.Reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ReservationController {

    private List<Reservation> reservations = new ArrayList<>();

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    public ReservationController() {
        reservations.add(new Reservation(1L, "브라운", LocalDate.of(2023, 1, 1), "10:00"));
        reservations.add(new Reservation(2L, "브라운", LocalDate.of(2023, 1, 2), "11:00"));
        reservations.add(new Reservation(3L, "브라운", LocalDate.of(2023, 1, 3), "12:00"));
    }

    @GetMapping("/reservations")
    @ResponseBody
    public List<Reservation> getReservations() {
        return reservations;
    }
}
