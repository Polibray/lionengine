/*
 * Copyright (C) 2013-2015 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.drawable;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.ImageInfo;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Localizable;
import com.b3dgs.lionengine.Origin;
import com.b3dgs.lionengine.Viewer;
import com.b3dgs.lionengine.core.Graphic;
import com.b3dgs.lionengine.core.Graphics;
import com.b3dgs.lionengine.core.ImageBuffer;
import com.b3dgs.lionengine.core.Media;

/**
 * Image implementation.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
class ImageImpl
        implements Image
{
    /** Unsupported enum. */
    static final String ERROR_ENUM = "Unknown enum type: ";
    /** Error already loaded. */
    private static final String ERROR_ALREADY_LOADED = "Image has already been loaded: ";

    /** Media file name. */
    private final Media media;
    /** Sprite width. */
    private final int width;
    /** Sprite height. */
    private final int height;
    /** Image horizontal position. */
    private double x;
    /** Image vertical position. */
    private double y;
    /** Render horizontal position. */
    private int rx;
    /** Render vertical position. */
    private int ry;
    /** Image surface. */
    private ImageBuffer surface;
    /** Origin point. */
    private Origin origin;

    /**
     * Internal constructor.
     * 
     * @param media The image media.
     * @throws LionEngineException If the media is <code>null</code>.
     */
    ImageImpl(Media media) throws LionEngineException
    {
        Check.notNull(media);

        this.media = media;

        final ImageInfo info = ImageInfo.get(media);
        width = info.getWidth();
        height = info.getHeight();
        origin = Origin.TOP_LEFT;
    }

    /**
     * Internal constructor.
     * 
     * @param surface The surface to share.
     * @throws LionEngineException If the surface is <code>null</code>.
     */
    ImageImpl(ImageBuffer surface) throws LionEngineException
    {
        Check.notNull(surface);

        this.surface = surface;
        width = surface.getWidth();
        height = surface.getHeight();
        origin = Origin.TOP_LEFT;
        media = null;
    }

    /*
     * Image
     */

    @Override
    public void load(boolean alpha) throws LionEngineException
    {
        if (surface != null)
        {
            throw new LionEngineException(media, ERROR_ALREADY_LOADED);
        }
        surface = Graphics.getImageBuffer(media, alpha);
    }

    @Override
    public void render(Graphic g)
    {
        g.drawImage(surface, rx, ry);
    }

    @Override
    public void setOrigin(Origin origin)
    {
        this.origin = origin;
    }

    @Override
    public void setLocation(double x, double y)
    {
        this.x = x;
        this.y = y;
        rx = (int) origin.getX(x, width);
        ry = (int) origin.getY(y, height);
    }

    @Override
    public void setLocation(Viewer viewer, Localizable localizable)
    {
        setLocation(viewer.getViewpointX(localizable.getX()), viewer.getViewpointY(localizable.getY()));
    }

    @Override
    public double getX()
    {
        return x;
    }

    @Override
    public double getY()
    {
        return y;
    }

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
    public ImageBuffer getSurface()
    {
        return surface;
    }

    /*
     * Object
     */

    @Override
    public boolean equals(Object object)
    {
        if (object == this)
        {
            return true;
        }
        if (object instanceof Image)
        {
            final Image image = (Image) object;

            final boolean sameSurface = image.getSurface() == getSurface();
            final boolean sameWidth = image.getWidth() == getWidth();
            final boolean sameHeight = image.getHeight() == getHeight();

            return sameWidth && sameHeight && sameSurface;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + surface.hashCode();
        return result;
    }
}
