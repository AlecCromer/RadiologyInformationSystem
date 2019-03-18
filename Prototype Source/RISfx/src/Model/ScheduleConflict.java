package Model;

import Controller.databaseConnector;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
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


      ////////////////////
     //Database Queries//
    ////////////////////
    public static ResultSet queryConflicts(LocalDate scheduleDate, int employeeId) throws Exception{
        Connection conn = databaseConnector.getConnection();

        PreparedStatement conflicts = conn.prepareStatement(
                "SELECT appointments.appointment_id, appointments.appointment_date, appointments.appointment_time, " +
                        "procedures.procedure_length, procedures.procedure_name, CONCAT(employees.first_name, \" \", employees.last_name) AS employee_name, employees.employee_id, " +
                        "modality.machine_name, modality.machine_id " +
                        "FROM   appointments " +
                        "INNER JOIN procedures ON appointments.procedure_id=procedures.procedure_id " +
                        "INNER JOIN employees ON appointments.employee_id=employees.employee_id " +
                        "INNER JOIN modality ON appointments.machine_id=modality.machine_id " +
                        "WHERE " +
                        "appointments.appointment_date = ? &&" +
                        "employees.employee_id = ? " +
                        "ORDER BY `appointments`.`employee_id` DESC, `appointments`.`appointment_time`  ASC"
        );
        conflicts.setDate(1, Date.valueOf(scheduleDate));
        conflicts.setInt(2, employeeId);
        return conflicts.executeQuery();
    }
}
