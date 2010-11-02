/* 
 * The Fascinator - Indexer
 * Copyright (C) 2009-2010 University of Southern Queensland
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
package au.edu.usq.fascinator.indexer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.jms.JMSException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.request.DirectXmlRequest;
import org.python.core.Py;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.edu.usq.fascinator.api.PluginDescription;
import au.edu.usq.fascinator.api.PluginException;
import au.edu.usq.fascinator.api.PluginManager;
import au.edu.usq.fascinator.api.indexer.Indexer;
import au.edu.usq.fascinator.api.indexer.IndexerException;
import au.edu.usq.fascinator.api.indexer.SearchRequest;
import au.edu.usq.fascinator.api.indexer.rule.RuleException;
import au.edu.usq.fascinator.api.storage.DigitalObject;
import au.edu.usq.fascinator.api.storage.Payload;
import au.edu.usq.fascinator.api.storage.Storage;
import au.edu.usq.fascinator.api.storage.StorageException;
import au.edu.usq.fascinator.common.JsonConfig;
import au.edu.usq.fascinator.common.JsonConfigHelper;
import au.edu.usq.fascinator.common.MessagingServices;
import au.edu.usq.fascinator.common.PythonUtils;

public class SolrIndexer implements Indexer {

    /** A fake OID for Anotar - Used in caching/execution */
    private static String ANOTAR_RULES_OID = "FakeAnotarRulesOid1234";

    /** The name of the core class inside rules files */
    private static String SCRIPT_CLASS_NAME = "IndexData";

    /** The name of the activation method required on instantiated classes */
    private static String SCRIPT_ACTIVATE_METHOD = "__activate__";

    /** Default payload for object metadata */
    private static String DEFAULT_METADATA_PAYLOAD = "TF-OBJ-META";

    /** Actual payload for object metadata */
    private String propertiesId;

    /** Logging */
    private Logger log = LoggerFactory.getLogger(SolrIndexer.class);

    /** Configuration */
    private JsonConfig config;

    /** Storage API */
    private Storage storage;

    /** Main Solr core */
    private SolrServer solr;

    /** Anotar core */
    private SolrServer anotar;

    /** Auto-commit flag for main core */
    private boolean autoCommit;

    /** Auto-commit flag for anotar core */
    private boolean anotarAutoCommit;

    /** Username for Solr */
    private String username;

    /** Password for Solr */
    private String password;

    /** Flag if init() has been run before */
    private boolean loaded;

    /** Keep track of custom parameters */
    private Map<String, String> customParams;

    /** Utility function for use inside python scripts */
    private PythonUtils pyUtils;

    /** Cache of instantiated python scripts */
    private HashMap<String, PyObject> scriptCache;

    /** Cache of instantiated config files */
    private HashMap<String, JsonConfigHelper> configCache;

    /** Flag for use of the cache */
    private boolean useCache;

    /** Messaging services */
    private MessagingServices messaging;

    /**
     * Get the ID of the plugin
     * 
     * @return String : The ID of this plugin
     */
    @Override
    public String getId() {
        return "solr";
    }

    /**
     * Get the name of the plugin
     * 
     * @return String : The name of this plugin
     */
    @Override
    public String getName() {
        return "Apache Solr Indexer";
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

    public SolrIndexer() {
        loaded = false;
    }

    /**
     * Initialize the plugin
     * 
     * @param jsonString The JSON configuration to use as a string
     * @throws IndexerException if errors occur during initialization
     */
    @Override
    public void init(String jsonString) throws IndexerException {
        try {
            config = new JsonConfig(new ByteArrayInputStream(
                    jsonString.getBytes("UTF-8")));
            init();
        } catch (UnsupportedEncodingException e) {
            throw new IndexerException(e);
        } catch (IOException e) {
            throw new IndexerException(e);
        }
    }

    /**
     * Initialize the plugin
     * 
     * @param jsonFile A file containing the JSON configuration
     * @throws IndexerException if errors occur during initialization
     */
    @Override
    public void init(File jsonFile) throws IndexerException {
        try {
            config = new JsonConfig(jsonFile);
            init();
        } catch (IOException ioe) {
            throw new IndexerException(ioe);
        }
    }

    /**
     * Private method wrapped by the above two methods to perform the actual
     * initialization after the JSON config is accessed.
     * 
     * @throws IndexerException if errors occur during initialization
     */
    private void init() throws IndexerException {
        if (!loaded) {
            loaded = true;

            String storageType = config.get("storage/type");
            try {
                storage = PluginManager.getStorage(storageType);
                storage.init(config.toString());
            } catch (PluginException pe) {
                throw new IndexerException(pe);
            }

            solr = initCore("solr");
            anotar = initCore("anotar");

            autoCommit = Boolean.parseBoolean(config.get(
                    "indexer/solr/autocommit", "true"));
            anotarAutoCommit = Boolean.parseBoolean(config.get(
                    "indexer/anotar/autocommit", "true"));
            propertiesId = config.get("indexer/propertiesId",
                    DEFAULT_METADATA_PAYLOAD);

            customParams = new HashMap<String, String>();

            try {
                pyUtils = new PythonUtils(config);
            } catch (PluginException ex) {
                throw new IndexerException(ex);
            }
            // Caching
            scriptCache = new HashMap<String, PyObject>();
            configCache = new HashMap<String, JsonConfigHelper>();
            useCache = Boolean.parseBoolean(config.get("indexer/useCache",
                    "true"));

            try {
                messaging = MessagingServices.getInstance();
            } catch (JMSException jmse) {
                log.error("Failed to start connection: {}", jmse.getMessage());
            }
        }
        loaded = true;
    }

    /**
     * Set a value in the custom parameters of the indexer.
     * 
     * @param property : The index to use
     * @param value : The value to store
     */
    public void setCustomParam(String property, String value) {
        customParams.put(property, value);
    }

    /**
     * Initialize a Solr core object.
     * 
     * @param coreName : The core to initialize
     * @return SolrServer : The initialized core
     */
    private SolrServer initCore(String coreName) {
        try {
            URI solrUri = new URI(config.get("indexer/" + coreName + "/uri"));
            CommonsHttpSolrServer thisCore = new CommonsHttpSolrServer(
                    solrUri.toURL());
            username = config.get("indexer/" + coreName + "/username");
            password = config.get("indexer/" + coreName + "/password");
            if (username != null && password != null) {
                UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                        username, password);
                HttpClient hc = ((CommonsHttpSolrServer) solr).getHttpClient();
                hc.getParams().setAuthenticationPreemptive(true);
                hc.getState().setCredentials(AuthScope.ANY, credentials);
            }
            return thisCore;
        } catch (MalformedURLException mue) {
            log.error(coreName + " : Malformed URL", mue);
        } catch (URISyntaxException urise) {
            log.error(coreName + " : Invalid URI", urise);
        } catch (IOException ioe) {
            log.error(coreName + " : Failed to read Solr configuration", ioe);
        }
        return null;
    }

    /**
     * Shutdown the plugin
     * 
     * @throws PluginException if any errors occur during shutdown
     */
    @Override
    public void shutdown() throws PluginException {
        pyUtils.shutdown();
    }

    /**
     * Return a reference to this plugins instantiated storage layer
     * 
     * @return Storage : the storage API being used
     */
    public Storage getStorage() {
        return storage;
    }

    /**
     * Perform a Solr search and stream the results into the provided output
     * 
     * @param request : A prepared SearchRequest object
     * @param response : The OutputStream to send results to
     * @throws IndexerException if there were errors during the search
     */
    @Override
    public void search(SearchRequest request, OutputStream response)
            throws IndexerException {
        SolrSearcher searcher = new SolrSearcher(
                ((CommonsHttpSolrServer) solr).getBaseURL());
        if (username != null && password != null) {
            searcher.authenticate(username, password);
        }
        InputStream result;
        try {
            StringBuilder extras = new StringBuilder();
            for (String name : request.getParamsMap().keySet()) {
                for (String param : request.getParams(name)) {
                    extras.append(name);
                    extras.append("=");
                    extras.append(URLEncoder.encode(param, "UTF-8"));
                    extras.append("&");
                }
            }
            extras.append("wt=json");
            result = searcher.get(request.getQuery(), extras.toString(), false);
            IOUtils.copy(result, response);
            result.close();
        } catch (IOException ioe) {
            throw new IndexerException(ioe);
        }
    }

    /**
     * Remove the specified object from the index
     * 
     * @param oid : The identifier of the object to remove
     * @throws IndexerException if there were errors during removal
     */
    @Override
    public void remove(String oid) throws IndexerException {
        log.debug("Deleting " + oid + " from index");
        try {
            solr.deleteByQuery("storage_id:\"" + oid + "\"");
            solr.commit();
        } catch (SolrServerException sse) {
            throw new IndexerException(sse);
        } catch (IOException ioe) {
            throw new IndexerException(ioe);
        }
    }

    /**
     * Remove the specified payload from the index
     * 
     * @param oid : The identifier of the payload's object
     * @param pid : The identifier of the payload to remove
     * @throws IndexerException if there were errors during removal
     */
    @Override
    public void remove(String oid, String pid) throws IndexerException {
        log.debug("Deleting {}::{} from index", oid, pid);
        try {
            solr.deleteByQuery("storage_id:\"" + oid + "\" AND identifer:\""
                    + pid + "\"");
            solr.commit();
        } catch (SolrServerException sse) {
            throw new IndexerException(sse);
        } catch (IOException ioe) {
            throw new IndexerException(ioe);
        }
    }

    /**
     * Remove all annotations from the index against an object
     * 
     * @param oid : The identifier of the object
     * @throws IndexerException if there were errors during removal
     */
    @Override
    public void annotateRemove(String oid) throws IndexerException {
        log.debug("Deleting " + oid + " from Anotar index");
        try {
            anotar.deleteByQuery("rootUri:\"" + oid + "\"");
            anotar.commit();
        } catch (SolrServerException sse) {
            throw new IndexerException(sse);
        } catch (IOException ioe) {
            throw new IndexerException(ioe);
        }
    }

    /**
     * Remove the specified annotation from the index
     * 
     * @param oid : The identifier of the object
     * @param annoId : The identifier of the annotation
     * @throws IndexerException if there were errors during removal
     */
    @Override
    public void annotateRemove(String oid, String annoId)
            throws IndexerException {
        log.debug("Deleting {}::{} from Anotar index", oid, annoId);
        try {
            anotar.deleteByQuery("rootUri:\"" + oid + "\" AND id:\"" + annoId
                    + "\"");
            anotar.commit();
        } catch (SolrServerException sse) {
            throw new IndexerException(sse);
        } catch (IOException ioe) {
            throw new IndexerException(ioe);
        }
    }

    /**
     * Index an object and all of its payloads
     * 
     * @param oid : The identifier of the object
     * @throws IndexerException if there were errors during indexing
     */
    @Override
    public void index(String oid) throws IndexerException {
        try {
            DigitalObject object = storage.getObject(oid);
            // Some workflow actions create payloads, so we can't iterate
            // directly against the object.
            String[] oldManifest = {};
            oldManifest = object.getPayloadIdList().toArray(oldManifest);
            for (String payloadId : oldManifest) {
                Payload payload = object.getPayload(payloadId);
                index(object, payload);
            }
        } catch (StorageException ex) {
            throw new IndexerException(ex);
        }
    }

    /**
     * Index a specific payload
     * 
     * @param oid : The identifier of the payload's object
     * @param pid : The identifier of the payload
     * @throws IndexerException if there were errors during indexing
     */
    @Override
    public void index(String oid, String pid) throws IndexerException {
        try {
            DigitalObject object = storage.getObject(oid);
            Payload payload = object.getPayload(pid);
            index(object, payload);
        } catch (StorageException ex) {
            throw new IndexerException(ex);
        }
    }

    /**
     * Index a specific payload
     * 
     * @param object : The payload's object
     * @param pid : The payload
     * @throws IndexerException if there were errors during indexing
     */
    public void index(DigitalObject object, Payload payload)
            throws IndexerException {
        String oid = object.getId();
        String pid = payload.getId();

        // Don't proccess annotations through this function
        if (pid.startsWith("anotar.")) {
            annotate(object, payload);
            return;
        }
        // log.info("Indexing OID:'{}', PID: '{}'", oid, pid);

        // get the indexer properties or we can't index
        Properties props;
        try {
            props = object.getMetadata();
        } catch (StorageException ex) {
            throw new IndexerException("Failed loading properties : ", ex);
        }

        try {
            // Get the harvest files
            String confOid = props.getProperty("jsonConfigOid");
            String rulesOid = props.getProperty("rulesOid");

            // Generate the Solr document
            String doc = index(object, payload, confOid, rulesOid, props);

            // Did the indexer alter metadata?
            String toClose = props.getProperty("objectRequiresClose");
            if (toClose != null) {
                log.debug("Indexing has altered metadata, closing object.");
                props.remove("objectRequiresClose");
                object.close();
                try {
                    props = object.getMetadata();
                } catch (StorageException ex) {
                    throw new IndexerException("Failed loading properties : ",
                            ex);
                }
            }

            addToBuffer(oid + "/" + pid, doc);
        } catch (Exception e) {
            log.error("Indexing failed!\n-----\n", e);
        }
    }

    /**
     * Call a manual commit against the index
     * 
     */
    @Override
    public void commit() {
        sendToIndex("{ \"event\" : \"commit\" }");
    }

    /**
     * Add a new document into the buffer, and check if submission is required
     * 
     * @param document : The Solr document to add to the buffer.
     */
    private void addToBuffer(String index, String document) {
        JsonConfigHelper message = new JsonConfigHelper();
        message.set("event", "index");
        message.set("index", index);
        message.set("document", document);
        sendToIndex(message.toString());
    }

    /**
     * Send the document to buffer directly
     * 
     * @param index
     * @param fields
     */
    public void sendIndexToBuffer(String index, Map<String, List<String>> fields) {
        String doc = pyUtils.solrDocument(fields);
        addToBuffer(index, doc);
    }

    /**
     * To put events to subscriber queue
     * 
     * @param oid Object id
     * @param eventType type of events happened
     * @param context where the event happened
     * @param jsonFile Configuration file
     */
    private void sendToIndex(String message) {
        messaging.queueMessage(SolrWrapperQueueConsumer.QUEUE_ID, message);
    }

    /**
     * Index an annotation
     * 
     * @param oid : The identifier of the annotation's object
     * @param pid : The identifier of the annotation
     * @throws IndexerException if there were errors during indexing
     */
    @Override
    public void annotate(String oid, String pid) throws IndexerException {
        // At this stage this is identical to the 'index()' method
        // above, but there may be changes at a later date.
        try {
            DigitalObject object = storage.getObject(oid);
            Payload payload = object.getPayload(pid);
            annotate(object, payload);
        } catch (StorageException ex) {
            throw new IndexerException(ex);
        }
    }

    /**
     * Index a specific annotation
     * 
     * @param object : The annotation's object
     * @param pid : The annotation payload
     * @throws IndexerException if there were errors during indexing
     */
    private void annotate(DigitalObject object, Payload payload)
            throws IndexerException {
        String oid = object.getId();
        String pid = payload.getId();
        if (propertiesId.equals(pid)) {
            return;
        }

        try {
            Properties props = new Properties();
            props.setProperty("metaPid", pid);

            String doc = index(object, payload, null, ANOTAR_RULES_OID, props);
            if (doc != null) {
                doc = "<add>" + doc + "</add>";
                anotar.request(new DirectXmlRequest("/update", doc));
                if (anotarAutoCommit) {
                    anotar.commit();
                }
            }
        } catch (Exception e) {
            log.error("Indexing failed!\n-----\n", e);
        }
    }

    /**
     * Search for annotations and return the result to the provided stream
     * 
     * @param request : The SearchRequest object
     * @param response : The OutputStream to send responses to
     * @throws IndexerException if there were errors during indexing
     */
    @Override
    public void annotateSearch(SearchRequest request, OutputStream response)
            throws IndexerException {
        SolrSearcher searcher = new SolrSearcher(
                ((CommonsHttpSolrServer) anotar).getBaseURL());
        if (username != null && password != null) {
            searcher.authenticate(username, password);
        }
        InputStream result;
        try {
            StringBuilder extras = new StringBuilder();
            for (String name : request.getParamsMap().keySet()) {
                for (String param : request.getParams(name)) {
                    extras.append(name);
                    extras.append("=");
                    extras.append(URLEncoder.encode(param, "UTF-8"));
                    extras.append("&");
                }
            }
            extras.append("wt=json");
            result = searcher.get(request.getQuery(), extras.toString(), false);
            IOUtils.copy(result, response);
            result.close();
        } catch (IOException ioe) {
            throw new IndexerException(ioe);
        }
    }

    /**
     * Index a payload using the provided data
     * 
     * @param object : The DigitalObject to index
     * @param payload : The Payload to index
     * @param in : Reader containing the new empty document
     * @param inConf : An InputStream holding the config file
     * @param rulesOid : The oid of the rules file to use
     * @param props : Properties object containing the object's metadata
     * @return File : Temporary file containing the output to index
     * @throws IOException if there were errors accessing files
     * @throws RuleException if there were errors during indexing
     */
    private String index(DigitalObject object, Payload payload, String confOid,
            String rulesOid, Properties props) throws IOException,
            RuleException {
        try {
            JsonConfigHelper jsonConfig = getConfigFile(confOid);

            // Get our data ready
            Map<String, Object> bindings = new HashMap();
            Map<String, List<String>> fields = new HashMap();
            bindings.put("fields", fields);
            bindings.put("jsonConfig", jsonConfig);
            bindings.put("indexer", this);
            bindings.put("object", object);
            bindings.put("payload", payload);
            bindings.put("params", props);
            bindings.put("pyUtils", pyUtils);

            // Run the data through our script
            PyObject script = getPythonObject(rulesOid);
            if (script.__findattr__(SCRIPT_ACTIVATE_METHOD) != null) {
                script.invoke(SCRIPT_ACTIVATE_METHOD, Py.java2py(bindings));
                object.close();
            } else {
                log.warn("Activation method not found!");
            }

            return pyUtils.solrDocument(fields);
        } catch (Exception e) {
            throw new RuleException(e);
        }
    }

    /**
     * Evaluate the rules file stored under the provided object ID. If caching
     * is configured the compiled python object will be cached to speed up
     * subsequent access.
     * 
     * @param oid : The rules OID to retrieve if cached
     * @return PyObject : The cached object, null if not found
     */
    private PyObject getPythonObject(String oid) {
        // Try the cache first
        PyObject rulesObject = deCachePythonObject(oid);
        if (rulesObject != null) {
            return rulesObject;
        }
        // We need to evaluate then
        InputStream inStream;
        if (oid.equals(ANOTAR_RULES_OID)) {
            // Anotar rules
            inStream = getClass().getResourceAsStream("/anotar.py");
            log.debug("First time parsing rules script: 'ANOTAR'");
            rulesObject = evalScript(inStream);
            cachePythonObject(oid, rulesObject);
            return rulesObject;

        } else {
            // A standard rules file
            DigitalObject object;
            Payload payload;
            try {
                object = storage.getObject(oid);
                payload = object.getPayload(object.getSourceId());
                inStream = payload.open();
                log.debug("First time parsing rules script: '{}'", oid);
                rulesObject = evalScript(inStream);
                payload.close();
                cachePythonObject(oid, rulesObject);
                return rulesObject;
            } catch (StorageException ex) {
                log.error("Rules file could not be retrieved! '{}'", oid, ex);
                return null;
            }
        }
    }

    /**
     * Retrieve and parse the config file stored under the provided object ID.
     * If caching is configured the instantiated config object will be cached to
     * speed up subsequent access.
     * 
     * @param oid : The rules OID to retrieve if cached
     * @return PyObject : The cached object, null if not found
     */
    private JsonConfigHelper getConfigFile(String oid) {
        if (oid == null) {
            return null;
        }

        // Try the cache first
        JsonConfigHelper configFile = deCacheConfig(oid);
        if (configFile != null) {
            return configFile;
        }
        // Or evaluate afresh
        try {
            DigitalObject object = storage.getObject(oid);
            Payload payload = object.getPayload(object.getSourceId());
            log.debug("First time parsing config file: '{}'", oid);
            configFile = new JsonConfigHelper(payload.open());
            payload.close();
            cacheConfig(oid, configFile);
            return configFile;
        } catch (IOException ex) {
            log.error("Rules file could not be parsed! '{}'", oid, ex);
            return null;
        } catch (StorageException ex) {
            log.error("Rules file could not be retrieved! '{}'", oid, ex);
            return null;
        }
    }

    /**
     * Evaluate and return a Python script.
     * 
     * @param inStream : InputStream containing the script to evaluate
     * @return PyObject : Compiled result
     */
    private PyObject evalScript(InputStream inStream) {
        // Execute the script
        PythonInterpreter python = new PythonInterpreter();
        python.execfile(inStream);
        // Get the result and cleanup
        PyObject scriptClass = python.get(SCRIPT_CLASS_NAME);
        python.cleanup();
        // Instantiate and return the result
        return scriptClass.__call__();
    }

    /**
     * Add a python object to the cache if caching if configured
     * 
     * @param oid : The rules OID to use as an index
     * @param pyObject : The compiled PyObject to cache
     */
    private void cachePythonObject(String oid, PyObject pyObject) {
        if (useCache && pyObject != null) {
            scriptCache.put(oid, pyObject);
        }
    }

    /**
     * Return a python object from the cache if configured
     * 
     * @param oid : The rules OID to retrieve if cached
     * @return PyObject : The cached object, null if not found
     */
    private PyObject deCachePythonObject(String oid) {
        if (useCache && scriptCache.containsKey(oid)) {
            return scriptCache.get(oid);
        }
        return null;
    }

    /**
     * Add a config class to the cache if caching if configured
     * 
     * @param oid : The config OID to use as an index
     * @param config : The instantiated JsonConfigHelper to cache
     */
    private void cacheConfig(String oid, JsonConfigHelper config) {
        if (useCache && config != null) {
            configCache.put(oid, config);
        }
    }

    /**
     * Return a config class from the cache if configured
     * 
     * @param oid : The config OID to retrieve if cached
     * @return JsonConfigHelper : The cached config, null if not found
     */
    private JsonConfigHelper deCacheConfig(String oid) {
        if (useCache && configCache.containsKey(oid)) {
            return configCache.get(oid);
        }
        return null;
    }
}
