/* 
 * The Fascinator - Solr Portal
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
package au.edu.usq.fascinator.harvester.fedora.restclient;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "result", namespace = ResultType.NAMESPACE)
@XmlAccessorType(XmlAccessType.NONE)
public class ResultType {

    public static final String NAMESPACE = "http://www.fedora.info/definitions/1/0/types/";

    @XmlElement(name = "listSession", namespace = NAMESPACE)
    private ListSessionType listSession;

    @XmlElementWrapper(name = "resultList", namespace = NAMESPACE)
    @XmlElement(name = "objectFields", namespace = NAMESPACE)
    private List<ObjectFieldType> objectFields;

    public ListSessionType getListSession() {
        return listSession;
    }

    public List<ObjectFieldType> getObjectFields() {
        return objectFields;
    }
}