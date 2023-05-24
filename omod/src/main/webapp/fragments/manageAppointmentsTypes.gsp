
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
                    alert("Cancelled");
                    location.reload();
        });

        jq("#appointmentTypesTb").DataTable();
    });
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

    <table border="1" cellpadding="0" cellspacing="0" id="appointmentTypesTb" width="100%">
        <thead>
            <tr>
                <th>Name</th>
                <th>Visit Type</th>
                <th>Duration</th>
                <th>Description</th>
            </tr>
        </thead>
        <tbody>
            <% if (appointmentTypes.empty) { %>
                <tr>
                    <td colspan="4">
                        No records found for specified period
                    </td>
                </tr>
            <% } %>
            <% if (appointmentTypes) { %>
                <% appointmentTypes.each {%>
                <tr>
                    <td>${it.name}</td>
                    <td>${it.visitType.name}</td>
                    <td>${it.duration}</td>
                    <td>${it.description}</td>
                </tr>
                <%}%>
            <%}%>
        </tbody>
    </table>