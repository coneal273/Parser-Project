package edu.lehigh.cse216.gitgood.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

//import java.util.ArrayList;
import java.util.Map;
import edu.lehigh.cse216.gitgood.admin.rows.*;

/**
 * App is our basic admin app. For now, it is a demonstration of the six key
 * operations on a database: connect, insert, update, query, delete, disconnect
 */
public class App {

    /**
     * Print the menu for our program
     */
    static void menu() {
        System.out.println("Main Menu");
        System.out.println("  [T] Create a Table");
        System.out.println("  [D] Drop a Table");
        System.out.println("  [+] Insert a new row");
        System.out.println("  [-] Delete a row");
        System.out.println("  [VU] Validate a User");
        System.out.println("  [IVU] Invalidate a User");
        System.out.println("  [VP] Validate a Post");
        System.out.println("  [IVP] Invalidate a Post");
        System.out.println("  [VC] Validate a Comment");
        System.out.println("  [IVC] Invalidate a Comment");
        System.out.println("  [P] View data on specific post");
        System.out.println("  [PA] View data in all posts");
        System.out.println("  [U] View data on specific user");
        System.out.println("  [UA] View data in all users");
        System.out.println("  [F] Get one file by id");
        System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help (this message)");
    }

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * 
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(BufferedReader in) {
        // The valid actions:
        String actions = "TD1*-+~q?";

        // We repeat until a valid single-character option is selected
        while (true) {
            System.out.print("Enter a command or enter ? for command menu: ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    /**
     * Ask the user to enter a String message
     * 
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The string that the user provided. May be "".
     */
    static String getString(BufferedReader in, String message) {
        String s;
        try {
            System.out.print(message + " :> ");
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }

    /**
     * Ask the user to enter an integer
     * 
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The integer that the user provided. On error, it will be -1
     */
    static int getInt(BufferedReader in, String message) {
        int i = -1;
        try {
            System.out.print(message + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * The main routine runs a loop that gets a request from the user and
     * processes it
     * 
     * @param argv Command-line options. Ignored by this program.
     */
    public static void main(String[] argv) {
        // get the Postgres configuration from the environment
        Map<String, String> env = System.getenv();
        // String ip = env.get("POSTGRES_IP");
        // String port = env.get("POSTGRES_PORT");
        // String user = env.get("POSTGRES_USER");
        // String pass = env.get("POSTGRES_PASS");

        // Get a fully-configured connection to the database, or exit
        // immediately
        String url = "postgres://atzgtjalvsgtyq:919890037bdcb834a3360045d6cedd5c2e24009cb2d70d4bd7bee20fe27500c9@ec2-18-207-37-30.compute-1.amazonaws.com:5432/d935tnfa5i5gku";
        Database db = Database.getDatabase(url);
        if (db == null)
            return;

        // Start our basic command-line interpreter:
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Get the user's request, and do it

            // NB: for better testability, each action should be a separate
            // function call
            String action = getString(in, "enter an action: ");
            if (action.equals("?")) {
                menu();
                // quit
            } else if (action.equals("q")) {
                break;
                // invalidate user
            } else if (action.equals("IVU")) {
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                int res = db.updateValidationForUser(id, false);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
                // validate user
            } else if (action.equals("VU")) {
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                int res = db.updateValidationForUser(id, true);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
                // create table
            } else if (action.equals("IVP")) {
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                int res = db.updateValidationForPost(id, false);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
                // validate user
            } else if (action.equals("VP")) {
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                int res = db.updateValidationForPost(id, true);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
                // create table
            } else if (action.equals("IVC")) {
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                int res = db.updateValidationForComment(id, false);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
                // validate user
            } else if (action.equals("VC")) {
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                int res = db.updateValidationForComment(id, true);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
                // create table
            } else if (action.equals("F")) {
                int id = getInt(in, "Enter the file ID :> ");
                if (id == -1)
                    continue;
                FileRow res = db.getOneFile(id);
                if (res == null)
                    continue;
                System.out
                        .println(" id: " + res.id + " url: " + res.url + " created: " + res.created + " rows updated");
                // create table
            } else if (action.equals("T")) {
                String create = getString(in,
                        "what kind of table would you like to create \n [P] Create a Posts Table \n [V] Create a Votes Table \n [C] Create a Comments Table \n [U] Create a Users Table\n");
                if (create.equals("P")) {
                    db.createPostsTable();
                } else if (create.equals("V")) {
                    db.createVotesTable();
                } else if (create.equals("C")) {
                    db.createCommentsTable();
                } else if (create.equals("U")) {
                    db.createUsersTable();
                }
                // drop table
            } else if (action.equals("D")) {
                String create = getString(in,
                        "what table would you like to drop \n [P] Posts Table \n [V] Votes Table \n [C] Comments Table \n [U] Users Table\n");
                if (create.equals("P")) {
                    db.dropPostsTable();
                } else if (create.equals("V")) {
                    db.dropVotesTable();
                } else if (create.equals("C")) {
                    db.dropCommentsTable();
                } else if (create.equals("U")) {
                    db.dropUsersTable();
                }
                // delete a row
            } else if (action.equals("-")) {
                String create = getString(in,
                        "what table would you like to delete a row from \n [P] Posts Table \n [V] Votes Table \n [C] Comments Table \n [U] Users Table\n");
                // delete a post row
                if (create.equals("P")) {
                    int id = getInt(in, "Enter the row ID");
                    if (id == -1)
                        continue;
                    int res = db.deletePost(id);
                    if (res == -1)
                        continue;
                    System.out.println("  " + res + " rows deleted");
                    // delete a votes row
                } else if (create.equals("V")) {
                    int id = getInt(in, "Enter the row ID");
                    if (id == -1)
                        continue;
                    int res = db.deleteVote(id);
                    if (res == -1)
                        continue;
                    System.out.println("  " + res + " rows deleted");
                    // delete comments row
                } else if (create.equals("C")) {
                    int id = getInt(in, "Enter the row ID");
                    if (id == -1)
                        continue;
                    int res = db.deleteComment(id);
                    if (res == -1)
                        continue;
                    System.out.println("  " + res + " rows deleted");
                    // delete user row
                } else if (create.equals("U")) {
                    int id = getInt(in, "Enter the row ID");
                    if (id == -1)
                        continue;
                    int res = db.deleteUser(id);
                    if (res == -1)
                        continue;
                    System.out.println("  " + res + " rows deleted");
                }
                // insert a row
            } else if (action.equals("+")) {
                String table = getString(in,
                        "what table would you like to add a row to \n [P] Posts Table \n [V] Votes Table \n [C] Comments Table \n [U] Users Table\n");
                // insert a post
                if (table.equals("P")) {
                    int poster = getInt(in, "Enter poster id");
                    String message = getString(in, "Enter the message");
                    int valid = getInt(in, "Enter 1 if idea is validated or 0 if idea is invalidated");
                    String file_id = getString(in, "Enter file id. Enter nothing if not providing file");
                    String created = getString(in, "Enter time created.");
                    int res = db.insertPostRow(poster, message, valid, file_id, created);
                    System.out.println(res + " rows added");
                    // insert a vote row
                } else if (table.equals("V")) {
                    int voter = getInt(in, "Enter user id");
                    int post = getInt(in, "Enter the post Id");
                    int liked = getInt(in, "Was this post liked by the voter");
                    int res = db.insertVoteRow(voter, post, liked);
                    System.out.println(res + " rows added");
                    // insert a comment row
                } else if (table.equals("C")) {
                    int poster = getInt(in, "Enter user id");
                    int post = getInt(in, "Enter the post Id");
                    String message = getString(in, "Enter comment");
                    String file_id = getString(in, "Enter file id. Enter nothing if not providing file");
                    int res = db.insertCommentRow(poster, post, message, file_id);
                    System.out.println(res + " rows added");
                    // insert a user row
                } else if (table.equals("U")) {
                    String name = getString(in, "Enter name");
                    String username = getString(in, "Enter username");
                    String email = getString(in, "Enter email");
                    String gi = getString(in,
                            "Enter gender identity \n [M] Male \n [F] Female \n [TM] Transgender man/trans man \n [TW] Transgender woman/trans woman \n [G] Genderqueer/gender nonconforming neither exclusively male nor female \n [O] Additional gender category (or other) \n [D] Decline to answer\n");
                    if (gi.equals("M")) {
                        gi = "Male";
                    }
                    if (gi.equals("F")) {
                        gi = "Female";
                    }
                    if (gi.equals("TM")) {
                        gi = "Trans Male";
                    }
                    if (gi.equals("TW")) {
                        gi = "Trans Female";
                    }
                    if (gi.equals("G")) {
                        gi = "Genderqueer";
                    }
                    if (gi.equals("O")) {
                        gi = getString(in, "Please specify gender identity: ");
                    }
                    if (gi.equals("D")) {
                        gi = "Declined to answer";
                    }
                    String so = getString(in,
                            "Enter sexual orientation \n [S] Straight or heterosexual \n [G] Gay \n [L] Lesbian \n [B] Bisexual \n [Q] Queer \n [P] Pansexual \n [O] Other \n [D] Decline to answer\n");
                    if (so.equals("S")) {
                        so = "Straight";
                    }
                    if (so.equals("G")) {
                        so = "Gay";
                    }
                    if (so.equals("L")) {
                        so = "Lesbian";
                    }
                    if (so.equals("B")) {
                        so = "Bisexual";
                    }
                    if (so.equals("Q")) {
                        so = "Queer";
                    }
                    if (so.equals("P")) {
                        so = "Pansexual";
                    }
                    if (so.equals("O")) {
                        so = getString(in, "Please specify sexual orientation: ");
                    }
                    if (so.equals("D")) {
                        so = "Declined to answer";
                    }
                    String note = getString(in, "Enter note");
                    String valid = getString(in, "Enter T if user is validated or F if user is invalidated");
                    String created = getString(in, "Enter created time");
                    boolean validated = true;
                    if (valid.equals("T")) {
                        validated = true;
                    }
                    if (valid.equals("F")) {
                        validated = false;
                    }
                    int res = db.insertUserRow(name, username, email, gi, so, note, validated, created);
                    System.out.println(res + " rows added");
                }
                // get specific post row
            } else if (action.equals("P")) {
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                PostRow res = db.getOnePost(id);
                if (res != null) {
                    System.out.println("  [" + res.id + "] " + res.user_id + " --> " + res.content);
                }
                // get specific user row
            } else if (action.equals("U")) {
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                UserRow res = db.getOneUser(id);
                if (res != null) {
                    System.out.println("  [" + res.id + "] " + res.name + "  " + res.username + "  " + res.email + "  "
                            + res.gender_identity + "  " + res.sexual_orientation + "  " + res.note + "  "
                            + res.validated + " " + res.created + "  ");
                }
                // get all post rows
            } else if (action.equals("PA")) {
                ArrayList<PostRow> res = db.getAllPosts();
                if (res == null)
                    continue;
                System.out.println("  Current Database Contents");
                System.out.println("  -------------------------");
                for (PostRow rd : res) {
                    System.out.println("  [" + rd.id + "] " + rd.user_id + " --> " + rd.content);
                }
                // get all user rows
            } else if (action.equals("UA")) {
                ArrayList<UserRow> res = db.getAllUsers();
                if (res == null)
                    continue;
                System.out.println("  Current Database Contents");
                System.out.println("  -------------------------");
                for (UserRow rd : res) {
                    System.out.println("  [" + rd.id + "] " + rd.name + "  " + rd.username + "  " + rd.email + "  "
                            + rd.gender_identity + "  " + rd.sexual_orientation + "  " + rd.note + "  "
                            + rd.validated + " " + rd.created + "  ");
                }
            }

        }
        db.disconnect();

    }
}