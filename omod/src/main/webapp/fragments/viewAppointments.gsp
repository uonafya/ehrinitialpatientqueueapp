<%
    ui.includeJavascript("initialpatientqueueapp", "calendar/calendar-main.js")
    ui.includeJavascript("initialpatientqueueapp", "calendar/moment.min.js")
    ui.includeJavascript("initialpatientqueueapp", "jquery.timepicker.min.js")
    ui.includeJavascript("initialpatientqueueapp", "jquery-ui-1.13.1.min.js")
    ui.includeJavascript("initialpatientqueueapp", "jquery.autocomplete.min.js")

    ui.includeCss("initialpatientqueueapp", "jquery-ui-1.13.1.min.css")
    ui.includeCss("initialpatientqueueapp", "calendar/calendar-main.css")
    ui.includeCss("initialpatientqueueapp", "jquery.timepicker.min.css")
%>
<script type="text/javascript">
   jQuery.noConflict(true);
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
                let selectedDate = info.dateStr;
                let formattedDate = moment(selectedDate).format('dddd, MMMM DD yyyy');
                jq('#appointmentDate').val(selectedDate);
                jq('#appointmentDateDisplay').text(formattedDate);
                jq('#createAppointmentDlg').show();
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

    jq(function () {
        var appointmentDialog = emr.setupConfirmationDialog({
                            dialogOpts: {
                                overlayClose: false,
                                close: true
                            },
                            selector: '#createAppointmentDlg',
                            actions: {
                                confirm: function () {
                                   updateAppointments();
                                },
                                cancel: function () {
                                    location.reload();
                                }
                            }
                        });
        jq("#createAppointmentForm").submit(function (e) {
            e.preventDefault();

            let appointmentDate = jq('#appointmentDate').val();
            let startTime = jq("#startTime").timepicker('getTime');
            let endTime = jq("#endTime").timepicker('getTime');
            let type = jq('#appointmentType').val();
            let patient = jq('#patient').val();
            let heading = jq('#heading').val();
            let description = jq('#description').val();

            const params = {
                'patientId': patient,
                'appointmentDate': moment(appointmentDate).format('YYYY-MM-DD'),
                'location': ${location.id},
                'startTime': moment(startTime).format("hh:mm a"),
                'endTime': moment(endTime).format("hh:mm a"),
                'type': type,
                'notes': heading + ":" + description
            };

            jq.getJSON('${ ui.actionLink("botswanaemr", "appointments/myAppointments", "createAppointment")}', params)
                .success(function (data) {
                    jq().toastmessage('showNoticeToast', "Appointment created successfully");
                    location.reload();
                })
        });

        jq('.timepicker').timepicker({
            timeFormat: 'h:mm p',
            interval: 30,
            minTime: '08:00am',
            maxTime: '5:00pm',
            defaultTime: '8:00am',
            startTime: '8:00am',
            dynamic: false,
            dropdown: true,
            scrollbar: true,
            zindex: 9999999,
            change: function (timeValue) {
                let element = jq(this);
                if (element.is("#startTime")) {
                    const time = element.val();
                    const picker = jq("#endTime");
                    const defaultEndDate = moment(timeValue).add(moment.duration(30, 'minutes')).toDate();
                    const minTime = moment(defaultEndDate).format("hh:mm a");
                    picker.timepicker('setTime', defaultEndDate);
                    picker.timepicker('option', 'minTime', minTime);
                    picker.timepicker('option', 'defaultTime', time);
                }
            }
        });
    });

    jq(function () {
        const getData = function (request, response) {
            jq.getJSON(
                '/' + OPENMRS_CONTEXT_PATH + '/ws/rest/v1/patient?identifier=' + request.term,
                function (data) {
                    let results = [];
                    let rawData = data.results;
                    if (rawData) {
                        for (let i in rawData) {
                            const result = {
                                label: rawData[i]?.display,
                                value: rawData[i]?.uuid
                            };
                            results.push(result);
                        }
                    }
                    response(results);
                });
        };

        const selectItem = function (event, ui) {
            jq("#patient-search-box").val(ui.item.label);
            jq("#patient").val(ui.item.value);
            return false;
        }

        jq("#patient-search-box").autocomplete({
            source: getData,
            select: selectItem,
            minLength: 2
        });

        jq( "#patient-search-box" ).autocomplete( "option", "appendTo", ".createAppointmentForm");
    });
    function updateQueue() {
            jq.getJSON('${ ui.actionLink("initialpatientqueueapp", "viewAppointments", "createAppointment") }', {
                appointmentDate:jq("#queueValue").val(),
                startTime: jq("#servicePointValue").val(),
                endTime: jq("#rooms2").val(),
                type: jq("#rooms1").val(),
                flow: jq("#rooms1").val(),
                patient: jq("#rooms1").val(),
                notes: jq("#rooms1").val(),
            }).success(function(data) {
                jq().toastmessage('showSuccessToast', "Patient's Queue updated successfully");
                location.reload();
            });
      }
