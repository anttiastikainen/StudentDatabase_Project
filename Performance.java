/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package opiskelijarekisteri_app;

/**
 *
 * @author antti
 */
public class Performance {
    int performance_ID;
    String course_ID;
    String student_ID;
    String grade;
    String completion_date;

    public void setPerformance_ID(int performance_ID) {
        this.performance_ID = performance_ID;
    }

    public int getPerformance_ID() {
        return performance_ID;
    }

    public void setCourse_ID(String course_ID) {
        this.course_ID = course_ID;
    }

    public String getCourse_ID() {
        return course_ID;
    }

    public void setStudent_ID(String student_ID) {
        this.student_ID = student_ID;
    }

    public String getStudent_ID() {
        return student_ID;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }

    public void setCompletion_date(String completion_date) {
        this.completion_date = completion_date;
    }

    public String getCompletion_date() {
        return completion_date;
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
        return ("\t" + checkLength(String.valueOf(performance_ID)) + checkLength(course_ID) 
                + checkLength(student_ID) + checkLength(grade)+  checkLength(completion_date)+ "\n");
    }
    
    
   
    
}
