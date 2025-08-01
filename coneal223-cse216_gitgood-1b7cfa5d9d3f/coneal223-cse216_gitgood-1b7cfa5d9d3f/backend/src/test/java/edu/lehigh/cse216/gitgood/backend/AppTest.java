package edu.lehigh.cse216.gitgood.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */


    /**
     * tests the DataRow constructor 
     */
    public void testConstructor() {
        String content = "Test Content";
        int likes = 0;
        int id = 20;

        // DataRow d = new DataRow(id, content, likes);
        // assertTrue(d.mContent.equals(content));
        // assertTrue(d.mLikeCount == likes);
        // assertTrue(d.mId == id);
    }
    /**
     * tests the DataRow copy constructor 
     */
    public void testCopyConstructor() {
        String content = "Test Content for Copy";
        int likes = 0;
        int id = 22;

    }

    //     DataRow d = new DataRow(id, content, likes);
    //     DataRow d2 = new DataRow(d);

    //     assertTrue(d2.mContent.equals(d.mContent));
    //     assertTrue(d2.mLikeCount == d.mLikeCount);
    //     assertTrue(d2.mId == d.mId);
    // }


    String db_url = "postgres://atzgtjalvsgtyq:919890037bdcb834a3360045d6cedd5c2e24009cb2d70d4bd7bee20fe27500c9@ec2-18-207-37-30.compute-1.amazonaws.com:5432/d935tnfa5i5gku";
    Database db = Database.getDatabase(db_url);

    // /**
    //  * tests the insert row in Database.java
    //  */
    // public void testInsertRow() {
    //     String content = "Testing";
    //     assertTrue(this.db.insertRow(content)==1);
    // }

    /**
     * tests the select all in Database.java
     */
    // public void testSelectAll() {
    //     assertTrue(this.db.selectAll()!=null);
    // }

    /**
     * tests the select one in Database.java
     * UNFINISHED: not sure how to select by ID 
     */
    // public void testSelectOne() {
    //     assertTrue(this.db.selectOne(id)!=null);
    // }

    /**
     * tests the delete row in Database.java
     * UNFINISHED: not sure how to select by ID 
     */
    // public void testDeleteRow() {
    //     assertTrue(this.db.deleteRow(id)!=-1);
    // }

    /**
     * tests the update one in Database.java
     * UNFINISHED: not sure how to select by ID 
     */
    // public void testUpdateOne() {
    //     assertTrue(this.db.updateOne(id)!=-1);
    // }


}
