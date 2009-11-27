from au.edu.usq.fascinator.api.indexer import SearchRequest
from au.edu.usq.fascinator.common import JsonConfigHelper
from java.io import ByteArrayInputStream, ByteArrayOutputStream
from java.util import LinkedHashMap

class HomeData:
    def __init__(self):
        sessionState.remove("fq")
        action = formData.get("verb")
        if action == "delete-portal":
            portalName = formData.get("value")
            print " * home.py: delete portal %s" % portalName
            Services.portalManager.remove(portalName)
        self.__portal = Services.portalManager.get(portalId)
        self.__latest = JsonConfigHelper()
        self.__result = JsonConfigHelper()
        self.__search()
        
    
    def __search(self):
        indexer = Services.getIndexer()
        
        req = SearchRequest("*:*")
        req.setParam("fq", 'item_type:"object"')
        req.setParam("rows", "10")
        req.setParam("sort", "date_usq_document_effective_date desc, title_sort asc");
        out = ByteArrayOutputStream()
        indexer.search(req, out)
        self.__latest = JsonConfigHelper(ByteArrayInputStream(out.toByteArray()))
        
        req = SearchRequest("*:*")
        req.setParam("fq", 'item_type:"object"')
        req.setParam("rows", "0")
        req.setParam("facet", "true")
        req.setParam("facet.sort", "false")
        req.setParam("facet.field", ["f_title_group", "f_usq_document_policy_type"])
        out = ByteArrayOutputStream()
        indexer.search(req, out)
        self.__result = JsonConfigHelper(ByteArrayInputStream(out.toByteArray()))
    
    def getLatest(self):
        return self.__latest.getList("response/docs")
    
    def getTotal(self):
        return self.__result.get("response/numFound")
    
    def getFacetName(self, key):
        return self.__portal.facetFields.get(key)
    
    def getFacetCounts(self, key):
        values = LinkedHashMap()
        valueList = self.__result.getList("facet_counts/facet_fields/%s" % key)
        for i in range(0,len(valueList),2):
            name = valueList[i]
            count = valueList[i+1]
            if count > 0:
                values[name] = count
        return values

scriptObject = HomeData()
