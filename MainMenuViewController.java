/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package opiskelijarekisteri_app;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author antti
 */
public class MainMenuViewController implements Initializable {

    @FXML
    private Button btnStudent;
    @FXML
    private Button btnCourse;
    @FXML
    private Button btnPerformance;
    @FXML
    private Button btnExit;
    @FXML
    private Label txtStudentHint;
    @FXML
    private Label txtCourseHint;
    @FXML
    private Label txtPerformanceHint;
    // replace user and password to your own 
    String connString = "jdbc:mariadb://localhost:3306?user=keke&password=keke";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Alustetaan tietokanta
        try {
            initializeDatabase();
        } catch (SQLException ex) {
            Logger.getLogger(MainMenuViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnStudentPressed(ActionEvent event) {
        System.out.println("opiskelija nappia painettu");
        // AVATAAN UUSI NÄKYMÄ
        try {
            Parent newRoot;
            newRoot = FXMLLoader.load(getClass().getResource("StudentView.fxml"));
            Scene newScene = new Scene(newRoot);
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.setTitle("Opiskelija näkymä");
            newStage.show();

        } catch (IOException ex) {
            System.out.println("virhe: " + ex);
            Logger.getLogger(MainMenuViewController.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    @FXML
    private void btnCoursePressed(ActionEvent event) {
        System.out.println("kurssi nappia painettu");
        // AVATAAN UUSI NÄKYMÄ
        try {
            Parent newRoot;
            newRoot = FXMLLoader.load(getClass().getResource("CourseView.fxml"));
            Scene newScene = new Scene(newRoot);
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.setTitle("Kurssi näkymä");
            newStage.show();

        } catch (IOException ex) {
            Logger.getLogger(MainMenuViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnPerformancePressed(ActionEvent event) {
        System.out.println("suoritukset nappia painettu");
        // AVATAAN UUSI NÄKYMÄ
        try {
            Parent newRoot;
            newRoot = FXMLLoader.load(getClass().getResource("PerformanceView.fxml"));
            Scene newScene = new Scene(newRoot);
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.setTitle("Suoritus näkymä");
            newStage.show();

        } catch (IOException ex) {
            Logger.getLogger(MainMenuViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnExitPressed(ActionEvent event) {
        System.out.println("Poistu nappia painettu");
        Platform.exit();
        System.exit(0);
    }

    // alustetaan tietokanta ja demo dataa käytettäväksi
    private void initializeDatabase() throws SQLException {
        Connection conn = DataAccess.openConnection(
                connString);
        // luodaan tietokanta
        createDatabase(conn, "Opiskelija_rekisteri");
        // luodaan taulu
        createTable(conn,
                "CREATE TABLE Students ("
                + "Firstname VARCHAR(50),"
                + "Lastname VARCHAR(50),"
                + "Student_id VARCHAR(7) NOT NULL PRIMARY KEY,"
                + "Address VARCHAR(50),"
                + "Starting_date DATE)"
        );

        createTable(conn,
                "CREATE TABLE Courses ("
                + "Course_name VARCHAR(50),"
                + "Course_id VARCHAR(7) NOT NULL PRIMARY KEY,"
                + "Credits VARCHAR(2))"
        );

        createTable(conn,
                "CREATE TABLE Performance ("
                + "Performance_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                + "Course_id VARCHAR(7) NOT NULL,"
                + "Student_id VARCHAR(7) NOT NULL,"
                + "Grade VARCHAR(1),"
                + "Completion_date DATE,"
                + "FOREIGN KEY (Course_id) REFERENCES Courses(Course_id),"
                + "FOREIGN KEY (Student_id) REFERENCES Students(Student_id))"
        );
        // luodaan demodataa
        Statement stmt = conn.createStatement();
        //stmt.executeQuery("USE opiskelija_rekisteri");

        // demodataa Opiskelijoihin
        conn.createStatement().executeQuery(
                " INSERT INTO Students (Firstname, Lastname, Student_id, Address, Starting_date)"
                + " VALUES('Maikki','Mallikas','1098945','Mallikatu 3 b','2017-06-15'),"
                + "('Teemu','Testi','1095645','Esimerkkistreet 1','2017-06-15'),"
                + "('Simo','Taikuri','1023945','Kaitapolku 12','2017-06-15')");

        // demodataa Kursseihin
        conn.createStatement().executeQuery(
                " INSERT INTO Courses (Course_name, Course_id, Credits)"
                + " VALUES('Aakkoset kurssi','ABC1234','5'),"
                + "('Käyttöliittymä ohjelmointi','LTD6058','5'),"
                + "('Kokkauksen perusteet','BRG7447','5'),"
                + "('Ruotsin perusteet','UPS5050','5')");

        //demo dataa Suorituksiin
        conn.createStatement().executeQuery(
                " INSERT INTO Performance (Course_id, Student_id, Grade, Completion_date)"
                + " VALUES('ABC1234','1023945','2','2021-06-15'),"
                + "('ABC1234','1095645','3','2021-06-15'),"
                + "('ABC1234','1098945','5','2021-06-15'),"
                + "('LTD6058','1095645','5','2021-06-15'),"
                + "('LTD6058','1098945','3','2021-06-15'),"
                + "('LTD6058','1023945','4','2021-06-15'),"
                + "('BRG7447','1098945','5','2021-06-15'),"
                + "('BRG7447','1095645','4','2021-06-15'),"
                + "('BRG7447','1023945','4','2021-06-15')");
        System.out.println("\t>> Demodata luotu ja lisatty");
        DataAccess.closeConnection(conn);

    }

    private static void createDatabase(Connection c, String db) throws SQLException { // db on tietokannan nimi

        Statement stmt = c.createStatement();
        stmt.executeQuery("DROP DATABASE IF EXISTS " + db);

        System.out.println("\t>> Tietokanta " + db + " tuhottu");

        // luodaan tietokanta
        stmt.executeQuery("CREATE DATABASE " + db);

        System.out.println("\t>> Tietokanta " + db + " luotu");

        stmt.executeQuery("USE " + db);

        System.out.println("\t>> Kaytetaan tietokantaa " + db);

    }

    private static void createTable(Connection c, String sql) throws SQLException {

        Statement stmt = c.createStatement();
        stmt.executeQuery(sql);
        System.out.println("\t>> Taulu luotu");
    }
    
    @FXML
    private void showStudentHintBox(MouseEvent event) {
        txtStudentHint.setVisible(true);
    }

    @FXML
    private void hideStudentHintBox(MouseEvent event) {
        txtStudentHint.setVisible(false);
    }
    
    @FXML
    private void showCourseHintBox(MouseEvent event) {
        txtCourseHint.setVisible(true);
    }

    @FXML
    private void hideCourseHitBox(MouseEvent event) {
        txtCourseHint.setVisible(false);
    }


    @FXML
    private void showPerformanceHintBox(MouseEvent event) {
        txtPerformanceHint.setVisible(true);
    }

    @FXML
    private void hidePerformanceHintBox(MouseEvent event) {
        txtPerformanceHint.setVisible(false);
    }

}
