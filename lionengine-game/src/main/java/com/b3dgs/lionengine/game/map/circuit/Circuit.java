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
package com.b3dgs.lionengine.game.map.circuit;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.Constant;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.game.map.GroupTransition;

/**
 * Represents a tile circuit from two groups.
 */
public class Circuit
{
    /** The circuit type. */
    private final CircuitType type;
    /** Group transition. */
    private final GroupTransition groups;

    /**
     * Create the circuit.
     * 
     * @param type The transition type.
     * @param groupIn The group inside.
     * @param groupOut The group outside.
     * @throws LionEngineException If <code>null</code> arguments.
     */
    public Circuit(CircuitType type, String groupIn, String groupOut)
    {
        Check.notNull(type);
        Check.notNull(groupIn);
        Check.notNull(groupOut);

        this.type = type;
        groups = new GroupTransition(groupIn, groupOut);
    }

    /**
     * Get the circuit type.
     * 
     * @return The circuit type.
     */
    public CircuitType getType()
    {
        return type;
    }

    /**
     * Get the group inside.
     * 
     * @return The group inside.
     */
    public String getIn()
    {
        return groups.getIn();
    }

    /**
     * Get the group outside.
     * 
     * @return The group outside.
     */
    public String getOut()
    {
        return groups.getOut();
    }

    /*
     * Object
     */

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + groups.hashCode();
        result = prime * result + type.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof Circuit))
        {
            return false;
        }
        final Circuit other = (Circuit) obj;
        return groups.equals(other.groups) && type == other.type;
    }

    @Override
    public String toString()
    {
        return new StringBuilder().append(type).append(Constant.SPACE).append(groups).toString();
    }
}