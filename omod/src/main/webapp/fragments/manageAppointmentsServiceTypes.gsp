<script type="text/javascript">
   var jq = jQuery.noConflict();
</script>
<script type="text/javascript">
 var jq = jQuery;
    jq(function () {
        jq('#confirm').on( 'click',function () {
            saveAppointmentServiceTypes()
        });

        jq('#cancel').on( 'click',function () {
                    location.reload();
        });
        var tbl = jq("#appointmentServiceTypesTb").DataTable(
        {
             searching: true,
             lengthChange: false,
             pageLength: 10,
             jQueryUI: true,
             pagingType: 'full_numbers',
             sort: false,
             dom: 't<"fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix datatables-info-and-pg"ip>',
             language: {
                 zeroRecords: 'No appointment types recorded.',
                 paginate: {
                     first: 'First',
                     previous: 'Previous',
                     next: 'Next',
                     last: 'Last'
                 }
             }
         });
    });

    function saveAppointmentServiceTypes() {
                jq.getJSON('${ ui.actionLink("initialpatientqueueapp", "manageAppointmentsTypes", "createAppointmentServiceType") }', {
                    name:jq("#appointment-service-type-name").val(),
                    duration: jq("#appointment-service-type").val(),
                    appointmentServiceDefinition: jq("#appointmentServiceDefinition").val(),
                }).success(function(data) {
                    jq().toastmessage('showSuccessToast', "Appointment service type created successfully");
                    location.reload();
                });
    }
</script>
<div class="ke-panel-frame">
    <div class="ke-panel-heading">Create Appointment Service Types</div>
        <div class="ke-panel-content" style="background-color: #F3F9FF">
          <div>
              <table border="0">
                  <tr>
                      <td>Name</td>
                      <td><input type="text" id="appointment-service-type-name" name="name" /></td>
                  </tr>
                  <tr>
                      <td>Appointment Service</td>
                      <td>
                      <select id="appointmentServiceDefinition" name="appointmentServiceDefinition">
                          <option value="">Please select appointment service</option>
                          <% appointmentServiceDefinitionList.each { type -> %>
                              <option value="${type.uuid}">${type.name}</option>
                          <% } %>
                      </select>
                      </td>
                  </tr>
                  <tr>
                      <td>Appointment duration</td>
                      <td><input type="text" id="appointment-service-type" name="appointmentDuration" />(in Minutes)</td>
                  </tr>
              </table>
              <div class="onerow" style="margin-top:10px;">
                  <button class="button cancel" id="cancel">Cancel</button>
                  <button class="button confirm right" id="confirm">Confirm</button>
              </div>
          </div>
        </div>
    </div>
</div>
<br />
<div>
    <table border="0" cellpadding="0" cellspacing="0" id="appointmentServiceTypesTb" width="100%">
        <thead>
            <tr>
                <th>Name</th>
                <th>Appointment Service</th>
                <th>Duration</th>
                <th>Description</th>
            </tr>
        </thead>
        <tbody>
            <% if (appointmentServicesTypes) { %>
                <% appointmentServicesTypes.each {%>
                <tr>
                    <td>${it.name}</td>
                    <% if(it.appointmentServiceDefinition) {%>
                      <td>${it.appointmentServiceDefinition.name}</td>
                    <%} else {%>
                    <td>&nbsp;</td>
                   <%}%>
                    <td>${it.duration}</td>
                    <% if(it.appointmentServiceDefinition.description) {%>
                        <td>${it.appointmentServiceDefinition.description}</td>
                      <%} else {%>
                      <td>&nbsp;</td>
                     <%}%>
                </tr>
                <%}%>
            <%}%>
        </tbody>
    </table>
</div>
