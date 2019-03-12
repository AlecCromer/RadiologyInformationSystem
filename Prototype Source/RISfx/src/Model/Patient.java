package Model;

public class Patient {

    String  firstname, lastname, dob, sex, email;
    int  pnumber, patientID;

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getPnumber() {
        return pnumber;
    }
    public void setPnumber(int pnumber) {
        this.pnumber = pnumber;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public int getPatientID() {
        return patientID;
    }
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public Patient(int PatientID, String firstname, String lastname, String DoB, String Sex, int pnumber, String email){
        this.patientID = PatientID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = DoB;
        this.sex = Sex;
        this.pnumber = pnumber;
        this.email = email;
    }
}
