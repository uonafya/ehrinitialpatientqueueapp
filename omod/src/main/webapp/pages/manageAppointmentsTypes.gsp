<%
    ui.decorateWith("kenyaemr", "standardPage", [ layout: "sidebar" ])
    ui.includeJavascript("ehrconfigs", "jquery.dataTables.min.js")
    ui.includeCss("ehrconfigs", "jquery.dataTables.min.css")
%>

<div class="ke-page-content">
    ${ui.includeFragment("initialpatientqueueapp", "manageAppointmentsTypes")}
</div>