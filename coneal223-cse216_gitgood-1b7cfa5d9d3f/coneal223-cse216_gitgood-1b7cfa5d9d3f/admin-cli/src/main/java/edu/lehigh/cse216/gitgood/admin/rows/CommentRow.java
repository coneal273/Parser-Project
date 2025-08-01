package edu.lehigh.cse216.gitgood.admin.rows;

/**
 * CommentRow holds a row of information for a comment. This information
 * consists of
 * a comment id, a poster id, a post id, a string for the comment text, a
 * boolean for
 * whether the comment is active or disabled, and a creation date
 */
public class CommentRow {
    /**
     * Unique identifier associated with this comment
     */
    public final int id;

    /**
     * Identifier associated with the user who created this comment
     */
    public final int user_id;

    /**
     * Identifier associated with the post this comment is associated with
     */
    public final int post_id;

    /**
     * The text for the comment
     */
    public String content;

    /**
     * A boolean to indicate if the comment is active or disabled
     */
    public boolean active;

    public String file_id;

    public final String created;

    /**
     * Create a new CommentRow with the provided comment information
     * 
     * @param id      The id to associate with this comment, unique
     * @param user_id The id associated with the user who created the comment
     * @param post_id The id associated with the post this comment is associated to
     * @param content The text string for this comment
     * @param active  The boolean indicating if this comment is active or disabled
     * @param created The creation date of this comment
     */
    public CommentRow(int id, int user_id, int post_id, String content, boolean active,
            String file_id, String created) {
        this.id = id;
        this.user_id = user_id;
        this.post_id = post_id;
        this.content = content;
        this.active = active;
        this.file_id = file_id;
        this.created = created;
    }

    /**
     * Copy constructor to create a CommentRow from another
     * 
     * @param comment The CommentRow object to copy
     */
    public CommentRow(CommentRow comment) {
        this.id = comment.id;
        this.user_id = comment.user_id;
        this.post_id = comment.post_id;
        this.content = comment.content;
        this.active = comment.active;
        this.created = comment.created;
    }
}
