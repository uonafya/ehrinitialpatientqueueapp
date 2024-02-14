<script type="text/javascript">
   jq("#appointmentsList").DataTable({
          searching: true,
          lengthChange: false,
          pageLength: 10,
          jQueryUI: true,
          pagingType: 'full_numbers',
          sort: false,
          dom: 't<"fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix datatables-info-and-pg"ip>',
          language: {
              zeroRecords: 'No appointments recorded.',
              paginate: {
                  first: 'First',
                  previous: 'Previous',
                  next: 'Next',
                  last: 'Last'
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
