/*
 * Copyright (C) 2013-2016 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.game.feature;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Service dependency injection. Any element annotated with will be injected with a compatible instance found in the
 * current {@link Service} during {@link Featurable#prepareFeatures(Featurable, Services)}.
 * <p>
 * It is automatically called by the:
 * </p>
 * <ul>
 * <li>{@link com.b3dgs.lionengine.game.object.Factory} after a call to
 * {@link com.b3dgs.lionengine.game.object.Factory#create(com.b3dgs.lionengine.Media)}.</li>
 * <li>{@link com.b3dgs.lionengine.game.handler.Handler} once a {@link Featurable} is added to the main list.</li>
 * </ul>
 * <p>
 * If manually used, do not forget to call {@link Featurable#prepareFeatures(Featurable, Services)}, else annotated
 * fields will remain <code>null</code>.
 * </p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service
{
    // Nothing
}