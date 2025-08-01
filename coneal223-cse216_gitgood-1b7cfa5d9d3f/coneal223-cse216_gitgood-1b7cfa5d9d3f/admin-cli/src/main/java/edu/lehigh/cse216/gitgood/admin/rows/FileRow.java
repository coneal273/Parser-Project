package edu.lehigh.cse216.gitgood.admin.rows;

/**
 * FileRow holds a row of information for a file. This information
 * consists of
 * a file id, a url, a string for the created date
 */
public class FileRow {
    /**
     * Unique identifier associated with this file
     */
    public final int id;

    /**
     * Identifier associated with the url for the file
     */
    public final String url;

    /**
     * Identifier associated with the time created
     */
    public final String created;

    /**
     * Create a new FileRow with the provided file information
     * 
     */

    public FileRow(int id, String url, String created) {
        this.id = id;
        this.url = url;
        this.created = created;
    }
}
