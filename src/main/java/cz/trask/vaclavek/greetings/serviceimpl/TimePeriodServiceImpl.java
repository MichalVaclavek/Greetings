package cz.trask.vaclavek.greetings.serviceimpl;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cz.trask.vaclavek.greetings.errors.InvalidParameterException;
import cz.trask.vaclavek.greetings.service.TimePeriodService;

/**
 * Implementation of the {@link TimePeriodService}
 * <p>
 * Defines constant String attributes of the begining and finishing<br>
 * of the respective time period (in "HH:mm" format).<br>
 * These constant values are then used to evaluate the {@code TimePeriod}<br>
 * of the 'usersTime' inserted as input parameter to {@link #getTimePeriod(String)}<br>
 * method.<br>
 * TODO Would be better to define such values in a configuration file.
 * 
 * @author Michal Vaclavek
 *
 */
@Service
public class TimePeriodServiceImpl implements TimePeriodService
{

    private static final Logger log = LoggerFactory.getLogger(TimePeriodServiceImpl.class);
    
    /**
     * Time string constants defining start and end of the respective day period<br>
     * i.e. defines respective {@link TimePeriod} value.<br>
     */
    // MORNING
    private final String mornigTimeFrom = "05:00";
    private final String mornigTimeTo = "11:59";
    
    // AFTERNOON
    private final String afternoonTimeFrom = "12:00";
    private final String afternoonTimeTo = "16:59";
    
    // EVENING
    private final String eveningTimeFrom = "17:00";
    private final String eveningTimeTo = "21:59";
    
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
    /**
     * {@inheritDoc}
     */
    @Override
    public TimePeriod getTimePeriod(String usersTime) {
        
        if (isInTimeInterval(usersTime, mornigTimeFrom, mornigTimeTo)) {
            return TimePeriod.MORNING;
        }
        if (isInTimeInterval(usersTime, afternoonTimeFrom, afternoonTimeTo)) {
            return TimePeriod.AFTERNOON;
        }
        if (isInTimeInterval(usersTime, eveningTimeFrom, eveningTimeTo)) {
            return TimePeriod.EVENING;
        }
            
        return TimePeriod.GENERAL_PURPOSE;
    }
    
    /**
     * Determines if the {@code checkedTime} is in the time interval defined by {@code timeIntervalFrom} and  {@code timeIntervalTo}<br>
     * Both {@code timeIntervalFrom} and {@code timeIntervalTo} times are considered to be part of the interval.
     * 
     * @param checkedTime - time string in "HH:mm" format to be checked if it is in time interval defined by {@code timeIntervalFrom} and  {@code timeIntervalTo}
     * @param timeIntervalFrom - lower time limit of the interval, included in the interval
     * @param timeIntervalTo - higher time limit of the interval, included in the interval
     * @return true if the {@code checkedTime} is within the correctly defined time interval. False in all other cases.
     */
    private boolean isInTimeInterval(String checkedTime, String timeIntervalFrom, String timeIntervalTo) {
        
        boolean retVal = false;
        try {
            if (LocalTime.parse(timeIntervalFrom).isBefore(LocalTime.parse(timeIntervalTo))) {
                LocalTime usersDateTime = LocalTime.parse(checkedTime, timeFormatter);
                retVal = usersDateTime.isAfter( LocalTime.parse(timeIntervalFrom, timeFormatter).minusMinutes(1)) && usersDateTime.isBefore( LocalTime.parse(timeIntervalTo, timeFormatter).plusMinutes(1));
            }
        } catch (Exception ex) {
            log.error("Failed to found if the checkedTime={} is part of the from {} to {} interval", checkedTime, timeIntervalFrom, timeIntervalTo);
            throw new InvalidParameterException("usersTime", checkedTime);
        }
        return retVal;
    }

}
