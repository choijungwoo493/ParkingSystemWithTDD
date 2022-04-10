package com.nhnacademy.park;

public class IllegalCarSizeException extends IllegalArgumentException {
    public IllegalCarSizeException(){
        super("주차할 수 없는 사이즈입니다");
    }
}
