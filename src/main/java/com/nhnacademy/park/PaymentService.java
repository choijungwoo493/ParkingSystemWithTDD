package com.nhnacademy.park;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PaymentService implements Pay {

    public static final Long THIRTY_MINUTES = 30L * 60L;
    public static final Long THIRTY_TO_HOUR_MINUTES = 60L * 60L;
    public static final Long TWO_ONE_ZERO_MINUTES = 210L * 60L;
    public static final Long THREE_THREE_ZERO_MINUTES = 330L * 60L;


    public LocalDateTime getMidnightFromStart(LocalDateTime startDateTime) {
        return LocalDateTime.of(startDateTime.getYear(), startDateTime.getMonth(), startDateTime.getDayOfMonth() + 1, 0, 0, 0);
    }

    public LocalDateTime getMidnightFromEnd(LocalDateTime endDateTime) {
        return LocalDateTime.of(endDateTime.getYear(), endDateTime.getMonth(), endDateTime.getDayOfMonth(), 0, 0, 0);
    }

    public long isEndDayFeeGetMax(LocalDateTime endDateTime, int policyVersion) {
        if (policyVersion == 1 && (endDateTime.getHour() >= 3 && ((endDateTime.getMinute() >= 20 && endDateTime.getSecond() >= 1) || (endDateTime.getMinute() >= 21)) || endDateTime.getHour() >= 4)) {
            return 10_000L;
        }
        if (policyVersion == 2 && (endDateTime.getHour() >= 5 && ((endDateTime.getMinute() >= 30 && endDateTime.getSecond() >= 1) || (endDateTime.getMinute() >= 31)) || endDateTime.getHour() >= 6)) {
            return 15_000L;
        }
        return 0L;
    }

    public long isStartDayFeeGetMax(LocalDateTime startDateTime, int policyVersion) {
        if (policyVersion == 1 && ((startDateTime.getHour() <= 20 && startDateTime.getMinute() <= 39) || startDateTime.getHour() <= 19)) {
            return 10_000L;
        }
        if (policyVersion == 2 && ((startDateTime.getHour() <= 18 && startDateTime.getMinute() <= 29) || startDateTime.getHour() <= 15)) {
            return 15_000L;
        }
        return 0L;
    }

    public long timePeriodUnderThirtyMinutes(Long timePeriod, int policyVersion) {
        if (policyVersion == 1 && timePeriod <= THIRTY_MINUTES) {
            return 1_000L;
        }
        return 0L;
    }

    public long timePeriodUnderTwoOneThreeMinutes(Long timePeriod, int policyVersion) {
        if (policyVersion == 1 && timePeriod <= TWO_ONE_ZERO_MINUTES) {
            return ((timePeriod - THIRTY_MINUTES) / 600 + 1) * 500L + 1000L;
        }
        return 0L;
    }
    public long timePeriodUnderThreeThreeZeroMinutes(Long timePeriod, int policyVersion) {
        if (policyVersion == 2 && timePeriod <= THREE_THREE_ZERO_MINUTES && timePeriod <= THIRTY_MINUTES) {
            return 0L;
        }
        if (policyVersion == 2 && timePeriod <= THREE_THREE_ZERO_MINUTES && timePeriod <= THIRTY_TO_HOUR_MINUTES) {
            return 1_000L;
        }
        if (policyVersion == 2 && timePeriod <= THREE_THREE_ZERO_MINUTES) {
            return ((timePeriod - THIRTY_TO_HOUR_MINUTES - 1L) / 600 + 1) * 500L + 1000L;
        }
        return 0L;
    }


    public long startToEndMaxDaysFee(LocalDateTime startDateTime, LocalDateTime endDateTime, int policyVersion) {
        if (policyVersion == 1 && endDateTime.getDayOfMonth() - startDateTime.getDayOfMonth() >= 1) {
            return (endDateTime.getDayOfMonth() - startDateTime.getDayOfMonth() - 1) * 10_000L;
        }
        if (policyVersion == 2 && endDateTime.getDayOfMonth() - startDateTime.getDayOfMonth() >= 1) {
            return (endDateTime.getDayOfMonth() - startDateTime.getDayOfMonth() - 1) * 15_000L;
        }
        return 0L;
    }

    @Override
    public long payPolicy(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        int policyVersion = 1;
        long timePeriod = ChronoUnit.SECONDS.between(startDateTime, endDateTime);
        if (endDateTime.getDayOfMonth() == startDateTime.getDayOfMonth() || timePeriod <= TWO_ONE_ZERO_MINUTES) {
            if (timePeriod <= THIRTY_MINUTES) {
                return 1000L;
            }
            if (timePeriod <= TWO_ONE_ZERO_MINUTES) {
                return ((timePeriod - THIRTY_MINUTES -1L) / 600 + 1) * 500L + 1000L;
            }
            return 10_000L;
        }
        return parkingFee(startDateTime,endDateTime,policyVersion);
    }

    @Override
    public long payPolicyWithCar(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        int policyVersion2 = 2;
        long timePeriod = ChronoUnit.SECONDS.between(startDateTime, endDateTime);
        if (endDateTime.getDayOfMonth() == startDateTime.getDayOfMonth() || timePeriod <= THREE_THREE_ZERO_MINUTES) {
            if (timePeriod <= THIRTY_MINUTES) {
                return 0L;
            }
            if (timePeriod <= THIRTY_TO_HOUR_MINUTES) {
                return 1_000L;
            }
            if (timePeriod <= THREE_THREE_ZERO_MINUTES) {
                return ((timePeriod - THIRTY_TO_HOUR_MINUTES - 1L) / 600 + 1) * 500L + 1000L;
            }
            return 15_000L;
        }
        return parkingFee(startDateTime,endDateTime,policyVersion2);
    }


    public Long parkingFee(LocalDateTime startDateTime,LocalDateTime endDateTime,int policyVersion){
        long result = 0;
        LocalDateTime midnightFromStart = getMidnightFromStart(startDateTime);
        LocalDateTime midnightFromEnd = getMidnightFromEnd(endDateTime);

        long timePeriodFirstDay = ChronoUnit.SECONDS.between(startDateTime, midnightFromStart);
        long timePeriodEndDay = ChronoUnit.SECONDS.between(midnightFromEnd, endDateTime);

        result += isStartDayFeeGetMax(startDateTime, policyVersion);
        result += isEndDayFeeGetMax(endDateTime, policyVersion);
        result += timePeriodUnderThirtyMinutes(timePeriodFirstDay, policyVersion);
        result += timePeriodUnderTwoOneThreeMinutes(timePeriodFirstDay, policyVersion);
        result += timePeriodUnderThreeThreeZeroMinutes(timePeriodFirstDay, policyVersion);
        result += timePeriodUnderThirtyMinutes(timePeriodEndDay, policyVersion);
        result += timePeriodUnderTwoOneThreeMinutes(timePeriodEndDay, policyVersion);
        result += timePeriodUnderThreeThreeZeroMinutes(timePeriodEndDay, policyVersion);
        result += startToEndMaxDaysFee(startDateTime, endDateTime, policyVersion);

        return result;

    }

    public long carSizeDiscount(long parkingFee,Car car) {
        int size = car.getCarSize();
        if (size == 1){
            return parkingFee;
        }
        return parkingFee / 2;
    }
}
