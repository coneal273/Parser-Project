package edu.lehigh.cse216.gitgood.backend;


//xMemcached imports
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import java.lang.InterruptedException;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

//java data structure imports
import java.util.Map;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;


import java.util.Collections;

// Import the Spark package, so that we can make use of the "get" function to 
// create an HTTP GET route
import spark.Spark;

// Import Google's JSON library
import com.google.gson.*;

//import google oauth2 libraries
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

//import row objects
import edu.lehigh.cse216.gitgood.backend.rows.*;
import io.grpc.Context.Storage;
//import request objects
import edu.lehigh.cse216.gitgood.backend.requests.*;

/**
 * For now, our app creates an HTTP server that can only get and add data.
 */
public class App {
    public static void main(String[] args) {

        // gson provides us with a way to turn JSON into objects, and objects
        // into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe. See
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();
        // dataStore holds all of the data that has been provided via HTTP
        // requests
        //
        // NB: every time we shut down the server, we will lose all data, and 
        //     every time we start the server, we'll have an empty dataStore,
        //     with IDs starting over from 0.
        //final DataStore dataStore = new DataStore();
        
        //Memcached implementation
        List<InetSocketAddress> servers =
        AddrUtil.getAddresses(System.getenv("mc2.c1.nyc1.do.memcachier.com:11211").replace(",", " "));
      AuthInfo authInfo =
        AuthInfo.plain(System.getenv("C4B001"),
                       System.getenv("989AB7019CB0463B3D67762641FD9A02"));
  
      MemcachedClientBuilder builder = new XMemcachedClientBuilder(servers);
  
      // Configure SASL auth for each server
      for(InetSocketAddress server : servers) {
        builder.addAuthInfo(server, authInfo);
      }
  
      // Use binary protocol
      builder.setCommandFactory(new BinaryCommandFactory());
      // Connection timeout in milliseconds (default: )
      builder.setConnectTimeout(1000);
      // Reconnect to servers (default: true)
      builder.setEnableHealSession(true);
      // Delay until reconnect attempt in milliseconds (default: 2000)
      builder.setHealSessionInterval(2000);
  
      try {
        MemcachedClient mc = builder.build();
        try {
          mc.set("foo", 0, "bar");
          String val = mc.get("foo");
          System.out.println(val);
        } catch (TimeoutException te) {
          System.err.println("Timeout during set or get: " +
                             te.getMessage());
        } catch (InterruptedException ie) {
          System.err.println("Interrupt during set or get: " +
                             ie.getMessage());
        } catch (MemcachedException me) {
          System.err.println("Memcached error during get or set: " +
                             me.getMessage());
        }
      } catch (IOException ioe) {
        System.err.println("Couldn't create a connection to MemCachier: " +
                           ioe.getMessage());
      }
    
  

        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL"); // ?sslmode=require
        //db_url = "postgres://atzgtjalvsgtyq:919890037bdcb834a3360045d6cedd5c2e24009cb2d70d4bd7bee20fe27500c9@ec2-18-207-37-30.compute-1.amazonaws.com:5432/d935tnfa5i5gku";
        db_url = "postgres://ddemejuv:1H80SFvzKsgTqQI9SGbkWrTranlVOkrX@peanut.db.elephantsql.com:5432/ddemejuv";
        Database db = Database.getDatabase(db_url);

        // Get the port on which to listen for requests
        Spark.port(getIntFromEnv("PORT", 4567));

        // Set up the location for serving static files. If the STATIC_LOCATION
        // environment variable is set, we will serve from it. Otherwise, serve
        // from "/web"
        String static_location_override = System.getenv("STATIC_LOCATION");
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        }

        // Set up a route for serving the main page
        Spark.get("/", (req, res) -> {
            res.redirect("/index.html"); // have to change i think
            return "";
        });

        String cors_enabled = env.get("CORS_ENABLED");

        if ("True".equalsIgnoreCase(cors_enabled)) {
            final String acceptCrossOriginRequestsFrom = "*";
            final String acceptedCrossOriginRoutes = "GET,PUT,POST,DELETE,OPTIONS";
            final String supportedRequestHeaders = "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin";
            enableCORS(acceptCrossOriginRequestsFrom, acceptedCrossOriginRoutes, supportedRequestHeaders);
        }

