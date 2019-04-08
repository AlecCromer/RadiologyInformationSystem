package Model;

import java.util.ArrayList;

public class Invoice {
    private Appointment appointment;
    private Patient patient;
    private ArrayList<Item> items;
    private Procedure procedure;

    public Appointment getAppointment() {
        return appointment;
    }
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Patient getPaitient() {
        return patient;
    }
    public void setPaitient(Patient paitient) {
        this.patient = paitient;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public Procedure getProcedure() {
        return procedure;
    }
    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public Invoice(Appointment appointment, Patient patient, ArrayList<Item> items, Procedure procedure) {
        this.appointment    = appointment;
        this.patient        = patient;
        this.items          = items;
        this.procedure      = procedure;
    }
}
