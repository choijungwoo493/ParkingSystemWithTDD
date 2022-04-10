package com.nhnacademy.park;

public class User {
    String id;
    long amount;
    public Car car;
    public User(String id,long amount){
        this.id = id;
        this.amount = amount;
    }

    public void payParkingFee(long parkingFee){
        minusAmount(parkingFee);
    }

    public void minusAmount(long parkingFee){
        if(this.amount - parkingFee < 0){
            throw new MinusAmountException();
        }
        this.amount -= parkingFee;
    }

    public void getCar(Car car){
        this.car = car;
    }

    public long getAmount(){
        return this.amount;
    }

}
