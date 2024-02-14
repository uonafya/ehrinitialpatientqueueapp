<script type="text/javascript">
var MODEL;
    var jq = jQuery;
    jq(function () {
       var table = jq("#detailsTb").DataTable(
       {
            searching: true,
            lengthChange: false,
            pageLength: 10,
            jQueryUI: true,
            pagingType: 'full_numbers',
            sort: false,
            dom: 't<"fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix datatables-info-and-pg"ip>',
            language: {
                zeroRecords: 'No queued yet.',
                paginate: {
                    first: 'First',
                    previous: 'Previous',
                    next: 'Next',
                    last: 'Last'
                }
            }
        });
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
          updateRooms();
    });
    function updateQueue() {
        jq.getJSON('${ ui.actionLink("initialpatientqueueapp", "viewQueuedPatients", "updatePatientQueue") }', {
            queueId:jq("#queueValue").val(),
            servicePoint: jq("#servicePointValue").val(),
            rooms2: jq("#rooms2").val(),
            rooms1: jq("#rooms1").val(),
            providerToVisit: jq("#provider-to-visit").val(),
        }).success(function(data) {
            jq().toastmessage('showSuccessToast', "Patient's Queue updated successfully");
            location.reload();
        });
    }
    function updateRooms() {
        jq("#rooms1").on("change", function () {
             LoadRoomsTypes();
        });
    }
    function LoadRoomsTypes() {
        jq('#rooms2').empty();
        if (jq("#rooms1").val() == 1) {
            PAGE.fillOptions("#rooms2", {
                data: MODEL.TRIAGE,
                delimiter: ",",
                optionDelimiter: "|"
            });
        }
        else if (jq("#rooms1").val() == 2) {
            PAGE.fillOptions("#rooms2", {
                data: MODEL.OPDs,
                delimiter: ",",
                optionDelimiter: "|"
            });
        }
        else if (jq("#rooms1").val() == 3) {
            PAGE.fillOptions("#rooms2", {
                data: MODEL.SPECIALCLINIC,
                delimiter: ",",
                optionDelimiter: "|"
            });
        }
    }

    PAGE = {
        fillOptions: function (divId, option) {
            jq(divId).empty();
            if (option.delimiter == undefined) {
                if (option.index == undefined) {
                    jq.each(option.data, function (index, value) {
                        if (value.length > 0) {
                            jq(divId).append(
                                "<option value='" + value + "'>" + value
                                + "</option>");
                        }
                    });
                } else {
                    jq.each(option.data, function (index, value) {
                        if (value.length > 0) {
                            jq(divId).append(
                                "<option value='" + option.index[index] + "'>"
                                + value + "</option>");
                        }
                    });
                }
            } else {
                options = option.data.split(option.optionDelimiter);
                jq.each(options, function (index, value) {
                    values = value.split(option.delimiter);
                    optionValue = values[0];
                    optionLabel = values[1];
                    if (optionLabel != undefined) {
                        if (optionLabel.length > 0) {
                            jq(divId).append(
                                "<option value='" + optionValue + "'>"
                                + optionLabel + "</option>");
                        }
                    }
                });
            }
        }
    };

    MODEL = {
                TRIAGE: "${TRIAGE}",
                OPDs: "${OPDs}",
                SPECIALCLINIC: "${SPECIALCLINIC}"
            };

</script>
<div class="ke-panel-frame">
    <div class="ke-panel-heading">Scheduled Patients</div>
    <div class="ke-panel-content">
        <table id="detailsTb">
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
                                    <select id="rooms1" name="rooms1" class="required form-combo1">
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
