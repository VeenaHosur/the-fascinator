/* 
 * The Fascinator - File System Harvester Plugin
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
package au.edu.usq.fascinator.harvester.geonames;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.edu.usq.fascinator.api.harvester.HarvesterException;
import au.edu.usq.fascinator.api.storage.DigitalObject;
import au.edu.usq.fascinator.api.storage.Payload;
import au.edu.usq.fascinator.api.storage.Storage;
import au.edu.usq.fascinator.api.storage.StorageException;
import au.edu.usq.fascinator.common.JsonConfigHelper;
import au.edu.usq.fascinator.common.harvester.impl.GenericHarvester;
import au.edu.usq.fascinator.common.storage.StorageUtils;

/**
 * Harvests Geonames Server
 * <p>
 * Configuration options:
 * <ul>
 * <li>countryInfo: country information file</li>
 * <li>baseDir: file to harvest</li>
 * </ul>
 * 
 * @author Linda Octalina
 */
public class GeonamesHarvester extends GenericHarvester {

    /** logging */
    private Logger log = LoggerFactory.getLogger(GeonamesHarvester.class);

    /**
     * List of header
     */
    private Map<Integer, String> header;

    /**
     * File consists of the country codes and country names
     */
    private File countryInfoFile;

    /** Folder consists of countries to be harvested */
    private File countryFolder;

    /**
     * Skos harvester constructor
     */
    public GeonamesHarvester() {
        super("geonames", "Geonames Server Harvester");
    }

    /**
     * Init method to initialise geonames harvester
     */
    @Override
    public void init() throws HarvesterException {
        JsonConfigHelper config;
        // Read config
        try {
            config = new JsonConfigHelper(getJsonConfig().toString());
        } catch (IOException ex) {
            throw new HarvesterException("Failed reading configuration", ex);
        }
        String countryFile = config.get("harvester/geonames/countryInfo");
        if (countryFile != "") {
            countryInfoFile = new File(countryFile);
        } else {
            try {
                countryInfoFile = new File(GeonamesHarvester.class.getResource(
                        "/countryInfo.txt").toURI());
            } catch (URISyntaxException e) {
                throw new HarvesterException("Error getting file URI");
            }
        }
        String countryFolderName = config.get(
                "harvester/geonames/countryFolder", "");
        if (countryFolderName != "") {
            countryFolder = new File(countryFolderName);
        } else {
            throw new HarvesterException("Geoname folder is not specified");
        }
    }

    /**
     * Harvest the next set of skos concept, and return their Object IDs
     * 
     * @return Set<String> The set of object IDs just harvested
     * @throws HarvesterException If there are errors
     */
    @Override
    public Set<String> getObjectIdList() throws HarvesterException {
        Set<String> geonamesObjectIdList = new HashSet<String>();

        try {
            BufferedReader input = new BufferedReader(new FileReader(
                    countryInfoFile));
            String line = null;
            try {
                Boolean process = false;
                while ((line = input.readLine()) != null) {
                    if (process) {
                        String splitArray[] = line.split("\t");
                        String objectId = createGeonameObject(splitArray);
                        if (objectId != null)
                            geonamesObjectIdList.add(objectId);
                    }
                    if (line.startsWith("#ISO\t")) {
                        setHeader(line);
                        process = true;
                    }
                }
            } catch (StorageException e) {
                throw new HarvesterException("Fail to create new object");
            } finally {
                input.close();
            }
        } catch (FileNotFoundException e) {
            throw new HarvesterException("Fail to read file:"
                    + countryInfoFile.getName());
        } catch (IOException e) {
            throw new HarvesterException("Fail to close input reader");
        }
        return geonamesObjectIdList;
    }

    /**
     * Check if there is more object
     * 
     * @return true of there is more object
     */
    @Override
    public boolean hasMoreObjects() {
        return false;
    }

    /** 
     * Set header list
     * @param line
     */
    private void setHeader(String line) {
        header = new HashMap();
        line = line.substring(1, line.length());
        String lineSplits[] = line.split("\t");
        int count = 0;
        for (String lineSplit : lineSplits) {
            //Hard coded for now
            if (lineSplit.startsWith("Area"))
                lineSplit = "AreaInSqKm";
            lineSplit = lineSplit.trim().replace(" ", "_");
            header.put(count, lineSplit);
            count += 1;
        }
    }

    /**
     * Get header list
     * @return
     */
    public Map<Integer, String> getHeader() {
        return header;
    }

    /**
     * Create skos digital object and the payload
     * 
     * @param conceptUri concept to be created
     * @param newConceptUri new uri to be used in fascinator
     * @return created object id
     * @throws HarvesterException if fail to harvest skos concept
     * @throws StorageException if fail to create new object
     * @throws IOException if fail to create new payload
     */
    private String createGeonameObject(String[] geonameDetail)
            throws HarvesterException, StorageException, IOException {

        String ISOcode = "";
        String geonameName = "";
        String geonameUrl = "";
        JsonConfigHelper json = new JsonConfigHelper();
        log.info("Processing... {}", geonameDetail[0]);
        for (int count = 0; count < geonameDetail.length; count++) {
            String headerStr = header.get(count);
            String geonameDetailValue = geonameDetail[count];
            json.set(headerStr, geonameDetailValue);
            if (headerStr.equals("geonameid")) {
                geonameUrl = "http://geonames.org/" + geonameDetailValue;
                json.set("dc_identifier", geonameUrl);
            }
            if (headerStr.equals("Country")) {
                geonameName = geonameDetailValue;
                json.set("dc_title", geonameDetailValue);
            }
            if (headerStr.equals("ISO")) {
                ISOcode = geonameDetailValue;
            }
            json.set(headerStr, geonameDetailValue);
        }

        File fileName = new File(countryFolder, ISOcode + ".txt");
        if (fileName.exists()) {
            Storage storage = getStorage();
            log.info("Creating Geoname object: {} with url: {}", geonameName, geonameUrl);
            String oid = DigestUtils.md5Hex(geonameUrl);
            DigitalObject object = StorageUtils.getDigitalObject(storage, oid);

            // Payload to store the country metadata
            Payload payloadMetadata = StorageUtils.createOrUpdatePayload(object, ISOcode + ".json",
                    IOUtils.toInputStream(json.toString(), "UTF-8"));
            payloadMetadata.setContentType("text/json");
            payloadMetadata.close();
            
            // Payload to store the country areas
            Payload payload = StorageUtils.createOrUpdatePayload(object, ISOcode + ".txt",
                    new FileInputStream(fileName));
            payload.setContentType("text/plain");
            payload.close();

            // update object metadata
            Properties props = object.getMetadata();
            props.setProperty("ISOcode", ISOcode);
            props.setProperty("countryName", geonameName);
            props.setProperty("render-pending", "true");

            object.close();
            return object.getId();
        }
        return null;

    }

}