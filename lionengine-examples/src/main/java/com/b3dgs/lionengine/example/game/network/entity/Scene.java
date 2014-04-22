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
package com.b3dgs.lionengine.example.game.network.entity;

import com.b3dgs.lionengine.Graphic;
import com.b3dgs.lionengine.Resolution;
import com.b3dgs.lionengine.core.Keyboard;
import com.b3dgs.lionengine.core.Loader;
import com.b3dgs.lionengine.core.Sequence;
import com.b3dgs.lionengine.core.UtilityMedia;

/**
 * Game loop designed to handle our little world.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
final class Scene
        extends Sequence
{
    /** Native resolution. */
    private static final Resolution NATIVE = new Resolution(320, 240, 60);

    /** Keyboard reference. */
    private final Keyboard keyboard;
    /** World server reference. */
    private final World<?> world;

    /**
     * Constructor.
     * 
     * @param loader The loader reference.
     */
    Scene(Loader loader)
    {
        super(loader, Scene.NATIVE);
        keyboard = getInputDevice(Keyboard.class);
        setExtrapolated(true); // Recommended for network game

        final boolean server = false;

        if (server)
        {
            final WorldServer worldServer = new WorldServer(this);
            worldServer.startServer("Test", 7777, "Welcome !");
            world = worldServer;
        }
        else
        {
            final WorldClient worldClient = new WorldClient(this);
            worldClient.setName("Unnamed");
            worldClient.connect("127.0.0.1", 7777);
            world = worldClient;
        }
    }

    /*
     * Sequence
     */

    @Override
    protected void load()
    {
        world.loadFromFile(UtilityMedia.get("smb_level1-1.lvl"));
    }

    @Override
    protected void update(double extrp)
    {
        if (keyboard.isPressed(Keyboard.ESCAPE))
        {
            end();
        }
        world.receiveMessages();
        world.update(extrp);
        world.sendMessages();
    }

    @Override
    protected void render(Graphic g)
    {
        world.render(g);
    }

    @Override
    protected void onTerminate(boolean hasNextSequence)
    {
        world.disconnect();
    }
}