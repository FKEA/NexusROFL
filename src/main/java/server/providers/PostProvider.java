package server.providers;


import server.models.Event;
import server.models.Post;
import server.models.User;

import server.util.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;


/**
 * The purpose of this class is to communicate and making requests to the tables posts
 * in the DB cafe_nexus. This class contains prepared statements and communicates
 * with the Post-class in the package models for getting the variables for a post.
 * A post can both be an event and a comment. Notice a parent_id
 */
public class PostProvider {

    // PreparedStatement for getting all posts from posts in DB cafe_nexus
    public ArrayList<Post> getAllPosts() throws SQLException {

        DBConnector dbConn = new DBConnector();

        ArrayList<Post> allPosts = new ArrayList<>();

        ResultSet resultSet = null;

        PreparedStatement getAllPostsStmt = null;

        try {
            getAllPostsStmt = dbConn.getConnection().prepareStatement("SELECT * FROM posts WHERE parent_id is null AND event_id is null ORDER BY created DESC");

            resultSet = getAllPostsStmt.executeQuery();


            /*
            Getting all variables from the model class Post
            and adding all posts to the ArrayList
             */
            while (resultSet.next()) {
                Post post = new Post(
                        resultSet.getInt("post_id"),
                        resultSet.getTimestamp("created"),
                        new User(resultSet.getInt("user_id")),
                        resultSet.getString("content"),
                        new Event(resultSet.getInt("event_id")),
                        new Post(resultSet.getInt("parent_id"))
                );

                allPosts.add(post);

            } //Closing query
            resultSet.close();
            getAllPostsStmt.close();

        } catch (SQLException e) {
            e.printStackTrace();

        }

        //Returning all posts
        resultSet.close();
        getAllPostsStmt.close();
        dbConn.close();

        return allPosts;

    }

