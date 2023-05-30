<script type="text/javascript">
   var jq = jQuery.noConflict();
 </script>
 <script type="text/javascript">
     var jq = jQuery;
         jq(function () {
             var table = jq("#appTb").DataTable();

             jq('#appTb tbody').on( 'click', 'tr', function () {
                       var trData = table.row(this).data();
                       jq("#edit-appointment-id").val(trData[0]);
                       jq("#edit-appointment-list").show();
                //ui.navigate('initialpatientqueueapp', 'patientCategory', {patientId: trData[1]});
             });

             var editAppointmentDialog = emr.setupConfirmationDialog({
                 dialogOpts: {
                     overlayClose: false,
                     close: true
                 },
                 selector: '#edit-appointment-list',
                 actions: {
                     edit-appointment-confirm: function () {
                        updateAppointmentList();
                     },
                     edit-appointment-cancel: function () {
                         location.reload();
                     }
                 }
             });
         });
         function updateAppointmentList() {
            jq.getJSON('${ ui.actionLink("initialpatientqueueapp", "managePatientAppointments", "editAppointments") }', {
                        appointmentId: jq("#edit-appointment-type_id").val(),
                        editAppointmentType:jq("#edit-appointment-type-name").val(),
                        editAppointmentProvider: jq("#edit-appointment-visit-type").val(),
                        editAppointmentStatus: jq("#edit-appointment-duration").val(),
                        editAppointmentReason: jq("#edit-appointment-description").val(),
                        editAppointmentStartTime: jq("#edit-appointment-description").val(),
                        editAppointmentEndTime: jq("#edit-appointment-description").val(),
                        editAppointmentAction: jq("#edit-appointment-type-action").val(),
                    }).success(function(data) {
                        jq().toastmessage('showSuccessToast', "Appointment updated successfully");
                        location.reload();
                    });
         }
 </script>
 <div class="ke-panel-frame">
     <div class="ke-panel-heading">Scheduled Appointments</div>
         <div class="ke-panel-content">
            <table border="0" cellpadding="0" cellspacing="0" id="appTb" width="100%">
                <thead>
                    <tr>
                        <th>Appointment ID</th>
                        <th>Appointment type</th>
                        <th>PatientID</th>
                        <th>Provider</th>
                        <th>Status</th>
                        <th>Appointment reason</th>
                        <th>Start time</th>
                        <th>End time</th>
                        <th>Action</td>
                    </tr>
                </thead>
                <tbody>
                    <% if (allPatientAppointments.empty) { %>
                        <tr>
                            <td colspan="9">
                                No records found for specified period
                            </td>
                        </tr>
                    <% } %>
                    <% if (allPatientAppointments) { %>
                        <% allPatientAppointments.each {%>
                            <tr>
                                <td>${it.appointmentId}</td>
                                <td>${it.appointmentType}</td>
                                <td>${it.patientId}</td>
                                <td>${it.provider}</td>
                                <td>${it.Status}</td>
                                <td>${it.appointmentReason}</td>
                                <td>${it.startTime}</td>
                                <td>${it.endTime}</td>
                                <td>
                                <button id="editAppointment" class="button task">Action</button>
                                </td>
                            </tr>
                        <%}%>
                    <%}%>
                </tbody>
            </table>
         </div>
     </div>
 </div>
 <div id="edit-appointment-list" class="dialog" style="display:none;">
         <div class="dialog-header">
             <i class="icon-folder-open"></i>
             <h3>Edit Appointment</h3>
         </div>
         <div class="dialog-content">
         <input type="hidden" id="edit-appointment-id" />
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
                     <select id="edit-appointment-action" name="editAction">
                        <option value="1">Edit</option>
                        <option value="2">Void</option>
                        <option value="3">Delete</option>
                        <option value="3">Queue for visit</option>
                     </select>
                 </tr>

                 </tr>
             </table>
         </div>
         <div class="onerow" style="margin-top:10px;">
             <button class="button cancel" id="edit-appointment-cancel">Cancel</button>
             <button class="button confirm right" id="edit-appointment-confirm">Confirm</button>
         </div>
     </div>