
<script type="text/javascript">
   var jq = jQuery.noConflict();
</script>
<script type="text/javascript">
var jq = jQuery;
    jq(function () {
        jq('#confirm').on( 'click',function () {
            saveAppointmentService()
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
                confirm: function () {
                   updateAppointmentType();
                },
                cancel: function () {
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
                            editAppointmentRetire: jq("#edit-appointment-type-retire").val(),
                        }).success(function(data) {
                            jq().toastmessage('showSuccessToast', "Appointment type updated successfully");
                            location.reload();
                        });
    }
    function saveAppointmentService() {
                jq.getJSON('${ ui.actionLink("initialpatientqueueapp", "manageAppointmentsTypes", "createAppointmentService") }', {
                    name:jq("#appointment-type-name").val(),
                    description: jq("#description").val(),
                    speciality: jq("#speciality").val(),
                    startTime: jq("#startTime").val(),
                    endTime: jq("#endTime").val(),
                    maxAppointmentsLimit: jq("#maxAppointmentsLimit").val(),
                    durationMins: jq("#durationMins").val(),
                    initialAppointmentStatus: jq("#initialAppointmentStatus").val(),
                }).success(function(data) {
                    jq().toastmessage('showSuccessToast', "Appointment service created successfully");
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
                        <td><input type="text" id="name" name="name" /></td>
                    </tr>
                    <tr>
                        <td>Speciality</td>
                        <td>
                        <select id="speciality" name="speciality">
                            <option value="">Please select speciality type</option>
                            <% specialityTypes.each { type -> %>
                                <option value="${type.uuid}">${type.name}</option>
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
                <th>Name</th>
                <th>Speciality</th>
                <th>Start Time</th>
                <th>End Time</th>
                <th>Description</th>
            </tr>
        </thead>
        <tbody>
            <% if (appointmentService.empty) { %>
                <tr>
                    <td colspan="5">
                        No records found for specified period
                    </td>
                </tr>
            <% } %>
            <% if (appointmentService) { %>
                <% appointmentService.each {%>
                <tr>
                    <td>${it.name}</td>
                    <% if(it.speciality) {%>
                      <td>${it.speciality.name}</td>
                    <%}%>
                    <td>${it.startTime}</td>
                    <td>${it.endTime}</td>
                    <td>${it.description}</td>
                </tr>
                <%}%>
            <%}%>
        </tbody>
    </table>