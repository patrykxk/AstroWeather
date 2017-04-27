package com.patryk.astrocalculator;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void astro() throws Exception {
        AstroCalculator astro = new AstroCalculator(new AstroDateTime(), new AstroCalculator.Location(50,50) );

        System.out.println(astro.getSunInfo().getSunrise());
        assertEquals(4, 2 + 2);
    }
}