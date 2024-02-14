<script type="text/javascript">
   var jq = jQuery.noConflict();
 </script>
<script type="text/javascript">
    var jq = jQuery;
        jq(function () {
            jq('#confirm').on( 'click',function () {
                saveAppointment();
            });

            jq('#cancel').on( 'click',function () {
                location.reload();
            });
            jq('#editAppointment').on( 'click', function () {
                            editAppointment();
                        });

            jq('#appointmentDate').datepicker();
            jq("#typesTb").DataTable(
            {
               searching: true,
               lengthChange: false,
               pageLength: 10,
               jQueryUI: true,
               pagingType: 'full_numbers',
               sort: false,
               dom: 't<"fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix datatables-info-and-pg"ip>',
               language: {
                   zeroRecords: 'No scheduled appointments recorded.',
                   paginate: {
                       first: 'First',
                       previous: 'Previous',
                       next: 'Next',
                       last: 'Last'
                   }
               }
           });
        });
        function saveAppointment() {
                    jq.getJSON('${ ui.actionLink("initialpatientqueueapp", "scheduleAppointment", "createAppointment") }', {
                        appointmentNumber:jq("#appointmentNumber").val(),
                        patientId:jq("#patientId").val(),
                        service:jq("#service").val(),
                        serviceType:jq("#serviceType").val(),
                        provider: jq("#provider").val(),
                        startDateTime: jq("#startDateTime-field").val(),
                        endDateTime: jq("#endDateTime-field").val(),
                        comments: jq("#comments").val(),
                    }).success(function(data) {
                        jq().toastmessage('showSuccessToast', "Patient's Appointment created successfully");
                        location.reload();
                    });
        }
        function editAppointment() {
            alert("Editing of the patient appointment under development");
        }
</script>

<div class="ke-panel-frame">
        <div class="ke-panel-heading">Appointment Scheduling</div>
            <div class="ke-panel-content">
                <table border="0" cellpadding="0" cellspacing="0" id="appointments" width="75%">
                    <input type="hidden" id="patientId" value=${patientId} />
                    <tr>
                        <td>Appointment Number</td>
                        <td><input type="text" id="appointmentNumber" name="appointmentNumber" /></td>
                    </tr>
                    <tr>
                        <td>Appointment Service</td>

                       <td>
                           <select id="service" name="service">
                                <option value="">Please select appointment service</option>
                                <% appointmentServices.each { services -> %>
                                    <option value="${services.uuid }">${services.name}</option>
                                <% } %>
                            </select>
                       </td>
                    </tr>
                    <tr>
                        <td>Appointment Service Yype</td>

                       <td>
                           <select id="serviceType" name="serviceType">
                                <option value="">Please select appointment service type</option>
                                <% appointmentServicesTypes.each { type -> %>
                                    <option value="${type.uuid }">${type.name}</option>
                                <% } %>
                            </select>
                       </td>
                    </tr>
                    <tr>
                        <td>Provider</td>
                        <td>
                            <select id="provider" name="provider">
                                <option value="">Please select provider</option>
                                <% providerList.each { prod -> %>
                                    <option value="${prod.providerId }">${prod.name}</option>
                                <% } %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <h2>Start Date and time</h2>
                                <p class="input-position-class">
                                    ${ui.includeFragment("uicommons", "field/datetimepicker", [formFieldName: 'startDateTime', id: 'startDateTime', label: '', useTime: true, defaultToday: true, class: ['newdtp']])}
                                </p>
                        </td>
                        <td>
                            <h2>End Date and time</h2>
                                <p class="input-position-class">
                                    ${ui.includeFragment("uicommons", "field/datetimepicker", [formFieldName: 'endDateTime', id: 'endDateTime', label: '', useTime: true, defaultToday: true, class: ['newdtp']])}
                                </p>
                        </td>
                    <tr>
                    <tr>
                        <td colspan="2">Comments</td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <textarea id="comments" name="comments" rows="4" cols="50"></textarea>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="onerow" style="margin-top:10px;">
                <button class="button cancel" id="cancel">Cancel</button>
                <button class="button confirm right" id="confirm">Confirm</button>
            </div>
        </div>
        <br />
        <div class="ke-panel-content">
            <table id="typesTb">
                <thead>
                    <tr>
                        <th>Appointment Number</th>
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
                <tbody>
                    <% if (patientAppointments) { %>
                        <% patientAppointments.each {%>
                            <tr>
                                  <td>${it.appointmentNumber}</td>
                                  <td>${it.appointmentService}</td>
                                  <td>${it.appointmentServiceType}</td>
                                  <td>${it.provider}</td>
                                  <td>${it.response}</td>
                                  <td>${it.startTime}</td>
                                  <td>${it.endTime}</td>
                                  <td>${it.status}</td>
                                  <td>${it.appointmentReason}</td>
                            </tr>
                        <%}%>
                    <%}%>
                </tbody>
            </table>
        </div>
</div>
