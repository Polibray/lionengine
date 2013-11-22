/*
 * Copyright (C) 2013 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.game.purview;

/**
 * Purview representing an object with the ability of being mirrored. Mainly used with sprites (horizontal axis).
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public interface Mirrorable
{
    /**
     * Set the next mirror state and apply it on next turn.
     * 
     * @param state The next mirror state.
     */
    void mirror(boolean state);

    /**
     * Update mirror and apply it if necessary.
     */
    void updateMirror();

    /**
     * Set cancel state for the mirror operation.
     * 
     * @param state The state.
     */
    void setMirrorCancel(boolean state);

    /**
     * Get mirror cancel state.
     * 
     * @return The mirror cancel state.
     */
    boolean getMirrorCancel();

    /**
     * Get current mirror state.
     * 
     * @return The current mirror state.
     */
    boolean getMirror();
}