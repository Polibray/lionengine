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

import org.junit.Assert;
import org.junit.Test;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.tile.map.MapTile;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGame;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGroupModel;
import com.b3dgs.lionengine.util.UtilTests;

/**
 * Test the A Star class.
 */
public class AstarTest
{
    /**
     * Test the constructor.
     * 
     * @throws Exception If error.
     */
    @Test(expected = LionEngineException.class)
    public void testConstructor() throws Exception
    {
        UtilTests.testPrivateConstructor(Astar.class);
    }

    /**
     * Test the create path finder.
     */
    @Test
    public void testCreatePathFinder()
    {
        final Services services = new Services();
        final MapTile map = services.add(new MapTileGame());
        map.addFeature(new MapTileGroupModel());
        map.addFeature(new MapTilePathModel(services));
        Assert.assertNotNull(Astar.createPathFinder(map, 1, new HeuristicClosest()));
    }

    /**
     * Test the create heuristic closest.
     */
    @Test
    public void testCreateHeuristicClosest()
    {
        Assert.assertEquals(HeuristicClosest.class, Astar.createHeuristicClosest().getClass());
    }

    /**
     * Test the create heuristic closest squared.
     */
    @Test
    public void testCreateHeuristicClosestSquared()
    {
        Assert.assertEquals(HeuristicClosestSquared.class, Astar.createHeuristicClosestSquared().getClass());
    }

    /**
     * Test the create heuristic Manhattan.
     */
    @Test
    public void testCreateHeuristicManhattan()
    {
        Assert.assertEquals(HeuristicManhattan.class, Astar.createHeuristicManhattan(1).getClass());
    }
}
