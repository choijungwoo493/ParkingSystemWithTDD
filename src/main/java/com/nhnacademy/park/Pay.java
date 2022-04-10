package com.nhnacademy.park;

import java.time.LocalDateTime;

public interface Pay {

    long payPolicy(LocalDateTime startDateTime,LocalDateTime endDateTime);

    long payPolicyWithCar(LocalDateTime startDateTime,LocalDateTime endDateTime);
}
