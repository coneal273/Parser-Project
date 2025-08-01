package edu.lehigh.cse216.gitgood.backend.rows;

import java.util.Date;

/**
 * VoteRow holds a row of information for a vote. This information consists of
 * a user id, a post id, a liked boolean to indicate an upvote or downvote, and
 * a creation date
 */
public class VoteRow {
    /**
     * Unique identifier associated with the user who created this vote
     */
    public final int user_id;

    /**
     * Unique identifier associated with the post this vote is on
     */
    public final int post_id;

    /**
     * A boolean to indicate an upvote or downvote
     */
    public boolean liked;

    /**
     * The creation date for this vote
     */
    public Date created;

    /**
     * Create a new VoteRow with the provided vote information
     * 
     * @param user_id The id associated with the user who created this vote
     * @param post_id The id associated with the post this vote is on
     * @param liked The boolean indicating whether this is an upvote or downvote
     * @param created The creation date of this vote
     */
    public VoteRow(int user_id, int post_id, boolean liked, Date created) {
        this.user_id = user_id;
        this.post_id = post_id;
        this.liked = liked;
        this.created = created;
    }
}
