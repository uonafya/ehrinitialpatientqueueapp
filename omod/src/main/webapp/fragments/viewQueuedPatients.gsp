<script type="text/javascript">

    var jq = jQuery;
    jq(function () {
       var table = jq("#detailsTb").DataTable();
        var editroomDialog = emr.setupConfirmationDialog({
                    dialogOpts: {
                        overlayClose: false,
                        close: true
                    },
                    selector: '#new-room-dialog',
                    actions: {
                        confirm: function () {
                           updateQueue();
                        },
                        cancel: function () {
                            location.reload();
                        }
                    }
                });
          jq('#detailsTb tbody').on( 'click', 'tr', function () {
                      var trData = table.row(this).data();
                      jq("#queueValue").val(trData[0])
                      jq("#servicePointValue").val(trData[6])
                      jq("#new-room-dialog").show();
          });
    });
    function updateQueue() {
        jq.getJSON('${ ui.actionLink("initialpatientqueueapp", "viewQueuedPatients", "updatePatientQueue") }', {
            queueId:jq("#queueValue").val(),
            servicePoint: jq("#servicePointValue").val(),
            rooms2: jq("#rooms2").val(),
            rooms1: jq("#rooms1").val(),
        }).success(function(data) {
            jq().toastmessage('showSuccessToast', "Patient's Queue updated successfully");
            location.reload();
        });
  }

</script>
<div class="ke-panel-frame">
    <div class="ke-panel-heading">Scheduled Patients</div>
    <div class="ke-panel-content">
        <table border="0" cellpadding="0" cellspacing="0" id="detailsTb" width="100%">
            <thead>
                <tr>
                    <th>Queue Id</th>
                    <th>Visit Date</th>
                    <th>Patient Identifier</th>
                    <th>Patient Names</th>
                    <th>Sex</th>
                    <th>Visit status</th>
                    <th>Service point</th>
                    <th>Requested time</th>
                    <th>Time taken</th>
                    <th>Assigned Provider</th>
                    <th>Patient Category</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% if (viewQueuedPatientsList.empty) { %>
                    <tr>
                        <td colspan="12">
                            No records found for specified period
                        </td>
                    </tr>
                <% } %>
                <% if (viewQueuedPatientsList) { %>
                    <% viewQueuedPatientsList.each {%>
                        <tr>
                            <td>${it.queueId}</td>
                            <td>${it.visitDate}</td>
                            <td>${it.patientIdentifier}</td>
                            <td>${it.patientNames}</td>
                            <td>${it.sex}</td>
                            <td>${it.visitStatus}</td>
                            <td>${it.serviceConceptName}</td>
                            <td>${it.startTime}</td>
                            <td>${it.duration}</td>
                            <td>${it.provider}</td>
                            <td>${it.category}</td>
                            <td>
                                <button id="editQueue" class="button task">Edit</button>
                            </td>
                        </tr>
                    <%}%>
                <%}%>
            </tbody>
        </table>
</div>
</div>
<div id="new-room-dialog" class="dialog" style="display:none;">
    <div class="dialog-header">
        <i class="icon-folder-open"></i>
        <h3>Edit Patient Service Point</h3>
    </div>

    <div class="dialog-content">
    <input type="hidden" id="queueValue" />
    <input type="hidden" id="servicePointValue" />
        <table border="0">
            <tr>
                <td colspan="2">
                    <h2>Room to Visit</h2>
                </td>
            </tr>
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
                                    <select id="rooms1" name="rooms1" onchange="LoadRoomsTypes();" class="required form-combo1">
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
                        <label for="provider-to-visit" style="margin:0px;">Provider</label>
                    </div>
                </td>
                <td valign="top">
                    <div class="col-auto">
                        <span class="select-arrow" style="width: 100%">
                            <field>
                                <select id="provider-to-visit" name="providerToVisit">
                                    <option value="">-Please select-</option>
                                    <% listProviders.each { prod -> %>
                                    <option value="${prod.providerId }">${prod.names}</option>
                                    <% } %>
                                </select>
                            </field>
                        </span>
                    </div>
                </td>
            </tr>

        </table>
        <div class="onerow" style="margin-top:10px;">
            <button class="button cancel" id="cancel">Cancel</button>
            <button class="button confirm right" id="confirm">Confirm</button>
        </div>
    </div>
</div>
