import Data.Patient;
import Data.User;
import Database.Manage_Patient_db;
import Database.Manage_User_db;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

@WebServlet(
        name = "remote_patient_monitoring_server",
        description = "remote_patient_monitoring_server",
        urlPatterns = {
                "/rpm",
                "/rpm/login",
                "/rpm/add_patient",
                "/rpm/add_user",
                "/rpm/request_patients",
                "/rpm/request_users"
                }
        )
public class Run_Server extends HttpServlet {
    Manage_User_db user_db;
    Manage_Patient_db patient_db;
    boolean debug = true;


    public Run_Server() throws IOException, SQLException {
        print_datetime();
        user_db = new Manage_User_db();
        patient_db = new Manage_Patient_db();
        if (debug) {
            System.out.println("Starting server");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (debug) {
            System.out.println("---------------------------------");
            print_datetime();
            System.out.println("Do get");
        }

        PrintWriter writer = resp.getWriter();
        writer.append("Get needs to be implemented still");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (debug) {
            System.out.println("---------------------------------");
            print_datetime();
            System.out.println("Do post");
        }

        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println(String.format("reqBody: %s", reqBody));
        resp.setContentType("text/html");


        String server_path = req.getServletPath();

        Messenger messenger = new Messenger();
        messenger.set_success(true);

        switch (server_path) {
            case "/rpm/add_patient": {
                Gson gson = new Gson();
                Patient p = gson.fromJson(reqBody, Patient.class);
                p.print_patient_details();
                try {
                    patient_db.add_patient(p);
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("failed to add patient");
                    messenger.set_success(false);
                }
                break;
            }
            case "/rpm/add_user": {
                Gson gson = new Gson();
                User u = gson.fromJson(reqBody, User.class);
                try {
                    user_db.add_user(u);
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("failed to add user");
                    messenger.set_success(false);
                }
                break;
            }
            case "/rpm/request_users": {
                Gson gson = new Gson();
                try {
                    String users_json = user_db.get_users();
                    messenger.set_message(users_json);
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("failed to get users");
                    messenger.set_success(false);
                }
                break;
            }
            case "/rpm/request_patients": {
                Gson gson = new Gson();
                User u = gson.fromJson(reqBody, User.class);
                if (check_valid_user(u)) {
                    try {
                        String patients_json = patient_db.get_patients();
                        messenger.set_message(patients_json);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        messenger.set_success(false);
                    }
                } else {
                    req.setAttribute("error", "Unknown user, please try again.");
                    messenger.set_success(false);
                }
                break;
            }
            case "/rpm/login": {
                // Used login backend code as inspiration: https://stackoverflow.com/questions/2349633/doget-and-dopost-in-servlets
                Gson gson = new Gson();
                User u = gson.fromJson(reqBody, User.class);
                boolean valid_user = check_valid_user(u);
                if (!valid_user) {
                    req.setAttribute("error", "Unknown user, please try again.");
                    messenger.set_success(false);
                }
                break;
            }
        }
        if (debug) {
            System.out.println(String.format("Success: %b", messenger.get_success()));
        }
        if (messenger.get_success()) {
            resp.getWriter().write(String.format("Success for action: %s", server_path));
        } else {
            resp.getWriter().write(String.format("Failure for action: %s", server_path));
        }
    }

    private boolean check_valid_user(User u){
        boolean valid_user = true;
//        boolean valid_user = false;
//        try {
//            // valid_user = user_db.find_user(u); not implemented yet
//            valid_user = true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        if (valid_user){
            System.out.println("valid user");
        } else {
            System.out.println("invalid user");
        }
        return valid_user;
    }

    private void print_datetime(){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        System.out.println("Current time: " + strDate);
    }
}

class Messenger {
    private boolean success;
    private String message;

    public Messenger(){}

    public Messenger(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public String get_message(){
        return message;
    }

    public boolean get_success(){
        return success;
    }

    public void set_message(String message){
        this.message = message;
    }

    public void set_success(Boolean success){
        this.success = success;
    }
}