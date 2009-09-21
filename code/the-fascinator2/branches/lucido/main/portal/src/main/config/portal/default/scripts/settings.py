from au.edu.usq.fascinator.api import PluginManager

from java.io import ByteArrayInputStream, ByteArrayOutputStream

class SettingsData:

    def __init__(self):
        self.__portal = None
        action = formData.get("verb")
        if action == "create_view":
            fq = [q for q in sessionState.get("fq") if q != 'item_type:"object"']
            if fq == []:
                name = "new"
                desc = "New View"
                query = ""
            else:
                name = ""
                desc = ""
                query = str(" ".join(fq))
            newPortal = Portal(name, desc, query)
            newPortal.setFacetFields(Services.portalManager.default.facetFields)
            newPortal.setQuery(query)
            self.__portal = newPortal
        else:
            portalName = formData.get("portalName")
            print " * settings.py: portalName=%s" % portalName
            if portalName is None or (formData.get("portalAction") == "Cancel"):
                self.__portal = Services.portalManager.get(portalId)
            else:
                self.__portal = Portal()
                self.__portal.name = portalName
                Services.portalManager.add(self.__portal)
            if formData.get("portalAction") == "Update":
                self.__updatePortal()
        if formData.get("emailAction") == "Update":
            self.__updateEmail()
        if formData.get("backupAction") == "Update":    
            self.__updateBackupPaths()
        
    def __updateEmail(self):
        #This email temporarily will be defined here so the backup
        #server can differentiate multiple userspace
        self.__portal.email = formData.get("emailAddress")
        Services.portalManager.save(self.__portal)
        
    def __updateBackupPaths(self):
        backupPaths = self.__portal.backupPaths
        backupPaths.clear()
        size = int(formData.get("backupUrlSize"))
        for i in range (1, size+2):  
            keyName = "backupPaths_%s_name" % i
            valueName = "backupPaths_%s_label" % i
            name = formData.get(keyName)
            value = formData.get(valueName)
            print "key: %s, value: %s" % (name, value)
            if name is not None and value is not None:
                backupPaths.put(name, value)
        Services.portalManager.save(self.__portal)
    
    def __updatePortal(self):
        print " * settings.py: updatePortal %s" % formData
        self.__portal.name = formData.get("portalName")
        self.__portal.description = formData.get("portalDescription")
        self.__portal.query = formData.get("portalQuery")
        self.__portal.recordsPerPage = int(formData.get("portalRecordsPerPage"))
        self.__portal.facetCount = int(formData.get("portalFacetLimit"))
        self.__portal.facetSort = formData.get("portalFacetSort") is not None
        facetFields = self.__portal.facetFields
        facetFields.clear()
        size = int(formData.get("portalFacetSize"))
        for i in range(1,size+1):
            nameKey = "portalFacet_%s_name" % i
            labelKey = "portalFacet_%s_label" % i
            name = formData.get(nameKey)
            label = formData.get(labelKey)
            print "key: %s, label: %s" % (name, label)
            facetFields.put(name, label)
        Services.portalManager.save(self.__portal)
    
    def getPortal(self):
        return self.__portal
    
    def getIndexerPlugins(self):
        return PluginManager.getIndexerPlugins()

    def getStoragePlugins(self):
        return PluginManager.getStoragePlugins()

    def getHarvesterPlugins(self):
        return PluginManager.getHarvesterPlugins()

    def getTransformerPlugins(self):
        return PluginManager.getTransformerPlugins()
    
    
    

scriptObject = SettingsData()
