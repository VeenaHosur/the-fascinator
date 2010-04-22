#if($self.mimeType.startsWith("image/"))
<script type="text/javascript" src="$portalPath/js/anotar/image.annotate.js"></script>
<script type="text/javascript">
    $(window).load(function() {
        $("#image-content").annotateImage({
            portalPath: "$portalPath",
            editable: true,
            saveUrl: "$portalPath/actions/anotar.ajax?action=save-image&rootUri=$oid",
            getUrl: "$portalPath/actions/anotar.ajax?action=get-image&rootUri=$oid"
        });
    });
</script>
#end
