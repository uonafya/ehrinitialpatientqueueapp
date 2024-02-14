
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
        var tbl = jq("#appointmentTypesTb").DataTable(
        {
           searching: true,
           lengthChange: false,
           pageLength: 10,
           jQueryUI: true,
           pagingType: 'full_numbers',
           sort: false,
           dom: 't<"fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix datatables-info-and-pg"ip>',
           language: {
               zeroRecords: 'No appointment types recorded.',
               paginate: {
                   first: 'First',
                   previous: 'Previous',
                   next: 'Next',
                   last: 'Last'
               }
           }
       });
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
                    name:jq("#appointment-service-name").val(),
                    description: jq("#description").val(),
                    speciality: jq("#speciality").val(),
                    hourStartTime: jq("#hourStartTime").val(),
                    minutesStartTime: jq("#minutesStartTime").val(),
                    hourEndTime: jq("#hourEndTime").val(),
                    minutesEndTime: jq("#minutesEndTime").val(),
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
    <div class="ke-panel-heading">Create Appointment Service</div>
        <div class="ke-panel-content" style="background-color: #F3F9FF">
            <div>
                <table border="0">
                    <tr>
                        <td>Name</td>
                        <td><input type="text" id="appointment-service-name" name="name" /></td>
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
                      <td>Start Time</td>
                      <td>
                        Hour:
                        <select id="hourStartTime" name="hourStartTime">
                            <option value="06">06</option>
                            <option value="07">07</option>
                            <option value="08">08</option>
                            <option value="09">09</option>
                            <option value="10">10</option>
                            <option value="11">11</option>
                            <option value="12">12</option>
                            <option value="13">13</option>
                            <option value="14">14</option>
                            <option value="15">15</option>
                            <option value="16">16</option>
                            <option value="17">17</option>
                            <option value="18">18</option>
                            <option value="19">19</option>
                        </select>
                        Min
                        <select id="minutesStartTime" name="minutesStartTime">
                            <option value="00">00</option>
                            <option value="15">15</option>
                            <option value="30">30</option>
                            <option value="45">45</option>
                        </select>
                      </td>
                    </tr>
                    <tr>
                      <td>End Time</td>
                      <td>
                        Hour:
                        <select id="hourEndTime" name="hourEndTime">
                            <option value="06">06</option>
                            <option value="07">07</option>
                            <option value="08">08</option>
                            <option value="09">09</option>
                            <option value="10">10</option>
                            <option value="11">11</option>
                            <option value="12">12</option>
                            <option value="13">13</option>
                            <option value="14">14</option>
                            <option value="15">15</option>
                            <option value="16">16</option>
                            <option value="17">17</option>
                            <option value="18">18</option>
                            <option value="19">19</option>
                        </select>
                        Min:
                        <select id="minutesEndTime" name="minutesEndTime">
                            <option value="00">00</option>
                            <option value="15">15</option>
                            <option value="30">30</option>
                            <option value="45">45</option>
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
                        <td colspan="2"><textarea id="description" name="description" rows="4" cols="50"></textarea></td>
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

    <table id="appointmentTypesTb">
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
            <% if (appointmentService) { %>
                <% appointmentService.each {%>
                <tr>
                    <td>${it.name}</td>
                    <% if(it.speciality) {%>
                      <td>${it.speciality.name}</td>
                    <%} else {%>
                    <td>&nbsp;</td>
                   <%}%>

                    <td>${it.startTime}</td>
                    <td>${it.endTime}</td>
                    <td>${it.description}</td>
                </tr>
                <%}%>
            <%}%>
        </tbody>
    </table>