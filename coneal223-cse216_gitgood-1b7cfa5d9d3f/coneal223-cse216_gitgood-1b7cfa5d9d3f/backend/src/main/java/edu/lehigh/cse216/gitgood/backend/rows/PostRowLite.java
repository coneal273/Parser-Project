package edu.lehigh.cse216.gitgood.backend.rows;

import java.util.Date;

/**
 * PostRowLite holds a subset of the information in a PostRow. This includes
 * a post id, a poster id, a string for the post text, a boolean for whether
 * the post is active or disabled, and a creation date. Does not include
 * the count of votes on the post or an array of comments on the post.
 */
public class PostRowLite {
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
     * A boolean to indicate if the post is active or disabled
     */
    public boolean active;

    /**
     * The creation date for this post
     */
    public final Date created;

    /**
     * Create a new PostRowLite object by copying fields from a PostRow
     * 
     * @param post The PostRow object to copy fields from
     */
    public PostRowLite(PostRow post) {
        this.id = post.id;
        this.user_id = post.user_id;
        this.content = post.content;
        this.active = post.active;
        this.created = post.created;
    }
    
}
