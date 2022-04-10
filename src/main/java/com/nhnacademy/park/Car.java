package com.nhnacademy.park;

import java.time.LocalDateTime;

public class Car {

    private String carNumber;
    private int carSize;
    public LocalDateTime intime;
    public LocalDateTime outtime;


    public Car(String carNumber, int carSize) {
        this.carNumber = carNumber;
        this.carSize = carSize;
        this.intime = LocalDateTime.now();
        this.outtime =LocalDateTime.now();;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public int getCarSize() {
        return carSize;
    }

    @Override
    public String toString() {
        return "Car{" +
                "carNumber='" + carNumber + '\'' +
                ", carSize=" + carSize +
                '}';
    }
}
