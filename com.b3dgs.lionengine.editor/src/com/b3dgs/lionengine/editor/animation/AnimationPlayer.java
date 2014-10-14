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
package com.b3dgs.lionengine.editor.animation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.b3dgs.lionengine.anim.Animation;
import com.b3dgs.lionengine.editor.UtilEclipse;
import com.b3dgs.lionengine.editor.UtilSwt;

/**
 * Represents the animation player view, where the animation can be controlled.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public class AnimationPlayer
{
    /** Play icon. */
    private static final Image ICON_ANIM_PLAY = UtilEclipse.getIcon("animation-editor", "anim-play.png");
    /** Pause icon. */
    private static final Image ICON_ANIM_PAUSE = UtilEclipse.getIcon("animation-editor", "anim-pause.png");
    /** Stop icon. */
    private static final Image ICON_ANIM_STOP = UtilEclipse.getIcon("animation-editor", "anim-stop.png");
    /** Previous animation icon. */
    private static final Image ICON_ANIM_PREVIOUS = UtilEclipse.getIcon("animation-editor", "anim-prev.png");
    /** Next animation icon. */
    private static final Image ICON_ANIM_NEXT = UtilEclipse.getIcon("animation-editor", "anim-next.png");

    /** Animation list. */
    final AnimationList animationList;
    /** Animation renderer. */
    final AnimationRenderer animationRenderer;
    /** Previous anim. */
    Button previousAnim;
    /** Play anim. */
    Button playAnim;
    /** Pause anim. */
    Button pauseAnim;
    /** Stop anim. */
    Button stopAnim;
    /** Next anim. */
    Button nextAnim;

    /**
     * Constructor.
     * 
     * @param animationList The animation list reference.
     * @param animationRenderer The animation renderer reference.
     */
    public AnimationPlayer(AnimationList animationList, AnimationRenderer animationRenderer)
    {
        this.animationList = animationList;
        this.animationRenderer = animationRenderer;
    }

    /**
     * Create the player area of the animation.
     * 
     * @param parent The composite parent.
     */
    public void createAnimationPlayer(Composite parent)
    {
        final Composite animatorPlayer = new Composite(parent, SWT.NONE);
        animatorPlayer.setLayout(new GridLayout(5, true));
        animatorPlayer.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        createButtonPreviousAnim(animatorPlayer);
        createButtonPlayAnim(animatorPlayer);
        createButtonPauseAnim(animatorPlayer);
        createButtonStopAnim(animatorPlayer);
        createButtonNextAnim(animatorPlayer);
    }

    /**
     * Called when animation is finished.
     */
    public void notifyAnimationFinished()
    {
        previousAnim.setEnabled(true);
        playAnim.setEnabled(true);
        pauseAnim.setEnabled(false);
        stopAnim.setEnabled(false);
        nextAnim.setEnabled(true);
    }

    /**
     * Create the previous animation button.
     * 
     * @param parent The composite parent.
     */
    private void createButtonPreviousAnim(Composite parent)
    {
        previousAnim = UtilSwt.createButton(parent, null, AnimationPlayer.ICON_ANIM_PREVIOUS);
        previousAnim.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent)
            {
                animationList.setNextSelection(-1);
            }
        });
    }

    /**
     * Create the play animation button.
     * 
     * @param parent The composite parent.
     */
    private void createButtonPlayAnim(Composite parent)
    {
        playAnim = UtilSwt.createButton(parent, null, AnimationPlayer.ICON_ANIM_PLAY);
        playAnim.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent)
            {
                final Animation animation = animationList.getSelectedObject();
                if (animation != null)
                {
                    animationRenderer.setAnimation(animation);
                    animationRenderer.setPause(false);
                    previousAnim.setEnabled(false);
                    playAnim.setEnabled(false);
                    pauseAnim.setEnabled(true);
                    stopAnim.setEnabled(true);
                    nextAnim.setEnabled(false);
                }
            }
        });
    }

    /**
     * Create the pause animation button.
     * 
     * @param parent The composite parent.
     */
    private void createButtonPauseAnim(Composite parent)
    {
        pauseAnim = UtilSwt.createButton(parent, null, AnimationPlayer.ICON_ANIM_PAUSE);
        pauseAnim.setEnabled(false);
        pauseAnim.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent)
            {
                animationRenderer.setPause(true);
                previousAnim.setEnabled(false);
                playAnim.setEnabled(true);
                pauseAnim.setEnabled(false);
                nextAnim.setEnabled(false);
            }
        });
    }

    /**
     * Create the stop animation button.
     * 
     * @param parent The composite parent.
     */
    private void createButtonStopAnim(Composite parent)
    {
        stopAnim = UtilSwt.createButton(parent, null, AnimationPlayer.ICON_ANIM_STOP);
        stopAnim.setEnabled(false);
        stopAnim.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent)
            {
                animationRenderer.stopAnimation();
                previousAnim.setEnabled(true);
                playAnim.setEnabled(true);
                pauseAnim.setEnabled(false);
                stopAnim.setEnabled(false);
                nextAnim.setEnabled(true);
            }
        });
    }

    /**
     * Create the next animation button.
     * 
     * @param parent The composite parent.
     */
    private void createButtonNextAnim(Composite parent)
    {
        nextAnim = UtilSwt.createButton(parent, null, AnimationPlayer.ICON_ANIM_NEXT);
        nextAnim.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent)
            {
                animationList.setNextSelection(1);
            }
        });
    }
}