package com.nhnacademy.park;

public class ParkSystem {

    PaymentService paymentService = new PaymentService();
    Park park;

    public ParkSystem(Park park){
        this.park = park;
    }

    public void carIn(Park park,Car car){
        park.parkingCar(car);
    }

    public void carOut(Park park,User user,Car car, Pay pay){
        long parkingFee = pay.payPolicyWithCar(park.getParkingTime(car),park.getUnParkingTime(car));
        parkingFee = paymentService.carSizeDiscount(parkingFee,car);
        user.getCar(car);   //들어올 때와 나갈때 다른 사람이 운전할 수 있으므로 해당 메소드를 호출할 때 user는 car 정보를 받는다.
        user.payParkingFee(parkingFee);
        park.unparkingCar(car);
    }


}
