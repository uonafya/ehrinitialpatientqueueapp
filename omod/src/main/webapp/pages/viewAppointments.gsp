<%
 ui.decorateWith("kenyaemr", "standardPage", [layout: "sidebar"])
 def menuItems = [
             [label: "Back to home", iconProvider: "kenyaui", icon: "buttons/back.png", label: "Back to home", href: ui.pageLink("initialpatientqueueapp", "patientQueueHome")]
     ]
 %>
 <div class="ke-page-sidebar">
     ${ui.includeFragment("kenyaui", "widget/panelMenu", [items: menuItems])}
 </div>

 <div class="ke-page-content">
    <div class="col col-sm-12 col-md-12 col-lg-12">
        ${ui.includeFragment("initialpatientqueueapp", "viewAppointments")}
    </div>
 </div>