/*
 * The Fascinator - GUI Toolkit
 * Copyright (C) 2010 University of Southern Queensland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package au.edu.usq.fascinator.portal.guitoolkit;

import au.edu.usq.fascinator.common.JsonConfig;
import au.edu.usq.fascinator.portal.services.DynamicPageService;
import java.util.List;

/**
 * A simple wrapper class for the various toolkit objects.
 * Responsible for instantiating them and exposing them to
 * the portal as required.
 *
 * @author Greg Pendlebury
 */
public class GUIToolkit {
    private JsonConfig sysConfig;

    public GUIToolkit(JsonConfig config) {
        sysConfig = config;
    }

    public GUIDisplay getDisplayComponent(DynamicPageService renderer) {
        return new GUIDisplay(sysConfig, renderer);
    }

    public GUIFileUploader getFileUploader(List<String> user_roles) {
        return new GUIFileUploader(sysConfig, user_roles);
    }

    public GUIFormRenderer getFormRenderer() {
        return new GUIFormRenderer(sysConfig);
    }
}
