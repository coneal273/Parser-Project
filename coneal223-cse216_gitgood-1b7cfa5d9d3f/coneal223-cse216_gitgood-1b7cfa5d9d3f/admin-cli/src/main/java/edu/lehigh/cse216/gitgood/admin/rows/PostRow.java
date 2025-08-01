package edu.lehigh.cse216.gitgood.admin.rows;

import java.util.ArrayList;
import java.util.Date;

/**
 * PostRow holds a row of information for a post. This information consists of
 * a post id, a poster id, a string for the post text, a count of the votes on
 * the post, an array of comments on the post, a boolean for whether the post is
 * active or disabled, and a creation date
 */
public class PostRow {
    /**
     * Unique identifier associated with the post
     */
    public final int id;

    /**
     * Unique identifier associated with the user who created the post
     */
    public final int user_id;

    /**
     * The text for the post
     */
    public String content;

    /**
     * The vote count on the post
     */
    public int votes;

    public boolean validated;

    public String file_id;

    public String link;

    public String created;

    /**
     * Create a new PostRow with the provided post information
     * 
     * @param id       The id to associate with this post, unique
     * @param user_id  The id associated with the user who created the post
     * @param content  The text string for this post
     * @param votes    The count of votes on this post
     * @param comments The array of comments for this post
     * @param valid    The boolean indicating if this post is active or disabled
     */
    public PostRow(int id, int user_id, String content, int votes, boolean valid, String file_id, String created) {
        this.id = id;
        this.user_id = user_id;
        this.content = content;
        this.votes = votes;
        this.validated = valid;
        this.file_id = file_id;
        this.created = created;
    }

    /**
     * Copy constructor to create a PostRow from another
     * 
     * @param post The PostRow object to copy
     */
    public PostRow(PostRow post) {
        this.id = post.id;
        this.user_id = post.user_id;
        this.content = post.content;
        this.votes = post.votes;
        this.validated = post.validated;
        this.file_id = post.file_id;
        this.created = created;

    }

}
