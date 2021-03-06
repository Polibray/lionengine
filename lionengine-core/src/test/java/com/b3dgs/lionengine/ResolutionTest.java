/*
 * Copyright (C) 2013-2017 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.b3dgs.lionengine;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test the resolution class.
 */
public class ResolutionTest
{
    /**
     * Test the display creation function.
     * 
     * @param width The width.
     * @param height The height.
     * @param rate The rate.
     */
    private static void testResolutionCreation(int width, int height, int rate)
    {
        try
        {
            final Resolution resolution = new Resolution(width, height, rate);
            Assert.assertNotNull(resolution);
            Assert.fail();
        }
        catch (final LionEngineException exception)
        {
            // Success
            Assert.assertNotNull(exception);
        }
    }

    /**
     * Test resolution getters.
     */
    @Test
    public void testGetters()
    {
        final Resolution resolution = new Resolution(320, 240, 60);

        Assert.assertEquals(320, resolution.getWidth());
        Assert.assertEquals(240, resolution.getHeight());
        Assert.assertEquals(60, resolution.getRate());
    }

    /**
     * Test the non strict positive width.
     */
    @Test(expected = LionEngineException.class)
    public void testNonStrictPositiveWidth()
    {
        Assert.assertNull(new Resolution(0, 240, 0));
    }

    /**
     * Test the non strict positive height.
     */
    @Test(expected = LionEngineException.class)
    public void testNonStrictPositiveHeight()
    {
        Assert.assertNull(new Resolution(320, 0, 0));
    }

    /**
     * Test the negative rate.
     */
    @Test(expected = LionEngineException.class)
    public void testNegativeRate()
    {
        Assert.assertNull(new Resolution(320, 240, -1));
    }

    /**
     * Test the resolution failure.
     */
    @Test
    public void testFailures()
    {
        testResolutionCreation(0, 0, -1);
        testResolutionCreation(0, 1, -1);
        testResolutionCreation(0, 0, -1);
        testResolutionCreation(0, 0, 1);
        testResolutionCreation(0, 1, 1);
        testResolutionCreation(0, 0, 1);
        testResolutionCreation(1, 0, -1);
        testResolutionCreation(1, 1, -1);
        testResolutionCreation(1, 1, -1);
    }

    /**
     * Test scale 2x function.
     */
    @Test
    public void testScale2x()
    {
        final Resolution resolution = new Resolution(320, 240, 60);
        Assert.assertEquals(new Resolution(640, 480, 60), resolution.get2x());
    }

    /**
     * Test scale 3x function.
     */
    @Test
    public void testScale3x()
    {
        final Resolution resolution = new Resolution(320, 240, 60);
        Assert.assertEquals(new Resolution(960, 720, 60), resolution.get3x());
    }

    /**
     * Test scale function with wrong factor X.
     */
    @Test(expected = LionEngineException.class)
    public void testScaleWrongFactorX()
    {
        Assert.assertNull(new Resolution(320, 240, 60).getScaled(0, 1));
    }

    /**
     * Test scale function with wrong factor Y.
     */
    @Test(expected = LionEngineException.class)
    public void testScaleWrongFactorY()
    {
        Assert.assertNull(new Resolution(320, 240, 60).getScaled(1, 0));
    }

    /**
     * Test the hash code.
     */
    @Test
    public void testHashCode()
    {
        final int resolution = new Resolution(320, 240, 60).hashCode();

        Assert.assertEquals(new Resolution(320, 240, 60).hashCode(), resolution);

        Assert.assertNotEquals(resolution, new Object().hashCode());
        Assert.assertNotEquals(resolution, new Resolution(100, 240, 60).hashCode());
        Assert.assertNotEquals(resolution, new Resolution(320, 100, 60).hashCode());
        Assert.assertNotEquals(resolution, new Resolution(320, 240, 30).hashCode());
    }

    /**
     * Test the equality.
     */
    @Test
    public void testEquals()
    {
        final Resolution resolution = new Resolution(320, 240, 60);

        Assert.assertEquals(resolution, resolution);
        Assert.assertEquals(new Resolution(320, 240, 60), resolution);

        Assert.assertNotEquals(resolution, null);
        Assert.assertNotEquals(resolution, new Object());
        Assert.assertNotEquals(resolution, new Resolution(100, 240, 60));
        Assert.assertNotEquals(resolution, new Resolution(320, 100, 60));
        Assert.assertNotEquals(resolution, new Resolution(320, 240, 30));
    }
}
