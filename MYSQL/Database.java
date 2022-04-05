package IA.MYSQL;

import IA.ErrorHandler;
import IA.Main;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class  Database {

    private Statement statement = null;
    private ResultSet resultSet = null;
    private final String space = "', '";

    public Database(){
        try {
            Connection connection = DriverManager.getConnection(
                    Main.databaseIP,
                    "vplanner_user",
                    "vpassword"); //connect to database
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorHandler.display(e.getMessage());
            if (Main.databaseIP.equals("jdbc:mariadb://141.144.250.164:3306/vplanner")){
                ErrorHandler.display("VPlanner could not connect to the server, switched to localhost mode!");
                Main.databaseIP = "jdbc:mariadb://localhost/vplanner";
                Main.db = new Database();
            }
        }
    }

    public void setUserData() throws SQLException {
        resultSet = statement.executeQuery("SELECT * FROM users WHERE id='" + Main.userId + "'");
        resultSet.next();
        //Assign values to Main public variables
        Main.username = resultSet.getString("username");
        Main.name = resultSet.getString("name");
        Main.lastName = resultSet.getString("last_name");
        Main.phone = resultSet.getString("phone");
        Main.email = resultSet.getString("email");
        Main.instagram = resultSet.getString("instagram");
        Main.facebook = resultSet.getString("facebook");
        Main.twitter = resultSet.getString("twitter");
        Main.fullName = Main.name + " " + Main.lastName;

    }

    public boolean getLogin(String username, String password){
        //encrypt password before transmitting to database
        String finalPassword = getSHA512(password);
        try {
            resultSet = statement.executeQuery(
                    "SELECT password,id " +
                            "FROM users " +
                            "WHERE username='" + username + "' OR email='" + username + "'"
            );
            if (resultSet.next()) {
                if (finalPassword.equals(resultSet.getString("password"))) {
                    Main.userId = resultSet.getInt("id");
                    Main.encPass = finalPassword;
                    setUserData(); //Assigns all user data to public variables in Main
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorHandler.display(e.getMessage());
        }
        return false;
    }

    public ResultSet getToDO() throws SQLException {
        return statement.executeQuery(
                "SELECT title,details,id,color,done " +
                        "FROM todo " +
                        "WHERE userid='" + Main.userId + "'"
        );

    }

    public void createToDo(String title, String details, String color) throws SQLException {
        statement.executeUpdate(
                "INSERT INTO todo (userid,title,details,color) " +
                        "VALUES ('" + Main.userId + space + title + space + details + space + color + "');"
        );
    }

    public int amountOfToDo() throws SQLException {
        resultSet = statement.executeQuery(
                "SELECT COUNT(id) " +
                        "AS count FROM todo " +
                        "WHERE userid='" + Main.userId + "'AND done=false"
        );
        resultSet.next();
        return resultSet.getInt("count");
    }

    public void finishToDo(int id) throws SQLException {
        statement.executeUpdate(
                "UPDATE todo " +
                        "SET done=true " +
                        "WHERE id=" + id)
        ;
    }

    public boolean usernameExist(String username) throws SQLException {
        return statement.executeQuery(
                "SELECT id FROM users " +
                        "WHERE username='" + username + "' AND  NOT id='" + Main.userId + "'").next();
    }

    public boolean emailExist(String email) throws SQLException {
        return statement.executeQuery(
                "SELECT id FROM users " +
                        "WHERE email='" + email + "' AND  NOT id='" + Main.userId + "'").next();
    }

    public boolean phoneExist(String phone) throws SQLException {
        return statement.executeQuery(
                "SELECT id FROM users " +
                        "WHERE phone='" + phone + "' AND NOT id='" + Main.userId + "'").next() && phone != null;
    }

    public void updateAccount(String username, String email, String name, String lastName, String phoneNumber) throws SQLException {
        statement.executeUpdate("UPDATE users " +
                "SET username='" + username + "', email='" + email + "', name='" + name +
                "', last_name='" + lastName + "', phone='" + phoneNumber + "'  " +
                "WHERE id=" + Main.userId);
        setUserData();
    }


    public void createUser(String username, String password, String email, String name, String lastName, String phoneNumber) throws SQLException {
        statement.executeUpdate("INSERT INTO users (username,password,name,last_name,phone,email) " +
                "VALUES ('" + username + space + getSHA512(password) + space + name + space +
                lastName + space + phoneNumber + space + email + "')");
    }

    public void deleteUser() throws SQLException {
        statement.executeUpdate("DELETE FROM users WHERE id = '" + Main.userId + "'");
        String[] tables = new String[]{"calendar", "contacts", "project", "subjects", "todo"};
        for(String i: tables){
            statement.executeUpdate("DELETE FROM " + i + " WHERE userid='"+ Main.userId + "'");
        }
        ResultSet resultSet = statement.executeQuery("SELECT id FROM subjects WHERE userId ='" +Main.userId+ "'");
        while(resultSet.next()){
            statement.executeUpdate("DELETE FROM project WHERE subjectid ='" +resultSet.getInt("subjectid") + "'");
            statement.executeUpdate("DELETE FROM grades WHERE subjectid= '" + resultSet.getInt("subjectid") + "'");
        }
        statement.executeUpdate("DELETE FROM subjects WHERE userid='"+ Main.userId + "'");
    }
    public void deleteSubject(int id) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT id FROM subjects WHERE id ='" +id+ "'");
        resultSet.next();
        int subjectId = resultSet.getInt("id");
        statement.executeUpdate("DELETE FROM project WHERE subjectid= '" + subjectId + "'");
        statement.executeUpdate("DELETE FROM grades WHERE subject_id= '" + subjectId + "'");
        statement.executeUpdate("DELETE FROM subjects WHERE id ='" + id + "'");
    }
    public static String getSHA512(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.append("0");
            }
            return hashtext.toString();
        } catch (
                NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet getCalendarEvents(String date) throws SQLException {
        resultSet = statement.executeQuery(
                "SELECT id, title, body, time_start, time_end " +
                        "FROM calendar " +
                        "WHERE userid='" + Main.userId + "'AND date= '" + date + "' ORDER BY time_start ASC");
        return resultSet;
    }

    public void addCalendarEvent(String title, String body, String date, String time_start, String time_end) throws SQLException {
        statement.executeUpdate(
                "INSERT INTO calendar (userid,title,body,date,time_start,time_end) " +
                        "VALUES ('" + Main.userId + space + title + space + body +
                        "', DATE '" + date + space + time_start + space + time_end + "')");
    }

    public int amountEventsToday(String date, String time) throws SQLException {
        resultSet = statement.executeQuery(
                "SELECT COUNT(id) " +
                        "AS count FROM calendar " +
                        "WHERE userid='" + Main.userId +
                        "'AND date='" + date + "' AND time_start>= '" + time + "'");
        resultSet.next();
        return resultSet.getInt("count");
    }

    public ResultSet getUserSubjects() throws SQLException {
        return statement.executeQuery("SELECT id, name FROM subjects " +
                "WHERE userid ='" + Main.userId + "'");
    }

    public ResultSet getSubjectInformation(int subjectId) throws SQLException {
        return statement.executeQuery("SELECT * FROM subjects " +
                "WHERE id ='" + subjectId + "'");
    }

    public void createSubject(String subjectName, String teacherName, String className) throws SQLException {
        statement.executeUpdate("INSERT INTO subjects (userid, name, teacher, class, absences) " +
                "VALUES ('" + Main.userId + space + subjectName + space + teacherName + space + className + space + "0')");

    }

    public void changeSubjectInfo(String teacher, String _class, int absences) throws SQLException {
        statement.executeUpdate("UPDATE subjects " +
                "SET teacher='" + teacher + "', class='" + _class + "', absences='" + absences + "' " +
                "WHERE id='" + Main.selectedSubjectId + "'");
    }

    public void addGrade(double grade) throws SQLException {
        statement.executeUpdate("INSERT INTO grades (grade,subject_id) VALUES ('"+grade+"','"+ Main.selectedSubjectId+"')");
    }
    public double[] getGrades() throws SQLException {
        List<Double> rows = new ArrayList<Double>();
        ResultSet rs = statement.executeQuery("SELECT grade FROM grades WHERE subject_id='" + Main.selectedSubjectId + "'");
        while (rs.next()) {
            rows.add(rs.getDouble("grade"));
        }
        return rows.stream().mapToDouble(i -> i).toArray();
    }

    public String getSubjectName(int subjectid) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT name FROM subjects WHERE id='" + subjectid + "'");
        rs.next();
        return rs.getString("name");
    }


    public ResultSet getProjects() throws SQLException {
        return statement.executeQuery(
                "SELECT * FROM project " +
                        "WHERE userid='" + Main.userId + "' ORDER BY datedue ASC");
    }


    public void addProject(String title, String details, LocalDate date, int subjectId) throws SQLException {
        statement.executeUpdate(
                "INSERT INTO project (userid,subjectid,title,description,datedue) " +
                        "VALUES ('" + Main.userId + space + subjectId + space + title + space + details + space + date + "')");
    }

    public ResultSet getProjectsBySubjectId() throws SQLException {
        return statement.executeQuery(
                "SELECT title FROM project " +
                        "WHERE subjectid='" + Main.selectedSubjectId + "'");
    }

    public void deleteProject(int id) throws SQLException {
        statement.executeUpdate(
                "DELETE FROM project " +
                        "WHERE id = '" + id + "'");
    }

    public int amountOfProjects() throws SQLException {
        resultSet = statement.executeQuery("SELECT COUNT(id) AS count FROM project WHERE userid='" + Main.userId + "'");
        resultSet.next();
        return resultSet.getInt("count");
    }

    public ResultSet getContacts() throws SQLException {
        return statement.executeQuery(
                "SELECT * FROM contacts " +
                        "WHERE userid='" + Main.userId + "' ORDER BY surname ASC, name ASC");
    }

    public void createContact(String name, String surname, String phone, String mail) throws SQLException {
        statement.executeUpdate(
                "INSERT INTO contacts (userid,name,surname,phone,mail) " +
                        "VALUES ('" + Main.userId + space + name + space + surname + space + phone + space + mail + "')");
    }

    public void deleteContact(int id) throws SQLException {
        statement.executeUpdate("DELETE FROM contacts WHERE id='" + id + "'");
    }

    public boolean hasSubject() throws SQLException {
        resultSet = statement.executeQuery("select exists(select 1 from subjects WHERE userid='" + Main.userId + "') AS output");
        resultSet.next();
        return resultSet.getBoolean("output");
    }

}