package com.nhnacademy.park;

public class NoParkingSpaceException extends IllegalArgumentException {
    public NoParkingSpaceException(){
        super("주차장에 남은 자리가 없습니다");
    }
}
