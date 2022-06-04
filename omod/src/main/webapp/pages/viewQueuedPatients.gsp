<%
    ui.decorateWith("kenyaemr", "standardPage", [ layout: "sidebar" ])
    ui.includeJavascript("kenyaemr", "controllers/account.js")
    def menuItems = [
            [ label: "Back to home", iconProvider: "kenyaui", icon: "buttons/back.png", label: "Back to home", href: ui.pageLink("initialpatientqueueapp", "viewQueuedPatients") ]
    ]
%>
<script type="text/javascript">
  jQuery(function() {
    var table = jQuery("#details").DataTable();
  });
</script>
<div class="ke-page-sidebar">
    ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ items: menuItems ]) }
</div>
<div class="ke-panel-frame">
    <div class="ke-panel-heading">Scheduled Patients</div>
    <div class="ke-panel-content">
        <table border="0" cellpadding="0" cellspacing="0" id="details" width="100%">
            <thead>
            <tr>
                <th>Visit Date</th>
                <th>Patient Identifier</th>
                <th>Patient Names</th>
                <th>Visit status</th>
                <th>Service status</th>
                <th>Referral room </th>
                <th>Service point </th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <% if (viewQueuedPatientsList.empty) { %>
            <tr>
                <td colspan="8">
                    No records found for specified period
                </td>
            </tr>
            <% } %>
            <% viewQueuedPatientsList.each {%>
            <tr>
                <td>${it.visitDate}</td>
                <td>${it.patientIdentifier}</td>
                <td>${it.patientNames}</td>
                <td>${it.visitStatus}</td>
                <td>${it.status}</td>
                <td>${it.referralConceptName}</td>
                <td>${it.serviceConceptName}</td>
                <td>
                    <a href="#" id="editQueue">Edit</a>
                </td>
            </tr>
            <%}%>
            </tbody>
        </table>
    </div>
</div>