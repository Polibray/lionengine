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

import java.io.IOException;
import java.io.InputStream;

/**
 * Read JPG image info.
 */
final class ImageHeaderJpg extends ImageHeaderReaderModel
{
    /** Header 1. */
    private static final int HEADER_1 = 0xFF;
    /** Header 1. */
    private static final int HEADER_2 = 0xD8;
    /** Marker 1. */
    private static final int MARKER_1 = 192;
    /** Marker 2. */
    private static final int MARKER_2 = 193;
    /** Marker 3. */
    private static final int MARKER_3 = 194;
    /** Invalid Jpg. */
    private static final String ERROR_JPG = "Invalid JPG file !";

    /**
     * Internal constructor.
     */
    ImageHeaderJpg()
    {
        super(HEADER_1, HEADER_2);
    }

    /*
     * ImageHeaderReader
     */

    @Override
    public ImageHeader readHeader(InputStream input) throws IOException
    {
        checkSkippedError(input.skip(2), 2);
        int current = input.read();
        while (255 == current)
        {
            final int marker = input.read();
            final int len = readInt(input, 2, true);
            if (MARKER_1 == marker || MARKER_2 == marker || MARKER_3 == marker)
            {
                final long skipped = input.skip(1);
                checkSkippedError(skipped, 1);
                final int height = readInt(input, 2, true);
                final int width = readInt(input, 2, true);
                final ImageFormat format = ImageFormat.JPG;

                return new ImageHeaderModel(width, height, format);
            }
            final long skipped = input.skip(len - 2L);
            checkSkippedError(skipped, len - 2);
            current = input.read();
        }
        throw new IOException(ERROR_JPG);
    }
}
