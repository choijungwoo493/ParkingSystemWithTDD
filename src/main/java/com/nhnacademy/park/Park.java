package com.nhnacademy.park;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Park {

    static Park park;
    static Car car;
    public static final int NO_SPACE = -1;
    public static final int PARK_SPACE_SIZE = 3;

    public static void main(String[] args) {
        park = new Park();
        car = new Car("123가 1234",1);
        park.parkingCar(car);
    }

    protected static final List<Car> park1 = new ArrayList<>(Collections.nCopies(PARK_SPACE_SIZE, new Car("null",-1)));

    public void parkingCar(Car car){

        if (car.getCarSize() != 1 && car.getCarSize() != 2){
            throw new IllegalCarSizeException();
        }
        int space = findSpace();
        park1.set(space,car);
        setCarInTime(car);
    }

    public int findSpace(){
        for (int i = 0; i < park1.size(); i++) {
            if (park1.get(i).getCarNumber().equals("null")) {
                return i;
            }
        }
        throw new NoParkingSpaceException();
    }
    public void setCarInTime(Car car){
        car.intime = LocalDateTime.now();
    }
    public void setCarOutTime(Car car){
        car.outtime = LocalDateTime.now();
    }

    public void unparkingCar(Car car){
        for (int i = 0; i < park1.size(); i++) {
            if (park1.get(i).equals(car)){
                park1.set(i,new Car("",-1));
                setCarOutTime(car);
            }

        }
    }

    public boolean findCar(Car car){
        for (Car cars: park1
             ) {
            if(cars.equals(car)){
                return true;
            }
        }
        return false;
    }

    public LocalDateTime getParkingTime(Car car) {
        return car.intime;
    }

    public LocalDateTime getUnParkingTime(Car car) {
        return car.outtime;
    }
}
