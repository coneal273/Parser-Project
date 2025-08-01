package edu.lehigh.cse216.gitgood.admin;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import edu.lehigh.cse216.gitgood.admin.rows.*;

public class Database {

    private Connection mConnection;

    private PreparedStatement mSelectOne;

    // insert statements
    private PreparedStatement mInsertNewPost;
    private PreparedStatement mInsertNewVote;
    private PreparedStatement mInsertNewComment;
    private PreparedStatement mInsertNewUser;

    // create statments
    private PreparedStatement mCreatePostsTable;
    private PreparedStatement mCreateVotesTable;
    private PreparedStatement mCreateCommentsTable;
    private PreparedStatement mCreateUsersTable;

    // drop table statements
    private PreparedStatement mDropPostsTable;
    private PreparedStatement mDropVotesTable;
    private PreparedStatement mDropUsersTable;
    private PreparedStatement mDropCommentsTable;

    // delete statements
    private PreparedStatement mDeleteVote;
    private PreparedStatement mDeleteUser;
    private PreparedStatement mDeletePost;
    private PreparedStatement mDeleteComment;

    // update statements
    private PreparedStatement mUpdateOne;
    private PreparedStatement mUpdateValidationForUser;
    private PreparedStatement mUpdateValidationForPost;
    private PreparedStatement mUpdateValidationForComment;
    private PreparedStatement mUpdateComment;
    private PreparedStatement mUpdateUser;
    private PreparedStatement mUpdateVote;

    // select one statements
    private PreparedStatement mSelectOneVote;
    private PreparedStatement mSelectOnePost;
    private PreparedStatement mSelectOneUser;
    private PreparedStatement mSelectOneFile;

    // select all statements
    private PreparedStatement mSelectVotes;
    private PreparedStatement mSelectUsers;
    private PreparedStatement mSelectPosts;
    private PreparedStatement mSelectComments;

    private Database() {
    }

