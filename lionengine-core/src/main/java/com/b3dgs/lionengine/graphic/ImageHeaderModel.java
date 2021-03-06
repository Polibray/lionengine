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
package com.b3dgs.lionengine.graphic;

/**
 * Image header model base.
 */
public class ImageHeaderModel implements ImageHeader
{
    /** Image width. */
    private final int width;
    /** Image height. */
    private final int height;
    /** Image format. */
    private final ImageFormat format;

    /**
     * Create image header.
     * 
     * @param width The image width.
     * @param height The image height.
     * @param format The image format.
     */
    public ImageHeaderModel(int width, int height, ImageFormat format)
    {
        this.width = width;
        this.height = height;
        this.format = format;
    }

    /*
     * ImageHeader
     */

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public ImageFormat getFormat()
    {
        return format;
    }
}
