<%
    ui.decorateWith("kenyaemr", "standardPage", [ layout: "sidebar" ])
    ui.includeJavascript("ehrconfigs", "datatables/jquery.dataTables.min.js")
    ui.includeCss("ehrconfigs", "jquery.dataTables.min.css")
    def menuItems = [
            [ label: "Back to home", iconProvider: "kenyaui", icon: "buttons/back.png", label: "Back to home", href: ui.pageLink("initialpatientqueueapp", "patientQueueHome") ]
    ]
%>
    <!--new-->
    <style>
      .ke-page-content .ke-panel-frame .edit {
        background: rgb(131, 127, 127);
        text-decoration: none;
        font-size: 17px;
        cursor: pointer;
        color: #f7f7f7;
        padding-left: 15px;
      }
      .ke-page-content .ke-panel-frame .edit:hover {
        background: rgb(155, 97, 97);
      }
      .buttons {
        width: 50%;
        margin: auto;
      }
      .buttons button {
        min-width: 70px;
        height: 35px;
        outline: none;
        border: none;
        margin-right: 10px;
        font-size: 15px;
      }
      dialog {
        text-align: center;
      }
      #secondDialog{
        border-radius:10px;
        background:#333;
        border:2px solid #333;
        padding:20px;

      }
    </style>

    <!--Html dialogue end-->
    <div class="ke-page-sidebar">
      ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ items: menuItems ])
      }
    </div>
    <div class="ke-page-content">
      <div class="ke-panel-frame">
        <div class="ke-panel-heading">Scheduled Patients</div>
        <table
          border="1"
          cellpadding="0"
          cellspacing="0"
          id="details"
          width="100%"
        >
          <thead>
            <tr>
              <th>Visit Date</th>
              <th>Patient Identifier</th>
              <th>Patient Names</th>
              <th>Sex</th>
              <th>Visit status</th>
              <th>Service point</th>
              <th>Request status</th>
              <th>Paying Category</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <% if (viewQueuedPatientsList.empty) { %>
            <tr>
              <td colspan="10">No records found for specified period</td>
            </tr>
            <% } %> <% if (viewQueuedPatientsList) { %> <%
            viewQueuedPatientsList.each {%>
            <tr>
              <td>${it.visitDate}</td>
              <td>${it.patientIdentifier}</td>
              <td>${it.patientNames}</td>
              <td>${it.sex}</td>
              <td>${it.visitStatus}</td>
              <td id="doc">${it.serviceConceptName}</td>
              <td>${it.status}</td>
              <td>${it.referralConceptName}</td>

              <td
                class="edit"
                id="show"
               
              >
                <div>
                  <dialog
                    id="myFirstDialog"
                    style="
                      width: 50%;
                      background-color: #f4ffef;
                      border: 1px dotted black;
                    "
                  >
                    <p>Confirm Queueing this patient.</p>
                    <div class="buttons">
                      <button id="close" onclick="function confirm()">Cancel</button>
                      <button id="changeRoom" >
                        change Room
                      </button>
                    </div>
                  </dialog>
                  <p>Edit</p>
                  <script>
                    (function confirm() {
                        var dialog = document.getElementById('myFirstDialog');
                        document.getElementById('show').onclick = function() {
                            dialog.show();
                        };
                        document.getElementById('close').onclick = function() {
                            dialog.close();
                        };
                    })();
                    jq(document).ready(function(){
                      jq('#changeRoom').click(function(){
                        jq('#myFirstDialog').css({
                          display:"none"                    
                        });
                        function pullup(){
                }
                      })
                    })
                  </script>

                  <dialog
                    id="secondDialog"
                    style="
                      top: 50%;
                      width: 50%;
                      background-color: #f4ffef;
                      border: 1px dotted black;
                    "
                  ><form method="post" enctype="multipart/formdata" id="form" ></form>
                  <div style="text-align:centre" class="new">

                      <table border="1" cellspacing="0" cellpadding="10" style="text-align:centre; width:100%; height:80%;" >
                        <tr>
                          <td colspan="2" style="text-align:center;"><h2>Room to Visit</h2></td>
                        </tr>
                        <tr>
                          <td valign="top">
                            <div class="col4">
                              <label
                                for="rooms1"
                                id="froom1"
                                style="margin: 0px"
                                >Room to Visit<span>*</span></label
                              >
                            </div>
                          </td>
                          <td valign="top">
                            <div class="col4">
                              <span class="select-arrow" style="width: 100%">
                                <field>
                                  <select
                                    id="rooms"
                                    name="rooms1"
                                 
                                    class="required form-combo1"
                                    
                                  >
                                    <option value="0">Select Room</option>
                                    <option value="1">TRIAGE ROOM</option>
                                    <option value="2">OPD ROOM</option>
                                    <option value="3">SPECIAL CLINIC</option>
                                  </select>
                                </field>
                              </span>
                            </div>
                          </td>
                        </tr>
                        <tr>
                          <td valign="top">
                            <div class="col4">
                              <label
                                for="rooms2"
                                id="froom2"
                                style="margin: 0px"
                                >Choose<span>*</span></label
                              >
                            </div>
                          </td>
                          <td valign="top">
                            <div class="col4">
                              <span class="select-arrow" style="width: 100%">
                                <field>
                                <select style="display: none" id="rooms1" name="rooms2" class="required form-combo1"><option value="">Select Type</option><option value="1000015">OPD Triage</option><option value="1000016">Casualty Triage</option><option value="1000017">MCH Triage</option><option value="1000018">Maternity Triage</option><option value="1000019">Paediatric Triage</option><option value="1000463">MOPC triage</option></select>
                                <select style="display: none"  id="rooms2" name="rooms2" class="required form-combo1"><option value="">Select Type</option><option value="1000022">General OPD</option><option value="1000024">Casuality OPD</option><option value="1000026">Trauma OPD</option><option value="1000027">Dental OPD</option><option value="1000042">Medical Outpatient Clinic</option></select>
                                <select style="display: none" id="rooms3" name="rooms2" class="required form-combo1"><option value="">Select Type</option><option value="1000043">Dental Clinic</option><option value="1000044">Tuberculosis Clinic</option><option value="162050">Comprehensive care center</option><option value="1000046">MCH Clinic</option><option value="1000047">Surgical Clinic</option><option value="1000048">Family Planing Clinic</option><option value="160459">Pediatric oncology department</option><option value="160465">Orthopedic department</option><option value="1000045">CCC Clinic</option><option value="1000155">Eye Clinic</option><option value="160455">Ear</option><option value="160469">Cardiology department</option><option value="1000209">occupational therapy</option><option value="160552">Nutrition program</option><option value="160475">Nephrology department</option><option value="1000484">Physio room</option><option value="160461">Psychiatry department</option><option value="119481">Diabetes Mellitus</option><option value="116214">Hypotension</option></select>
                                </field>
                              </span>
                            </div>
                          </td>
                        </tr>
                      </table>

                      <div class="onerow" style="margin-top: 60px">
                        <a
                        
                          class="button confirm"
                          style="
                            float: right;
                            display: inline-block;
                            margin-left: 5px;
                          "
                        >
                          <input type="submit" id="finish" value="Finish">
                        </a>

                        <a
                         id="close2"
                          class="button cancel"
                          onclick="window.location.href = window.location.href"
                          style="float: right; display: inline-block"
                        >
                        <input type="button" value="Close">
                        </a>
                      </div>
                    </div>
                  </form>                                  
                  </dialog>
                  <script type="text/JavaScript">
jq(document).ready(function(){
  var choice = 0;
  var rooms,
  subrooms;
  jq('#rooms').change(function(){
    choice = ${('#rooms').val()};
    if (choice==0) {
      ('#rooms1').css({display: "none"})
      jq('#rooms2').css({display: "none"})
      jq('#rooms3').css({display: "none"})
    }
    else if(choice == 1){
      jq('#rooms1').css({display: "block"})
      jq('#rooms2').css({display: "none"})
      jq('#rooms3').css({display: "none"})
      jq('#froom2').html("Triage Rooms")
    }else if(choice == 2){
      jq('#rooms2').css({display: "block"})
      jq('#rooms1').css({display: "none"})
      jq('#rooms3').css({display: "none"})
      jq('#froom2').html("OPD Rooms")
    }else if(choice == 3){
      jq('#rooms1').css({display: "none"})
      jq('#rooms2').css({display: "none"})
      jq('#rooms3').css({display: "block"})
      jq('#froom2').html("Special Clinic")
    }
  })
  jq('#form').submit(function(){
    if(choice==1){
      rooms = "triage room";
      subrooms = \$('#rooms1').val();
    }else if(choice==2){
      rooms = "opd rooms";
      subrooms = \$('#rooms2').val();
    }
    else if(choice == 3){
      rooms = "special clinic";
      subrooms = \$('#rooms3').val();
    }
});
});
                    (function() {
                        var dialog = document.getElementById('secondDialog');
                        document.getElementById('changeRoom').onclick = function() {
                            dialog.show();
                        };
                        document.getElementById('close2').onclick = function() {
                            dialog.close();
                        };
                    })();
                  </script>                  
                </div>
              </td>
            </tr>
            <%}%> <%}%>
          </tbody>
        </table>
      </div>
    </div>

