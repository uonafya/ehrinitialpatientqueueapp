<script type="text/javascript">
   var jq = jQuery.noConflict();
 </script>
 <script type="text/javascript">
     var jq = jQuery;
         jq(function () {
             jq("#appTb").DataTable();

             jq('#appTb tbody').on( 'click', 'tr', function () {
                       var trData = table.row(this).data();
                ui.navigate('initialpatientqueueapp', 'patientCategory', {patientId: trData[1]});
             });
         });
 </script>
 <div class="ke-panel-frame">
     <div class="ke-panel-heading">Scheduled Appointments</div>
         <div class="ke-panel-content">
            <table border="1" cellpadding="0" cellspacing="0" id="appTb" width="100%">
                <thead>
                    <tr>
                        <th>Appointment type</th>
                        <th>Patient</th>
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
                                <td>${it.appointmentType.name}</td>
                                <td>${it.patient}</td>
                                <td>${it.timeSlot.appointmentBlock.provider.name}</td>
                                <td>${it.status.name}</td>
                                <td>${it.reason}</td>
                                <td>${it.timeSlot.startDate}</td>
                                <td>${it.timeSlot.endDate}</td>
                            </tr>
                        <%}%>
                    <%}%>
                </tbody>
            </table>
         </div>
     </div>
 </div>