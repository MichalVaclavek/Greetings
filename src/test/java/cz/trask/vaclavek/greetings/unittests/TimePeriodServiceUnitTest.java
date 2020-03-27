package cz.trask.vaclavek.greetings.unittests;

/**
 * Unit tests of the {@link TimePeriodService}
 * 
 * @author Michal Vaclavek
 *
 */
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import cz.trask.vaclavek.greetings.errors.InvalidParameterException;
import cz.trask.vaclavek.greetings.service.TimePeriodService;
import cz.trask.vaclavek.greetings.service.TimePeriodService.TimePeriod;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TimePeriodServiceUnitTest
{
    
    @Autowired
    TimePeriodService timePeriodService;
    
    @Test
    public void whenValidTime_thenCorrectTimePeriodShouldReturn() {
        
        String time = "16:30";
        assertThat(timePeriodService.getTimePeriod(time)).isEqualTo(TimePeriod.AFTERNOON);
        
        time = "05:01";
        assertThat(timePeriodService.getTimePeriod(time)).isEqualTo(TimePeriod.MORNING);
        
        time = "21:59";
        assertThat(timePeriodService.getTimePeriod(time)).isEqualTo(TimePeriod.EVENING);
        
        time = "01:01";
        assertThat(timePeriodService.getTimePeriod(time)).isEqualTo(TimePeriod.GENERAL_PURPOSE);
      
     }
    
    @Test
    public void whenInvalidTime_thenInvalidParameterExcpetion() {
        
        String time = "0199:01";
        assertThatThrownBy(() -> {timePeriodService.getTimePeriod(time);}).as("Time parameter error exception not thrown.")
                                                                          .isInstanceOf(InvalidParameterException.class);
     }
    
}
