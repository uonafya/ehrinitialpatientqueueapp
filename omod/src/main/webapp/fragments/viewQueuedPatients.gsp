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
        <div id="new-room-dialog" class="dialog" style="display:none;">
            <div class="dialog-header">
                <i class="icon-folder-open"></i>

                <h3>Edit Patient Service Point</h3>
            </div>

            <div class="dialog-content">
                <ul>
                    <li>
                        <label>Room To Visit<span>*</span></label>
                        <select id="rooms1" name="rooms1" onchange="LoadRoomsTypes();"
                                class="required form-combo1" style="width: 90%!important;">
                            <option disabled>Select Room</option>
                            <option value="1">TRIAGE ROOM</option>
                            <option value="2">OPD ROOM</option>
                            <option value="3">SPECIAL CLINIC</option>
                        </select>
                    </li>
                    <li>
                        <label>Room Type<span>*</span></label>
                        <select id="rooms2" name="rooms2" class="required form-combo1">
                        </select>
                    </li>
                </ul>

                <div class="onerow">
                    <button class="button cancel">Cancel</button>
                    <button class="button confirm right">Confirm</button>
                </div>
            </div>
        </div>
</div>
</div>