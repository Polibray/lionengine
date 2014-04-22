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
package com.b3dgs.lionengine.tutorials.mario.d;

import java.util.EnumMap;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.anim.Animation;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.Movement;
import com.b3dgs.lionengine.game.platform.entity.EntityPlatform;

/**
 * Abstract entity base implementation.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
abstract class Entity
        extends EntityPlatform
{
    /** Map reference. */
    protected final Map map;
    /** Desired fps value. */
    protected final int desiredFps;
    /** Movement force. */
    protected final Movement movement;
    /** Movement jump force. */
    protected final Force jumpForce;
    /** Animations list. */
    private final EnumMap<EntityState, Animation> animations;
    /** Jump force. */
    protected double jumpForceValue;
    /** Movement max speed. */
    protected double movementSpeedValue;
    /** Key right state. */
    protected boolean right;
    /** Key left state. */
    protected boolean left;
    /** Key up state. */
    protected boolean up;
    /** Entity state. */
    protected EntityState state;
    /** Old state. */
    protected EntityState stateOld;
    /** Collision state. */
    protected EntityCollision coll;

    /**
     * Constructor.
     * 
     * @param setup The setup reference.
     */
    protected Entity(SetupEntity setup)
    {
        super(setup);
        map = setup.map;
        desiredFps = setup.desiredFps;
        animations = new EnumMap<>(EntityState.class);
        jumpForceValue = getDataDouble("jumpSpeed", "data");
        movementSpeedValue = getDataDouble("movementSpeed", "data");
        movement = new Movement();
        jumpForce = new Force();
        state = EntityState.IDLE;
        setMass(getDataDouble("mass", "data"));
        setFrameOffsets(0, 1);
        loadAnimations();
    }

    /**
     * Check if hero can jump.
     * 
     * @return <code>true</code> if can jump, <code>false</code> else.
     */
    public boolean canJump()
    {
        return isOnGround();
    }

    /**
     * Check if hero is jumping.
     * 
     * @return <code>true</code> if jumping, <code>false</code> else.
     */
    public boolean isJumping()
    {
        return getLocationY() > getLocationOldY();
    }

    /**
     * Check if hero is falling.
     * 
     * @return <code>true</code> if falling, <code>false</code> else.
     */
    public boolean isFalling()
    {
        return getLocationY() < getLocationOldY();
    }

    /**
     * Check if entity is on ground.
     * 
     * @return <code>true</code> if on ground, <code>false</code> else.
     */
    public boolean isOnGround()
    {
        return coll == EntityCollision.GROUND;
    }

    /**
     * Load all existing animations defined in the xml file.
     */
    private void loadAnimations()
    {
        for (final EntityState state : EntityState.values())
        {
            try
            {
                animations.put(state, getDataAnimation(state.getAnimationName()));
            }
            catch (final LionEngineException exception)
            {
                continue;
            }
        }
    }

    /**
     * Check the map limit and apply collision if necessary.
     */
    private void checkMapLimit()
    {
        final int limitLeft = 0;
        if (getLocationX() < limitLeft)
        {
            setLocationX(limitLeft);
            movement.reset();
        }
        final int limitRight = map.getWidthInTile() * map.getTileWidth();
        if (getLocationX() > limitRight)
        {
            setLocationX(limitRight);
            movement.reset();
        }
    }

    /**
     * Update the forces depending of the pressed key.
     */
    private void updateForces()
    {
        movement.setForceToReach(Force.ZERO);
        final double speed;
        if (right && !left)
        {
            speed = movementSpeedValue;
        }
        else if (left && !right)
        {
            speed = -movementSpeedValue;
        }
        else
        {
            speed = 0.0;
        }
        movement.setForceToReach(speed, 0.0);

        if (up && canJump())
        {
            jumpForce.setForce(0.0, jumpForceValue);
            resetGravity();
            coll = EntityCollision.NONE;
        }
    }

    /**
     * Update entity states.
     */
    private void updateStates()
    {
        final double diffHorizontal = getDiffHorizontal();
        stateOld = state;

        if (diffHorizontal != 0.0)
        {
            mirror(diffHorizontal < 0.0);
        }

        final boolean mirror = getMirror();
        if (!isOnGround())
        {
            state = EntityState.JUMP;
        }
        else if (mirror && right && diffHorizontal < 0.0)
        {
            state = EntityState.TURN;
        }
        else if (!mirror && left && diffHorizontal > 0.0)
        {
            state = EntityState.TURN;
        }
        else if (diffHorizontal != 0.0)
        {
            state = EntityState.WALK;
        }
        else
        {
            state = EntityState.IDLE;
        }
    }

    /*
     * EntityPlatform
     */

    @Override
    protected void handleActions(double extrp)
    {
        updateForces();
        updateStates();
    }

    @Override
    protected void handleMovements(double extrp)
    {
        movement.update(extrp);
        updateGravity(extrp, desiredFps, jumpForce, movement.getForce());
        updateMirror();
    }

    @Override
    protected void handleCollisions(double extrp)
    {
        checkMapLimit();
        coll = EntityCollision.NONE;
        if (getLocationY() < 31)
        {
            teleportY(31);
            coll = EntityCollision.GROUND;
        }
    }

    @Override
    protected void handleAnimations(double extrp)
    {
        // Assign an animation for each state
        if (state == EntityState.WALK)
        {
            setAnimSpeed(Math.abs(movement.getForce().getForceHorizontal()) / 12.0);
        }
        // Play the assigned animation
        if (stateOld != state)
        {
            play(animations.get(state));
        }
        updateAnimation(extrp);
    }
}