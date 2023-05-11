<%
    ui.decorateWith("kenyaemr", "standardPage", [ layout: "sidebar" ])
    def menuItems = [
            [
            label: "View Queued Patients", extra: "Access queued patients for changes", iconProvider: "kenyaui", icon: "buttons/patient_add.png", href: ui.pageLink("initialpatientqueueapp", "viewQueuedPatients")
            ],
            [
            label: "Appointments", extra: "Manage patient appointments", iconProvider: "kenyaui", icon: "buttons/visit_end.png", href: ui.pageLink("initialpatientqueueapp", "viewAppointments")
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
