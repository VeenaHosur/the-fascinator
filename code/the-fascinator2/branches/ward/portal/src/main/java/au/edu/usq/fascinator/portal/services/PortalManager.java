/* 
 * The Fascinator - Portal
 * Copyright (C) 2008-2009 University of Southern Queensland
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
package au.edu.usq.fascinator.portal.services;

import java.util.Map;

import au.edu.usq.fascinator.portal.Portal;

public interface PortalManager {

    public static final String DEFAULT_PORTAL_NAME = "default";

    public static final String DEFAULT_PORTAL_HOME_DIR = "/opt/the-fascinator/config/portal";

    public static final String DEFAULT_PORTAL_HOME_DIR_DEV = "src/main/config/portal";

    public Map<String, Portal> getPortals();

    public Portal getDefault();

    public Portal get(String name);

    public void add(Portal portal);

    public void remove(String name);

    public void save(Portal portal);

    public void backup(Portal portal);

    public void indexObject(String objectId);

    public void indexPortal(Portal portal);
}
