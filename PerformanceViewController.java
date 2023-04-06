/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package opiskelijarekisteri_app;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author antti
 */
public class PerformanceViewController implements Initializable {

    private ArrayList<Performance> performances = new ArrayList<Performance>();
    @FXML
    private TextField tfCourseGrade;
    @FXML
    private Button btnSavePerformance;
    @FXML
    private Label lblSaved;
    @FXML
    private TextField tfSearchPerf;
    @FXML
    private TextField tfNewGrade;
    @FXML
    private TextField tfDeletePerf;
    @FXML
    private Label txtDeleteSuccess;
    @FXML
    private TextArea taPerf;
    @FXML
    private Pane background1;
    @FXML
    private Pane background2;
    @FXML
    private Button btnUpdatePerf;
    @FXML
    private Pane background3;
    @FXML
    private Pane background4;
    @FXML
    private ColorPicker cpBackgroundColor;
    @FXML
    private Pane scenePane;
    @FXML
    private ComboBox<String> cbCourseID;
    @FXML
    private ComboBox<String> cbStdntID;
    @FXML
    private ComboBox<String> cbCourseID2;
    @FXML
    private ComboBox<String> cbStdntID2;
    @FXML
    private ComboBox<String> cbStdntID3;
    @FXML
    private ComboBox<String> cbCourseID3;

