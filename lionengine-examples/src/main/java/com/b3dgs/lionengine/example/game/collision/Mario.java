/*
 * Copyright (C) 2013-2015 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.example.game.collision;

import com.b3dgs.lionengine.ColorRgba;
import com.b3dgs.lionengine.Origin;
import com.b3dgs.lionengine.core.Core;
import com.b3dgs.lionengine.core.Graphic;
import com.b3dgs.lionengine.core.Renderable;
import com.b3dgs.lionengine.core.Updatable;
import com.b3dgs.lionengine.core.awt.Keyboard;
import com.b3dgs.lionengine.drawable.Drawable;
import com.b3dgs.lionengine.drawable.SpriteAnimated;
import com.b3dgs.lionengine.game.Axis;
import com.b3dgs.lionengine.game.Camera;
import com.b3dgs.lionengine.game.Direction;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.map.Tile;
import com.b3dgs.lionengine.game.object.ObjectGame;
import com.b3dgs.lionengine.game.object.Services;
import com.b3dgs.lionengine.game.object.SetupSurface;
import com.b3dgs.lionengine.game.trait.body.Body;
import com.b3dgs.lionengine.game.trait.body.BodyModel;
import com.b3dgs.lionengine.game.trait.collidable.Collidable;
import com.b3dgs.lionengine.game.trait.collidable.CollidableModel;
import com.b3dgs.lionengine.game.trait.collidable.TileCollidable;
import com.b3dgs.lionengine.game.trait.collidable.TileCollidableListener;
import com.b3dgs.lionengine.game.trait.collidable.TileCollidableModel;
import com.b3dgs.lionengine.game.trait.transformable.Transformable;
import com.b3dgs.lionengine.game.trait.transformable.TransformableModel;

/**
 * Implementation of our controllable entity.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 * @see com.b3dgs.lionengine.example.game.entity
 */
class Mario
        extends ObjectGame
        implements Updatable, Renderable, TileCollidableListener
{
    /** Setup. */
    private static final SetupSurface SETUP = new SetupSurface(Core.MEDIA.create("mario.xml"));

    /** Surface. */
    private final SpriteAnimated surface;
    /** Transformable model. */
    private final Transformable transformable;
    /** Body model. */
    private final Body body;
    /** Tile collidable. */
    private final TileCollidable tileCollidable;
    /** Object collidable. */
    private final Collidable collidable;
    /** Keyboard reference. */
    private final Keyboard keyboard;
    /** Camera reference. */
    private final Camera camera;
    /** Movement force. */
    private final Force movement;
    /** Jump force. */
    private final Force jump;

    /**
     * Constructor.
     * 
     * @param services The services reference.
     */
    public Mario(Services services)
    {
        super(SETUP, services);

        jump = new Force();
        movement = new Force();

        transformable = new TransformableModel(this, SETUP.getConfigurer());
        addTrait(transformable);

        body = new BodyModel(this);
        addTrait(body);

        tileCollidable = new TileCollidableModel(this, SETUP.getConfigurer(), services);
        tileCollidable.addListener(this);

        collidable = new CollidableModel(this, SETUP.getConfigurer(), services);
        addTrait(collidable);
        collidable.setCollisionVisibility(true);
        collidable.setOrigin(Origin.CENTER_BOTTOM);

        body.setVectors(movement, jump);
        body.setDesiredFps(services.get(Integer.class).intValue());
        body.setMass(2.0);

        jump.setVelocity(0.1);
        jump.setDestination(0.0, 0.0);

        keyboard = services.get(Keyboard.class);
        camera = services.get(Camera.class);

        surface = Drawable.loadSpriteAnimated(SETUP.surface, 7, 1);
        surface.setOrigin(Origin.CENTER_BOTTOM);
        surface.setFrameOffsets(-1, 0);
        transformable.teleport(80, 32);
    }

    @Override
    public void update(double extrp)
    {
        movement.setDirection(Direction.ZERO);
        if (keyboard.isPressed(Keyboard.LEFT))
        {
            movement.setDirection(-2, 0);
        }
        if (keyboard.isPressed(Keyboard.RIGHT))
        {
            movement.setDirection(2, 0);
        }
        if (keyboard.isPressedOnce(Keyboard.UP))
        {
            jump.setDirection(0.0, 8.0);
        }
        movement.update(extrp);
        jump.update(extrp);
        body.update(extrp);
        tileCollidable.update(extrp);
        collidable.update(extrp);

        if (transformable.getY() < 0)
        {
            transformable.teleportY(80);
            body.resetGravity();
        }
        camera.follow(transformable);
        surface.setLocation(camera, transformable);
    }

    @Override
    public void render(Graphic g)
    {
        surface.render(g);
        g.setColor(ColorRgba.GREEN);
        collidable.render(g);
    }

    @Override
    public void notifyTileCollided(Tile tile, Axis axis)
    {
        if (Axis.Y == axis)
        {
            body.resetGravity();
        }
    }
}
