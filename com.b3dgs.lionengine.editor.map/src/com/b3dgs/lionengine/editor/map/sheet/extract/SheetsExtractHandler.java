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
package com.b3dgs.lionengine.editor.map.sheet.extract;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

/**
 * Extract sheets handler implementation.
 */
public final class SheetsExtractHandler
{
    /** Element ID. */
    public static final String ID = "menu.map.extract-sheets";

    /**
     * Create handler.
     */
    public SheetsExtractHandler()
    {
        super();
    }

    /**
     * Execute the handler.
     * 
     * @param shell The shell reference.
     */
    @Execute
    public void execute(Shell shell)
    {
        final SheetsExtractDialog sheetsExtractDialog = new SheetsExtractDialog(shell);
        sheetsExtractDialog.open();
        if (!sheetsExtractDialog.isCanceled())
        {
            sheetsExtractDialog.save();
        }
    }
}
