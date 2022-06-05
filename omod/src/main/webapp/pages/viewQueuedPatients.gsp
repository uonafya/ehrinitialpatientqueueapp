<%
    ui.decorateWith("kenyaemr", "standardPage", [ layout: "sidebar" ])
    ui.includeJavascript("ehrconfigs", "datatables/jquery.dataTables.min.js")
    ui.includeCss("ehrconfigs", "jquery.dataTables.min.css")
    def menuItems = [
            [ label: "Back to home", iconProvider: "kenyaui", icon: "buttons/back.png", label: "Back to home", href: ui.pageLink("initialpatientqueueapp", "patientQueueHome") ]
    ]
%>
<script type="text/javascript">
  jQuery(function() {
    var table = jQuery("#details").DataTable();

  });
  function editScheduledQueues(queueId) {
    emr.setupConfirmationDialog({
      dialogOpts: {
        overlayClose: false,
        close: true
      },
      selector: '#editQueue',
      actions: {
        confirm: function() {
          acceptTest();
          acceptDialog.close();
        },
        cancel: function() {
          acceptDialog.close();
        }
      }
    });
  }
</script>
<div class="ke-page-sidebar">
    ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ items: menuItems ]) }
</div>
<div class="ke-page-content">
    ${ui.includeFragment("initialpatientqueueapp", "viewQueuedPatients")}
</div>