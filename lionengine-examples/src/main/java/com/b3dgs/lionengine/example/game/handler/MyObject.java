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
package com.b3dgs.lionengine.example.game.handler;

import com.b3dgs.lionengine.Graphic;
import com.b3dgs.lionengine.core.UtilityMedia;
import com.b3dgs.lionengine.game.ObjectGame;
import com.b3dgs.lionengine.game.SetupGame;

/**
 * My object implementation.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
final class MyObject
        extends ObjectGame
{
    /**
     * Constructor.
     */
    MyObject()
    {
        super(new SetupGame(UtilityMedia.get("my_object.xml")));
    }

    /**
     * Update the object.
     * 
     * @param extrp The extrapolation value.
     */
    public void update(double extrp)
    {
        System.out.println("I am updating: " + this);
    }

    /**
     * Render the object.
     * 
     * @param g The graphic output.
     */
    public void render(Graphic g)
    {
        System.out.println("I am rendering: " + this);
    }
}