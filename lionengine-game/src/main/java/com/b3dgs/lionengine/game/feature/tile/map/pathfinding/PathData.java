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
package com.b3dgs.lionengine.game.feature.tile.map.pathfinding;

import java.util.Arrays;
import java.util.Collection;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.Nameable;

/**
 * Represents the data associated to a path.
 */
public class PathData implements Nameable
{
    /** Category name. */
    private final String category;
    /** Path cost. */
    private final double cost;
    /** Blocking flag. */
    private final boolean blocking;
    /** Allowed movements. */
    private final Collection<MovementTile> movements;

    /**
     * Create a path data.
     * 
     * @param category The category name.
     * @param cost The cost value.
     * @param blocking The blocking flag.
     * @param movements The allowed movements.
     */
    public PathData(String category, double cost, boolean blocking, Collection<MovementTile> movements)
    {
        Check.notNull(category);
        Check.notNull(movements);

        this.category = category;
        this.cost = cost;
        this.blocking = blocking;
        this.movements = movements;
    }

    /**
     * Get the cost value.
     * 
     * @return The cost value.
     */
    public double getCost()
    {
        return cost;
    }

    /**
     * Get the blocking state.
     * 
     * @return <code>true</code> if blocking, <code>false</code> else.
     */
    public boolean isBlocking()
    {
        return blocking;
    }

    /**
     * Get the allowed movements.
     * 
     * @return The allowed movements.
     */
    public Collection<MovementTile> getAllowedMovements()
    {
        return movements;
    }

    /*
     * Nameable
     */

    @Override
    public String getName()
    {
        return category;
    }

    /*
     * Object
     */

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (blocking ? 1231 : 1237);
        result = prime * result + category.hashCode();
        final long temp = Double.doubleToLongBits(cost);
        result = prime * result + (int) (temp ^ temp >>> 32);
        result = prime * result + movements.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || object.getClass() != getClass())
        {
            return false;
        }
        final PathData other = (PathData) object;
        return getName().equals(other.getName())
               && Double.compare(cost, other.getCost()) == 0
               && blocking == other.isBlocking()
               && Arrays.deepEquals(movements.toArray(), other.getAllowedMovements().toArray());
    }
}
