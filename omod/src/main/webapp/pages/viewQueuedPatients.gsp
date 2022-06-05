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
    <div class="ke-panel-frame">
    <div class="ke-panel-heading">Scheduled Patients</div>
    <table border="0" cellpadding="0" cellspacing="0" id="details" width="100%">
        <thead>
        <tr>
            <th>Visit Date</th>
            <th>Patient Identifier</th>
            <th>Patient Names</th>
            <th>Sex</th>
            <th>Visit status</th>
            <th>Service point</th>
            <th>Request status</th>
            <th>Paying Category</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% if (viewQueuedPatientsList.empty) { %>
        <tr>
            <td colspan="10">
                No records found for specified period
            </td>
        </tr>
        <% } %>
        <% if (viewQueuedPatientsList) { %>
            <% viewQueuedPatientsList.each {%>
            <tr>
                <td>${it.visitDate}</td>
                <td>${it.patientIdentifier}</td>
                <td>${it.patientNames}</td>
                <td>${it.sex}</td>
                <td>${it.visitStatus}</td>
                <td>${it.serviceConceptName}</td>
                <td>${it.status}</td>
                <td>${it.referralConceptName}</td>

                <td>
                    <a href="#" id="editQueue" onclick="editScheduledQueues(it.queueId)">Edit</a>
                </td>
            </tr>
            <%}%>
        <%}%>
        </tbody>
    </table>
    </div>
</div>