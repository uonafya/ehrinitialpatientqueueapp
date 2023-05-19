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
    ${ui.includeFragment("initialpatientqueueapp", "viewAppointments")}
 </div>