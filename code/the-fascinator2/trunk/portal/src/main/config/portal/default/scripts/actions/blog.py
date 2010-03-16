import md5, os

from com.sun.syndication.feed.atom import Content
from com.sun.syndication.propono.atom.client import AtomClientFactory, BasicAuthStrategy

from java.io import ByteArrayInputStream, ByteArrayOutputStream, File, StringWriter
from java.net import Proxy, ProxySelector, URL, URLDecoder
from java.lang import Exception
from java.util import HashSet

from org.apache.commons.httpclient.methods import PostMethod
from org.apache.commons.io import FileUtils, IOUtils
from org.apache.commons.io.output import NullOutputStream
from org.dom4j.io import OutputFormat, XMLWriter, SAXReader

from org.w3c.tidy import Tidy

from java.lang import String, System

class ProxyBasicAuthStrategy(BasicAuthStrategy):
    def __init__(self, username, password, baseUrl):
        BasicAuthStrategy.__init__(self, username, password)
        self.__baseUrl = baseUrl
    
    def addAuthentication(self, httpClient, method):
        BasicAuthStrategy.addAuthentication(self, httpClient, method)
        url = URL(self.__baseUrl)
        proxy = ProxySelector.getDefault().select(url.toURI()).get(0)
        httpClient.getParams().setAuthenticationPreemptive(False);
        if not proxy.type().equals(Proxy.Type.DIRECT):
            address = proxy.address()
            proxyHost = address.getHostName()
            proxyPort = address.getPort()
            httpClient.getHostConfiguration().setProxy(proxyHost, proxyPort)
            print " * blog.py: Using proxy '%s:%s'" % (proxyHost, proxyPort)

