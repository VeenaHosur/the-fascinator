/* 
 * The Fascinator - File System storage plugin
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
package au.edu.usq.fascinator.storage.filesystem;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.edu.usq.fascinator.api.PluginDescription;
import au.edu.usq.fascinator.api.storage.DigitalObject;
import au.edu.usq.fascinator.api.storage.Storage;
import au.edu.usq.fascinator.api.storage.StorageException;
import au.edu.usq.fascinator.common.FascinatorHome;
import au.edu.usq.fascinator.common.JsonConfig;

/**
 * File system storage plugin based on Dflat/Pairtree
 * 
 * @author Oliver Lucido
 */
public class FileSystemStorage implements Storage {

    private static final String DEFAULT_HOME_DIR = FascinatorHome
            .getPath("storage");

    private static final String DEFAULT_EMAIL = "fascinator@usq.edu.au";

    private final Logger log = LoggerFactory.getLogger(FileSystemStorage.class);

    private File homeDir;

    private String email;

    private Set<String> objectList;

    private static String DEFAULT_METADATA_PAYLOAD = "TF-OBJ-META";

    @Override
    public String getId() {
        return "file-system";
    }

    @Override
    public String getName() {
        return "File System Storage";
    }

    /**
     * Gets a PluginDescription object relating to this plugin.
     * 
     * @return a PluginDescription
     */
    @Override
    public PluginDescription getPluginDetails() {
        return new PluginDescription(this);
    }

    public File getHomeDir() {
        return homeDir;
    }

    @Override
    public void init(String jsonString) throws StorageException {
        try {
            JsonConfig config = new JsonConfig(new ByteArrayInputStream(
                    jsonString.getBytes("UTF-8")));
            setVariable(config);
        } catch (UnsupportedEncodingException e) {
            throw new StorageException(e);
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void init(File jsonFile) throws StorageException {
        try {
            JsonConfig config = new JsonConfig(jsonFile);
            setVariable(config);
        } catch (IOException ioe) {
            throw new StorageException(ioe);
        }
    }

    private void setVariable(JsonConfig config) {
        email = config.get("email", DEFAULT_EMAIL);
        homeDir = new File(config.get("storage/file-system/home",
                DEFAULT_HOME_DIR), DigestUtils.md5Hex(email));
        if (!homeDir.exists()) {
            homeDir.mkdirs();
        }
    }

    @Override
    public void shutdown() throws StorageException {
        // Don't need to do anything
    }

    private File getPath(String oid) {
        String dir = oid.substring(0, 2) + File.separator + oid.substring(2, 4)
                + File.separator + oid.substring(4, 6) + File.separator;
        return new File(homeDir, dir + oid);
    }

    @Override
    public DigitalObject createObject(String oid) throws StorageException {
        // log.debug("createObject(" + oid + ")");
        File objHome = getPath(oid);
        if (objHome.exists()) {
            throw new StorageException("oID '" + oid
                    + "' already exists in storage.");
        }
        return new FileSystemDigitalObject(objHome, oid);
    }

    @Override
    public DigitalObject getObject(String oid) throws StorageException {
        File objHome = getPath(oid);
        if (objHome.exists()) {
            FileSystemDigitalObject obj = new FileSystemDigitalObject(objHome,
                    oid);
            // log.debug("getObject(" + oid + "), sourceId: " +
            // obj.getSourceId());
            return obj;
        }
        throw new StorageException("oID '" + oid
                + "' doesn't exist in storage.");
    }

    @Override
    public void removeObject(String oid) throws StorageException {
        // log.debug("removeObject(" + oid + ")");
        File objHome = getPath(oid);
        if (objHome.exists()) {
            DigitalObject object = new FileSystemDigitalObject(objHome, oid);
            String[] oldManifest = {};
            oldManifest = object.getPayloadIdList().toArray(oldManifest);
            for (String pid : oldManifest) {
                try {
                    object.removePayload(pid);
                } catch (StorageException ex) {
                    log.error("Error deleting payload", ex);
                }
            }
            try {
                object.close();
                FileUtils.deleteDirectory(objHome);
            } catch (IOException ex) {
                throw new StorageException("Error deleting object", ex);
            }
        } else {
            throw new StorageException("oID '" + oid
                    + "' doesn't exist in storage : " + objHome.getPath());
        }
    }

    @Override
    public Set<String> getObjectIdList() {
        // log.debug("getObjectIdList()");
        if (objectList == null) {
            objectList = new HashSet<String>();

            List<File> files = new ArrayList<File>();
            listFileRecur(files, homeDir);

            for (File file : files) {
                Properties sofMeta = new Properties();
                InputStream is;
                try {
                    is = new FileInputStream(file);
                    sofMeta.load(is);
                    String objectId = sofMeta.getProperty("objectId");
                    if (objectId != null) {
                        objectList.add(objectId);
                    } else {
                        log.error("Null object ID found in "
                                + file.getAbsolutePath());
                    }
                } catch (FileNotFoundException e) {
                    log.error("Error reading object metadata file", e);
                } catch (IOException e) {
                    log.error("Error loading properties metadata", e);
                }

            }
        }
        return objectList;
    }

    private void listFileRecur(List<File> files, File path) {
        // log.debug("listFileRecur()");
        if (path.isDirectory()) {
            for (File file : path.listFiles()) {
                if (path.isDirectory()) {
                    listFileRecur(files, file);
                } else {
                    if (file.getName().equals(DEFAULT_METADATA_PAYLOAD)) {
                        files.add(file);
                    }
                }
            }
        } else {
            if (path.getName().equals(DEFAULT_METADATA_PAYLOAD)) {
                files.add(path);
            }
        }
    }
}