</script>

<div id="myAppointments-calendar"></div>

<div id="createAppointmentDlg" style="display:none;">
    <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable" role="document">
        <div class="modal-content">
            <div class="modal-header bg-white">
                <h4 class="modal-title text-black" id="createAppointmentModalModalTitle">Create new</h4>
                <button type="button" id="createAppointmentModalBtn" class="btn btn-sm bg-white text-danger"
                        data-dismiss="modal"
                        aria-label="Close">
                    <i class="fa fa-times-circle fa-2x" aria-hidden="true"></i>
                </button>
            </div>

            <div class="modal-body">
                <form method="post" id="createAppointmentForm" class="createAppointmentForm">
                    <div class="row">
                        <div class="col mb-2">
                            <label for="appointmentDate">Date <span class="text-danger">*</span></label>
                            <input type="date" id="appointmentDate"/>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col col-md-4">
                            <div class="form-group">
                                <label for="startTime">Start Time
                                    <span class="text-danger">*</span>
                                </label>

                                <div class="input-group">
                                    <input required
                                           id="startTime"
                                           name="startTime"
                                           type="text"
                                           class="form-control input-sm timepicker"
                                           placeholder="Enter time">
                                </div>
                            </div>
                        </div>

                        <div class="col col-md-4">
                            <div class="form-group">
                                <label for="endTime">End Time
                                    <span class="text-danger">*</span>
                                </label>

                                <div class="input-group">
                                    <input required
                                           id="endTime"
                                           name="endTime"
                                           type="text"
                                           class="form-control input-sm timepicker"
                                           placeholder="Enter time">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col mb-2">
                            <label for="appointmentType">Type <span class="text-danger">*</span></label>
                            <select id="appointmentType" name="form_select" onchange="showDiv(this)">
                                <option value="Task">Task</option>
                                <option value="Consultation">Consultation</option>
                            </select>
                        </div>
                    </div>

                    <div class="row" id="hidden_patient_input" style="margin-top: 1rem;">
                        <div class="col mb-2">
                            <div class="frmSearch">
                                <label for="patient-search-box">Patient <span class="text-danger">*</span></label>
                                <input type="text" id="patient-search-box" placeholder="Patient name/identifier"/>
                                <input type="hidden" id="patient"/>
                            </div>
                        </div>
                    </div>

                    <div class="row" id="task_heading" style="margin-top: 1rem;">
                        <div class="col mb-2">
                            <label for="heading">Heading <span class="text-danger">*</span></label>
                            <input type="text" id="heading"/>
                        </div>
                    </div>

                    <div class="row" id="task_description" style="margin-top: 1rem;">
                        <div class="col mb-2">
                            <label for="description">Description</label>
                            <input type="text" id="description"/>
                        </div>
                    </div>

                    <div class="onerow" style="margin-top:10px;">
                        <button class="button cancel" id="cancel">Cancel</button>
                        <button class="button confirm right" id="confirm">Confirm</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>