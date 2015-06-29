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
package com.b3dgs.lionengine.editor.project.tester;

import org.eclipse.core.expressions.PropertyTester;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.core.Media;
import com.b3dgs.lionengine.editor.project.Project;
import com.b3dgs.lionengine.editor.project.ProjectsModel;
import com.b3dgs.lionengine.game.configurer.ConfigCollisionGroup;
import com.b3dgs.lionengine.stream.Stream;
import com.b3dgs.lionengine.stream.XmlNode;

/**
 * Test if the folder contains collisions file.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public class CollisionsTester
        extends PropertyTester
{
    /** Can add formulas property. */
    private static final String PROPERTY_ADD_COLLISIONS = "addCollisions";
    /** Can edit formulas property. */
    private static final String PROPERTY_EDIT_COLLISIONS = "editCollisions";

    /**
     * Check if the media is a collisions descriptor.
     * 
     * @param media The media to test.
     * @return <code>true</code> if valid, <code>false</code> else.
     */
    public static boolean isCollisionsFile(Media media)
    {
        try
        {
            final XmlNode root = Stream.loadXml(media);
            return ConfigCollisionGroup.COLLISIONS.equals(root.getNodeName());
        }
        catch (final LionEngineException exception)
        {
            return false;
        }
    }

    /*
     * PropertyTester
     */

    @Override
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
    {
        final Project project = Project.getActive();
        if (project != null)
        {
            final Media selection = ProjectsModel.INSTANCE.getSelection();
            if (selection != null)
            {
                if (PROPERTY_EDIT_COLLISIONS.equals(property))
                {
                    return isCollisionsFile(selection);
                }
                if (PROPERTY_ADD_COLLISIONS.equals(property))
                {
                    return selection.getFile().isDirectory();
                }
            }
        }
        return false;
    }
}