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
package com.b3dgs.lionengine.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

import com.b3dgs.lionengine.ColorRgba;
import com.b3dgs.lionengine.Graphic;
import com.b3dgs.lionengine.Transparency;

/**
 * Image buffer implementation.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
final class ImageBufferSwt
        implements ImageBuffer
{
    /**
     * Get the transparency equivalence.
     * 
     * @param transparency The transparency.
     * @return The equivalence.
     */
    static Transparency getTransparency(int transparency)
    {
        switch (transparency)
        {
            case SWT.TRANSPARENCY_NONE:
                return Transparency.OPAQUE;
            case SWT.TRANSPARENCY_MASK:
                return Transparency.BITMASK;
            case SWT.TRANSPARENCY_ALPHA:
                return Transparency.TRANSLUCENT;
            default:
                return Transparency.OPAQUE;
        }
    }

    /** Transparency. */
    private final Transparency transparency;
    /** Last image data. */
    private final ImageData data;
    /** Image. */
    private Image image;
    /** GC. */
    private GC gc;

    /**
     * Constructor.
     * 
     * @param image The image.
     */
    ImageBufferSwt(Image image)
    {
        this.image = image;
        data = image.getImageData();
        transparency = ImageBufferSwt.getTransparency(data.getTransparencyType());
    }

    /**
     * Get the image buffer.
     * 
     * @return The image buffer.
     */
    Image getBuffer()
    {
        return image;
    }

    /*
     * ImageBuffer
     */

    @Override
    public Graphic createGraphic()
    {
        gc = new GC(image);
        return new GraphicSwt(gc);
    }

    @Override
    public void setRgb(int x, int y, int rgb)
    {
        final ColorRgba rgba = new ColorRgba(rgb);
        final RGB color = new RGB(rgba.getRed(), rgba.getGreen(), rgba.getBlue());
        final int pixel = data.palette.getPixel(color);
        data.setPixel(x, y, pixel);
        image.dispose();
        image = new Image(ScreenSwt.display, data);
    }

    @Override
    public void setRgb(int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize)
    {
        data.setPixels(startX, startY, w, rgbArray, offset);
        image.dispose();
        image = new Image(ScreenSwt.display, data);
    }

    @Override
    public int getRgb(int x, int y)
    {
        final int pixel = data.getPixel(x, y);
        final PaletteData palette = data.palette;
        final RGB rgb = palette.getRGB(pixel);
        return new ColorRgba(rgb.red, rgb.green, rgb.blue).getRgba();
    }

    @Override
    public int[] getRgb(int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize)
    {
        data.getPixels(startX, startY, w, rgbArray, offset);
        return rgbArray;
    }

    @Override
    public int getWidth()
    {
        return data.width;
    }

    @Override
    public int getHeight()
    {
        return data.height;
    }

    @Override
    public Transparency getTransparency()
    {
        return transparency;
    }
}