<script type="text/javascript">
   var jq = jQuery.noConflict();
 </script>
 <script type="text/javascript">
     var jq = jQuery;
         jq(function () {
             var table = jq("#appTb").DataTable();
             jq('#edit-appointmentDate').datepicker();

             jq('#appTb tbody').on( 'click', 'tr', function () {
                       var trData = table.row(this).data();
                       jq("#edit-appointment-id").val(trData[0]);
                       jq("#editNotes").val(trData[5]);
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
                     confirm: function () {
                        updateAppointmentList();
                     },
                     cancel: function () {
                         location.reload();
                     }
                 }
             });
         });
         function updateAppointmentList() {
            jq.getJSON('${ ui.actionLink("initialpatientqueueapp", "managePatientAppointments", "editAppointments") }', {
                        editAppointmentId: jq("#edit-appointment-type_id").val(),
                        editAppointmentStatus: jq("#edit-appointment-status").val(),
                        editAppointmentVoidReason: jq("#edit-appointment-void-reason").val(),
                        editAppointmentAction: jq("#edit-appointment-action").val(),
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
         <input type="hidden" id="edit-appointment-id" name="editAppointmentId" />
             <table border="0">
                 <tr>
                    <td>Appointment Status</td>
                    <td>
                        <select id="edit-appointment-status" name="editAppointmentStatus">
                            <option value="1">Scheduled</option>
                            <option value="2">Active</option>
                            <option value="3">Cancelled</option>
                            <option value="4">Missed</option>
                            <option value="5">Completed</option>
                        </select>
                    </td>
                 </tr>
                 <tr>
                     <td>Action</td>
                     <td><select id="edit-appointment-action" name="editAppointmentAction">
                        <option value="1">Queue for visit</option>
                        <option value="2">Edit</option>
                        <option value="3">Void</option>
                        <option value="4">Delete</option>
                     </select></td>
                 </tr>
                 <tr>
                      <td colspan="2">Reason for voiding appointment</td>
                  </tr>
                  <tr>
                      <td colspan="2">
                          <textarea id="edit-appointment-void-reason" name="editAppointmentVoidReason" rows="4" cols="50"></textarea>
                      </td>
                  </tr>


             </table>
         </div>
         <div class="onerow" style="margin-top:10px;">
             <button class="button cancel" id="cancel">Cancel</button>
             <button class="button confirm right" id="confirm">Confirm</button>
         </div>
     </div>