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
package au.edu.usq.solr.index.rule.impl;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import au.edu.usq.solr.index.AddDocType;
import au.edu.usq.solr.index.FieldType;
import au.edu.usq.solr.index.rule.AbstractRule;
import au.edu.usq.solr.index.rule.RuleException;

public class DeleteFieldRule extends AbstractRule {

    private Logger log = Logger.getLogger(DeleteFieldRule.class);

    private String fieldName;

    private String regex;

    public DeleteFieldRule(String fieldName) {
        this(fieldName, "^\\s*$");
    }

    public DeleteFieldRule(String fieldName, String regex) {
        super("DeleteField");
        this.fieldName = fieldName;
        this.regex = regex;
    }

    @Override
    public void run(Reader in, Writer out) throws RuleException {
        log.info("Deleting '" + fieldName + "' fields that match '" + regex
            + "'");
        try {
            JAXBContext jc = JAXBContext.newInstance(AddDocType.class);
            Unmarshaller u = jc.createUnmarshaller();
            AddDocType addDoc = (AddDocType) u.unmarshal(in);
            List<FieldType> fields = addDoc.getFields(fieldName);
            List<FieldType> deletedFields = new ArrayList<FieldType>();
            for (FieldType field : fields) {
                String value = field.getValue();
                boolean match = Pattern.matches(regex, field.getValue());
                if (match) {
                    deletedFields.add(field);
                    log.info("Deleted matching value '" + value + "'");
                } else {
                    log.info("Keep unmatched value '" + value + "'");
                }
            }
            for (FieldType field : deletedFields) {
                fields.remove(field);
            }
            Marshaller m = jc.createMarshaller();
            m.marshal(addDoc, out);
        } catch (JAXBException jaxbe) {
            throw new RuleException(jaxbe.getLinkedException());
        }
    }
}
