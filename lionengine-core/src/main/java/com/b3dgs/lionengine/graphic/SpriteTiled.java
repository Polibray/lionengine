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

import com.b3dgs.lionengine.SurfaceTile;

/**
 * Tiled sprite are mainly used for tile based levels. It works by loading an image and render only a part of it
 * (virtually splited). The first tile is 0.
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * // Load
 * final SpriteTiled tilesheet = Drawable.loadSpriteTiled(Medias.create(&quot;tilesheet.png&quot;), 16, 16);
 * tilesheet.load(false);
 * tilesheet.setPosition(300, 300);
 * tilesheet.setTile(1);
 * 
 * // Render
 * tilesheet.render(g);
 * </pre>
 */
public interface SpriteTiled extends Sprite, SurfaceTile
{
    /**
     * Set the active tile.
     * 
     * @param tile The tile to render (superior or equal to 0 and inferior to {@link #getTilesHorizontal()} *
     *            {@link #getTilesVertical()}).
     */
    void setTile(int tile);

    /**
     * Get the number of horizontal tiles.
     * 
     * @return The number of horizontal tiles.
     */
    int getTilesHorizontal();

    /**
     * Get the number of vertical tiles.
     * 
     * @return The number of vertical tiles.
     */
    int getTilesVertical();
}
