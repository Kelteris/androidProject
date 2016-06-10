package com.cs246.rmgroup.rmplanner;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    Date date = new Date();
    FlyOutContainer flyOutContainer;

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void goodDate() throws Exception{
        assertNotEquals(0, date._am);
        assertNotEquals(0, date._day);
        assertNotEquals(0, date._hour);
        assertNotEquals(0, date._minute);
        assertNotEquals(0, date._month);
        assertNotEquals(0, date._year);
    }

    @Test
    public void goodSetDate() throws Exception{
        date.setDate(2016,4,30,12,12,false);
        assertEquals(false, date._am);
        assertEquals(12, date._minute);
        assertEquals(12, date._hour);
        assertEquals(30, date._day);
        assertEquals(4, date._month);
        assertEquals(2016, date._year);
    }
}