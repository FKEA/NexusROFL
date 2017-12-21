package server.models;

import java.util.ArrayList;

/**
 * This class defines the variables of User.
 *
 * Created by Filip on 10-10-2017.
 */
public class User {

    private int id;
    private String password;
    private String salt;
    private String firstName;
    private String lastName;
    private String email;
    private String description;
    private Character gender;
    private String major;
    private Integer semester;
    private ArrayList<Event> events;
    private ArrayList<Post> posts;

    public User (){
        this.events = new ArrayList<Event>();
        this.posts = new ArrayList<Post>();
    }

    public User(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public User(String email,String salt,String password) {
        this.email = email;
        this.salt = salt;
        this.password = password;
    }

    /*
    public User(String password, String salt, String firstName) {
        this.id = id;
        this.password = password;
        this.salt = salt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.description = description;
        this.gender = gender;
        this.major = major;
        this.semester = semester;

        this.events = new ArrayList<Event>();
        this.posts = new ArrayList<Post>();
    }
    */

    // Use this constructor when assembling data for new user creation
       public User(String password, String firstName, String lastName, String email, String description, String gender, String major, int semester){
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.description = description;
        this.gender = gender.charAt(0);
        this.major = major;
        this.semester = semester;

        this.events = new ArrayList<Event>();
        this.posts = new ArrayList<Post>();
    }




    // Use this constructor when getting users from the database -> Does not contain password and salt
    public User(int id, String firstName, String lastName, String email, String description, char gender, String major, int semester) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.description = description;
        this.gender = gender;
        this.major = major;
        this.semester = semester;
        this.events = new ArrayList<Event>();
        this.posts = new ArrayList<Post>();
    }

    public User(int id) {
        this.id = id;
    }

    public User(int user_id, String first_name, String last_name, String email, String password, String salt, String description, char gender, String major, int semester)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.description = description;
        this.gender = gender;
        this.major = major;
        this.semester = semester;
        this.events = new ArrayList<Event>();
        this.posts = new ArrayList<Post>();
    }

    public User(int id, String email, String salt, String password) {
        this.id = id;
        this.email = email;
        this.salt = salt;
        this.password = password;
    }


    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Getters

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public char getGender() {
        return gender;
    }

    public String getMajor() {
        return major;
    }

    public int getSemester() {
        return semester;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }
}
