<%
    ui.decorateWith("kenyaemr", "standardPage", [patient: patient])
    ui.includeJavascript("ehrconfigs", "jquery.dataTables.min.js")
    ui.includeCss("ehrconfigs", "jquery.dataTables.min.css")
    ui.includeCss("ehrconfigs", "referenceapplication.css")
%>

<div class="ke-page-content">
    ${ui.includeFragment("initialpatientqueueapp", "scheduleAppointment", [patientId: currentPatient])}
</div>