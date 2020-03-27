package cz.trask.vaclavek.greetings.service;

/**
 * Interface to declare methods, which needs to be impemented, when {@link TimePeriod} 
 * value is to be determined according given user's time.
 * <p>
 * User's time is the time of the day in "HH:mm" format
 * <p>
 * Defines available {@link TimePeriod} values i.e. parts of the day according time
 * 
 * @author Michal Vaclavek
 *
 */
public interface TimePeriodService
{
    /**
     * Available TimePeriod values i.e. time parts of the day
     */
    enum TimePeriod
    {
        MORNING, AFTERNOON, EVENING, GENERAL_PURPOSE
    }
    
    /**
     * Determines {@link TimePeriod} value according input user's time string.<br>
     * User's time is a string in "HH:mm" format (Hours in 24 h. format:minutes),<br>
     * i.e. 'HH' is a string, which can be parsed into number in 00 - to 24 range<br>
     * 'mm'  is a string, which can be parsed into number in 00 - to 60 range<br>
     * 
     * @param usersTime - user's time string in a "HH:mm" format (Hours in 24 h. format:minutes)
     * @return corresponding {@link TimePeriod} value according input user's time string
     */
    TimePeriod getTimePeriod(String usersTime);
}
