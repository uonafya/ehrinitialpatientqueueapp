<%
 ui.decorateWith("kenyaemr", "standardPage", [layout: "sidebar"])
 ui.includeJavascript("ehrconfigs", "emr.js")
 ui.includeJavascript("ehrconfigs", "jquery.dataTables.min.js")
 ui.includeJavascript("ehrconfigs", "bootstrap.min.js")
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
     });
 </script>
 <div class="ke-page-sidebar">
     ${ui.includeFragment("kenyaui", "widget/panelMenu", [items: menuItems])}
     ${ ui.decorate("kenyaui", "panel", [ heading: "Select scheduled date " ], """<div id="calendar"></div>""") }
 </div>
 <div class="ke-page-content">
 	${ ui.includeFragment("initialpatientqueueapp", "viewAppointments", [ pageProvider: "initialpatientqueueapp", page: "viewScheduledAppointmentList", scheduledDate: scheduledDate ]) }
 </div>