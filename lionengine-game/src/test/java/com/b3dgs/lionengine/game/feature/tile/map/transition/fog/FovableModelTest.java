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
package com.b3dgs.lionengine.game.feature.tile.map.transition.fog;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.b3dgs.lionengine.Constant;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.core.Medias;
import com.b3dgs.lionengine.game.feature.Featurable;
import com.b3dgs.lionengine.game.feature.FeaturableModel;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.game.feature.TransformableModel;
import com.b3dgs.lionengine.game.feature.UtilSetup;
import com.b3dgs.lionengine.game.feature.tile.map.MapTile;
import com.b3dgs.lionengine.game.feature.tile.map.UtilMap;

/**
 * Test the fovable model class.
 */
public class FovableModelTest
{
    /** Object config test. */
    private static Media config;

    /**
     * Prepare test.
     */
    @BeforeClass
    public static void setUp()
    {
        Medias.setResourcesDirectory(System.getProperty("java.io.tmpdir"));
        config = UtilSetup.createConfig();
    }

    /**
     * Clean up test.
     */
    @AfterClass
    public static void cleanUp()
    {
        Assert.assertTrue(config.getFile().delete());
        Medias.setResourcesDirectory(Constant.EMPTY_STRING);
    }

    /**
     * Test the fovable model.
     */
    @Test
    public void testFovable()
    {
        final Services services = new Services();
        final MapTile map = UtilMap.createMap(7);
        services.add(map);

        final Fovable fovable = new FovableModel(services);

        final Setup setup = new Setup(config);
        final Featurable featurable = new FeaturableModel();
        final Transformable transformable = featurable.addFeatureAndGet(new TransformableModel(setup));
        transformable.teleport(1, 2);
        transformable.setSize(3, 4);
        fovable.prepare(featurable);
        fovable.setFov(5);

        Assert.assertEquals(1, fovable.getInTileX());
        Assert.assertEquals(2, fovable.getInTileY());
        Assert.assertEquals(3, fovable.getInTileWidth());
        Assert.assertEquals(4, fovable.getInTileHeight());
        Assert.assertEquals(5, fovable.getInTileFov());
    }
}
