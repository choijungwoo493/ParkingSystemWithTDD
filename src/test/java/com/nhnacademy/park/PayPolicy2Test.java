package com.nhnacademy.park;

import com.nhnacademy.park.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PayPolicy2Test {
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

        when(park.getParkingTime(car)).thenReturn(set(13,15,20,0));
        when(park.getUnParkingTime(car)).thenReturn(set(13,15,21,1));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicyWithCar(startDateTime, endDateTime)).isEqualTo(0L);
    }

    @DisplayName("주차비 계산(30분 1초)")
    @Test
    void CalculateParkingPay2() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 20, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 50, 1));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicyWithCar(startDateTime, endDateTime)).isEqualTo(1_000L);

    }

    @DisplayName("주차비 계산(59분 59초 )")
    @Test
    void CalculateParkingTime3() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 0, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 59, 59));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicyWithCar(startDateTime, endDateTime)).isEqualTo(1_000L);
    }
    @DisplayName("주차비 계산(60분 1초 )")
    @Test
    void CalculateParkingTime4() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 0, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 16, 0, 1));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicyWithCar(startDateTime, endDateTime)).isEqualTo(1_500L);
    }

    @DisplayName("주차비 계산(5시간30분)")
    @Test
    void CalculateParkingTime5() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 10, 20, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 50, 0));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicyWithCar(startDateTime, endDateTime)).isEqualTo(14_500L);
    }

    @DisplayName("주차비 계산(5시간 30분 1초)")
    @Test
    void CalculateParkingTime6() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 10, 20, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 50, 1));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicyWithCar(startDateTime, endDateTime)).isEqualTo(15_000L);
    }

    @DisplayName("주차비 계산(1분 1초, 날짜 바뀜)")
    @Test
    void CalculateParkingTime7() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 23, 59, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 14, 0, 0, 1));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicyWithCar(startDateTime, endDateTime)).isEqualTo(0L);
    }

    @DisplayName("주차비 계산(30분 1초, 날짜 바뀜)")
    @Test
    void CalculateParkingTime8() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 23, 45, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 14, 0, 15, 1));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicyWithCar(startDateTime, endDateTime)).isEqualTo(1_000L);
    }

    @DisplayName("주차비 계산(60분, 날짜 바뀜)")
    @Test
    void CalculateParkingTime9() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 23, 20, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 14, 0, 20, 0));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicyWithCar(startDateTime, endDateTime)).isEqualTo(1_000L);
    }
    @DisplayName("주차비 계산(330분, 날짜 바뀜)")
    @Test
    void CalculateParkingTime10() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 23, 20, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 14, 4, 50, 0));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicyWithCar(startDateTime, endDateTime)).isEqualTo(14_500L);
    }

    @DisplayName("주차비 계산(340분, 전날 주차비 한계 미만,다음날 주차비 한계 미만)")
    @Test
    void CalculateParkingTime11() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 18, 30, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 14, 0, 10, 0));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicyWithCar(startDateTime, endDateTime)).isEqualTo(14_500L);
    }

    @DisplayName("주차비 계산(340분 1초, 전날 주차비 한계 미만, 다음날 주차비 한계 초과)")
    @Test
    void CalculateParkingTime12() {

        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 23, 55, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 14, 5, 35, 1));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicyWithCar(startDateTime, endDateTime)).isEqualTo(15_000L);
    }

    @DisplayName("주차비 계산(330분 2초, 전날 주차비 한계 초과,다음날 한계 미만)")
    @Test
    void CalculateParkingTime13() {
        when(park.getParkingTime(car)).thenReturn(set(13,18,29,59));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 14, 0, 0, 1));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicyWithCar(startDateTime, endDateTime)).isEqualTo(15_000L);
    }

    @DisplayName("주차비 계산(24시간, 전날주차비는 한계 초과,다음날 한계 초과)")
    @Test
    void CalculateParkingTime14() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 12, 0, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 14, 12, 0, 0));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicyWithCar(startDateTime, endDateTime)).isEqualTo(30_000L);
    }
    @DisplayName("경차가 아니라 할인 안됨")
    @Test
    void carSizeDiscountTest() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 12, 0, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 14, 12, 0, 0));
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.carSizeDiscount(paymentService.payPolicyWithCar(startDateTime, endDateTime),car)).isEqualTo(30_000L);
    }
    @DisplayName("경차할인 적용")
    @Test
    void carSizeDiscountTest2() {
        Car car2 = new Car("123나 1234",2);
        when(park.getParkingTime(car2)).thenReturn(LocalDateTime.of(2022, 7, 13, 12, 0, 0));
        when(park.getUnParkingTime(car2)).thenReturn(LocalDateTime.of(2022, 7, 14, 12, 0, 0));
        LocalDateTime startDateTime = park.getParkingTime(car2);
        LocalDateTime endDateTime = park.getUnParkingTime(car2);
        assertThat(paymentService.carSizeDiscount(paymentService.payPolicyWithCar(startDateTime, endDateTime),car2)).isEqualTo(15_000L);
    }
}

