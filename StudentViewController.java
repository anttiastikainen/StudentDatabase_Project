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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author antti
 */
public class StudentViewController implements Initializable {

    private ArrayList<Student> students = new ArrayList<Student>();
    @FXML
    private Menu helpMenuItem;
    @FXML
    private TextField tfStdntNum;
    @FXML
    private TextField tfStdntAddress;
    @FXML
    private Button btnSaveStdnt;
    @FXML
    private Label lblSaved;
    @FXML
    private TextField tfStdntFirstname;
    @FXML
    private TextField tfStdntLastname;
    @FXML
    private TextField tfNewFirstname;
    @FXML
    private TextField tfNewLastname;
    @FXML
    private TextField tfNewAddress;
    @FXML
    private TextField tfDeleteStdnt;
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
    private TextArea taStudentsList;
    @FXML
    private DatePicker dpStartingDate;
    @FXML
    private DatePicker dpNewStartingDate;
    @FXML
    private CheckBox chkNewStartingDate;
    @FXML
    private ComboBox<String> cbStdntID;
    @FXML
    private ComboBox<String> cbStdntID2;

    String connString = "jdbc:mariadb://localhost:3306?user=keke&password=keke";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Connection c = DataAccess.openConnection(connString);
            updateStudentList(c, taStudentsList);
            setItemsForComboBoxes(c);
            DataAccess.closeConnection(c);
        } catch (SQLException ex) {
            Logger.getLogger(StudentViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

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
    private void btnSaveStdntClicked(ActionEvent event) throws SQLException {
        String firstname = tfStdntFirstname.getText();
        String lastname = tfStdntLastname.getText();
        String number = tfStdntNum.getText();
        String address = tfStdntAddress.getText();
        String startingDate = dpStartingDate.getValue().toString();

        addStudent(firstname, lastname, number, address, startingDate);

        clearFields(event); // uudelleen käytetään tyhjennys metodia
        lblSaved.setVisible(true);
        System.out.println("Tallennettu");
    }

    @FXML
    // annetaan tallentaa vasta, kun kenttiin on syötetty vaadittu määrä tekstiä 
    private void checkForFill(KeyEvent event) {
        lblSaved.setVisible(false); // aina kun tekstiä lisätään, ei näytetä tallennettu ilmoitusta
        if (tfStdntFirstname.getText().length() > 3
                && tfStdntLastname.getText().length() > 3
                && tfStdntNum.getText().length() == 7 // Opiskelijanumeron pitää olla 7 merkkiä pitkä
                && tfStdntAddress.getText().length() > 3) {
            btnSaveStdnt.setDisable(false);
        } else {
            btnSaveStdnt.setDisable(true);
        }
    }

    @FXML
    private void clearFields(ActionEvent event) {
        tfStdntFirstname.clear();
        tfStdntLastname.clear();
        tfStdntNum.clear();
        tfStdntAddress.clear();
    }

    private void addStudent(String firstname, String lastname, String studentNumber, String address, String StartingDate) throws SQLException {
        //käytetään aina tätä yhteyttä
        Connection c = DataAccess.openConnection(connString);
        Statement stmt = c.createStatement();
        stmt.executeQuery("USE opiskelija_rekisteri");
        final String sql = "INSERT INTO students (Firstname, Lastname, Student_id, Address, Starting_date) "
                + " VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = c.prepareStatement(sql);

        ps.setString(1, firstname); 
        ps.setString(2, lastname);
        ps.setString(3, studentNumber);
        ps.setString(4, address);
        ps.setString(5, StartingDate);
        ps.execute();

        System.out.println("\t>> Lisatty " + firstname);
        updateStudentList(c, taStudentsList);
        // päivitetään comboboxit, että voidaan samassa ikkunassa luoda ja poista tunnukset
        setItemsForComboBoxes(c);
        DataAccess.closeConnection(c);

    }

    @FXML
    private void btnUpdateStdntPressed(ActionEvent event) throws SQLException {
        String searchedID = cbStdntID.getValue();
        String newFirstName = tfNewFirstname.getText();
        String newLastName = tfNewLastname.getText();
        String newAddress = tfNewAddress.getText();

        // Varmistetaan että syöte ei ole tyhjä
        if (searchedID.equals("") == false) {
            // jos opiskelijanumero syöte ei ole tyhjä niin otetaan yhteys tietokantaan
            Connection c = DataAccess.openConnection(connString);
            // etunimi vaihdetaan
            if (newFirstName.equals("") == false) {
                Statement stmt = c.createStatement();
                stmt.executeQuery("USE opiskelija_rekisteri");
                final String sql = "UPDATE students "
                        + " SET Firstname = ? "
                        + " WHERE Student_id = ? ";
                PreparedStatement ps = c.prepareStatement(sql);
                ps.setString(1, newFirstName); // 1 eli ensimmäinen kysymysmerkki
                ps.setString(2, searchedID);
                ps.execute();

                System.out.println("\t>> Etuimi muutettu " + newFirstName);
            }
            // sukunimi vaihdetaan
            if (newLastName.equals("") == false) {
                Statement stmt = c.createStatement();
                stmt.executeQuery("USE opiskelija_rekisteri");
                final String sql = "UPDATE students "
                        + " SET Lastname = ? "
                        + " WHERE Student_id = ? ";
                PreparedStatement ps = c.prepareStatement(sql);
                ps.setString(1, newLastName);
                ps.setString(2, searchedID);
                ps.execute();

            }
            //osoite vaihdettu
            if (newAddress.equals("") == false) {
                Statement stmt = c.createStatement();
                stmt.executeQuery("USE opiskelija_rekisteri");
                final String sql = "UPDATE students "
                        + " SET Address = ? "
                        + " WHERE Student_id = ? ";
                PreparedStatement ps = c.prepareStatement(sql);
                ps.setString(1, newAddress);
                ps.setString(2, searchedID);
                ps.execute();

            }
            //aloitus pvm. vaihdettu
            if (chkNewStartingDate.isSelected()) {
                String newStartingDate = dpNewStartingDate.getValue().toString();
                Statement stmt = c.createStatement();
                stmt.executeQuery("USE opiskelija_rekisteri");
                final String sql = "UPDATE students "
                        + " SET Starting_date = ? "
                        + " WHERE Student_id = ? ";
                PreparedStatement ps = c.prepareStatement(sql);
                ps.setString(1, newStartingDate);
                ps.setString(2, searchedID);
                ps.execute();
            }
            updateStudentList(c, taStudentsList);
            DataAccess.closeConnection(c);
        }
    }

    @FXML
    private void btnDeleteStdntPressed(ActionEvent event) throws SQLException {
        String studentNumber = cbStdntID2.getValue();
        // tarkistetaan, että syöte ei ole tyhjä
        if (studentNumber.equals("") == false && studentNumber.length() == 7) {
            Connection c = DataAccess.openConnection(connString);
            ButtonType btnOK = new ButtonType("OK");
            ButtonType btnCancel = new ButtonType("Peruuta");
            Alert alert = new Alert(Alert.AlertType.WARNING, "Olet poistamassa opiskelijan " + studentNumber + " tietoja tietokannasta!\nMuista, että"
                    + " poistat samalla opiskelijan suoritukset", btnOK, btnCancel);
            alert.setTitle("Varoitus");
            alert.setHeaderText("HUOMIO");

            Optional<ButtonType> result = alert.showAndWait();

            // Poistetaan opiskelijan tiedot
            if (result.get() == btnOK) {
                System.out.println("POISTETAAN " + studentNumber);
                Statement stmt = c.createStatement();
                // poistetaan suoritukset
                stmt.executeQuery("USE opiskelija_rekisteri");
                final String sql1 = "DELETE FROM performance "
                        + " WHERE Student_id = ?;";
                PreparedStatement ps = c.prepareStatement(sql1);
                ps.setString(1, studentNumber);
                ps.execute();
                // poistetaan opiskelija
                final String slq2 = "DELETE FROM students "
                        + " WHERE Student_id = ?";
                PreparedStatement ps2 = c.prepareStatement(slq2);
                ps2.setString(1, studentNumber);
                ps2.execute();
                txtDeleteSuccess.setVisible(true);

            } else if (result.get() == btnCancel) {
                System.out.println("PERUUTETAAN");
            }
            updateStudentList(c, taStudentsList);
            // päivitetään 
            setItemsForComboBoxes(c);
            DataAccess.closeConnection(c);
        }

    }

    // LISTING
    private void updateStudentList(Connection c, TextArea textBox) throws SQLException {
        Statement stmt = c.createStatement();
        textBox.setText("");
        stmt.executeQuery("USE opiskelija_rekisteri");
        ResultSet rs = stmt.executeQuery(
                "SELECT Firstname, Lastname, Student_id, Address, Starting_date FROM Students"
        );

        while (rs.next()) {
            Student student = new Student();
            student.setFirstname(rs.getString("Firstname"));
            student.setLastname(rs.getString("Lastname"));
            student.setStudent_ID(rs.getString("Student_id"));
            student.setStarting_Date(rs.getString("Starting_date"));
            student.setAddress(rs.getString("Address"));
            textBox.setText(textBox.getText()+student.toString());
            students.add(student);         
        }
    }

    private void setItemsForComboBoxes(Connection c) throws SQLException {
        // Kurssit
        cbStdntID.getItems().clear();
        cbStdntID2.getItems().clear();

        Statement stmt = c.createStatement();
        stmt.executeQuery("USE opiskelija_rekisteri");
        ResultSet rs = stmt.executeQuery(
                "SELECT Student_id FROM Students"
        );

        while (rs.next()) {
            cbStdntID.getItems().add(
                    rs.getString("Student_id")
            );
            cbStdntID2.getItems().add(
                    rs.getString("Student_id")
            );

        }
    }

    @FXML
    private void cpBackgroundColorClicked(ActionEvent event
    ) {

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
    private void closeClicked(ActionEvent event
    ) {
        Stage stage = (Stage) scenePane.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void chkNewStartingDateActive(ActionEvent event
    ) {
        if (chkNewStartingDate.isSelected()) {
            dpNewStartingDate.setVisible(true);
        } else {
            dpNewStartingDate.setVisible(false);
        }
    }

}
