<script type="text/javascript">
   var jq = jQuery.noConflict();
 </script>
<script type="text/javascript">
   var jq = jQuery;
 jq(function () {
     var table = jq("#appointmentsTb").DataTable(
     {
          searching: true,
          lengthChange: false,
          pageLength: 10,
          jQueryUI: true,
          pagingType: 'full_numbers',
          sort: false,
          dom: 't<"fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix datatables-info-and-pg"ip>',
          language: {
              zeroRecords: 'No patient appointments recorded.',
              paginate: {
                  first: 'First',
                  previous: 'Previous',
                  next: 'Next',
                  last: 'Last'
              }
          }
      });
      populateAppointmentDetails();
      jq("#filterAppointments").click(function () {
        populateAppointmentDetails();
      });
     jq('#appointmentsTb tbody').on( 'click', 'tr', function () {

               var trData = table.row(this).data();
               //jq("#edit-appointment-id").val(trData[0]);
               //jq("#editNotes").val(trData[5]);
               //jq("#edit-appointment-list").show();
        //ui.navigate('initialpatientqueueapp', 'patientCategory', {patientId: trData[1]});
        //console.log(trData);
     });
 });
 function populateAppointmentDetails() {
     fetchAppointmentSummariesByDateRange(jQuery("#summaryFromDate-field").val(), jQuery("#summaryToDate-field").val());

 }
  function fetchAppointmentSummariesByDateRange(fromDate, toDate) {
       var toReturn;
       jQuery.ajax({
           type: "GET",
           url: '${ui.actionLink("initialpatientqueueapp", "managePatientAppointments", "fetchAppointmentSummariesByDateRange")}',
           dataType: "json",
           global: false,
           async: false,
           data: {
               fromDate: fromDate,
               toDate: toDate
           },
           success: function (data) {
               toReturn = data;
           }
       });
       return populateAppointmentsBodyForPatientSummary(toReturn);
 }

 function populateAppointmentsBodyForPatientSummary(data) {
       jQuery("#appointmentsTb").DataTable().clear().destroy();

       data.map((item) => {
             jQuery("#appointmentTbody").append("<tr><td>" + item.appointmentNumber + "</td><td>"+ item.patientIdentifier + "</td><td>"+ item.patientNames + "</td><td>"+ item.appointmentService + "</td><td>" + item.appointmentServiceType + "</td><td>" + item.provider + "</td><td>" + item.response + "</td><td>" + item.startTime + "</td><td>" + item.endTime + "</td><td>" + item.appointmentReason +"</td><td>"+ item.status + "</td></tr>");
       });
 }
</script>
 <div class="ke-panel-frame">
     <div class="ke-panel-heading">Scheduled Appointments</div>
              <div class="ke-panel-content">
                 <div class="row">
                     <div class="col-6">
                         <div style="margin-top: -1px " class="onerow">
                             <i class="icon-filter" style="font-size: 26px!important; color: #5b57a6"></i>
                              <label>&nbsp;&nbsp;From&nbsp;</label>${ui.includeFragment("uicommons", "field/datetimepicker", [formFieldName: 'fromDate', id: 'summaryFromDate', label: '', useTime: false, defaultToday: false, class: ['newdtp']])}
                             <label>&nbsp;&nbsp;To&nbsp;</label  >${ui.includeFragment("uicommons", "field/datetimepicker", [formFieldName: 'toDate',    id: 'summaryToDate',   label: '', useTime: false, defaultToday: false, class: ['newdtp']])}
                             <button id="filterAppointments" type="button" class=" btn btn-primary">${ui.message("Filter")}
                             </button>
                         </div>
                     </div>
                 </div>
             </div>
             <br />
            <table id="appointmentsTb">
                <thead>
                    <tr>
                        <th>Appointment Number</th>
                        <th>Patient Identifier</th>
                        <th>Patient Name</th>
                        <th>Appointment Service</th>
                        <th>Appointment Service type</th>
                        <th>Provider</th>
                        <th>Response</th>
                        <th>Start time</th>
                        <th>End time</th>
                        <th>Status</th>
                        <th>Comments</th>
                    </tr>
                </thead>
                <tbody id="appointmentTbody"></tbody>
            </table>
     </div>
 </div>