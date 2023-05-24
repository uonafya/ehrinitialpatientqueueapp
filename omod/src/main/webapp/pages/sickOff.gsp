<%
    ui.decorateWith("kenyaemr", "standardPage", [ layout: "sidebar" ])
    ui.includeCss("ehrconfigs", "referenceapplication.css")
    ui.includeCss("ehrconfigs", "jquery.dataTables.min.css")
    ui.includeCss("ehrconfigs", "onepcssgrid.css")
    ui.includeJavascript("ehrconfigs", "jquery.dataTables.min.js")
    ui.includeJavascript("uicommons", "handlebars/handlebars.min.js")
    ui.decorateWith("kenyaemr", "standardPage", [patient: patient])
    def menuItems = [
            [ label: "Back to home",
              iconProvider: "kenyaui",
              icon: "buttons/back.png",
              href: ui.pageLink("initialpatientqueueapp", "patientCategory", [patientId: currentPatient])
            ]
    ]
%>
<script type="text/javascript">
   var jq = jQuery.noConflict();
 </script>
 <script type="text/javascript">
      var jq = jQuery;
          jq(function () {

          jq('#submitSickOff').on( 'click',function () {
              saveSickOff();
          });

          jq('#resetSickOff').on( 'click',function () {
              location.reload();
          });
              jq("#sickOffTbl").DataTable();

              jq('#sickOffTbl tbody').on( 'click', 'tr', function () {
                        var trData = table.row(this).data();
                 console.log(trData);
              });
          });
          function saveSickOff() {
                              jq.getJSON('${ ui.actionLink("initialpatientqueueapp", "scheduleAppointment", "saveSickOff") }', {
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

<div class="ke-page-sidebar">
    ${ui.includeFragment("kenyaui", "widget/panelMenu", [heading: "Tasks", items: menuItems])}
</div>

<div class="ke-page-content">
    <div class="ke-panel-frame">
        <div class="ke-panel-heading">Sickness Leave Form</div>
        <div class="ke-panel-content">
            <div class="container">
                    <div class="ke-form-content">
                        <div class="onerow">
                            <div class="col4">
                                <label>Provider</label>
                                <select id="user" name="user">
                                    class="required form-combo1">
                                    <option value="">Select provider</option>
                                    <option value="1">Dr. Super Admin</option>
                                </select>
                            </div>
                            <div class="col4">
                                <label>Date of Onset</label><input type="date" id="sickOffStartDate"
                                                                   class="focused">
                            </div>
                        </div>
                        <div class="onerow">
                            <div class="col4">
                                <label>Provider/Facility Notes</label>
                                <field>
                                    <textarea type="text" id="clinicianNotes" name="clinicianNotes"
                                              style="height: 80px; width: 700px;"></textarea>
                                </field>
                            </div>
                        </div>
                    </div>
                    <div class="onerow" style="margin-top: 100px">

                        <button id="submitSickOff" class="button confirm" type="submit"
                                style="float:right; display:inline-block; margin-left: 5px;">
                            <span>FINISH</span>
                        </button>

                        <button id="resetSickOff" class="cancel" type="reset" style="float:right; display:inline-block;"/>
                        <span>RESET</span>
                    </button>
                    </div>

            </div>
        </div>

    </div>
    <div>
        <section>
            <div>
                <table border="1" cellpadding="0" cellspacing="0" width="100%" id="sickOffTbl">
                    <thead>
                    <tr align="center">
                        <th style="width:200px">Patient ID</th>
                        <th>Provider ID</th>
                        <th>Notes</th>
                        <th style="width: 60px">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr align="center">
                        <td colspan="5">No patient found</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </section>

    </div>
</div>

