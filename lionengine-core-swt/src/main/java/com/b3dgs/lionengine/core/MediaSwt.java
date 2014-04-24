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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.UtilityFile;

/**
 * Media implementation.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
final class MediaSwt
        implements Media
{
    /** Media path. */
    private final String path;

    /**
     * Constructor.
     * 
     * @param path The media path.
     */
    public MediaSwt(String path)
    {
        this.path = path;
    }

    /*
     * Media
     */

    @Override
    public String getPath()
    {
        return path;
    }

    @Override
    public File getFile()
    {
        return new File(UtilityFile.getPath(UtilityMedia.getRessourcesDir(), path));
    }

    @Override
    public InputStream getStream()
    {
        return UtilityMedia.getInputStream(this, MediaSwt.class.getSimpleName(), false);
    }

    @Override
    public OutputStream getOutputStream()
    {
        return UtilityMedia.getOutputStream(this, MediaSwt.class.getSimpleName(), false);
    }
}
