/*
 * The Fascinator - Plugin - Transformer - ICE 2
 * Copyright (C) 2009 University of Southern Queensland
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
package au.edu.usq.fascinator.transformer.ice2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import au.edu.usq.fascinator.api.storage.PayloadType;
import au.edu.usq.fascinator.common.MimeTypeUtil;
import au.edu.usq.fascinator.common.storage.impl.GenericPayload;

/**
 * Wraps a ZipEntry as a payload
 * 
 * @author Linda Octalina
 * @author Oliver Lucido
 */
public class IcePayload extends GenericPayload {

    private File file;

    private ZipEntry zipEntry;

    public IcePayload(File file) {
        this(file, null);
    }

    public IcePayload(File file, ZipEntry zipEntry) {
        this.file = file;
        this.zipEntry = zipEntry;
        String name = file.getName();
        if (zipEntry != null) {
            name = zipEntry.getName();
        }
        setId(name);
        setLabel(name);
        setContentType(MimeTypeUtil.getMimeType(name));
        setType(PayloadType.Enrichment);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (zipEntry == null) {
            return new FileInputStream(file);
        } else {
            ZipFile zipFile = new ZipFile(file);
            return zipFile.getInputStream(zipEntry);
        }
    }
}