    //Creates a new post
    public int createPost(Post post) throws SQLException {

        DBConnector dbConn = new DBConnector();

        //Creating prepared statement
        PreparedStatement createPostStatement =
                dbConn.getConnection().prepareStatement("INSERT INTO posts " + "(content, event_id, parent_id, user_id)" +
                        "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

        //Inserting values into the prepared statement
        createPostStatement.setString(1, post.getContent());

        if (post.getEvent().getId() == 0) {
            createPostStatement.setNull(2, 1);
        } else {
            createPostStatement.setInt(2, post.getEvent().getId());
        }

        if (post.getParent().getId() == 0) {
            createPostStatement.setNull(3, 1);
        } else {
            createPostStatement.setInt(3, post.getParent().getId());
        }

        createPostStatement.setInt(4, post.getOwner().getId());

        //Execute update
        int rowsUpdated = createPostStatement.executeUpdate();

        //Checking if a row has been updated
        if (rowsUpdated != 1) {
            throw new SQLException("Error with creating a post, no rows are affected");
        }

        //Collect generated Post id
        ResultSet generatedKeys = createPostStatement.getGeneratedKeys();

        //Checking if primary key has been created
        if (generatedKeys.next()) {
            post.setId(generatedKeys.getInt(1));
        } else {
            throw new SQLException("Error with creating post, could not retrieve ID");
        }

        //Closing query
        generatedKeys.close();
        createPostStatement.close();
        dbConn.close();

        //Returning Post id
        return post.getId();
    }

    //Creating method for getting one post
    public Post getPost(int post_id) throws SQLException {

        DBConnector dbConn = new DBConnector();

        Post post = null;

        ResultSet resultSet = null;

        //Creating prepared statement for getting one post
        PreparedStatement getOnePostStatement = null;

        getOnePostStatement = dbConn.getConnection().prepareStatement("SELECT * FROM posts WHERE post_id = ?");

        getOnePostStatement.setInt(1, post_id);

        resultSet = getOnePostStatement.executeQuery();

        while (resultSet.next()) {
            post = new Post(
                    resultSet.getInt("post_id"),
                    resultSet.getTimestamp("created"),
                    new User(resultSet.getInt("user_id")), //Creating an owner to the post
                    resultSet.getString("content"),
                    new Event(resultSet.getInt("event_id")), //Creating an event to the post
                    new Post(resultSet.getInt("parent_id")) //Creating an parent to the post
            );

        }

        //Closing query
        resultSet.close();
        getOnePostStatement.close();
        dbConn.close();

        //Returning one post
        return post;

    }


    /**
     * Creating a method that gets all comments from one post by checking if the post has a parent_id
     *
     * @param parent_id
     * @return The method returns an ArrayList that contains all the comments to one post
     */
    public ArrayList<Post> getPostsByParentId(int parent_id) throws SQLException {

        DBConnector dbConn = new DBConnector();

        // Creating an object of the Arraylist and creating a prepared statement for getting all comments
        ArrayList<Post> allComments = new ArrayList<>();
        ResultSet resultSet = null;

        PreparedStatement getAllCommentsStatement = null;

        getAllCommentsStatement = dbConn.getConnection().prepareStatement("SELECT * FROM posts WHERE parent_id = ? ORDER BY created DESC");

        getAllCommentsStatement.setInt(1, parent_id);
        resultSet = getAllCommentsStatement.executeQuery();

        while (resultSet.next()) {
            Post post = new Post(
                    resultSet.getInt("post_id"),
                    resultSet.getTimestamp("created"),
                    new User(resultSet.getInt("user_id")),
                    resultSet.getString("content"),
                    new Event(resultSet.getInt("event_id")),
                    new Post(resultSet.getInt("parent_id"))
            );

            allComments.add(post); //Adding a post to all comments with a belonging parent_id
        }
        resultSet.close();
        getAllCommentsStatement.close();
        dbConn.close();

        //Returning all comments belonging with a parent_id to a specific post
        return allComments;

    }

    //Creating method for getting one post
    public ArrayList<Post> getPostByUserId(int user_id) throws SQLException {

        DBConnector dbConn = new DBConnector();

        ArrayList<Post> posts = new ArrayList<Post>();

        ResultSet resultSet = null;

        //Creating prepared statement for getting one post
        PreparedStatement getOnePostStatement = null;

        getOnePostStatement = dbConn.getConnection().prepareStatement("SELECT * FROM posts WHERE user_id = ? ORDER BY created DESC");

        getOnePostStatement.setInt(1, user_id);

        resultSet = getOnePostStatement.executeQuery();


        while (resultSet.next()) {
            Post post = new Post(
                    resultSet.getInt("post_id"),
                    resultSet.getTimestamp("created"),
                    new User(resultSet.getInt("user_id")), //Creating an owner to the post
                    resultSet.getString("content"),
                    new Event(resultSet.getInt("event_id")), //Creating an event to the post
                    new Post(resultSet.getInt("parent_id")) //Creating an parent to the post
            );

            posts.add(post);

        }

        //Closing query
        resultSet.close();
        getOnePostStatement.close();
        dbConn.close();

        //Returning one post
        return posts;

    }

    //Creating method for getting all posts by an event_id
    public ArrayList<Post> getAllPostsByEventId(int event_id) throws SQLException {

        DBConnector dbConn = new DBConnector();

        ArrayList<Post> posts = new ArrayList<Post>();

        ResultSet resultSet = null;

        //Creating an prepared statement for getting all posts by an event_id
        PreparedStatement getAllPostsByEventIdStmt = null;


        getAllPostsByEventIdStmt = dbConn.getConnection().prepareStatement("SELECT * FROM posts WHERE event_id = ? ORDER BY created DESC");

        getAllPostsByEventIdStmt.setInt(1, event_id);

        resultSet = getAllPostsByEventIdStmt.executeQuery();

        while (resultSet.next()) {
            Post post = new Post(
                    resultSet.getInt("post_id"),
                    resultSet.getTimestamp("created"),
                    new User(resultSet.getInt("user_id")),
                    resultSet.getString("content"),
                    new Event(resultSet.getInt("event_id")),
                    new Post(resultSet.getInt("parent_id"))
            );

            posts.add(post);
        }

        resultSet.close();
        getAllPostsByEventIdStmt.close();
        dbConn.close();

        return posts;
    }

}


