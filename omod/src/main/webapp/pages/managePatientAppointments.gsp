<%
    ui.decorateWith("kenyaemr", "standardPage")
    ui.includeJavascript("ehrconfigs", "jquery.dataTables.min.js")
        ui.includeCss("ehrconfigs", "jquery.dataTables.min.css")
        ui.includeCss("ehrconfigs", "referenceapplication.css")

%>

<div class="ke-page-content">
    ${ui.includeFragment("initialpatientqueueapp", "managePatientAppointments")}
</div>