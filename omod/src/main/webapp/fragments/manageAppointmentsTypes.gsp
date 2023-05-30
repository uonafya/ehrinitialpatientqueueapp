
<script type="text/javascript">
   var jq = jQuery.noConflict();
 </script>
<script type="text/javascript">
var jq = jQuery;
    jq(function () {
        jq('#confirm').on( 'click',function () {
            saveAppointmentType()
        });

        jq('#cancel').on( 'click',function () {
                    location.reload();
        });
        var tbl = jq("#appointmentTypesTb").DataTable();
        jq('#appointmentTypesTb tbody').on( 'click', 'tr', function () {
            var trData = tbl.row(this).data();
            jq("#edit-appointment-type_id").val(trData[0]);
            jq("#edit-appointment-type-name").val(trData[1]);
            jq("#edit-appointment-visit-type").val(trData[2]);
            jq("#edit-appointment-duration").val(trData[3]);
            jq("#edit-appointment-description").val(trData[4]);
            jq("#edit-appointment-type-dialog").show();
        });
        var editAppointmentTypeDialog = emr.setupConfirmationDialog({
            dialogOpts: {
                overlayClose: false,
                close: true
            },
            selector: '#edit-appointment-type-dialog',
            actions: {
                edit-confirm: function () {
                   updateAppointmentType();
                },
                edit-cancel: function () {
                    location.reload();
                }
            }
        });
    });
    function updateAppointmentType() {
        jq.getJSON('${ ui.actionLink("initialpatientqueueapp", "manageAppointmentsTypes", "editAppointmentType") }', {
                            appointmentTypeId: jq("#edit-appointment-type_id").val(),
                            editName:jq("#edit-appointment-type-name").val(),
                            editAppointmentVisitType: jq("#edit-appointment-visit-type").val(),
                            editAppointmentDuration: jq("#edit-appointment-duration").val(),
                            editDescription: jq("#edit-appointment-description").val(),
                            editAction: jq("#edit-appointment-type-action").val(),
                        }).success(function(data) {
                            jq().toastmessage('showSuccessToast', "Appointment type updated successfully");
                            location.reload();
                        });
    }
    function saveAppointmentType() {
                jq.getJSON('${ ui.actionLink("initialpatientqueueapp", "manageAppointmentsTypes", "createAppointmentType") }', {
                    name:jq("#appointment-type-name").val(),
                    appointmentVisitType: jq("#appointment-visit-type").val(),
                    appointmentDuration: jq("#appointment-duration").val(),
                    description: jq("#appointment-description").val(),
                }).success(function(data) {
                    jq().toastmessage('showSuccessToast', "Patient's Appointment type created successfully");
                    location.reload();
                });
    }
    function editAppointmentType() {
        alert("Editing appointments under development");
    }
    function deleteAppointmentType() {
            alert("Deleting appointments under development");
        }
</script>
<div class="ke-panel-frame">
    <div class="ke-panel-heading">Create Appointment Types</div>
        <div class="ke-panel-content" style="background-color: #F3F9FF">
            <div>
                <table border="0">
                    <tr>
                        <td>Name</td>
                        <td><input type="text" id="appointment-type-name" name="appointmentType" /></td>
                    </tr>
                    <tr>
                        <td>Visit type</td>
                        <td>
                        <select id="appointment-visit-type" name="appointmentVisitType">
                            <option value="">Please select visit type</option>
                            <% types.each { type -> %>
                                <option value="${type.visitTypeId }">${type.name}</option>
                            <% } %>
                        </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Appointment duration</td>
                        <td><input type="text" id="appointment-duration" name="appointmentDuration" />(in Minutes)</td>
                    </tr>
                    <tr>
                        <td colspan="2">Appointment Description</td>
                    </tr>
                    <tr>
                        <td colspan="2"><textarea id="appointment-description" name="description" rows="4" cols="50"></textarea></td>
                    </tr>
                </table>
                <div class="onerow" style="margin-top:10px;">
                    <button class="button cancel" id="cancel">Cancel</button>
                    <button class="button confirm right" id="confirm">Confirm</button>
                </div>
            </div>
        <div>
    </div>
</div>
    <br />

    <table border="0" cellpadding="0" cellspacing="0" id="appointmentTypesTb" width="100%">
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Visit Type</th>
                <th>Duration</th>
                <th>Description</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% if (appointmentTypes.empty) { %>
                <tr>
                    <td colspan="5">
                        No records found for specified period
                    </td>
                </tr>
            <% } %>
            <% if (appointmentTypes) { %>
                <% appointmentTypes.each {%>
                <tr>
                    <td>${it.appointmentTypeId}</td>
                    <td>${it.name}</td>
                    <td>${it.visitType.name}</td>
                    <td>${it.duration}</td>
                    <td>${it.description}</td>
                    <td>
                        <button id="editAppointmentType" class="button task">Edit</button>
                    </td>
                </tr>
                <%}%>
            <%}%>
        </tbody>
    </table>

    <div id="edit-appointment-type-dialog" class="dialog" style="display:none;">
        <div class="dialog-header">
            <i class="icon-folder-open"></i>
            <h3>Edit Appointment Type</h3>
        </div>
        <div class="dialog-content">
        <input type="hidden" id="edit-appointment-type_id" />
            <table border="0">
                <tr>
                    <td>Name</td>
                    <td><input type="text" id="edit-appointment-type-name" name="editName" /></td>
                </tr>
                <tr>
                    <td>Visit type</td>
                    <td>
                        <select id="edit-appointment-visit-type" name="editAppointmentVisitType">
                            <option value="">Please select visit type</option>
                            <% types.each { type -> %>
                                <option value="${type.visitTypeId }">${type.name}</option>
                            <% } %>
                        </select>
                    </td>
                </tr>
                    <td>Duration</td>
                    <td><input type="text" id="edit-appointment-duration" name="editAppointmentDuration" />
                    </td>
                <tr>
                <tr>
                    <td colspan="2">Description</td>
                </tr>
                <tr>
                    <td colspan="2"><textarea id="edit-appointment-description" name="editDescription" rows="4" cols="50"></textarea></td>
                </tr>
                <tr>
                    <td>Action</td>
                    <select id="edit-appointment-type-action" name="editAction">
                       <option value="1">Edit</option>
                       <option value="2">Void</option>
                       <option value="3">Delete</option>
                    </select>
                </tr>

                </tr>
            </table>
        </div>
        <div class="onerow" style="margin-top:10px;">
            <button class="button cancel" id="edit-cancel">Cancel</button>
            <button class="button confirm right" id="edit-confirm">Confirm</button>
        </div>
    </div>