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
package au.edu.usq.solr.model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;

import au.edu.usq.solr.model.types.ArrayType;

public class ResponseTest {

    @Test
    public void response() throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Response.class);
        Unmarshaller um = jc.createUnmarshaller();
        Response response = (Response) um.unmarshal(getClass().getResourceAsStream(
            "/response.xml"));
        Assert.assertEquals(0.052, response.getQueryTime(), 0.0);
        Assert.assertEquals(10, response.getResult().getItems().size());
    }

    @Test
    public void arrayType() throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(ArrayType.class);
        Unmarshaller um = jc.createUnmarshaller();
        ArrayType array = (ArrayType) um.unmarshal(getClass().getResourceAsStream(
            "/array.xml"));
        Assert.assertEquals("facet.field", array.getName());
        Assert.assertEquals(4, array.getValues().size());
    }
}
