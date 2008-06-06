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
package au.edu.usq.solr.portal;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.tapestry.services.Context;

public class State {

    private static final String DEFAULT_RESOURCE = "/WEB-INF/config.properties";

    private static final String SOLR_BASE_URL_KEY = "solr.base.url";

    private static final String SOLR_BASE_URL = "http://localhost:8080/solr";

    private Logger log = Logger.getLogger(State.class);

    private Properties props;

    private PortalManager portalManager;

    private Portal currentPortal;

    public State(Context context) {
        props = new Properties();
        try {
            URL configProps = context.getResource(DEFAULT_RESOURCE);
            InputStream configIn = configProps.openStream();
            props.load(configIn);
            configIn.close();
        } catch (Exception e) {
            log.warn("Failed to load defaults", e);
            setSolrBaseUrl(SOLR_BASE_URL);
        }
    }

    public PortalManager getPortalManager() {
        if (portalManager == null) {
            portalManager = new PortalManager();
        }
        return portalManager;
    }

    public void setSolrBaseUrl(String solrBaseUrl) {
        setProperty(SOLR_BASE_URL_KEY, solrBaseUrl);
    }

    public String getSolrBaseUrl() {
        return getProperty(SOLR_BASE_URL_KEY);
    }

    public String getPortalName() {
        return getCurrentPortal().getName();
    }

    public Portal getCurrentPortal() {
        if (currentPortal == null) {
            currentPortal = getPortalManager().getDefaultPortal();
        }
        return currentPortal;
    }

    public void setCurrentPortal(Portal currentPortal) {
        this.currentPortal = currentPortal;
    }

    public Set<Portal> getPortals() {
        return new TreeSet<Portal>(getPortalManager().getPortals().values());
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public void setProperty(String key, String value) {
        props.setProperty(key, value);
    }
}