        // SPARK ROUTES ------------------------------------------------------------


        
        //SPARK ROUTES ------------------------------------------------------------

        //get all users - commented out for security?
        // Spark.get("/users", (request, response) -> {
        // response.status(200);
        // response.type("application/json");
        // ArrayList<UserRow> data = db.getAllUsers();
        // return gson.toJson(new StructuredResponse("OK", null, data));
        // });


        

        //get one user profile
        Spark.get("/users/:id", (request, response) -> {
            response.status(200);
            response.type("application/json");
            int id = Integer.parseInt(request.params("id"));
            UserRow data = db.getOneUser(id);
            return gson.toJson(new StructuredResponse("OK", null, data));
        });

        // get all posts
        Spark.get("/posts", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            ArrayList<PostRow> data = db.getAllPosts();
            return gson.toJson(new StructuredResponse("ok", null, data));
        });

        // get one post
        Spark.get("/posts/:id", (request, response) -> {
            response.status(200);
            response.type("application/json");
            int id = Integer.parseInt(request.params("id"));
            String content = (request.params("content"));
            String b64 = (request.params("b64"));
            PostRow data = db.getOnePost(id);
        

            if (data == null) {
                return gson.toJson(new StructuredResponse("error", id + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        // create new post
        Spark.post("/posts", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            PostRequest req = gson.fromJson(request.body(), PostRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int user_id = req.user_id;
            String content = req.content;
            String b64 = req.b64;
            int newId = db.createPost(user_id, content, b64);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        // vote on post
        Spark.post("/posts/:post_id/votes", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            VoteRequest req = gson.fromJson(request.body(), VoteRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");

            int user_id = req.user_id;
            int post_id = Integer.parseInt(request.params("post_id"));
            boolean liked = req.liked;

            int result = db.voteOnPost(user_id, post_id, liked);

            if (result == -1) {
                return gson.toJson(new StructuredResponse("ERROR", "Error voting on post", null));
            } else {
                return gson.toJson(new StructuredResponse("OK", null, null));
            }
        });

        // comment on post
        Spark.post("/posts/:post_id/comments", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            CommentRequest req = gson.fromJson(request.body(), CommentRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int user_id = req.user_id;
            int post_id = Integer.parseInt(request.params("post_id"));
            String content = req.content;
            int newId = db.createComment(user_id, post_id, content);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error creating comment", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        // edit comment on post - MAKE IT SO HAS TO BE SAME USER EDITING?
        Spark.patch("/posts/:post_id/comments/:comment_id", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            CommentRequest req = gson.fromJson(request.body(), CommentRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");

            String content = req.content;
            int comment_id = Integer.parseInt(request.params("comment_id"));
            int result = db.editComment(comment_id, content);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("ERROR", "unable to update comment " + comment_id, null));
            } else {
                return gson.toJson(new StructuredResponse("OK", "updated comment " + comment_id, null));
            }
        });

        //update user profile
        Spark.patch("/users/:user_id", (request, response) -> {
            UserRequest req = gson.fromJson(request.body(), UserRequest.class);

            response.status(200);
            response.type("application/json");

            int user_id = Integer.parseInt(request.params("user_id"));
            int id = req.id;
            
            if (user_id != id) {
                return gson.toJson(new StructuredResponse("ERROR", "Cannot edit other user profiles", null));
            } else {
                UserRow user_data = new UserRow(req.id, req.name, req.username, req.email, req.gender_identity, req.sexual_orientation, req.note, req.active, null);
                int result = db.editProfile(user_id, user_data);
                if (result == -1) {
                    return gson.toJson(new StructuredResponse("ERROR", "Unable to update profile", null));
                } else {
                    return gson.toJson(new StructuredResponse("OK", "Updated profile", null));
                }
            }            
        });

        // login user?
        Spark.post("/login", (request, response) -> {
            response.status(200);
            response.type("application/json");

            // get idTokenString from client
            LoginRequest req = gson.fromJson(request.body(), LoginRequest.class);
            String idTokenString = (String) req.idTokenString;

            // should this be one id for the heroku site or an id for each user??
            // set up getting from env variables
            String CLIENT_ID = "830605994462-rokgojp9krkqijhetf72mfm1r9gq0rqc.apps.googleusercontent.com";

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                    new GsonFactory())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = null;

            try {
                idToken = verifier.verify(idTokenString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (idToken != null) {
                Payload payload = idToken.getPayload();

                // get user id
                String userId = payload.getSubject();
                System.out.println("User ID: " + userId);

                // check hosted domain for only lehigh.edu emails
                String hd = payload.getHostedDomain();

                // get profile information
                String email = payload.getEmail();
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                String locale = (String) payload.get("locale");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");

                System.out.println(String.format(
                        "Userid: %s, HostedDomain: %s, Email: %s, verified: %s, name: %s, pictureurl: %s, locale: %s, familyName: %s, givenName: %s",
                        userId, hd, email, emailVerified, name, pictureUrl, locale, familyName, givenName));
                // return profile information?

                // save the user ID and a random session key into a local hash table

                //insert new user into database
                UserRow res = db.getOneUserByEmail(email);
                if (res != null) { //user already exists
                    return gson.toJson(new StructuredResponse("OK", null, res));
                } else { //user doesn't exist
                    //add new user to database
                    UserRow new_user = new UserRow(0, name, "", email, "", "", "", true, null);
                    int result = db.createUser(new_user);
                    if (result == -1) {
                        return gson.toJson(new StructuredResponse("ERROR", "Unable to create new user", null));
                    } else {
                        return gson.toJson(new StructuredResponse("OK", "User logged in", result));
                    }
                }

                //return gson.toJson(new StructuredResponse("OK", null, null));
            } else {
                System.out.println("Invalid ID token.");
                return gson.toJson(new StructuredResponse("ERROR", "Invalid ID Token", null));
            }
        });

        /*
         * After you receive the ID token by HTTPS POST, you must verify the integrity
         * of the token.
         * 
         * To verify that the token is valid, ensure that the following criteria are
         * satisfied:
         * 
         * The ID token is properly signed by Google. Use Google's public keys
         * (available in JWK or PEM format) to verify the token's signature. These keys
         * are regularly rotated; examine the Cache-Control header in the response to
         * determine when you should retrieve them again.
         * The value of aud in the ID token is equal to one of your app's client IDs.
         * This check is necessary to prevent ID tokens issued to a malicious app being
         * used to access data about the same user on your app's backend server.
         * The value of iss in the ID token is equal to accounts.google.com or
         * https://accounts.google.com.
         * The expiry time (exp) of the ID token has not passed.
         * If you want to restrict access to only members of your G Suite domain, verify
         * that the ID token has an hd claim that matches your G Suite domain name.
         * 
         * Rather than writing your own code to perform these verification steps, we
         * strongly recommend using a Google API client library for your platform, or a
         * general-purpose JWT library. For development and debugging, you can call our
         * tokeninfo validation endpoint.
         */

    }

    /**
     * Get an integer environment varible if it exists, and otherwise return the
     * default value.
     * 
     * @envar The name of the environment variable to get.
     * @defaultVal The integer value to use as the default if envar isn't found
     * 
     * @returns The best answer we could come up with for a value for envar
     */
    static int getIntFromEnv(String envar, int defaultVal) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(envar) != null) {
            return Integer.parseInt(processBuilder.environment().get(envar));
        }
        return defaultVal;
    }

    /**
     * Set up CORS headers for the OPTIONS verb, and for every response that the
     * server sends. This only needs to be called once.
     * 
     * @param origin  The server that is allowed to send requests to this server
     * @param methods The allowed HTTP verbs from the above origin
     * @param headers The headers that can be sent with a request from the above
     *                origin
     */
    private static void enableCORS(String origin, String methods, String headers) {
        // Create an OPTIONS route that reports the allowed CORS headers and methods
        Spark.options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        // 'before' is a decorator, which will run before any
        // get/post/put/delete. In our case, it will put three extra CORS
        // headers into the response
        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
        });
    }

}
