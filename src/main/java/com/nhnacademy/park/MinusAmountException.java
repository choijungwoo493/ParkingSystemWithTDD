package com.nhnacademy.park;

public class MinusAmountException extends IllegalArgumentException{
    public MinusAmountException() {
        super("가격이 음수에요");
    }
}