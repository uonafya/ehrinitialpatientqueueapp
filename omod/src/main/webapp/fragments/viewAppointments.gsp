<%
    ui.includeJavascript("initialpatientqueueapp", "calendar/calendar-main.js")
    ui.includeJavascript("initialpatientqueueapp", "calendar/moment.min.js")
    ui.includeJavascript("initialpatientqueueapp", "jquery-ui-1.13.1.min.js")
    ui.includeJavascript("initialpatientqueueapp", "jquery.autocomplete.min.js")

    ui.includeCss("initialpatientqueueapp", "jquery-ui-1.13.1.min.css")
    ui.includeCss("initialpatientqueueapp", "calendar/calendar-main.css")
%>
<script type="text/javascript">
   var jq = jQuery.noConflict();
 </script>
<style>
#hidden_patient_input {
    display: none;
}

#task_description {
    display: block;
}

#task_heading {
    display: block;
}

</style>
<script>
    function showDiv(element) {
        document.getElementById('hidden_patient_input').style.display = element.value == 'Consultation' ? 'block' : 'none';
        document.getElementById('task_heading').style.display = element.value == 'Task' ? 'block' : 'none';
        document.getElementById('task_description').style.display = element.value == 'Task' ? 'block' : 'none';
    }

    document.addEventListener('DOMContentLoaded', () => {
        const todayDate = moment().startOf("day");
        const TODAY = todayDate.format("YYYY-MM-DD");

        let calendarEvents = [];
        <% events.each { event -> %>
        calendarEvents.push({
            title: '${event.title}',
            start: '${event.startDate}',
            end: '${event.endDate}'
        });
        <% } %>

        const calendarEl = document.getElementById("myAppointments-calendar");
        const calendar = new FullCalendar.Calendar(calendarEl, {
            headerToolbar: {
                left: "prev,next today",
                center: "title",
                right: "dayGridMonth,timeGridWeek,timeGridDay,listMonth"
            },

            height: 800,
            contentHeight: 780,
            aspectRatio: 3,

            nowIndicator: true,
            now: TODAY + "T09:25:00",

            views: {
                dayGridMonth: {buttonText: "month"},
                timeGridWeek: {buttonText: "week"},
                timeGridDay: {buttonText: "day"}
            },

            initialView: "dayGridMonth",
            initialDate: TODAY,

            eventRender: function (event, element, view) {
                element.bind('click', function () {
                    const day = (jq().fullCalendar.formatDate(event.start, 'dd'));
                    const month = (jq().fullCalendar.formatDate(event.start, 'MM'));
                    const year = (jq().fullCalendar.formatDate(event.start, 'yyyy'));
                    alert(year + '-' + month + '-' + day);
                });
            },
            editable: true,
            dayMaxEvents: true,
            navLinks: true,
            dateClick: function (info) {
                console.log("A date clicked", info);
                let selectedDate = info.dateStr;
                let formattedDate = moment(selectedDate).format('dddd, MMMM DD yyyy');
                jq('#appointmentDate').val(selectedDate);
                jq('#appointmentDateDisplay').text(formattedDate);
                let startDateTime = new Date(selectedDate);
                jq("#startTime").timepicker('option', 'minTime', startDateTime);
                jq("#startTime").timepicker('setTime', startDateTime);
                jq("#startTime").timepicker('defaultTime', startDateTime);
            },

            events: calendarEvents,
            selectable: true
        });

        calendar.render();
    });
</script>

<div id="myAppointments-calendar"></div>