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
package com.b3dgs.lionengine.editor.project;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;

import com.b3dgs.lionengine.core.UtilityMedia;
import com.b3dgs.lionengine.editor.UtilEclipse;
import com.b3dgs.lionengine.editor.dialogs.ImportProjectDialog;

/**
 * Import project handler implementation.
 * 
 * @author Pierre-Alexandre
 */
public class ImportProjectHandler
{
    /**
     * Import the project and update the view.
     * 
     * @param project The project to import.
     * @param partService The part service.
     */
    public static void importProject(Project project, EPartService partService)
    {
        UtilityMedia.setResourcesDirectory(project.getResourcesPath().getPath());

        final ProjectsPart part = UtilEclipse.getPart(partService, ProjectsPart.ID, ProjectsPart.class);
        ProjectsModel.INSTANCE.setRoot(project.getPath());
        part.setInput(project, partService);
    }

    /**
     * Execute the handler.
     * 
     * @param shell The shell reference.
     * @param partService The part service reference.
     */
    @Execute
    public void execute(Shell shell, EPartService partService)
    {
        final ImportProjectDialog importProjectDialog = new ImportProjectDialog(shell);
        importProjectDialog.open();

        final Project project = importProjectDialog.getProject();
        if (project != null)
        {
            ImportProjectHandler.importProject(project, partService);
        }
    }
}