    String connString = "jdbc:mariadb://localhost:3306?user=keke&password=keke";
    @FXML
    private DatePicker dpCompletionDate;
    @FXML
    private CheckBox chkNewCompletionDate;
    @FXML
    private DatePicker dpNewCompletionDate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //taPerf.setText("Suoritus Nro.\t\tKurssi tunnus\t\tOpiskelija Nro.\t \tArvosana");
            Connection c = DataAccess.openConnection(connString);
            String sql = "SELECT Performance_id, Course_id, Student_id, Grade, Completion_date FROM Performance";
            updatePerformanceList(c, taPerf, sql);
            setItemsForComboBoxes(c);
        } catch (SQLException ex) {
            Logger.getLogger(PerformanceViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnSavePerformanceClicked(ActionEvent event) throws SQLException {
        String courseID = cbCourseID.getValue();
        String number = cbStdntID.getValue();
        String grade = tfCourseGrade.getText();
        String completionDate = dpCompletionDate.getValue().toString();

        if (grade.equals("") == false) {
            addPerformance(courseID, number, grade, completionDate);
        }

        Connection c = DataAccess.openConnection(connString);
        String sql = "SELECT Performance_id, Course_id, Student_id, Grade, Completion_date FROM Performance";
        updatePerformanceList(c, taPerf, sql);
        lblSaved.setVisible(true);
        System.out.println("Tallennettu");
    }

    private void addPerformance(String courseID, String number, String grade, String completionDate) throws SQLException {
        Connection c = DataAccess.openConnection(connString);
        Statement stmt = c.createStatement();
        stmt.executeQuery("USE opiskelija_rekisteri");
        final String sql = "INSERT INTO Performance (Course_id, Student_id, Grade, Completion_date) "
                + " VALUES(?, ?, ? ,?)";
        PreparedStatement ps = c.prepareStatement(sql);

        ps.setString(1, courseID); // 1 eli ensimmäinen kysymysmerkki
        ps.setString(2, number);
        ps.setString(3, grade);
        ps.setString(4, completionDate);
        ps.execute();

        System.out.println("\t>> Lisatty suoritus kurssista " + courseID);
        DataAccess.closeConnection(c);

    }

    @FXML
    private void checkForFill(KeyEvent event) {
        lblSaved.setVisible(false);
        int numericValue = Integer.parseInt(tfCourseGrade.getText());
        if (tfCourseGrade.getText().length() == 1 && numericValue > 0 && numericValue <= 5) {
            btnSavePerformance.setDisable(false);
        } else {
            btnSavePerformance.setDisable(true);
        }
    }

    @FXML
    private void btnUpdatePerfClicked(ActionEvent event) throws SQLException {
        String searchedPerfID = tfSearchPerf.getText();

        // tarkistetaan, että syöte ei ole tyhjä
        if (searchedPerfID.equals("") == false) {
            Connection c = DataAccess.openConnection(connString);
            // uusi kurssi tunnus
            if (cbCourseID2.getValue() != null) {
                String newCourseID = cbCourseID2.getValue();
                Statement stmt = c.createStatement();
                stmt.executeQuery("USE opiskelija_rekisteri");
                final String sql = "UPDATE Performance "
                        + " SET Course_id = ? "
                        + " WHERE Performance_id = ? ";
                PreparedStatement ps = c.prepareStatement(sql);
                ps.setString(1, newCourseID); // 1 eli ensimmäinen kysymysmerkki
                ps.setString(2, searchedPerfID);
                ps.execute();
                System.out.println("ARVO: " + cbCourseID2.getValue());
            }
            // uusi opiskelija tunnus
            if (cbStdntID2.getValue() != null) {
                String newStdntID = cbStdntID2.getValue();
                Statement stmt2 = c.createStatement();
                stmt2.executeQuery("USE opiskelija_rekisteri");
                final String sql2 = "UPDATE Performance "
                        + " SET Student_id = ? "
                        + " WHERE Performance_id = ? ";
                PreparedStatement ps2 = c.prepareStatement(sql2);
                ps2.setString(1, newStdntID); // 1 eli ensimmäinen kysymysmerkki
                ps2.setString(2, searchedPerfID);
                ps2.execute();
            }

            // uusi arvosana (ei voi olla pienempi kuin 0 ja suurempi kuin 5)   
            if (tfNewGrade.getText().equals("") == false
                    && Integer.parseInt(tfNewGrade.getText()) > 0
                    && Integer.parseInt(tfNewGrade.getText()) <= 5) {
                String newGradeS = tfNewGrade.getText();
                Statement stmt3 = c.createStatement();
                stmt3.executeQuery("USE opiskelija_rekisteri");
                final String sql3 = "UPDATE Performance "
                        + " SET Grade = ? "
                        + " WHERE Performance_id = ? ";
                PreparedStatement ps3 = c.prepareStatement(sql3);
                ps3.setString(1, newGradeS); // 1 eli ensimmäinen kysymysmerkki
                ps3.setString(2, searchedPerfID);
                ps3.execute();
            }
            
            // uusi suoritus päivä
                        //aloitus pvm. vaihdettu
            if (chkNewCompletionDate.isSelected()) {
                String newCompletionDate = dpNewCompletionDate.getValue().toString();
                Statement stmt = c.createStatement();
                stmt.executeQuery("USE opiskelija_rekisteri");
                final String sql = "UPDATE performance "
                        + " SET Completion_date = ? "
                        + " WHERE Performance_id = ? ";
                PreparedStatement ps = c.prepareStatement(sql);
                ps.setString(1, newCompletionDate);
                ps.setString(2, searchedPerfID);
                ps.execute();
            }
            String sql = "SELECT Performance_id, Course_id, Student_id, Grade, Completion_date FROM Performance";
            updatePerformanceList(c, taPerf, sql);
            DataAccess.closeConnection(c);
        }
    }

    @FXML
    private void btnDeletePerfClicked(ActionEvent event) throws SQLException {
        String perfID = tfDeletePerf.getText();
        // tarkistetaan, että syöte ei ole tyhjä
        if (perfID.equals("") == false) {
            Connection c = DataAccess.openConnection(connString);
            ButtonType btnOK = new ButtonType("OK");
            ButtonType btnCancel = new ButtonType("Peruuta");
            Alert alert = new Alert(Alert.AlertType.WARNING, "Olet poistamassa suorituksen " + perfID + " tietoja tietokannasta!", btnOK, btnCancel);
            alert.setTitle("Varoitus");
            alert.setHeaderText("HUOMIO");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == btnOK) {
                System.out.println("POISTETAAN SUORITUS NRO." + perfID);
                Statement stmt = c.createStatement();
                stmt.executeQuery("USE opiskelija_rekisteri");
                final String sql = "DELETE FROM Performance "
                        + " WHERE Performance_id = ? ";
                PreparedStatement ps = c.prepareStatement(sql);
                ps.setString(1, perfID);
                ps.execute();
                txtDeleteSuccess.setText("Suorituksen nro." + perfID + " tiedot poistettu!");
                txtDeleteSuccess.setVisible(true);

                // listan päivitys
                String sql2 = "SELECT Performance_id, Course_id, Student_id, Grade, Completion_date FROM Performance";
                updatePerformanceList(c, taPerf, sql2);
            } // peruuta poisto
            else if (result.get() == btnCancel) {
                System.out.println("PERUUTETAAN");
            }

            DataAccess.closeConnection(c);
        }
    }

    // OPISKELIJA TUNNUKSEN PERUSTEELLA
    @FXML
    private void btnShowStdntPerfClicked(ActionEvent event) throws SQLException {
        String searchedStdntID = cbStdntID3.getValue();
        taPerf.setText("");
        Connection c = DataAccess.openConnection(connString);
        Statement stmt = c.createStatement();
        stmt.executeQuery("USE opiskelija_rekisteri");

        ResultSet rs = stmt.executeQuery(
                "SELECT Performance_id, Course_id, Student_id, Grade, Completion_date FROM Performance WHERE Student_id=" + searchedStdntID
        );

        while (rs.next()) {
            taPerf.setText(
                    taPerf.getText()
                    + "\t" + rs.getString("Performance_id") + "\t\t\t "
                    + rs.getString("Course_id") + " \t\t"
                    + rs.getString("Student_id") + " \t\t\t"
                    + rs.getString("Grade") + "\t\t\t"
                    + rs.getString("Completion_date") + "\n"
            );
        }

        DataAccess.closeConnection(c);
    }

    // NÄYTÄ KAIKKI
    @FXML
    private void btnShowAllPerfClicked(ActionEvent event) throws SQLException {

        Connection c = DataAccess.openConnection(connString);
        Statement stmt = c.createStatement();
        stmt.executeQuery("USE opiskelija_rekisteri");
        taPerf.setText("");
        String sql = "SELECT Performance_id, Course_id, Student_id, Grade, Completion_date FROM Performance";
        updatePerformanceList(c, taPerf, sql);
        DataAccess.closeConnection(c);

    }

    //KURSSIN PERUSTEELLA
    @FXML
    private void btnShowCoursePerfClicked(ActionEvent event) throws SQLException {
        String searchedCourseID = cbCourseID3.getValue();

        Connection c = DataAccess.openConnection(connString);
        Statement stmt = c.createStatement();

        stmt.executeQuery("USE opiskelija_rekisteri");
        String sql = "SELECT Performance_id, Course_id, Student_id, Grade, Completion_date FROM Performance WHERE Course_id='" + searchedCourseID + "'";
        updatePerformanceList(c, taPerf, sql);
        DataAccess.closeConnection(c);
    }

    private void updatePerformanceList(Connection c, TextArea textBox, String sql) throws SQLException {
        Statement stmt = c.createStatement();
        stmt.executeQuery("USE opiskelija_rekisteri");
        ResultSet rs = stmt.executeQuery(sql);

        textBox.setText("");
        while (rs.next()) {
            Performance performance = new Performance();
            performance.setPerformance_ID(rs.getInt("Performance_id"));
            performance.setCourse_ID(rs.getString("Course_id"));
            performance.setStudent_ID(rs.getString("Student_id"));
            performance.setGrade(rs.getString("Grade"));
            performance.setCompletion_date(rs.getString("Completion_date"));
            textBox.setText(textBox.getText() + performance.toString());
            performances.add(performance);
        }
    }

    // apumetodi comboboxin täyttöön kutsutaan initalize vaiheessa
    private void setItemsForComboBoxes(Connection c) throws SQLException {
        // alustetaan boxit
        cbStdntID.getItems().clear();
        cbStdntID2.getItems().clear();
        cbStdntID3.getItems().clear();
        cbCourseID.getItems().clear();
        cbCourseID2.getItems().clear();
        cbCourseID3.getItems().clear();
        // Kurssit
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
            cbCourseID3.getItems().add(
                    rs.getString("Course_id")
            );
        }

        stmt.executeQuery("USE opiskelija_rekisteri");
        ResultSet rs2 = stmt.executeQuery(
                "SELECT Student_id FROM Students"
        );
        while (rs2.next()) {
            cbStdntID.getItems().add(
                    rs2.getString("Student_id")
            );
            cbStdntID2.getItems().add(
                    rs2.getString("Student_id")
            );
            cbStdntID3.getItems().add(
                    rs2.getString("Student_id")
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
        background4.setStyle("-fx-background-color: " + webFormat + ";");
    }

    @FXML
    private void instructionsMenuItemClicked(ActionEvent event) {
        Alert instructions = new Alert(Alert.AlertType.INFORMATION,
                "Jelppi", ButtonType.OK);

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

    @FXML
    private void chkNewCompletionDateActive(ActionEvent event) {

        if (chkNewCompletionDate.isSelected()) {
            dpNewCompletionDate.setVisible(true);
        } else {
            dpNewCompletionDate.setVisible(false);
        }
    }

}
