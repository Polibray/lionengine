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
package com.b3dgs.lionengine.editor.map.tile;

import java.util.Locale;

import org.eclipse.osgi.util.NLS;

/**
 * Messages internationalization.
 */
public final class Messages extends NLS
{
    /** Group chooser title. */
    public static String Title;
    /** Group chooser header title. */
    public static String HeaderTitle;
    /** Group chooser header description. */
    public static String HeaderDesc;
    /** Group choice. */
    public static String Choice;
    /** Group add. */
    public static String Add;
    /** Tile group property. */
    public static String TileGroup;
    /** Tile sheet property. */
    public static String TileSheet;
    /** Tile number property. */
    public static String TileNumber;
    /** Tile size property. */
    public static String TileSize;
    /** Tile features property. */
    public static String TileFeatures;
    /** Tile feature property. */
    public static String TileFeature;

    /**
     * Initialize.
     */
    static
    {
        NLS.initializeMessages(Messages.class.getName().toLowerCase(Locale.ENGLISH), Messages.class);
    }
}
