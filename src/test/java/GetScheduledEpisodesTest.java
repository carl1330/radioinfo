import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetScheduledEpisodesTest {
    @Test
    void programStartTimeIsMoreThan12HoursBefore() {
        Program program = new Program(LocalDateTime.now().minusHours(13));
        Schedule schedule = new Schedule();
        schedule.add(program);
        assertFalse(schedule.getPrograms().contains(program));
    }

    @Test
    void programStartTimeIsMoreThan6HoursAfter() {
        Program program = new Program(LocalDateTime.now().plusHours(7));
        Schedule schedule = new Schedule();
        schedule.add(program);
        assertFalse(schedule.getPrograms().contains(program));
    }

    @Test
    void programStartTimeBetween12BeforeAnd6After() {
        Program program = new Program(LocalDateTime.now());
        Schedule schedule = new Schedule();
        schedule.add(program);
        assertTrue(schedule.getPrograms().contains(program));
    }

}
