/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package opiskelijarekisteri_app;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author antti
 */
public class CourseViewController implements Initializable {

    private ArrayList<Course> courses = new ArrayList<Course>();
    @FXML
    private TextField tfCourseID;
    @FXML
    private TextField tfCourseName;
    @FXML
    private TextField tfCourseCredits;
    @FXML
    private Label lblSaved;
    @FXML
    private Button btnSaveCourse;
    @FXML
    private TextField tfNewCourseName;
    @FXML
    private Label lblCourseUpdated;
    @FXML
    private Label txtDeleteSuccess;
    @FXML
    private ColorPicker cpBackgroundColor;
    @FXML
    private Pane background1;
    @FXML
    private Pane background2;
    @FXML
    private Pane background3;
    @FXML
    private Pane scenePane;
    @FXML
    private TextArea taCourseList;
    @FXML
    private ComboBox<String> cbCourseID;
    @FXML
    private ComboBox<String> cbCourseID2;
    @FXML
    private TextField tfNewCourseCredits;

    String connString = "jdbc:mariadb://localhost:3306?user=keke&password=keke";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Connection c = DataAccess.openConnection(connString);
            updateCourseList(c, taCourseList);
            setItemsForComboBoxes(c);
            DataAccess.closeConnection(c);
        } catch (SQLException ex) {
            Logger.getLogger(StudentViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void clearFields(ActionEvent event) {
        tfCourseID.clear();
        tfCourseName.clear();
        tfCourseCredits.clear();
    }

    @FXML
    private void btnSaveCourseClicked(ActionEvent event) throws SQLException {
        String courseID = tfCourseID.getText();
        String courseName = tfCourseName.getText();
        String courseCredits = tfCourseCredits.getText();

        addCourse(courseID, courseName, courseCredits);

        clearFields(event); // uudelleen käytetään tyhjennys metodia
        lblSaved.setVisible(true);
        System.out.println("Tallennettu");
    }

    private void addCourse(String courseID, String courseName, String courseCredits) {
        //käytetään aina tätä yhteyttä
        Connection c;
        try {
            c = DataAccess.openConnection(connString);
            Statement stmt = c.createStatement();
            stmt.executeQuery("USE opiskelija_rekisteri");
            final String sql = "INSERT INTO Courses (Course_name, Course_id, Credits) "
                    + " VALUES(?, ?, ?)";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, courseName); // 1 eli ensimmäinen kysymysmerkki
            ps.setString(2, courseID);
            ps.setString(3, courseCredits);
            ps.execute();

            System.out.println("\t>> Lisatty " + courseName);

            // näytä käyttäjälle
            updateCourseList(c, taCourseList);
            setItemsForComboBoxes(c);
            DataAccess.closeConnection(c);
        } catch (SQLException ex) {
            Logger.getLogger(CourseViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void checkForFill(KeyEvent event) {
        lblSaved.setVisible(false); // aina kun tekstiä lisätään, ei näytetä tallennettu ilmoitusta
        if (tfCourseID.getText().length() == 7 // kurssin tunnuksen pitää olla 7 merkkiä pitkä
                && tfCourseName.getText().length() > 3
                && tfCourseCredits.getText().length() > 0 && tfCourseCredits.getText().length() <= 2) {
            btnSaveCourse.setDisable(false);
        } else {
            btnSaveCourse.setDisable(true);
        }
    }

    @FXML
    private void btnUpdateCoursesClicked(ActionEvent event) throws SQLException {
        String searchedID = cbCourseID.getValue();

        if (searchedID.equals("") == false) {

            Connection c = DataAccess.openConnection(connString);
            if (tfNewCourseName.getText().equals("") == false) {
                String newCourseName = tfNewCourseName.getText();
                Statement stmt = c.createStatement();
                stmt.executeQuery("USE opiskelija_rekisteri");
                final String sql = "UPDATE courses "
                        + " SET Course_name = ? "
                        + " WHERE Course_id = ? ";
                PreparedStatement ps = c.prepareStatement(sql);
                ps.setString(1, newCourseName); // 1 eli ensimmäinen kysymysmerkki
                ps.setString(2, searchedID);
                ps.execute();

                lblCourseUpdated.setVisible(true);
                lblCourseUpdated.setText(searchedID + " Kurssin nimi muutettu: " + newCourseName);
                System.out.println("\t>> Kurssin nimi muutettu: " + newCourseName);
            }
            if (tfNewCourseCredits.getText().equals("") == false) {
                String newCourseCredits = tfNewCourseCredits.getText();
                int creditsNum = Integer.parseInt(tfNewCourseCredits.getText());
                System.out.println("credits :" + creditsNum);
                if (creditsNum <= 15 && creditsNum > 0) {
                    Statement stmt = c.createStatement();
                    stmt.executeQuery("USE opiskelija_rekisteri");
                    final String sql = "UPDATE courses "
                            + " SET Credits = ? "
                            + " WHERE Course_id = ? ";
                    PreparedStatement ps = c.prepareStatement(sql);
                    ps.setString(1, newCourseCredits); // 1 eli ensimmäinen kysymysmerkki
                    ps.setString(2, searchedID);
                    ps.execute();

                    lblCourseUpdated.setVisible(true);
                    lblCourseUpdated.setText(lblCourseUpdated.getText() + "\n" + searchedID + " Kurssin laajuus muutettu: " + newCourseCredits);
                }
                else {
                    lblCourseUpdated.setText("Virheellinen syöte!");
                    lblCourseUpdated.setVisible(true);
                }
            }
            updateCourseList(c, taCourseList);
            setItemsForComboBoxes(c);
            DataAccess.closeConnection(c);
        }
    }

    @FXML
    private void btnDeleteCourseClicked(ActionEvent event) throws SQLException {
        String courseID = cbCourseID2.getValue();

        if (courseID.equals("") == false) {
            Connection c = DataAccess.openConnection(connString);
            ButtonType btnOK = new ButtonType("OK");
            ButtonType btnCancel = new ButtonType("Peruuta");
            Alert alert = new Alert(Alert.AlertType.WARNING, "Olet poistamassa kurssin " + courseID + " tietoja tietokannasta!", btnOK, btnCancel);
            alert.setTitle("Varoitus");
            alert.setHeaderText("HUOMIO");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == btnOK) {
                System.out.println("POISTETAAN " + courseID);
                Statement stmt = c.createStatement();
                stmt.executeQuery("USE opiskelija_rekisteri");
                //poistetaan suoritukset
                final String sql1 = "DELETE FROM performance "
                        + " WHERE Course_id = ?;";
                PreparedStatement ps = c.prepareStatement(sql1);
                ps.setString(1, courseID);
                ps.execute();
                // poistetaan kurssi
                final String sql2 = "DELETE FROM Courses "
                        + " WHERE Course_id = ? ";
                PreparedStatement ps2 = c.prepareStatement(sql2);
                ps2.setString(1, courseID);
                ps2.execute();
                txtDeleteSuccess.setText("Kurssin:" + courseID + " tiedot poistettu!");
                txtDeleteSuccess.setVisible(true);
            } // peruuta poisto
            else if (result.get() == btnCancel) {
                System.out.println("PERUUTETAAN");
            }
            updateCourseList(c, taCourseList);
            setItemsForComboBoxes(c);
            DataAccess.closeConnection(c);
        }
    }

    // LISTING
    private void updateCourseList(Connection c, TextArea textBox) throws SQLException {
        Statement stmt = c.createStatement();
        textBox.setText("");
        stmt.executeQuery("USE opiskelija_rekisteri");
        ResultSet rs = stmt.executeQuery(
                "SELECT Course_name, Course_id, Credits FROM Courses"
        );

        while (rs.next()) {   // tulostetaan niin kauan kun rivejä on 
                    Course course = new Course();
                    course.setCourse_ID(rs.getString("Course_id"));
                    course.setCredits(rs.getString("Credits"));
                    course.setCourse_name(rs.getString("Course_name"));
                    textBox.setText(textBox.getText()+course.toString());
                    courses.add(course);
        }
    }

    private void setItemsForComboBoxes(Connection c) throws SQLException {
        // Kurssit
        cbCourseID.getItems().clear();
        cbCourseID2.getItems().clear();

        Statement stmt = c.createStatement();
        stmt.executeQuery("USE opiskelija_rekisteri");
        ResultSet rs = stmt.executeQuery(
                "SELECT Course_id FROM Courses"
        );

        while (rs.next()) {
            cbCourseID.getItems().add(
                    rs.getString("Course_id")
            );
            cbCourseID2.getItems().add(
                    rs.getString("Course_id")
            );

        }
    }

    @FXML
    private void cpBackgroundColorClicked(ActionEvent event) {
        Color color = cpBackgroundColor.getValue();
        String webFormat = String.format("#%02x%02x%02x",
                (int) (255 * color.getRed()),
                (int) (255 * color.getGreen()),
                (int) (255 * color.getBlue()));

        background1.setStyle("-fx-background-color: " + webFormat + ";");
        background2.setStyle("-fx-background-color: " + webFormat + ";");
        background3.setStyle("-fx-background-color: " + webFormat + ";");
    }

    @FXML
    private void instructionsMenuItemClicked(ActionEvent event) {
        Alert instructions = new Alert(Alert.AlertType.INFORMATION,
                "JELPPI", ButtonType.OK);

        instructions.setTitle("Help");
        instructions.setHeaderText("Ongelma");
        instructions.show();

    }

    @FXML
    private void aboutMenuItemClicked(ActionEvent event) {
        Alert about = new Alert(Alert.AlertType.INFORMATION, "(c) Antti Astikainen", ButtonType.CLOSE);

        about.setTitle("About");
        about.setHeaderText("OpiskelijaRekisteri v1.0 24.2.2022");
        about.show();
    }

    @FXML
    private void closeClicked(ActionEvent event) {
        Stage stage = (Stage) scenePane.getScene().getWindow();
        stage.close();

    }

}
