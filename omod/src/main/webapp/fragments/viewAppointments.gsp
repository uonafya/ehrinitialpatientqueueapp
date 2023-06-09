<script type="text/javascript">
   jq("#appointmentsList").DataTable({
    searchPanes: true,
        searching: true,
        "pagingType": 'simple_numbers',
        'dom': 'flrtip',
        "oLanguage": {
            "oPaginate": {
                "sNext": '<i class="fa fa-chevron-right py-1" ></i>',
                "sPrevious": '<i class="fa fa-chevron-left py-1" ></i>'
            }
        }
   });
</script>
<div>
    <table id="appointmentsList">
        <thead>
            <th>Patient Name</th>
            <th>Appointment type</th>
            <th>Provider</th>
            <th>Scheduled start date and time</th>
            <th>Scheduled end date and time</th>
            <td>Appointment Reason</td>
            <th>Status</th>
        </thead>
        <tbody>
            <% getTodaysAppointments.each { appointment -> %>
                <tr>
                    <td>${appointment.patient.givenName} ${appointment.patient.familyName}</td>
                    <td>${appointment.appointmentType}</td>
                    <td>${appointment.provider}</td>
                    <td>${appointment.startTime}</td>
                    <td>${appointment.endTime}</td>
                    <td>${appointment.appointmentReason}</td>
                    <td>${appointment.Status}</td>
                </tr>
            <% } %>
        </tbody>
    <table>
</div>
