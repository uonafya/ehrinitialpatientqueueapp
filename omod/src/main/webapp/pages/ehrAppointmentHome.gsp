<%
    ui.decorateWith("kenyaemr", "standardPage", [ layout: "sidebar" ])
    def menuItems = [
            [
                label: "Appointment Service", extra: "Manage appointments types", iconProvider: "kenyaui", icon: "buttons/developer_overview.png", href: ui.pageLink("initialpatientqueueapp", "manageAppointmentsTypes")
            ],
            [
                label: "Patient Appointments", extra: "Manage Patient appointment", iconProvider: "kenyaui", icon: "buttons/visit_retrospective.png", href: ui.pageLink("initialpatientqueueapp", "managePatientAppointments")
            ]
    ]
%>

<div class="ke-page-sidebar">
        ${ ui.includeFragment("kenyaemr", "patient/patientSearchForm", [ defaultWhich: "all" ]) }
        ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ heading: "Tasks", items: menuItems ]) }
</div>
<div class="ke-page-content">
        ${ ui.includeFragment("kenyaemr", "patient/patientSearchResults", [ pageProvider: "initialpatientqueueapp", page: "patientAppointmentManagement" ]) }
    </div>
    <script type="text/javascript">
        jQuery(function() {
            jQuery('input[name="query"]').focus();
        });
    </script>