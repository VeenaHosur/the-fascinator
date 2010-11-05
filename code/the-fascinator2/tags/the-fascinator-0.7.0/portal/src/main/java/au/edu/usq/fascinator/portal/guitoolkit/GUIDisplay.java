/*
 * The Fascinator - GUI Display
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

import java.io.ByteArrayOutputStream;

import au.edu.usq.fascinator.common.JsonConfig;
import au.edu.usq.fascinator.portal.FormData;
import au.edu.usq.fascinator.portal.JsonSessionState;
import au.edu.usq.fascinator.portal.services.DynamicPageService;

/**
 * Displays arbitrary content in the portal.
 * 
 * @author Greg Pendlebury
 */
public class GUIDisplay {
    private DynamicPageService pageService;

    public GUIDisplay(JsonConfig config, DynamicPageService renderer) {
        pageService = renderer;
    }

    public String renderTemplate(String portalId, String template,
            FormData formData, JsonSessionState sessionState) {
        return renderTemplate(portalId, template, formData, sessionState, false);
    }

    public String renderTemplate(String portalId, String template,
            FormData formData, JsonSessionState sessionState, boolean useLayout) {
        // Do we want to see our content wrapped in app. layout?
        if (!useLayout) {
            // If not, pretend this is an ajax call
            template += ".ajax";
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        pageService.render(portalId, template, out, formData, sessionState);

        return out.toString();
    }
}
