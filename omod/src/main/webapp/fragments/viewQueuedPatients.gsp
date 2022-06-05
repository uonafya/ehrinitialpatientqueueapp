<div class="ke-panel-frame">
    <div class="ke-panel-heading">Scheduled Patients</div>
    <div class="ke-panel-content">
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
            <th>Patient Category</th>
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
            <td>${it.category}</td>

            <td>
                <a href="${it.queueId}" id="editQueue">Edit</a>
            </td>
        </tr>
        <%}%>
        <%}%>
        </tbody>
    </table>
</div>
</div>