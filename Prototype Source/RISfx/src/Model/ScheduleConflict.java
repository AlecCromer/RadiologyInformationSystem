package Model;

import java.time.LocalDateTime;

public class ScheduleConflict {
    private int conflictLength;
    private LocalDateTime conflictDateTime;
    private String technician, machine;

    public String getTechnician() {
        return technician;
    }

    public LocalDateTime getConflictDateTime() {
        return conflictDateTime;
    }

    public int getConflictLength() {
        return conflictLength;
    }

    public ScheduleConflict(int conflictLength, LocalDateTime conflictDateTime) {
        this.conflictLength = conflictLength;
        this.conflictDateTime = conflictDateTime;
    }
}
