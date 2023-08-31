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
        var tbl = jq("#appointmentServiceTypesTb").DataTable();
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
                      <td><input type="text" id="name" name="name" /></td>
                  </tr>
                  <tr>
                      <td>Speciality</td>
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
