from jarray import array
from au.edu.usq.fascinator.api.indexer import SearchRequest
from au.edu.usq.fascinator.common import JsonConfigHelper
from java.io import ByteArrayInputStream, ByteArrayOutputStream
from java.util import HashMap, ArrayList

class Facet:
    def __init__(self, value, count):
        self.__value = value
        self.__count = count
        self.__subFacets = ArrayList()

    def getName(self):
        slash = self.__value.rfind("/")
        return self.__value[slash+1:]

    def getValue(self):
        return self.__value

    def getCount(self):
        return self.__count

    def addSubFacet(self, facet):
        self.__subFacets.add(facet)

    def getSubFacets(self):
        return self.__subFacets

class FacetList:
    def __init__(self, name, json):
        self.__name = name
        self.__facetMap = HashMap()
        self.__facetList = ArrayList()
        entries = json.getList("facet_counts/facet_fields/file_path")
        for i in range(0, len(entries), 2):
            value = entries[i]
            count = entries[i+1]
            if count > 0:
                facet = Facet(value, count)
                self.__facetMap.put(value, facet)
                slash = value.rfind("/")
                if slash == -1:
                    self.__facetList.add(facet)
                else:
                    parent = self.getFacet(value[:slash])
                    if parent is not None:
                        parent.addSubFacet(facet)

    def getFacets(self):
        return self.__facetList

    def getFacet(self, name):
        return self.__facetMap.get(name)

class SearchTreeData:
    def __init__(self):
        self.__id = formData.get("id")
        self.__result = JsonConfigHelper()
        self.__portal = Services.getPortalManager().get(portalId)
        self.__search()

    def __search(self):
        query = formData.get("query")
        if query is None or query == "":
            query = "*:*"
        req = SearchRequest(query)
        req.setParam("facet", ["true"])
        req.setParam("fl", ["id"])
        req.setParam("fq", ['item_type:"object"'])
        req.setParam("rows", ["100"])
        req.setParam("facet.field", "file_path")
        out = ByteArrayOutputStream()
        indexer = Services.getIndexer()
        indexer.search(req, out)
        self.__result = JsonConfigHelper(ByteArrayInputStream(out.toByteArray()))
        self.__facetList = FacetList("file_path", self.__result)

    def getFacetList(self):
        return self.__facetList

    def getFacet(self, value):
        return self.__facetList.get(value)

scriptObject = SearchTreeData()
