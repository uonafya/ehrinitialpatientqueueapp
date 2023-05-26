<script type="text/javascript">
   var jq = jQuery.noConflict();
 </script>
 <script type="text/javascript">
     var jq = jQuery;
         jq(function () {
             var table = jq("#appTb").DataTable();

             jq('#appTb tbody').on( 'click', 'tr', function () {
                       var trData = table.row(this).data();
                ui.navigate('initialpatientqueueapp', 'patientCategory', {patientId: trData[1]});
             });
         });
 </script>
 <div class="ke-panel-frame">
     <div class="ke-panel-heading">Scheduled Appointments</div>
         <div class="ke-panel-content">
            <table border="0" cellpadding="0" cellspacing="0" id="appTb" width="100%">
                <thead>
                    <tr>
                        <th>Appointment type</th>
                        <th>PatientID</th>
                        <th>Provider</th>
                        <th>Status</th>
                        <th>Appointment reason</th>
                        <th>Start time</th>
                        <th>End time</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (allPatientAppointments.empty) { %>
                        <tr>
                            <td colspan="7">
                                No records found for specified period
                            </td>
                        </tr>
                    <% } %>
                    <% if (allPatientAppointments) { %>
                        <% allPatientAppointments.each {%>
                            <tr>
                                <td>${it.appointmentType}</td>
                                <td>${it.patientId}</td>
                                <td>${it.provider}</td>
                                <td>${it.Status}</td>
                                <td>${it.appointmentReason}</td>
                                <td>${it.startTime}</td>
                                <td>${it.endTime}</td>
                            </tr>
                        <%}%>
                    <%}%>
                </tbody>
            </table>
         </div>
     </div>
 </div>