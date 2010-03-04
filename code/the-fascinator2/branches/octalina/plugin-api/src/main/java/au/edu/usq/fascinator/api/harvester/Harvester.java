/* 
 * The Fascinator - Plugin API
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
package au.edu.usq.fascinator.api.harvester;

import java.io.File;
import java.util.List;

import au.edu.usq.fascinator.api.Plugin;
import au.edu.usq.fascinator.api.storage.DigitalObject;

/**
 * Provides digital objects from any data source
 * 
 * @author Oliver Lucido
 */
public interface Harvester extends Plugin {

    /**
     * Gets a list of digital objects. If there are no objects, this method
     * should return an empty list, not null.
     * 
     * @return a list of objects, possibly empty
     * @throws HarvesterException if there was an error retrieving the objects
     */
    public List<DigitalObject> getObjects() throws HarvesterException;

    /**
     * Get an individual uploaded file as a digital object.
     * For consistancy this should be in a list.
     *
     * @return a list of one object
     * @throws HarvesterException if there was an error retrieving the objects
     */
    public List<DigitalObject> getObject(File uploadedFile) throws HarvesterException;

    /**
     * Gets a list of deleted digital objects. If there are no deleted objects,
     * this method should return an empty list, not null.
     * 
     * @return a list of objects, possibly empty
     * @throws HarvesterException if there was an error retrieving the objects
     */
    public List<DigitalObject> getDeletedObjects() throws HarvesterException;

    /**
     * Tests whether there are more objects to retrieve. This method should
     * return true if called before getObjects.
     * 
     * @return true if there are more objects to retrieve, false otherwise
     */
    public boolean hasMoreObjects();

    /**
     * Tests whether there are more deleted objects to retrieve. This method
     * should return true if called before getDeletedObjects.
     * 
     * @return true if there are more deleted objects to retrieve, false
     *         otherwise
     */
    public boolean hasMoreDeletedObjects();

}
