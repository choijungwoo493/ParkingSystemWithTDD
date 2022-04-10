package com.nhnacademy.park;

import com.nhnacademy.park.Car;
import com.nhnacademy.park.NoParkingSpaceException;
import com.nhnacademy.park.Park;
import com.nhnacademy.park.ParkSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ParkTest {
    Park park;
    Car car;
    ParkSystem parkSystem;

    @BeforeEach
    void setUp(){
        park = spy(Park.class);
        parkSystem = new ParkSystem(park);
        car = new Car("123가 1234",1);
    }

    @DisplayName("차량 출입시 스캐너 정상작동 확인")
    @Test
    void carInScannerTest(){
        String carNumber = "123가 1234";
        int carSize = 1;
        Car car = new Car("123가 1234",1);
        assertThat(car.getCarNumber()).isEqualTo(carNumber);
        assertThat(car.getCarSize()).isEqualTo(carSize);
    }//스캐너는 정상작동

    @DisplayName("주차장 자리확인 테스트")
    @Test
    void findSpaceSuccessTest(){
        assertThat(park.findSpace()).isNotNull();
        System.out.println(park.findSpace());
    }
    @DisplayName("주차장 주차 실패 테스트")
    @Test
    void findSpaceFailTest(){
        parkSystem.carIn(park,new Car("1",1));
        parkSystem.carIn(park,new Car("2",1));
        parkSystem.carIn(park,new Car("3",1));
        assertThatThrownBy(()-> park.findSpace())
                .isInstanceOf(NoParkingSpaceException.class)
                .hasMessage("주차장에 남은 자리가 없습니다");
    }

    @DisplayName("차량을 주차한다(레포지토리에 등록)")
    @Test
    void parkingCarTest2(){

        park = new Park();
        car = new Car("123가 1234",1);

        assertThat(park.findCar(car)).isFalse();
        park.parkingCar(car);
        assertThat(park.findCar(car)).isTrue();
    }


    @DisplayName("주차장에서 차가나간다.")
    @Test
    void unParkingCarTest(){
        assertThat(park.findCar(car)).isFalse();
        park.parkingCar(car);
        assertThat(park.findCar(car)).isTrue();
        park.unparkingCar(car);
        assertThat(park.findCar(car)).isFalse();
    }


    @DisplayName("차량이 주차장에 있는 시간 계산")
    @Test
    void setCarInTimeAndOutTimeTest(){
        when(park.getParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 20, 0));
        when(park.getUnParkingTime(car)).thenReturn(LocalDateTime.of(2022, 7, 13, 15, 25, 0));
        parkSystem.carIn(park,car);
        park.unparkingCar(car);
        LocalDateTime startDateTime = park.getParkingTime(car);
        LocalDateTime endDateTime = park.getUnParkingTime(car);
        Long btx_seconds = ChronoUnit.SECONDS.between(startDateTime, endDateTime);
        assertThat(btx_seconds).isEqualTo(300L);

    }

}
