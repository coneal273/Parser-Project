package edu.lehigh.cse216.gitgood.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.*;
import java.io.File;
import org.apache.commons.codec.binary.Base64;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
//import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.util.Arrays;

import edu.lehigh.cse216.gitgood.backend.rows.*;

public class Database {
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;



    private PreparedStatement mSelectUsers;
    private PreparedStatement mSelectOneUser;

    private PreparedStatement mSelectOneUserByEmail;

    private PreparedStatement mSelectVotes;

    private PreparedStatement mSelectComments;

    /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectPosts;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOnePost;

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertNewUser;

    private PreparedStatement mInsertNewPost;

    private PreparedStatement mInsertNewComment;

    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mUpdateComment;

    private PreparedStatement mSelectOneVote;
    private PreparedStatement mInsertVote;
    private PreparedStatement mUpdateVote;
    private PreparedStatement mDeleteVote;

    private PreparedStatement mUpdateUser;


    
    /**
     * The Database constructor is private: we only create Database objects 
     * through the getDatabase() method.
     */
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

    static Database getDatabase(String db_url) {
        // Create an un-configured Database object
        Database db = new Database();

        // Give the Database object a connection, fail if we cannot get one
        try {
            Class.forName("org.postgresql.Driver");
            URI dbUri = new URI(db_url);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            Connection conn = DriverManager.getConnection(dbUrl, username, password);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Unable to find postgresql driver");
            return null;
        } catch (URISyntaxException s) {
            System.out.println("URI Syntax Error");
            return null;
        }

        // Attempt to create all of our prepared statements.  If any of these 
        // fail, the whole getDatabase() call should fail
        try {
            // NB: we can easily get ourselves in trouble here by typing the
            //     SQL incorrectly.  We really should have things like "tblData"
            //     as constants, and then build the strings for the statements
            //     from those constants.

            //select user information
            db.mSelectUsers = db.mConnection.prepareStatement("SELECT id, name, username, email, gender_identity, sexual_orientation, note, active, created FROM users");
            db.mSelectOneUser = db.mConnection.prepareStatement("SELECT id, name, username, email, gender_identity, sexual_orientation, note, active, created FROM users WHERE id=?");

            db.mSelectOneUserByEmail = db.mConnection.prepareStatement("SELECT id, name, username, email, gender_identity, sexual_orientation, note, active, created FROM users WHERE email=?");

            //select all votes on a post
            db.mSelectVotes = db.mConnection.prepareStatement("SELECT user_id, post_id, liked, created FROM votes WHERE post_id=?");
            //select all comments on a post
            db.mSelectComments = db.mConnection.prepareStatement("SELECT id, user_id, post_id, content, active, created FROM comments WHERE post_id=?");

            //select post information
            db.mSelectPosts = db.mConnection.prepareStatement("SELECT id, user_id, content, active, created FROM posts");
            db.mSelectOnePost = db.mConnection.prepareStatement("SELECT * FROM posts WHERE id=?");

            db.mInsertNewUser = db.mConnection.prepareStatement("INSERT INTO users (id, name, username, email, gender_identity, sexual_orientation, note, active, created) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, DEFAULT, DEFAULT) RETURNING id");

            //insert new post into table
            db.mInsertNewPost = db.mConnection.prepareStatement("INSERT INTO posts (id, user_id, content, active, created) VALUES (DEFAULT, ?, ?, DEFAULT, DEFAULT)");

            //insert new comment into table
            db.mInsertNewComment = db.mConnection.prepareStatement("INSERT INTO comments (id, user_id, post_id, content, active, created) VALUES (DEFAULT, ?, ?, ?, DEFAULT, DEFAULT)");

            //update comment content
            db.mUpdateComment = db.mConnection.prepareStatement("UPDATE comments SET content=?, created=now() WHERE id=?");

            //voting statements ***********
            db.mSelectOneVote = db.mConnection.prepareStatement("SELECT user_id, post_id, liked, created FROM votes WHERE user_id=? AND post_id=?");
            db.mInsertVote = db.mConnection.prepareStatement("INSERT INTO votes (user_id, post_id, liked, created) VALUES (?, ?, ?, DEFAULT)");
            db.mUpdateVote = db.mConnection.prepareStatement("UPDATE votes SET liked=?, created=now() WHERE user_id=? AND post_id=?");
            db.mDeleteVote = db.mConnection.prepareStatement("DELETE FROM votes WHERE user_id=? and post_id=?");

            //update user profile
            db.mUpdateUser = db.mConnection.prepareStatement("UPDATE users SET name=?, username=?, email=?, gender_identity=?, sexual_orientation=?, note=? WHERE id=?");

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
     *     error occurred during the closing operation.
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
     * Get information about all users that exist
     * 
     * @return List of all users or null on error
     */
    ArrayList<UserRow> getAllUsers() {
        try {
            ArrayList<UserRow> res = new ArrayList<UserRow>();
            ResultSet rs = mSelectUsers.executeQuery();
            while (rs.next()) {
                //store user id, name, username, email, gender identity, sexual orientation, note, active boolean, and creation date
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String gender_identity = rs.getString("gender_identity");
                String sexual_orientation = rs.getString("sexual_orientation");
                String note = rs.getString("note");
                boolean active = rs.getBoolean("active");
                Date created = rs.getDate("created");

                res.add(new UserRow(id, name, username, email, gender_identity, sexual_orientation, note, active, created));
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
            //configure and execute query
            mSelectOneUser.setInt(1, user_id);
            ResultSet rs = mSelectOneUser.executeQuery();

            if(rs.next()) { //user was found
                //store user id, name, username, email, gender identity, sexual orientation, note, active boolean, and creation date
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String gender_identity = rs.getString("gender_identity");
                String sexual_orientation = rs.getString("sexual_orientation");
                String note = rs.getString("note");
                boolean active = rs.getBoolean("active");
                Date created = rs.getDate("created");

                rs.close();
                return new UserRow(id, name, username, email, gender_identity, sexual_orientation, note, active, created);
            } else { //user not found?
                rs.close();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    UserRow getOneUserByEmail(String email) {
        try {
            //configure and execute query
            mSelectOneUserByEmail.setString(1, email);
            ResultSet rs = mSelectOneUserByEmail.executeQuery();

            if(rs.next()) { //user was found
                //store user id, name, username, email, gender identity, sexual orientation, note, active boolean, and creation date
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String username = rs.getString("username");
                //String email = rs.getString("email");
                String gender_identity = rs.getString("gender_identity");
                String sexual_orientation = rs.getString("sexual_orientation");
                String note = rs.getString("note");
                boolean active = rs.getBoolean("active");
                Date created = rs.getDate("created");

                rs.close();
                return new UserRow(id, name, username, email, gender_identity, sexual_orientation, note, active, created);
            } else { //user not found?
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
     * @return Count of votes on post (likes - dislikes) or 0 on error (IS THERE A BETTER ERROR RETURN VALUE?)
     */
    int getVoteCount(int post_id) {
        try {
            int vcount = 0;
            //configure and execute query
            mSelectVotes.setInt(1, post_id);
            ResultSet rs = mSelectVotes.executeQuery();
            while (rs.next()) {
                boolean liked = rs.getBoolean("liked");
                //increment or decrement vote counter
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
            return 0; //IS THERE SOMETHING BETTER TO RETURN HERE?
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
            //configure and execute query
            mSelectComments.setInt(1, post_id);
            ResultSet rs = mSelectComments.executeQuery();
            while (rs.next()) {
                //store id, poster id, comment content, active boolean, and creation date
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                String content = rs.getString("content");
                boolean active = rs.getBoolean("active");
                Date created = rs.getDate("created");
                res.add(new CommentRow(id, user_id, post_id, content, active, created));
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
            while (rs.next()) {//REPLACE WITH GETONEPOST?
                //store post id, poster id, and post content
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                String content = rs.getString("content");

                //calculate vote count
                int votes = getVoteCount(id);
                //create arraylist of comments
                ArrayList<CommentRow> comments = getAllCommentsOnPost(id);

                //store active boolean and creation date
                boolean active = rs.getBoolean("active");
                Date created = rs.getDate("created");
                res.add(new PostRow(id, user_id, content, votes, comments, active, created));
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
            //configure and execute query
            mSelectOnePost.setInt(1, post_id);
            ResultSet rs = mSelectOnePost.executeQuery();

            if(rs.next()) { //post was found
                //store post id, user id, and post content
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                String content = rs.getString("content");

                //calculate vote count
                int votes = getVoteCount(post_id);
                //create arraylist of comments
                ArrayList<CommentRow> comments = getAllCommentsOnPost(post_id);

                //store active boolean and creation date
                boolean active = rs.getBoolean("active");
                Date created = rs.getDate("created");

                rs.close();
                return new PostRow(id, user_id, content, votes, comments, active, created);
            } else { //post not found?
                rs.close();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


//CHECK RETURN VALUES FOR INSERT AND UPDATE POSTGRES - is it row updated?

    int createUser(UserRow new_user) {
        try {
            //set user information
            mInsertNewUser.setString(1, new_user.name);
            mInsertNewUser.setString(2, new_user.username);
            mInsertNewUser.setString(3, new_user.email);
            mInsertNewUser.setString(4, new_user.gender_identity);
            mInsertNewUser.setString(5, new_user.sexual_orientation);
            mInsertNewUser.setString(6, new_user.note);

            //execute query and return user id
            ResultSet rs = mInsertNewUser.executeQuery();

            if(rs.next()) { //user was created
                int id = rs.getInt("id");

                rs.close();
                return id;
            } else { //user not created?
                rs.close();
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /*
     * Create new file
     * 
     * 
     */
 
//     int createFile(String b64) throws IOException{
//         byte[] decoded = Base64.decodeBase64(b64);
//         FileOutputStream fos = null;
//         fos = new FileOutputStream("C:\\output\\image.jpg");
//         fos.write(decoded);
//         fos.close();

        
//         GoogleCredentials credentials = GoogleCredentials.fromStream()
//         .createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
//         HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
//         credentials);

//         // Build a new authorized API client service.
//         Drive service = new Drive.Builder(new NetHttpTransport(),
//         GsonFactory.getDefaultInstance(),
//         requestInitializer)
//         .setApplicationName("Drive samples")
//         .build();
//     // Upload file photo.jpg on drive.
//     File fileMetadata = new File();
//     fileMetadata.setName("photo.jpg");
//     // File's content.
//     java.io.File filePath = new java.io.File("files/photo.jpg");
//     // Specify media type and file-path for file.
//     FileContent mediaContent = new FileContent("image/jpeg", filePath);
//     try {
//       File file = service.files().create(fileMetadata, mediaContent)
//           .setFields("id")
//           .execute();
//       System.out.println("File ID: " + file.getId());
//       return file.getId();
//     } catch (GoogleJsonResponseException e) {
//       // TODO(developer) - handle error appropriately
//       System.err.println("Unable to upload file: " + e.getDetails());
//       throw e;
//     }
//   }


     

     
    /**
     * Create new post
     * 
     * @param user_id Id of user creating post
     * @param content String content of post
     * 
     * @return res?
     * @throws IOException
     */
    int createPost(int user_id, String content, String b64) throws IOException {
        try {
            mInsertNewPost.setInt(1, user_id);
            mInsertNewPost.setString(2, content);
            //int filepost = createFile(b64);
            int res = mInsertNewPost.executeUpdate(); //check for errors? should this be executeUpdate or something else?
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; //IS THERE SOMETHING BETTER TO RETURN HERE?
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
     * @param content New content of comment
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

    int editProfile(int user_id, UserRow user_data) {
        try {
            //set updated information
            mUpdateUser.setString(1, user_data.name);
            mUpdateUser.setString(2, user_data.username);
            mUpdateUser.setString(3, user_data.email);
            mUpdateUser.setString(4, user_data.gender_identity);
            mUpdateUser.setString(5, user_data.sexual_orientation);
            mUpdateUser.setString(6, user_data.note);

            mUpdateUser.setInt(7, user_id);

            int res = mUpdateUser.executeUpdate();
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
            //configure and execute query
            mSelectOneVote.setInt(1, user_id);
            mSelectOneVote.setInt(2, post_id);
            ResultSet rs = mSelectOneVote.executeQuery();

            if(rs.next()) { //vote was found
                boolean liked = rs.getBoolean("liked");
                Date created = rs.getDate("created");

                rs.close();
                return new VoteRow(user_id, post_id, liked, created);
            } else { //vote not found
                rs.close();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Vote on a post
     * 
     * @param user_id Id of the user who created the vote
     * @param post_id Id of the post the vote is on
     * @param liked Boolean indicating upvote or downvote
     * 
     * @return res?
     */
    int voteOnPost(int user_id, int post_id, boolean liked) {
        try {
            int res = -1;
            //check if vote already exists
            VoteRow oldVote = getVote(user_id, post_id);
            if (oldVote == null) {
                //create new vote
                mInsertVote.setInt(1, user_id);
                mInsertVote.setInt(2, post_id);
                mInsertVote.setBoolean(3, liked);
                res = mInsertVote.executeUpdate();
            } else if (oldVote.liked == liked) {
                //delete vote
                mDeleteVote.setInt(1, user_id);
                mDeleteVote.setInt(2, post_id);
                res = mDeleteVote.executeUpdate();
            } else if (oldVote.liked != liked) {
                //update vote
                mUpdateVote.setBoolean(1, liked);
                mUpdateVote.setInt(2, user_id);
                mUpdateVote.setInt(3, post_id);
                res = mUpdateVote.executeUpdate();
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    int updateUserProfile() {
        return -1;
    }

}
