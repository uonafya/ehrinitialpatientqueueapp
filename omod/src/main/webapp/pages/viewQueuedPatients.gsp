<%
    ui.decorateWith("kenyaemr", "standardPage", [ layout: "sidebar" ])
    ui.includeJavascript("ehrconfigs", "jquery.dataTables.min.js")
    ui.includeCss("ehrconfigs", "jquery.dataTables.min.css")
    ui.includeJavascript("ehrconfigs", "emr.js")
    ui.includeCss("ehrconfigs", "onepcssgrid.css")
    ui.includeCss("ehrconfigs", "custom.css")
    ui.includeCss("ehrconfigs", "referenceapplication.css")
    ui.includeJavascript("ehrconfigs", "jquery.simplemodal.1.4.4.min.js")

    def menuItems = [
            [ label: "Back to home", iconProvider: "kenyaui", icon: "buttons/back.png", label: "Back to home", href: ui.pageLink("initialpatientqueueapp", "patientQueueHome") ]
    ]
%>
<script type="text/javascript">
    var jq = jQuery;
    jq(function (){
        jq("#details").DataTable();
        var editroomDialog = emr.setupConfirmationDialog({
            dialogOpts: {
                overlayClose: false,
                close: true
            },
            selector: '#new-room-dialog',
            actions: {
                confirm: function() {
                    editroomDialog.close();
                },
                cancel: function() {
                    editroomDialog.close();
                }
            }
        });
        jq("#editQueue").on("click", function (e) {
            e.preventDefault();
            editroomDialog.show();
        });
    });
</script>
<div class="ke-page-sidebar">
    ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ items: menuItems ]) }
</div>
<div class="ke-page-content">
    ${ui.includeFragment("initialpatientqueueapp", "viewQueuedPatients")}
</div>