class AtomEntryPoster:
    def __init__(self):
        responseType = "text/html"
        responseMsg = ""
        func = formData.get("func")
        if func == "url-history":
            responseType = "text/plain"
            responseMsg = "\n".join(self.getUrls())
        else:
            try:
                url = formData.get("url")
                title = formData.get("title")
                username = formData.get("username")
                password = formData.get("password")
                try:
                    auth = ProxyBasicAuthStrategy(username, password, url)
                    self.__service = AtomClientFactory.getAtomService(url, auth)
                    oid = formData.get("oid")
                    if oid is not None:
                        self.__object = Services.getStorage().getObject(oid)
                        content = self.__getContent(oid)
                    else:
                        content = self.__getManifestContent(formData.get("portalId"))
                    success, value = self.__post(title, content)
                except Exception, e:
                    e.printStackTrace()
                    success = False
                    value = e.getMessage()
                if success:
                    altLinks = value.getAlternateLinks()
                    if altLinks is not None:
                        self.saveUrl(url)
                        responseMsg = "<p>Success! Visit the <a href='%s' target='_blank'>blog post</a>.</p>" % altLinks[0].href
                    else:
                        responseMsg = "<p class='warning'>The server did not return a valid link!</p>"
                else:
                    responseMsg = "<p class='error'>%s</p>" % value
            except Exception, e:
                print " * blog.py: Failed to post: %s" % e.getMessage()
                responseMsg = "<p class='error'>%s</p>"  % e.getMessage()
        writer = response.getPrintWriter(responseType)
        writer.println(responseMsg)
        writer.close()
    
    def getUrls(self):
        return FileUtils.readLines(self.__getHistoryFile())
    
    def saveUrl(self, url):
        historyFile = self.__getHistoryFile()
        if historyFile.exists():
            urls = FileUtils.readLines(historyFile)
            urls.add(url)
        FileUtils.writeLines(historyFile, [url])
    
    def __getHistoryFile(self):
        f = File(System.getProperty("user.home"), ".fascinator/blog/history.txt")
        if not f.exists():
            f.getParentFile().mkdirs()
            f.createNewFile()
        return f
    
    def __getManifestContent(self, portalId):
        portal = Services.getPortalManager().get(portalId)
        manifest = portal.getJsonMap("manifest")
        contentStr = "<div>"
        for key in manifest.keySet():
            item = manifest.get(key)
            if item.get("hidden", "false") == "false":
                contentStr += "<div><h2>%s</h2>" % item.get("title")
                contentStr += self.__getContent(item.get("id"))
                contentStr += "</div>"
        contentStr += "</div>"
        print "[\n%s\n]" % contentStr
        return contentStr
    
    def __getContent(self, oid):
        content = "<div>content not found!</div>"
        slash = oid.rfind("/")
        payload = self.__getPreviewPayload()
        pid = payload.getId()
        print " * blog.py: preview payload: %s" % payload
        if payload is None:
            print " * blog.py: Failed to get content: %s" % oid
            return ""
        else:
            mimeType = payload.getContentType()
            if mimeType.startswith("image/"):
                content = '<img src="%s" />' % pid
            elif mimeType in ["text/html", "text/xml", "application/xhtml+xml"]:
                content = self.__getPayloadAsString(payload)
            elif mimeType.startswith("text/"):
                content = "<html><body><pre>%s</pre></body></html>" % \
                    self.__getPayloadAsString(payload)
            else:
                content = "<div>unsupported content type: %s</div>" % mimeType
        content, doc = self.__tidy(content)
        content = self.__processMedia(doc, content)
        return content
    
    def __getPreviewPayload(self):
        payloadIdList = self.__object.getPayloadIdList()
        for payloadId in payloadIdList:
            try:
                payload = self.__object.getPayload(payloadId)
                if str(payload.getType()) == "Preview":
                    return payload
            except Exception, e:
                pass
        return None
    
    def __getPayloadAsString(self, payload):
        sw = StringWriter()
        IOUtils.copy(payload.open(), sw)
        sw.flush()
        payload.close()
        return sw.toString()
    
    def __tidy(self, content):
        tidy = Tidy()
        tidy.setIndentAttributes(False)
        tidy.setIndentContent(False)
        tidy.setPrintBodyOnly(True)
        tidy.setSmartIndent(False)
        tidy.setWraplen(0)
        tidy.setXHTML(False)
        tidy.setNumEntities(True)
        out = ByteArrayOutputStream()
        doc = tidy.parseDOM(ByteArrayInputStream(String(content).getBytes()), out)
        content = out.toString("UTF-8")
        return content, doc
    
    def __processMedia(self, doc, content):
        content = self.__uploadMedia(doc, content, "a", "href")
        content = self.__uploadMedia(doc, content, "img", "src")
        return content
    
    def __uploadMedia(self, doc, content, elem, attr):
        links = doc.getElementsByTagName(elem)
        for i in range(0, links.getLength()):
            elem = links.item(i)
            attrValue = elem.getAttribute(attr)
            if attrValue == '':
                continue
            pid = attrValue
            print " * blog.py: uploading '%s' (%s, %s)" % (pid, elem.tagName, attr)
            found = False
            try:
                payload = self.__object.getPayload(pid)
                found = True
            except Exception, e:
                pid = URLDecoder.decode(pid, "UTF-8")
                try:
                    payload = self.__object.getPayload(pid)
                    found = True
                except Exception, e:
                    print " * blog.py: payload not found '%s'" % pid
            if found:
                #HACK to upload PDFs
                contentType = payload.getContentType().replace("application/", "image/")
                entry = self.__postMedia(payload.getLabel(), contentType, payload.open())
                payload.close()
                if entry is not None:
                    id = entry.getId()
                    print " * blog.py: replacing %s with %s" % (attrValue, id)
                    content = content.replace('%s="%s"' % (attr, attrValue),
                                              '%s="%s"' % (attr, id))
                    content = content.replace("%s='%s'" % (attr, attrValue),
                                              "%s='%s'" % (attr, id))
                else:
                    print " * blog.py: failed to upload %s" % pid
        return content
    
    def __getCollection(self, type):
        workspaces = self.__service.getWorkspaces()
        if len(workspaces) > 0:
            return workspaces[0].findCollection(None, type)
        return None
    
    def __postMedia(self, slug, type, media):
        print " * blog.py: uploading media %s, %s" % (slug, type)
        collection = self.__getCollection(type)
        if collection is not None:
            entry = collection.createMediaEntry(slug, slug, type, media)
            collection.addEntry(entry)
            return entry
        return None
    
    def __post(self, title, content):
        collection = self.__getCollection("application/atom+xml;type=entry")
        if collection is not None:
            entry = collection.createEntry()
            entry.setTitle(title)
            entry.setContent(content, Content.HTML)
            collection.addEntry(entry)
            return True, entry
        else:
            return False, "No valid collection found"
        return False, "An unknown error occured"
    

scriptObject = AtomEntryPoster()
