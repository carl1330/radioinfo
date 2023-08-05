import java.time.LocalDateTime;
import java.util.ArrayList;

public class Schedule {
    ArrayList<Program> programs = new ArrayList<>();

    public ArrayList<Program> getPrograms() {
        return programs;
    }

    public void add(Program program) {
        LocalDateTime twelveHoursBefore = LocalDateTime.now().minusHours(12);
        LocalDateTime sixHoursAfter = LocalDateTime.now().plusHours(6);
        if(program.startTime.isBefore(twelveHoursBefore) || program.startTime.isAfter(sixHoursAfter))
            return;
        programs.add(program);
    }
}
