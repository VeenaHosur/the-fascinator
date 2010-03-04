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
package au.edu.usq.fascinator.api.storage;

/**
 * Types of payloads
 * 
 * @author Oliver Lucido
 */
public enum PayloadType {

    /** Descriptions of an object */
    Annotation,

    /** Original or source content */
    Data,

    /** Content derived from another source */
    Enrichment,

    /** Content that is stored externally */
    External,

    /** Content that is uploaded to the server
        and should be forced to store locally */
    Uploaded

}
