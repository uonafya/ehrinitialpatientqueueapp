<%
    ui.decorateWith("kenyaemr", "standardPage", [ layout: "sidebar" ])
    def menuItems = [
            [
                label: "View Queued Patients", extra: "Access queued patients for changes", iconProvider: "kenyaui", icon: "buttons/patient_add.png", href: ui.pageLink("initialpatientqueueapp", "viewQueuedPatients")
            ],
            [
                label: "Create Speciality", extra: "Manage speciality types", iconProvider: "kenyaui", icon: "buttons/developer_overview.png", href: ui.pageLink("initialpatientqueueapp", "manageSpecialityTypes")
            ],
            [
                label: "Appointment Services", extra: "Manage appointments services", iconProvider: "kenyaui", icon: "buttons/developer_overview.png", href: ui.pageLink("initialpatientqueueapp", "manageAppointmentsTypes")
            ],
            [
                label: "Appointment Services Type", extra: "Manage appointments service types", iconProvider: "kenyaui", icon: "buttons/developer_overview.png", href: ui.pageLink("initialpatientqueueapp", "manageAppointmentsServiceTypes")
            ],
            [
                label: "Patient Appointments", extra: "Manage Patient appointment", iconProvider: "kenyaui", icon: "buttons/visit_end.png", href: ui.pageLink("initialpatientqueueapp", "managePatientAppointments")
            ],
            [
                label: "Sick Off", extra: "Manage Patient Sick Offs", iconProvider: "kenyaui", icon: "buttons/facility.png", href: ui.pageLink("initialpatientqueueapp", "sickOffList")
            ]
    ]
%>

    <div class="ke-page-sidebar">
        ${ ui.includeFragment("kenyaemr", "patient/patientSearchForm", [ defaultWhich: "all" ]) }
        ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ heading: "Tasks", items: menuItems ]) }
    </div>

    <div class="ke-page-content">
        ${ ui.includeFragment("kenyaemr", "patient/patientSearchResults", [ pageProvider: "initialpatientqueueapp", page: "patientCategory" ]) }
    </div>
    <script type="text/javascript">
        jQuery(function() {
            jQuery('input[name="query"]').focus();
        });
    </script>
