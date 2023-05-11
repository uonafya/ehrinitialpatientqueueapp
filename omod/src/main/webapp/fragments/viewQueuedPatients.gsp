<script type="text/javascript">

    var jq = jQuery;
    jq(function () {
        jq("#details").DataTable();
        var editroomDialog = emr.setupConfirmationDialog({
                    dialogOpts: {
                        overlayClose: false,
                        close: true
                    },
                    selector: '#new-room-dialog',
                    actions: {
                        confirm: function () {
                           console.log("The dialog worked");
                        },
                        cancel: function () {
                            editroomDialog.close();
                        }
                    }
                });
    });

</script>
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
                    <a href="${it.queueId}" id="editQueue" class="button task">Edit</a>
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
                <table>
                    <tr><td colspan="2"><h2>Room to Visit</h2></td></tr>
                    <tr>

                        <div class="onerow" style="margin-top:10px;">
                            <td valign="top">
                                <div class="col-auto">
                                    <label for="rooms1" id="froom1" style="margin:0px;">Room to Visit<span>*</span></label>
                                </div>
                            </td>
                            <td valign="top">
                                <div class="col-auto">
                                    <span class="select-arrow" style="width: 100%">
                                        <field>
                                            <select id="rooms1" name="rooms1" onchange="LoadRoomsTypes();"
                                                    class="required form-combo1">
                                                <option value="">Select Room</option>
                                                <option value="1">TRIAGE ROOM</option>
                                                <option value="2">OPD ROOM</option>
                                                <option value="3">SPECIAL CLINIC</option>
                                            </select>
                                        </field>
                                    </span>
                                </div>
                            </td>
                        </div>
                    </tr>
                    <tr>
                        <div class="onerow" style="margin-top:10px;">
                            <td valign="top">
                                <div class="col-auto">
                                    <label for="rooms2" id="froom2" style="margin:0px;">Room Type<span>*</span></label>
                                </div>
                            </td>
                            <td valign="top">
                                <div class="col-auto">
                                    <span class="select-arrow" style="width: 100%">
                                        <field>
                                            <select id="rooms2" name="rooms2" class="required form-combo1">
                                            </select>
                                        </field>
                                    </span>
                                </div>
                            </td>
                        </div>
                    </tr>
                    <tr>
                        <td valign="top">
                            <div class="col-auto last">
                                <label for="rooms3" id="froom3" style="margin:0px;">File Number</label>
                            </div>
                        </td>
                        <td valign="top">
                            <div class="col4 last">
                                <field><input type="text" id="rooms3" name="rooms3" value="N/A" placeholder="FILE NUMBER"
                                              readonly=""/></field>
                            </div>
                        </td>
                    </tr>

                </table>
                <div class="onerow" style="margin-top:10px;">
                    <button class="button cancel">Cancel</button>
                    <button class="button confirm right">Confirm</button>
                </div>
            </div>
        </div>
    </div>
</div>