package Model;

public class Patient {

    String  firstname;
    String lastname;
    String dob;
    String sex;
    String email;
    String address;
    int phoneNumber, patientID, insuranceNumber;

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

    public int getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public int getInsuranceNumber(){return insuranceNumber;}
    public void setInsuranceNumber(int insuranceNumber){this.insuranceNumber = insuranceNumber;}

    public Patient(){
        this.patientID          = -1;
        this.firstname          = null;
        this.lastname           = null;
        this.dob                = null;
        this.sex                = null;
        this.phoneNumber        = -1;
        this.email              = null;
        this.address            = null;
        this.insuranceNumber    = -1;
    }
    public Patient(int PatientID, String firstname, String lastname, String DoB, String Sex, int phoneNumber, String email, int insuranceNumber, String address ){
        this.patientID = PatientID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = DoB;
        this.sex = Sex;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.insuranceNumber = insuranceNumber;
        this.address = address;
    }
}
