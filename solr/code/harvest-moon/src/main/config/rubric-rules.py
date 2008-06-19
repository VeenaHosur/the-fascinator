from java.lang import String
from au.edu.usq.solr.index.rule import RuleManager
from au.edu.usq.solr.index.rule.impl import TransformRule, AddFieldRule

#
# Available objects:
#    self: harvester
#    pid : registry pid
#    name: harvest job name
#    item: metadata item
#
# Return:
#    rules: RuleManager instance
#
rules = RuleManager()

# dc to solr transform
rules.add(TransformRule(self.getResource("/xsl/dc_solr.xsl")))

# unique identifier
rules.add(AddFieldRule("id", item.getId()))

# registry pid
rules.add(AddFieldRule("pid", pid))

# group access
#   default to "guest"
#   set to "admin" for ADT items
groupAccess = AddFieldRule("group_access", "guest")
rules.add(groupAccess)
nodes = item.getMetadata().selectNodes("//dc:type")
for node in nodes:
    dcType = node.getText().strip()
    if dcType == "Australasian Digital Thesis":
        groupAccess.setValue("admin")
        break

# item class
rules.add(AddFieldRule("item_class", "document"))

# item type
rules.add(AddFieldRule("item_type", "object"))

# full text - get the FULLTEXT datastream
if item.hasDatastreams():
    ds = item.getDatastream("FULLTEXT")
    rules.add(AddFieldRule("full_text", String(ds.getContent())))

