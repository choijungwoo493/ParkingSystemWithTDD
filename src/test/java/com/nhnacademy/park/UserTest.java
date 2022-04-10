package com.nhnacademy.park;

import com.nhnacademy.park.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserTest {
    Car car;
    User user;
    Park park;
    ParkSystem parkSystem;
    PaymentService paymentService;


    @BeforeEach
    void setUp(){
        car = new Car("123가 1234", 1);
        user = new User("choi",20_000L);
        user.getCar(car);
        parkSystem = new ParkSystem(park);
        park = mock(Park.class);
        paymentService = new PaymentService();

    }


    @DisplayName("car를 잘 탔는가")
    @Test
    void getCarTest() {

        assertThat(user.car).isEqualTo(car);
    }

    @DisplayName("car의 주차비용 계산")
    @Test
    void getCarParkingFee() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 20, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 50, 1));
        car.intime = park.getParkingTime(car);
        car.outtime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicy(car.intime,car.outtime)).isEqualTo(1500L);
    }

    @DisplayName("user가 주차비용을 계산하고 car가 주차장에서 잘 나오는지")
    @Test
    void minusAmount() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 20, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 50, 1));
        car.intime = park.getParkingTime(car);
        car.outtime = park.getUnParkingTime(car);

    }



    @DisplayName("돈 결제 테스트( 결제 가능)")
    @Test
    void payParkingFeeSuccessTest() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 20, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 50, 1));
        car.intime = park.getParkingTime(car);
        car.outtime = park.getUnParkingTime(car);
        long parkingFee = paymentService.payPolicy(car.intime,car.outtime);
        assertThat(user.getAmount()).isEqualTo(20_000L);
        user.payParkingFee(parkingFee);
        assertThat(user.getAmount()).isEqualTo(18_500L);
    }

    @DisplayName("돈 결제 테스트( 결제 불가능)")
    @Test
    void payParkingFeeFailTest() {
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 20, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 16, 15, 50, 1));
        car.intime = park.getParkingTime(car);
        car.outtime = park.getUnParkingTime(car);
        long parkingFee = paymentService.payPolicy(car.intime,car.outtime);
        assertThat(user.getAmount()).isEqualTo(20_000L);
        assertThatThrownBy(()->user.payParkingFee(parkingFee))
                .isInstanceOf(MinusAmountException.class)
                .hasMessage("가격이 음수에요");

    }
}