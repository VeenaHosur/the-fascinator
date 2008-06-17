/* 
 * Sun of Fedora - Solr Portal
 * Copyright (C) 2008  University of Southern Queensland
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
package au.edu.usq.solr.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class MapEntryListType {

    @XmlElements( { @XmlElement(name = "field"), @XmlElement(name = "user") })
    private List<MapEntryType> entries;

    public MapEntryListType() {
    }

    public MapEntryListType(Map<String, String> map) {
        entries = new ArrayList<MapEntryType>();
        for (String key : map.keySet()) {
            entries.add(new MapEntryType(key, map.get(key)));
        }
    }

    public List<MapEntryType> getEntries() {
        return entries;
    }
}
