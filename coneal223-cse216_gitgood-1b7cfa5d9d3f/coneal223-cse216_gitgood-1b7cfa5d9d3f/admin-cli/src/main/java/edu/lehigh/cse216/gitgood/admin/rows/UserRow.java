package edu.lehigh.cse216.gitgood.admin.rows;

/**
 * UserRow holds a row of information for a user. This information consists of
 * a user id, a name string, a username string, an email string, a gender
 * identity
 * string, a sexual orientation string, a note string, a boolean for whether the
 * user is validated or disabled, and a creation date
 */
public class UserRow {
    /**
     * Unique identifier associated with the user
     */
    public final int id;

    /**
     * The name of the user
     */
    public String name;

    /**
     * The username of the user
     */
    public String username;

    /**
     * The email of the user
     */
    public String email;

    /**
     * The gender identity of the user
     */
    public String gender_identity;

    /**
     * The sexual orientation of the user
     */
    public String sexual_orientation;

    /**
     * The note of the user
     */
    public String note;

    /**
     * A boolean to indicate if the post is validated or disabled
     */
    public boolean validated;

    public String created;

    /**
     * Create a new UserRow with the provided user information
     * 
     * @param id                 The id to associate with the user, unique
     * @param name               The name of the user
     * @param username           The username of the user
     * @param email              The email of the user
     * @param gender_identity    The gender identity of the user
     * @param sexual_orientation The sexual orientation of the user
     * @param note               The note of the user
     * @param validated          The boolean indicating if this user is validated or
     *                           disabled
     */
    public UserRow(int id, String name, String username, String email, String gender_identity,
            String sexual_orientation, String note, boolean validated, String created) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.gender_identity = gender_identity;
        this.sexual_orientation = sexual_orientation;
        this.note = note;
        this.validated = validated;
        this.created = created;

    }

    /**
     * Copy constructor to create a UserRow from another
     * 
     * @param user The UserRow object to copy
     */
    public UserRow(UserRow user) {
        this.id = user.id;
        this.name = user.name;
        this.username = user.username;
        this.email = user.email;
        this.gender_identity = user.gender_identity;
        this.sexual_orientation = user.sexual_orientation;
        this.note = user.note;
        this.validated = user.validated;
        this.created = created;

    }

}