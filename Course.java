/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package opiskelijarekisteri_app;

/**
 *
 * @author antti
 */
public class Course {
    String course_name;
    String course_ID;
    String credits;

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_name() {
        return course_name;
    }
    
    public void setCourse_ID(String course_ID) {
        this.course_ID = course_ID;
    }

    public String getCourse_ID() {
        return course_ID;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getCredits() {
        return credits;
    }
    
    
    // tehdään tulosteesta nätti
    public String checkLength(String item) {

        String result;

        while (item.length() < 30) {
            item = item + " ";
            if (item.length() == 30) {
                result = item;
                return result;
            }

        }
        return null;
    }

    @Override
    public String toString() {
        return ("\t" + checkLength(course_ID) + checkLength(credits) + checkLength(course_name)+ "\n");
    }
    
    
    
}
