from au.edu.usq.solr.index.rule import RuleManager
from au.edu.usq.solr.index.rule.impl import *

#
# Available objects:
#    self: Harvest instance
#    rules: RuleManager instance
#    item: metadata item
#    pid: registry object pid
#    dsId: datastream id or None if item is object
#

# dc to solr transform
rules.add(TransformRule(self.getResource("/xsl/dc_solr.xsl")))

# at least one identifier with url value
rules.add(CheckFieldRule("identifier", "http.*"))

# at least one non-blank title
rules.add(CheckFieldRule("title", ".+"))

# use only the year from date field
rules.add(ModifiyFieldRule("date", ".*(\\d{4}).*", "$1"))

# delete blank subject
rules.add(DeleteFieldRule("subject", "^\\s*$"))

# delete blank creator
rules.add(DeleteFieldRule("creator", "^\\s*$"))

# delete invalid dates
rules.add(DeleteFieldRule("date", "(.*[^0-9].*)|(^\\s*$)"))

# repository name
rules.add(AddFieldRule("repository_name", "RUBRIC"))

if dsId is None:
    solrId = item.getId();
    itemType = "object"
    # full text - get the FULLTEXT datastream
    if item.hasDatastreams():
        ds = item.getDatastream("FULLTEXT")
        if ds is not None:
            rules.add(AddFieldRule("full_text", ds.getContentAsString()))
else:
    solrId = item.getId() + "/" + dsId
    itemType = "datastream"

# unique identifier
rules.add(AddFieldRule("id", solrId))

# registry pid
rules.add(AddFieldRule("pid", pid))

# item type
rules.add(AddFieldRule("item_type", itemType))

# group access
#   default to "guest"
#   set to "admin" for ADT items
#   add "on_campus" for conference proceedings
groupAccess = AddFieldRule("group_access", "guest")
rules.add(groupAccess)
nodes = item.getMetadata().selectNodes("//dc:type")
for node in nodes:
    dcType = node.getText().strip()
    if dcType == "Australasian Digital Thesis":
        groupAccess.setValue("admin")
    elif dcType == "conference proceedings":
        rules.add(AddFieldRule("group_access", "on_campus"))

# item class
rules.add(AddFieldRule("item_class", "document"))
