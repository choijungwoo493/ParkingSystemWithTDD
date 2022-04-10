package com.nhnacademy.park;

import com.nhnacademy.park.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PayPolicy1Test {
    Park park;
    Car car;
    ParkSystem parkSystem;
    User user;
    PaymentService paymentService;

    public LocalDateTime set(int day, int hours, int minutes, int seconds){
        return LocalDateTime.of(2022, 7, day, hours, minutes, seconds);
    }
    @BeforeEach
    void setUp() {
        park = mock(Park.class);
        parkSystem = new ParkSystem(park);
        car = new Car("123가 1234", 1);
        user = new User("choi", 20_000L);
        paymentService = new PaymentService();
    }

    @DisplayName("주차비 계산(1분 1초)")
    @Test
    void CalculateParkingPay1() {

        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 49, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 50, 1));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicy(startDateTime, endDateTime)).isEqualTo(1000L);
    }

    @DisplayName("주차비 계산(30분 1초)")
    @Test
    void CalculateParkingPay2() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 20, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 50, 1));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicy(startDateTime, endDateTime)).isEqualTo(1500L);

    }

    @DisplayName("주차비 계산(기본요금 초과)")
    @Test
    void CalculateParkingTime3() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 10, 20, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 25, 0));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicy(startDateTime, endDateTime)).isEqualTo(10_000L);
    }

    @DisplayName("주차비 계산(3시간)")
    @Test
    void CalculateParkingTime4() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 10, 20, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 13, 20, 0));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicy(startDateTime, endDateTime)).isEqualTo(8_500L);
    }

    @DisplayName("주차비 계산(1분 1초, 날짜 바뀜)")
    @Test
    void CalculateParkingTime5() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 23, 59, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 14, 0, 0, 1));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicy(startDateTime, endDateTime)).isEqualTo(1_000L);
    }

    @DisplayName("주차비 계산(30분 1초, 날짜 바뀜)")
    @Test
    void CalculateParkingTime6() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 23, 45, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 14, 0, 15, 1));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicy(startDateTime, endDateTime)).isEqualTo(1_500L);
    }

    @DisplayName("주차비 계산(10분 1초, 날짜 바뀜)")
    @Test
    void CalculateParkingTime7() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 23, 20, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 14, 1, 0, 1));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicy(startDateTime, endDateTime)).isEqualTo(5_000L);
    }

    @DisplayName("주차비 계산(30분 1초, 전날 주차비 만원 미만,다음날 주차비 만원 미만)")
    @Test
    void CalculateParkingTime8() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 21, 30, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 14, 2, 30, 1));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicy(startDateTime, endDateTime)).isEqualTo(15_000L);
    }

    @DisplayName("주차비 계산(360분 1초, 전날 주차비 만원 미만, 다음날 만원 초과)")
    @Test
    void CalculateParkingTime9() {

        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 22, 20, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 14, 4, 20, 1));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicy(startDateTime, endDateTime)).isEqualTo(15_000L);
    }

    @DisplayName("주차비 계산(360분 1초, 전날주차비는 만원 초과,다음날 만원 미만)")
    @Test
    void CalculateParkingTime10() {
        when(park.getParkingTime(car)).thenReturn(set(13,3,20,0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 14, 1, 20, 1));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicy(startDateTime, endDateTime)).isEqualTo(14_000L);
    }

    @DisplayName("주차비 계산(720분, 전날주차비는 만원 초과,다음날 만원 초과)")
    @Test
    void CalculateParkingTime11() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 18, 20, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 14, 6, 20, 0));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicy(startDateTime, endDateTime)).isEqualTo(20_000L);
    }
}

