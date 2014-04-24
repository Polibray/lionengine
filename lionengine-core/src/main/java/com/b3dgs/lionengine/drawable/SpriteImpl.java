/*
 * Copyright (C) 2013-2014 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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

import java.util.Arrays;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.ColorRgba;
import com.b3dgs.lionengine.Filter;
import com.b3dgs.lionengine.ImageInfo;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.core.Core;
import com.b3dgs.lionengine.core.Graphic;
import com.b3dgs.lionengine.core.ImageBuffer;

/**
 * Sprite implementation.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
class SpriteImpl
        implements Sprite
{
    /** Width error. */
    private static final String ERROR_WIDTH = "Width percent must be strictly positive !";
    /** Height error. */
    private static final String ERROR_HEIGHT = "Height percent must be strictly positive !";
    /** Alpha error. */
    private static final String ERROR_ALPHA = "Alpha must be >= 0 and <= 255 !";

    /** Sprite original width. */
    protected final int widthOriginal;
    /** Sprite original height. */
    protected final int heightOriginal;
    /** Sprite file name. */
    private final Media media;
    /** Sprite original surface. */
    protected ImageBuffer surfaceOriginal;
    /** Sprite current surface. */
    protected ImageBuffer surface;
    /** Sprite width. */
    protected int width;
    /** Sprite height. */
    protected int height;
    /** Sprite raw data (used for alpha). */
    private int[][] rgb;
    /** First alpha. */
    private boolean firstAlpha;

    /**
     * Constructor.
     * 
     * @param media The sprite media.
     * @param surface The surface to share.
     */
    SpriteImpl(Media media, ImageBuffer surface)
    {
        this.media = media;
        this.surface = surface;
        if (media != null)
        {
            final ImageInfo info = ImageInfo.get(media);
            widthOriginal = info.getWidth();
            heightOriginal = info.getHeight();
        }
        else
        {
            widthOriginal = surface.getWidth();
            heightOriginal = surface.getHeight();
        }
        width = widthOriginal;
        height = heightOriginal;
        rgb = null;
    }

    /**
     * Stretch the surface with the specified new size.
     * 
     * @param newWidth The new width.
     * @param newHeight The new height.
     */
    protected void stretchSurface(int newWidth, int newHeight)
    {
        width = newWidth;
        height = newHeight;
        surface = Core.GRAPHIC.resize(surfaceOriginal, newWidth, newHeight);
    }

    /**
     * Backup the original surface before modification only if needed.
     */
    private void lazySurfaceBackup()
    {
        if (surfaceOriginal == null)
        {
            surfaceOriginal = Core.GRAPHIC.getImageBuffer(surface);
        }
    }

    /*
     * Sprite
     */

    @Override
    public void load(boolean alpha)
    {
        if (surface == null)
        {
            surface = Core.GRAPHIC.getImageBuffer(media, alpha);
        }
    }

    @Override
    public void scale(int percent)
    {
        stretch(percent, percent);
    }

    @Override
    public void stretch(int widthPercent, int heightPercent)
    {
        Check.argument(widthPercent > 0, SpriteImpl.ERROR_WIDTH);
        Check.argument(heightPercent > 0, SpriteImpl.ERROR_HEIGHT);

        if (widthPercent != 100 || heightPercent != 100)
        {
            final int newWidth = getWidthOriginal() * widthPercent / 100;
            final int newHeight = getHeightOriginal() * heightPercent / 100;
            lazySurfaceBackup();
            stretchSurface(newWidth, newHeight);
        }
    }

    @Override
    public void rotate(int angle)
    {
        lazySurfaceBackup();
        surface = Core.GRAPHIC.rotate(surfaceOriginal, angle);
    }

    @Override
    public void flipHorizontal()
    {
        lazySurfaceBackup();
        surface = Core.GRAPHIC.flipHorizontal(surfaceOriginal);
    }

    @Override
    public void flipVertical()
    {
        lazySurfaceBackup();
        surface = Core.GRAPHIC.flipVertical(surfaceOriginal);
    }

    @Override
    public void filter(Filter filter)
    {
        lazySurfaceBackup();
        surface = Core.GRAPHIC.applyFilter(surfaceOriginal, filter);
    }

    @Override
    public void setTransparency(ColorRgba mask)
    {
        lazySurfaceBackup();
        surface = Core.GRAPHIC.applyMask(surfaceOriginal, mask);
    }

    @Override
    public void setAlpha(int alpha)
    {
        Check.argument(alpha >= 0 && alpha <= 255, SpriteImpl.ERROR_ALPHA);
        setFade(alpha, -255);
    }

    @Override
    public void setFade(int alpha, int fade)
    {
        if (rgb == null)
        {
            rgb = new int[width][height];
            firstAlpha = true;
        }
        for (int cx = 0; cx < width; cx++)
        {
            for (int cy = 0; cy < height; cy++)
            {
                if (firstAlpha)
                {
                    lazySurfaceBackup();
                    rgb[cx][cy] = surfaceOriginal.getRgb(cx, cy);
                }
                final int alphaDec = 24;
                final int alphaKey = 0x00ffffff;
                final int mc = Math.abs(alpha) << alphaDec | alphaKey;
                surface.setRgb(cx, cy, new ColorRgba(rgb[cx][cy]).inc(fade + alpha, fade + alpha, fade + alpha) & mc);
            }
        }
        firstAlpha = false;
    }

    @Override
    public void render(Graphic g, int x, int y)
    {
        g.drawImage(surface, x, y);
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
    public int getWidthOriginal()
    {
        return widthOriginal;
    }

    @Override
    public int getHeightOriginal()
    {
        return heightOriginal;
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
        if (object instanceof Sprite)
        {
            final Sprite sprite = (Sprite) object;

            final boolean sameSurface = sprite.getSurface() == getSurface();
            final boolean sameWidth = sprite.getWidth() == getWidth();
            final boolean sameHeight = sprite.getHeight() == getHeight();

            return sameWidth && sameHeight && sameSurface;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + height;
        result = prime * result + heightOriginal;
        result = prime * result + (media == null ? 0 : media.hashCode());
        result = prime * result + Arrays.hashCode(rgb);
        result = prime * result + (surface == null ? 0 : surface.hashCode());
        result = prime * result + width;
        result = prime * result + widthOriginal;
        return result;
    }
}
