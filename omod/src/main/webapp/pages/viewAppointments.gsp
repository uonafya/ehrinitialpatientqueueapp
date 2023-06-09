<%
 ui.decorateWith("kenyaemr", "standardPage", [layout: "sidebar"])
 ui.includeJavascript("ehrconfigs", "jquery.dataTables.min.js")
 ui.includeCss("ehrconfigs", "jquery.dataTables.min.css")
 ui.includeCss("ehrconfigs", "referenceapplication.css")
 def menuItems = [
             [label: "Back to home", iconProvider: "kenyaui", icon: "buttons/back.png", label: "Back to home", href: ui.pageLink("initialpatientqueueapp", "patientQueueHome")]
     ]
 %>

 <style type="text/css">
 #calendar {
     text-align: center;
 }
 #calendar .ui-widget-content {
     border: 0;
     background: inherit;
     padding: 0;
     margin: 0 auto;
 }
 </style>

 <script type="text/javascript">
     jQuery(function() {
         jQuery('#calendar').datepicker({
             dateFormat: 'yy-mm-dd',
             defaultDate: '${ kenyaui.formatDateParam(scheduledDate) }',
             gotoCurrent: true,
             onSelect: function(dateText) {
                 ui.navigate('initialpatientqueueapp', 'viewAppointments', { scheduledDate: dateText });
             }
         });
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
            jq('#authdialog').hide();
     });
 </script>
 <div class="ke-page-sidebar">
     ${ui.includeFragment("kenyaui", "widget/panelMenu", [items: menuItems])}
     ${ ui.decorate("kenyaui", "panel", [ heading: "Select scheduled date " ], """<div id="calendar"></div>""") }
 </div>
 <div class="ke-page-content">
 <br />
 <br />
 <div>
 	<table id="appointmentsList">
        <thead>
            <tr>
                <th>Patient Name</th>
                <th>Appointment type</th>
                <th>Provider</th>
                <th>Scheduled start date and time</th>
                <th>Scheduled end date and time</th>
                <td>Appointment Reason</td>
                <th>Status</th>
            </tr>
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
 </div>