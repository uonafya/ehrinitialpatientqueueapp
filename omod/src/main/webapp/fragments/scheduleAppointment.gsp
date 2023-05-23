<script type="text/javascript">
    var jq = jQuery;
        jq(function () {
            jq('#confirm').on( 'click',function () {
                saveAppointment();
                location.reload();
            });

            jq('#cancel').on( 'click',function () {
                location.reload();
            });

            jq('#appointmentDate').datepicker()();
        });
        function saveAppointment() {
                    jq.getJSON('${ ui.actionLink("initialpatientqueueapp", "scheduleAppointment", "createAppointment") }', {
                        appointmentDate:jq("#appointmentDate").val(),
                        startTime: jq("#startTime").val(),
                        endTime: jq("#endTime").val(),
                        type: jq("#type").val(),
                        patientId: jq("#patient").val(),
                        provider: jq("#provider").val(),
                        notes: jq("#notes").val(),
                    }).success(function(data) {
                        jq().toastmessage('showSuccessToast', "Patient's Appointment created successfully");
                        location.reload();
                    });
              }
</script>

<div class="ke-panel-frame">
        <div class="ke-panel-heading">Appointment Scheduling</div>
            <div class="ke-panel-content">
                <table>
                    <input type="text" id="patient" value=${patientId} />
                    <tr>
                        <td>Appointment Date</td>
                        <td><input type="text" id="appointmentDate" name="appointmentDate" /></td>
                    </tr>
                    <tr>
                        <td>Appointment Type</td>
                        <td>
                            <select id="type" name="type">
                                <option value="">Please select visit type</option>
                                <% appointmentTypes.each { type -> %>
                                    <option value="${type.visitTypeId }">${type.name}</option>
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
                                    <option value="${prod.providerId }">${type.name}</option>
                                <% } %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td> From:
                            <select id="startTime" name="startTime">
                                <option value="07:00">07:00</option>
                                <option value="08:00">08:00</option>
                                <option value="09:00">09:00</option>
                                <option value="10:00">10:00</option>
                                <option value="11:00">11:00</option>
                                <option value="12:00">12:00</option>
                                <option value="13:00">13:00</option>
                                <option value="14:00">14:00</option>
                                <option value="15:00">15:00</option>
                                <option value="16:00">16:00</option>
                                <option value="17:00">17:00</option>
                            </select>
                        </td>
                        <td> To:
                            <select id="startTime" name="endTime">
                                <option value="08:00">08:00</option>
                                <option value="09:00">09:00</option>
                                <option value="10:00">10:00</option>
                                <option value="11:00">11:00</option>
                                <option value="12:00">12:00</option>
                                <option value="13:00">13:00</option>
                                <option value="14:00">14:00</option>
                                <option value="15:00">15:00</option>
                                <option value="16:00">16:00</option>
                                <option value="17:00">17:00</option>
                                <option value="18:00">18:00</option>
                            </select>
                        </td>
                    <tr>
                    <tr>
                        <td colspan="2">Reason for appointment</td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <textarea id="notes" name="notes" rows="4" cols="50"></textarea>
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
        <br />
        <div class="ke-panel-heading">Historical Patient Appointments</div>
            <div class="ke-panel-content">
                <table border="0" cellpadding="0" cellspacing="0" id="types" width="100%">
                    <thead>
                        <tr>
                            <th>Appointment type</th>
                            <th>Provider</th>
                            <th>Status</th>
                            <th>Appointment reason</th>
                            <th>Start time</th>
                            <th>End time</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (patientAppointments.empty) { %>
                            <tr>
                                <td colspan="6">
                                    No records found for specified period
                                </td>
                            </tr>
                        <% } %>
                        <% if (patientAppointments) { %>
                            <% patientAppointments.each {%>
                                <tr>
                                    <td>${it.appointmentType.name}</td>
                                    <td>${it.timeSlot.appointmentBlock.provider.name}</td>
                                    <td>${it.status.name}</td>
                                    <td>${it.reason}</td>
                                    <td>${it.timeSlot.startDate}</td>
                                    <td>${it.timeslot.endDate}</td>
                                </tr>
                            <%}%>
                        <%}%>
                    </tbody>
                </table>
            </div>
        </div>
</div>
