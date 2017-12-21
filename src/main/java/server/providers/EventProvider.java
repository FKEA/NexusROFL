package server.providers;

import server.models.Event;

import server.models.User;
import server.util.DBConnector;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import java.sql.Statement;


/**
 * The purpose of this class is to communicate and making requests to the tables events and
 * events_has_users in the DB cafe_nexus. This class contains prepared statements and communicates
 * with the Event-class in the package models for getting the variables for an event
 */
public class EventProvider {

    /*
    PreparedStatement for getting all events ordered by id from DB cafe_nexus
     */
    public ArrayList<Event> getAllEvents() throws SQLException {

        DBConnector dbConn = new DBConnector();

        ArrayList<Event> allEvents = new ArrayList<>();

        ResultSet resultSet = null;

        PreparedStatement getAllEventsStmt = null;

        getAllEventsStmt = dbConn.getConnection().
                prepareStatement("SELECT * FROM events ORDER BY created DESC");


        resultSet = getAllEventsStmt.executeQuery();

         /*
         Getting variables from Models_Event class
         and adding events to ArrayList
         */
        while (resultSet.next()) {
            Event event = new Event(
                    resultSet.getInt("event_id"),
                    resultSet.getString("title"),
                    resultSet.getTimestamp("created"),
                    new User(resultSet.getInt("owner_id")),
                    resultSet.getTimestamp("beginning"),
                    resultSet.getTimestamp("ending"),
                    resultSet.getString("description"));

            allEvents.add(event);


        }

        //Return all events by id
        resultSet.close();
        getAllEventsStmt.close();
        dbConn.close();


        return allEvents;

    }


    //method for getting a single event by event_id
    public Event getEvent(int event_id) throws SQLException {

        DBConnector dbConn = new DBConnector();

        ArrayList<Event> getEvent = new ArrayList<>();
        Event event = null;
        ResultSet resultSet = null;

        PreparedStatement getEventStmt = dbConn.getConnection()
                .prepareStatement("SELECT * FROM events WHERE event_id = ?");

        getEventStmt.setInt(1, event_id);

        resultSet = getEventStmt.executeQuery();

        while (resultSet.next()) {
            event = new Event(
                    resultSet.getInt("event_id"),
                    resultSet.getString("title"),
                    resultSet.getTimestamp("created"),
                    new User(resultSet.getInt("owner_id")),
                    resultSet.getTimestamp("beginning"),
                    resultSet.getTimestamp("ending"),
                    resultSet.getString("description"));
        }


        resultSet.close();
        getEventStmt.close();
        dbConn.close();

        return event;
    }

    public ArrayList<Event> getEventByUserId(int user_id) throws SQLException {

        DBConnector dbConn = new DBConnector();

        ArrayList<Event> events = new ArrayList<>();
        ResultSet resultSet = null;

        PreparedStatement getEventStmt = dbConn.getConnection()
                .prepareStatement("SELECT * FROM events WHERE owner_id = ? ORDER BY created DESC");

        getEventStmt.setInt(1, user_id);

        resultSet = getEventStmt.executeQuery();

        while (resultSet.next()) {
            Event event = new Event(
                    resultSet.getInt("event_id"),
                    resultSet.getString("title"),
                    resultSet.getTimestamp("created"),
                    new User(resultSet.getInt("owner_id")), //Creating an owner to the event
                    resultSet.getTimestamp("beginning"),
                    resultSet.getTimestamp("ending"),
                    resultSet.getString("description"));
            events.add(event);
        }

        resultSet.close();
        getEventStmt.close();
        dbConn.close();

        return events;
    }


    //Method for creating a new event
    public void createEvent(Event event) throws SQLException {

        DBConnector dbConn = new DBConnector();

        PreparedStatement createEventStmt = dbConn.getConnection().
                prepareStatement("INSERT INTO events (title, description, beginning, ending, owner_id) VALUES (?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);

        createEventStmt.setString(1, event.getTitle());
        createEventStmt.setString(2, event.getDescription());
        createEventStmt.setTimestamp(3, event.getStartDate());
        createEventStmt.setTimestamp(4, event.getEndDate());
        createEventStmt.setInt(5, event.getOwner().getId());

        createEventStmt.executeUpdate();

        createEventStmt.close();

        dbConn.close();

    }

    //Creating a method for subscribe to an event by user_id
    public void subscribeToEvent(int user_id, int event_id) throws SQLException {

        DBConnector dbConn = new DBConnector();

        PreparedStatement subscribeToEventStmt = dbConn.getConnection()
                .prepareStatement("INSERT INTO events_has_users (user_id, event_id) VALUES (?,?)");

        subscribeToEventStmt.setInt(1, user_id);
        subscribeToEventStmt.setInt(2, event_id);
        subscribeToEventStmt.executeUpdate();

        subscribeToEventStmt.close();
        dbConn.close();

    }

    //Method for getting participants to events by id
    public ArrayList<Integer> getParticipantIdsByEventId(int event_id) throws SQLException {

        DBConnector dbConn = new DBConnector();

        ResultSet resultSet = null;
        ArrayList<Integer> user_ids = new ArrayList<Integer>();

        PreparedStatement getParticipantIdByEventId = dbConn.getConnection().prepareStatement("SELECT * FROM events_has_users WHERE event_id = ?");

        getParticipantIdByEventId.setInt(1, event_id);

        resultSet = getParticipantIdByEventId.executeQuery();


        //Return participants by id
        while (resultSet.next()) {
            user_ids.add(resultSet.getInt("user_id"));
        }

        resultSet.close();
        getParticipantIdByEventId.close();
        dbConn.close();

        return user_ids;
    }

}