    /**
     * Get a fully-configured connection to the database
     * 
     * @param ip   The IP address of the database server
     * @param port The port on the database server to which connection requests
     *             should be sent
     * @param user The user ID to use when connecting
     * @param pass The password to use when connecting
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    static Database getDatabase(String url) {
        // Create an un-configured Database object
        Database db = new Database();

        // Give the Database object a connection, fail if we cannot get one
        try {
            Class.forName("org.postgresql.Driver");
            URI dbUri = new URI(url);
            String user = dbUri.getUserInfo().split(":")[0];
            String pass = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + dbUri.getPort() + dbUri.getPath();
            Connection conn = DriverManager.getConnection(dbUrl, user, pass);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Unable to find postgresql driver");
            return null;
        } catch (URISyntaxException e) {
            System.out.println("URI syntax error");
            return null;
        }

        try {

            // create tables
            db.mCreatePostsTable = db.mConnection.prepareStatement(
                    "CREATE TABLE posts (id SERIAL PRIMARY KEY, poster_id INT NOT NULL, text VARCHAR(500) NOT NULL, validated BOOLEAN NOT NULL, file_id VARCHAR(500), link VARCHAR(500))");
            db.mCreateVotesTable = db.mConnection.prepareStatement(
                    "CREATE TABLE votes (id SERIAL PRIMARY KEY, voter_id INT NOT NULL, post_id INT NOT NULL, liked BOOLEAN NOT NULL)");
            db.mCreateCommentsTable = db.mConnection.prepareStatement(
                    "CREATE TABLE comments (id SERIAL PRIMARY KEY, poster_id INT NOT NULL, post_id INT NOT NULL, text VARCHAR(500) NOT NULL, file_id VARCHAR(500), link VARCHAR(500), validated BOOLEAN NOT NULL)");
            db.mCreateUsersTable = db.mConnection.prepareStatement(
                    "CREATE TABLE users (id SERIAL PRIMARY KEY, name VARCHAR(500) NOT NULL, username VARCHAR(500) NOT NULL, email VARCHAR(500) NOT NULL, gender_identity VARCHAR(500) NOT NULL, sexual_orientation VARCHAR(500) NOT NULL, note VARCHAR(500) NOT NULL, validated BOOLEAN NOT NULL)");

            // drop tables
            db.mDropPostsTable = db.mConnection.prepareStatement("DROP TABLE posts");
            db.mDropVotesTable = db.mConnection.prepareStatement("DROP TABLE votes");
            db.mDropUsersTable = db.mConnection.prepareStatement("DROP TABLE users");
            db.mDropCommentsTable = db.mConnection.prepareStatement("DROP TABLE comments");

            // inser new rows
            db.mInsertNewPost = db.mConnection
                    .prepareStatement("INSERT INTO posts VALUES (default, ?, ?, ?, ?, ?)");
            db.mInsertNewVote = db.mConnection.prepareStatement("INSERT INTO votes VALUES (default, ?, ?, ?)");
            db.mInsertNewComment = db.mConnection
                    .prepareStatement("INSERT INTO comments VALUES (default, ?, ?, ?, ?, ?, ?)");
            db.mInsertNewUser = db.mConnection
                    .prepareStatement("INSERT INTO users VALUES (default, ?, ?, ?, ?, ?, ?, ?)");

            // select one content
            db.mSelectOneUser = db.mConnection.prepareStatement(
                    "SELECT id, name, username, email, gender_identity, sexual_orientation, note, validated FROM Users WHERE id=?");
            db.mSelectOnePost = db.mConnection.prepareStatement("SELECT * FROM posts WHERE id=?");
            db.mSelectOneVote = db.mConnection.prepareStatement(
                    "SELECT user_id, post_id, liked, created FROM votes WHERE user_id=? AND post_id=?");
            db.mSelectOneFile = db.mConnection.prepareStatement(
                    "SELECT id, url, created FROM file WHERE user_id=?");

            // select all content
            db.mSelectVotes = db.mConnection
                    .prepareStatement("SELECT user_id, post_id, liked, created FROM votes WHERE post_id=?");
            db.mSelectPosts = db.mConnection
                    .prepareStatement("SELECT id, user_id, content, active, created FROM posts");
            db.mSelectUsers = db.mConnection.prepareStatement(
                    "SELECT id, name, username, email, gender_identity, sexual_orientation, note, active, created FROM users");
            db.mSelectComments = db.mConnection.prepareStatement(
                    "SELECT id, user_id, post_id, content, active, created FROM comments WHERE post_id=?");

            // update content
            db.mUpdateValidationForUser = db.mConnection.prepareStatement("UPDATE users SET validated=? WHERE id=?");
            db.mUpdateValidationForPost = db.mConnection.prepareStatement("UPDATE posts SET validated=? WHERE id=?");
            db.mUpdateValidationForComment = db.mConnection
                    .prepareStatement("UPDATE comments SET validated=? WHERE id=?");
            db.mUpdateComment = db.mConnection
                    .prepareStatement("UPDATE comments SET content=?, created=now() WHERE id=?");
            db.mUpdateVote = db.mConnection
                    .prepareStatement("UPDATE votes SET liked=?, created=now() WHERE user_id=? AND post_id=?");
            db.mUpdateUser = db.mConnection.prepareStatement(
                    "UPDATE users SET name=?, username=?, email=?, gender_identity=?, sexual_orientation=?, note=? WHERE id=?");

            // delete rows
            db.mDeleteVote = db.mConnection.prepareStatement("DELETE FROM votes WHERE id = ?");
            db.mDeletePost = db.mConnection.prepareStatement("DELETE FROM posts WHERE id = ?");
            db.mDeleteUser = db.mConnection.prepareStatement("DELETE FROM users WHERE id = ?");
            db.mDeleteComment = db.mConnection.prepareStatement("DELETE FROM comments WHERE id = ?");

        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }

    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an
     * error occurred during the closing operation.
     * 
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }

    /**
     * Insert a row into the posts table
     * 
     * @param poster  for the user making the post
     * @param content The body of the post
     * @param valid   for if the post is validated or not
     * 
     * @return The number of rows that were inserted
     */
    int insertPostRow(int poster, String content, int valid, String file_id, String created) {
        int count = 0;
        try {
            mInsertNewPost.setInt(1, poster);
            mInsertNewPost.setString(2, content);
            mInsertNewPost.setInt(3, valid);
            mInsertNewPost.setString(4, file_id);
            mInsertNewPost.setString(5, created);
            count += mInsertNewPost.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Insert a row into the votes table
     * 
     * @param voter for the user voting
     * @param post  for the id of the post being voted on
     * @param liked for if the post was liked
     * 
     * @return The number of rows that were inserted
     */
    int insertVoteRow(int voter, int post, int liked) {
        int count = 0;
        try {
            mInsertNewVote.setInt(1, voter);
            mInsertNewVote.setInt(2, post);
            mInsertNewVote.setInt(3, liked);
            count += mInsertNewVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Insert a row into the comments table
     * 
     * @param voter   for the user leaving a comment
     * @param post    for the id of the post being commented on
     * @param content for the body of the comment
     * 
     * @return The number of rows that were inserted
     */
    int insertCommentRow(int poster, int post, String content, String file_id) {
        int count = 0;
        try {
            mInsertNewComment.setInt(1, poster);
            mInsertNewComment.setInt(2, post);
            mInsertNewComment.setString(3, content);
            mInsertNewComment.setString(4, file_id);
            mInsertNewComment.setBoolean(6, true);
            count += mInsertNewComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Insert a row into the users table
     * 
     * @param name     for the name of the user
     * @param username for the username of the user
     * @param email    for the user's email
     * @param gi       for the user's gender identity
     * @param so       for the user's sexual orientation
     * @param note     for a note about the user
     * @param valid    to indicate if the user is validated or not
     * 
     * @return The number of rows that were inserted
     */
    int insertUserRow(String name, String username, String email, String gi, String so, String note, boolean valid,
            String created) {
        int count = 0;
        try {
            mInsertNewUser.setString(1, name);
            mInsertNewUser.setString(2, username);
            mInsertNewUser.setString(3, email);
            mInsertNewUser.setString(4, gi);
            mInsertNewUser.setString(5, so);
            mInsertNewUser.setString(6, note);
            mInsertNewUser.setBoolean(7, valid);
            mInsertNewUser.setString(8, created);
            count += mInsertNewUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Delete a row in the posts table by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted. -1 indicates an error.
     */
    int deletePost(int id) {
        int res = -1;
        try {
            mDeletePost.setInt(1, id);
            res = mDeletePost.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row in the votes table by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted. -1 indicates an error.
     */
    int deleteVote(int id) {
        int res = -1;
        try {
            mDeleteVote.setInt(1, id);
            res = mDeleteVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row in the user table by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted. -1 indicates an error.
     */
    int deleteUser(int id) {
        int res = -1;
        try {
            mDeleteUser.setInt(1, id);
            res = mDeleteUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row in the comments table by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted. -1 indicates an error.
     */
    int deleteComment(int id) {
        int res = -1;
        try {
            mDeleteComment.setInt(1, id);
            res = mDeleteComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Method to update the validation of a user
     * 
     * @param id    for the id of the user
     * @param valid for if the user should be made valid or invalid
     * @return 1 if update was executed
     */
    int updateValidationForUser(int id, boolean valid) {
        int res = -1;
        try {
            mUpdateValidationForUser.setBoolean(1, valid);
            mUpdateValidationForUser.setInt(2, id);
            res = mUpdateValidationForUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int updateValidationForPost(int id, boolean valid) {
        int res = -1;
        try {
            mUpdateValidationForPost.setBoolean(1, valid);
            mUpdateValidationForPost.setInt(2, id);
            res = mUpdateValidationForPost.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int updateValidationForComment(int id, boolean valid) {
        int res = -1;
        try {
            mUpdateValidationForComment.setBoolean(1, valid);
            mUpdateValidationForComment.setInt(2, id);
            res = mUpdateValidationForComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Create a posts table. If it already exists, this will print an error
     */
    void createPostsTable() {
        try {
            mCreatePostsTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Table Created!");
    }

    /**
     * Create a votes table. If it already exists, this will print an error
     */
    void createVotesTable() {
        try {
            mCreateVotesTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Table Created!");
    }

    /**
     * Create a comments table. If it already exists, this will print an error
     */
    void createCommentsTable() {
        try {
            mCreateCommentsTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Table Created!");
    }

    /**
     * Create a users table. If it already exists, this will print an error
     */
    void createUsersTable() {
        try {
            mCreateUsersTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Table Created!");
    }

    /**
     * Remove vote table from the database. If it does not exist, this will print
     * an error.
     */
    void dropVotesTable() {
        try {
            mDropVotesTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove posts table from the database. If it does not exist, this will print
     * an error.
     */
    void dropPostsTable() {
        try {
            mDropPostsTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove comment table from the database. If it does not exist, this will print
     * an error.
     */
    void dropCommentsTable() {
        try {
            mDropCommentsTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove users table from the database. If it does not exist, this will print
     * an error.
     */
    void dropUsersTable() {
        try {
            mDropUsersTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get information about all users that exist
     * 
     * @return List of all users or null on error
     */
    ArrayList<UserRow> getAllUsers() {
        try {
            ArrayList<UserRow> res = new ArrayList<UserRow>();
            ResultSet rs = mSelectUsers.executeQuery();
            while (rs.next()) {
                // store user id, name, username, email, gender identity, sexual orientation,
                // note, active boolean, and creation date
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String gender_identity = rs.getString("gender_identity");
                String sexual_orientation = rs.getString("sexual_orientation");
                String note = rs.getString("note");
                boolean active = rs.getBoolean("active");
                String created = rs.getString("created");

                res.add(new UserRow(id, name, username, email, gender_identity, sexual_orientation, note, active,
                        created));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get information about a user
     * 
     * @param user_id Id number of user to get
     * 
     * @return User information or null on error
     */
    UserRow getOneUser(int user_id) {
        try {
            // configure and execute query
            mSelectOneUser.setInt(1, user_id);
            ResultSet rs = mSelectOneUser.executeQuery();

            if (rs.next()) { // user was found
                // store user id, name, username, email, gender identity, sexual orientation,
                // note, active boolean, and creation date
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String gender_identity = rs.getString("gender_identity");
                String sexual_orientation = rs.getString("sexual_orientation");
                String note = rs.getString("note");
                boolean active = rs.getBoolean("validated");
                String created = rs.getString("created");

                rs.close();
                return new UserRow(id, name, username, email, gender_identity, sexual_orientation, note, active,
                        created);
            } else { // user not found?
                rs.close();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the count of votes on the given post
     * 
     * @param post_id Id number of the post to get votes from
     * 
     * @return Count of votes on post (likes - dislikes) or 0 on error (IS THERE A
     *         BETTER ERROR RETURN VALUE?)
     */
    int getVoteCount(int post_id) {
        try {
            int vcount = 0;
            // configure and execute query
            mSelectVotes.setInt(1, post_id);
            ResultSet rs = mSelectVotes.executeQuery();
            while (rs.next()) {
                boolean liked = rs.getBoolean("liked");
                // increment or decrement vote counter
                if (liked) {
                    vcount++;
                } else {
                    vcount--;
                }
            }
            rs.close();
            return vcount;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0; // IS THERE SOMETHING BETTER TO RETURN HERE?
        }
    }

    /**
     * Get a list of comments on the given post
     * 
     * @param post_id Id number of post to get comments from
     * 
     * @return List of comments on post or null on error
     */
    ArrayList<CommentRow> getAllCommentsOnPost(int post_id) {
        try {
            ArrayList<CommentRow> res = new ArrayList<CommentRow>();
            // configure and execute query
            mSelectComments.setInt(1, post_id);
            ResultSet rs = mSelectComments.executeQuery();
            while (rs.next()) {
                // store id, poster id, comment content, active boolean, and creation date
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                String content = rs.getString("content");
                boolean active = rs.getBoolean("active");
                String file_id = rs.getString("file_id");
                String created = rs.getString("created");
                res.add(new CommentRow(id, user_id, post_id, content, active, file_id, created));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get information about all posts that have been made
     * 
     * @return List of all posts or null on error
     */
    ArrayList<PostRow> getAllPosts() {
        try {
            ArrayList<PostRow> res = new ArrayList<PostRow>();
            ResultSet rs = mSelectPosts.executeQuery();
            while (rs.next()) {// REPLACE WITH GETONEPOST?
                // store post id, poster id, and post content
                int id = rs.getInt("id");
                int user_id = rs.getInt("post_id");
                String content = rs.getString("content");

                // calculate vote count
                int votes = getVoteCount(id);
                boolean valid = rs.getBoolean("validated");
                String file_id = rs.getString("file_id");
                String created = rs.getString("created");
                // create arraylist of comments
                ArrayList<CommentRow> comments = getAllCommentsOnPost(id);

                res.add(new PostRow(id, user_id, content, votes, valid, file_id, created));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get information about a post
     * 
     * @param post_id Id number of post to get
     * 
     * @return Post information or null on error
     */
    PostRow getOnePost(int post_id) {
        try {
            // configure and execute query
            mSelectOnePost.setInt(1, post_id);
            ResultSet rs = mSelectOnePost.executeQuery();

            if (rs.next()) { // post was found
                // store post id, user id, and post content
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                String content = rs.getString("content");

                // calculate vote count
                int votes = getVoteCount(post_id);
                // create arraylist of comments
                ArrayList<CommentRow> comments = getAllCommentsOnPost(post_id);

                // store active boolean and creation date
                boolean active = rs.getBoolean("active");
                String file_id = rs.getString("file_id");
                String created = rs.getString("created");

                rs.close();
                return new PostRow(id, user_id, content, votes, active, file_id, created);
            } else { // post not found?
                rs.close();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // CHECK RETURN VALUES FOR INSERT AND UPDATE POSTGRES

    /**
     * Create new post
     * 
     * @param user_id Id of user creating post
     * @param content String content of post
     * 
     * @return res?
     */
    int createPost(int user_id, String content) {
        try {
            mInsertNewPost.setInt(1, user_id);
            mInsertNewPost.setString(2, content);
            int res = mInsertNewPost.executeUpdate(); // check for errors? should this be executeUpdate or something
                                                      // else?
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // IS THERE SOMETHING BETTER TO RETURN HERE?
        }
    }

    /**
     * Create new comment on a post
     * 
     * @param user_id Id of user creating comment
     * @param post_id Id of post that comment is on
     * @param content String content of comment
     * 
     * @return res?
     */
    int createComment(int user_id, int post_id, String content) {
        try {
            mInsertNewComment.setInt(1, user_id);
            mInsertNewComment.setInt(2, post_id);
            mInsertNewComment.setString(3, content);
            int res = mInsertNewComment.executeUpdate();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Update the content of a comment
     * 
     * @param comment_id Id of comment to update
     * @param content    New content of comment
     * 
     * @return res?
     */
    int editComment(int comment_id, String content) {
        try {
            mUpdateComment.setString(1, content);
            mUpdateComment.setInt(2, comment_id);
            int res = mUpdateComment.executeUpdate();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Get a single vote on a post
     * 
     * @param user_id Id of the user who created the vote
     * @param post_id Id of the post the vote is on
     * 
     * @return Vote information or null on error/not found
     */
    VoteRow getVote(int user_id, int post_id) {
        try {
            // configure and execute query
            mSelectOneVote.setInt(1, user_id);
            mSelectOneVote.setInt(2, post_id);
            ResultSet rs = mSelectOneVote.executeQuery();

            if (rs.next()) { // vote was found
                boolean liked = rs.getBoolean("liked");
                String created = rs.getString("created");

                rs.close();
                return new VoteRow(user_id, post_id, liked, created);
            } else { // vote not found
                rs.close();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    int updatePost(int id, String likes) {
        int res = -1;
        try {
            mUpdateOne.setString(1, likes);
            mUpdateOne.setInt(2, id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    PostRow selectPost(int id) {
        PostRow res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new PostRow(rs.getInt("id"), rs.getInt("user_id"), rs.getString("content"), rs.getInt("likes"),
                        rs.getBoolean("validated"), rs.getString("file_id"), rs.getString("created"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Get information about a file
     * 
     * @param file_id Id number of file to get
     * 
     * @return file information or null on error
     */
    FileRow getOneFile(int file_id) {
        try {
            // configure and execute query
            mSelectOneFile.setInt(1, file_id);
            ResultSet rs = mSelectOneUser.executeQuery();

            if (rs.next()) { // file was found
                // store file id, url, created
                int id = rs.getInt("id");
                String url = rs.getString("url");
                String created = rs.getString("created");

                rs.close();
                return new FileRow(id, url, created);
            } else { // file not found?
                rs.close();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}