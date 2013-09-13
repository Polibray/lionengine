package com.b3dgs.lionengine.example.c_platform.e_lionheart.entity;

import java.util.Locale;

/**
 * List of entity states.
 */
public enum TypeEntityState implements TypeState
{
    /** Idle state. */
    IDLE,
    /** Die state. */
    DIE,
    /** Die state. */
    DEAD,
    /** Fallen state. */
    FALLEN,
    /** Walk state. */
    WALK,
    /** Turning state. */
    TURN,
    /** Jumping state. */
    JUMP,
    /** Falling state. */
    FALL;

    /** Animation name. */
    private final String animationName;

    /**
     * Constructor.
     */
    private TypeEntityState()
    {
        animationName = name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getAnimationName()
    {
        return animationName;
    }
}