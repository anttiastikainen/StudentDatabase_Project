/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package opiskelijarekisteri_app;

/**
 *
 * @author antti
 */
public class Student {

    String firstname;
    String lastname;
    String student_ID;
    String address;
    String starting_Date;

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setStudent_ID(String student_ID) {
        this.student_ID = student_ID;
    }

    public String getStudent_ID() {
        return student_ID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStarting_Date(String starting_Date) {
        this.starting_Date = starting_Date;
    }

    public String getStarting_Date() {
        return starting_Date;
    }

    // tehdään tulosteesta nätti
    public String checkLength(String item) {

        String result;

        while (item.length() < 25) {
            item = item + " ";
            if (item.length() == 25) {
                result = item;
                return result;
            }

        }
        return null;
    }

    @Override
    public String toString() {
        return ("\t" + checkLength(firstname) + checkLength(lastname) + checkLength(student_ID)
                + checkLength(starting_Date) + checkLength(address) + "\n");
//        return (firstname + "\t\t\t" + lastname + "\t\t\t" + student_ID + "\t\t\t" + starting_Date + "\t\t\t" + address + "\n");
    }
}
