/*
 * Copyright (C) 2013-2016 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import com.b3dgs.lionengine.Media;

/**
 * Media mock.
 */
final class MediaMock implements Media
{
    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public String getPath()
    {
        return null;
    }

    @Override
    public String getParentPath()
    {
        return null;
    }

    @Override
    public File getFile()
    {
        return null;
    }

    @Override
    public Collection<Media> getMedias()
    {
        return null;
    }

    @Override
    public InputStream getInputStream()
    {
        return new InputStream()
        {
            @Override
            public int read() throws IOException
            {
                throw new IOException();
            }
        };
    }

    @Override
    public OutputStream getOutputStream()
    {
        return null;
    }

    @Override
    public boolean exists()
    {
        return false;
    }

}
