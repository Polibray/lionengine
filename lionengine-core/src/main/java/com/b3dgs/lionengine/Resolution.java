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

/**
 * Describes a display resolution. It allows to define different parameters:
 * <ul>
 * <li><code>width</code> and <code>height</code> : represent the screen size</li>
 * <li>
 * <code>ratio</code>, which is computed by using the <code>width</code> and <code>height</code>, allows to know the
 * screen ratio.</li>
 * <li><code>rate</code> : represents the screen refresh rate (in frames per seconds)</li>
 * </ul>
 * This class is mainly used to describe the display resolution chosen.
 * <p>
 * This class is Thread-Safe.
 * </p>
 */
public final class Resolution
{
    /** Double factor. */
    private static final int FACTOR_2X = 2;
    /** Triple factor. */
    private static final int FACTOR_3X = 3;

    /** Display rate. */
    private final int rate;
    /** Resolution width. */
    private final int width;
    /** Resolution height. */
    private final int height;

    /**
     * Create a resolution.
     * 
     * @param width The resolution width (in pixel) (strictly positive).
     * @param height The resolution height (in pixel) (strictly positive).
     * @param rate The refresh rate (usually 50 or 60) (positive).
     * @throws LionEngineException If arguments are invalid.
     */
    public Resolution(int width, int height, int rate)
    {
        Check.superiorStrict(width, 0);
        Check.superiorStrict(height, 0);
        Check.superiorOrEqual(rate, 0);

        this.width = width;
        this.height = height;
        this.rate = rate;
    }

    /**
     * Get scaled by 2.
     * 
     * @return The scaled by 2 resolution.
     */
    public Resolution get2x()
    {
        return getScaled(FACTOR_2X, FACTOR_2X);
    }

    /**
     * Get scaled by 3.
     * 
     * @return The scaled by 3 resolution.
     */
    public Resolution get3x()
    {
        return getScaled(FACTOR_3X, FACTOR_3X);
    }

    /**
     * Get scaled resolution.
     * 
     * @param factorX The horizontal scale factor.
     * @param factorY The vertical scale factor.
     * @return The scaled resolution.
     * @throws LionEngineException If factor is not strictly superior to 0.
     */
    public Resolution getScaled(double factorX, double factorY)
    {
        Check.superiorStrict(factorX, 0);
        Check.superiorStrict(factorY, 0);

        return new Resolution((int) (width * factorX), (int) (height * factorY), rate);
    }

    /**
     * Get the resolution width.
     * 
     * @return The resolution width.
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Get the resolution height.
     * 
     * @return The resolution height.
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Get the display rate.
     * 
     * @return The display rate.
     */
    public int getRate()
    {
        return rate;
    }

    /*
     * Object
     */

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + height;
        result = prime * result + rate;
        result = prime * result + width;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        final Resolution other = (Resolution) obj;
        return width == other.width && height == other.height && rate == other.rate;
    }
}
