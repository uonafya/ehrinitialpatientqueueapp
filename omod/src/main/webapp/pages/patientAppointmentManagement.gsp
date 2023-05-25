<%
    ui.decorateWith("kenyaemr", "standardPage", [ patient: currentPatient ])
    ui.includeCss("ehrconfigs", "referenceapplication.css")
%>
<div class="ke-page-content">
    ${ ui.includeFragment("initialpatientqueueapp", "scheduleAppointment", [patientId: currentPatient])}
</div>