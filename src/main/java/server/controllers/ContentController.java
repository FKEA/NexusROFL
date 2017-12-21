package server.controllers;

import server.models.Event;
import server.models.Post;
import server.models.User;

import java.sql.Timestamp;

/**
 * Created by Filip on 10-10-2017.
 */
public class ContentController {

    public ContentController() {

    }

    /**
     * This method is needed to validate the time issues.
     */

    public Event validateEventCreation(int id, String title, Timestamp created, User owner, Timestamp startDate,
                                       Timestamp endDate, String description) throws IllegalArgumentException {
        /**
         * An event object is created, which is needed to validate the different parameters.
         */

        Event validateEvent = new Event(id, title, created, owner, startDate, endDate, description);
        /**
         * The if method does not make it possible to create an event, if the start date is before current time.
         */
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if (timestamp.after(startDate)) {
            System.out.println("Can not create an event before current time");
            throw new IllegalArgumentException("Event can not be before current time");

            /**
             * The else if method does not make it possible to create an event, if the end date is before the start date.
             */

        } else if (startDate.after(endDate)) {
            System.out.println("The choosen date can not be after end date");
            throw new IllegalArgumentException("End date can not be before start date");

        } else if (title.length() == 0) {
            System.out.println("Title can not be empty");
            throw new IllegalArgumentException("Title can not be empty");
        }

        return validateEvent;
    }

    /**
     * This method is needed to validate the post issues.
     */
    public Post validatePostCreation(int id, Timestamp created, User owner, String content, Event event, Post parent)
            throws IllegalArgumentException {
        /**
         * An object is created, which is need to validate the different parameters.
         */
        Post validatePost = new Post(id, created, owner, content, event, parent);
        /**
         * The if method does not make it possible to create a post, if the content is empty.
         */
        if (content.length() == 0) {
            System.out.println("Post can not be empty");
            throw new IllegalArgumentException("Content can not be empty");
        }

        return validatePost;

    }


}
