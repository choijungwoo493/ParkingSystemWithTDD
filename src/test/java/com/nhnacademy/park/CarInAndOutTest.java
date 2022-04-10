package com.nhnacademy.park;

import com.nhnacademy.park.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CarInAndOutTest {
    Car car;
    Park park;
    ParkSystem parkSystem;
    User user;
    PaymentService paymentService;
    Pay pay;

    @BeforeEach
    void setUp(){
        car = new Car("123가 1234",1);
        park = spy(Park.class);
        parkSystem = new ParkSystem(park);
        user = new User("choi",20_000L);
        paymentService = new PaymentService();
        pay = new PaymentService();
    }
    @DisplayName("차량 출입 테스트")
    @Test
    void carInTest(){
        parkSystem.carIn(park,car);
        assertThat(park.findCar(car)).isTrue();
    }
    @DisplayName("차량 사이즈 출입 테스트")
    @Test
    void carInFailTest(){
        Car bigSizeCar = new Car("123가 1212",0);
        assertThatThrownBy(()->parkSystem.carIn(park,bigSizeCar))
                .isInstanceOf(IllegalCarSizeException.class)
                .hasMessage("주차할 수 없는 사이즈입니다");

    }

    @DisplayName("차량 출차 테스트(+ 돈계산 가능)")
    @Test
    void carOutSuccessTest(){

        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 49, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 50, 1));
        parkSystem.carIn(park,car);
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicy(startDateTime,endDateTime)).isEqualTo(1000L);

        assertThat(user.getAmount()).isEqualTo(20_000L);
        assertThat(park.findCar(car)).isTrue();
        parkSystem.carOut(park,user,car,pay);
        assertThat(user.getAmount()).isEqualTo(20_000L);
        assertThat(park.findCar(car)).isFalse();
    }

    @DisplayName("차량 출차 테스트(+ 돈계산 불가능)")
    @Test
    void carOutFailTest(){

        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 10, 15, 49, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 50, 1));
        parkSystem.carIn(park,car);
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        assertThat(paymentService.payPolicy(startDateTime,endDateTime)).isEqualTo(40_000L);

        assertThat(user.getAmount()).isEqualTo(20_000L);
        assertThat(park.findCar(car)).isTrue();
        assertThatThrownBy(()->parkSystem.carOut(park,user,car,pay))
                .isInstanceOf(MinusAmountException.class)
                .hasMessage("가격이 음수에요");

        assertThat(user.getAmount()).isEqualTo(20_000L);
        assertThat(park.findCar(car)).isTrue();
    }
}
