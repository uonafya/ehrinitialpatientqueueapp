<%
    ui.decorateWith("kenyaemr", "standardPage", [ layout: "sidebar" ])
    ui.includeCss("ehrconfigs", "referenceapplication.css")
    ui.includeCss("ehrconfigs", "jquery.dataTables.min.css")
    ui.includeCss("ehrconfigs", "onepcssgrid.css")
    ui.includeJavascript("ehrconfigs", "jquery.dataTables.min.js")
    ui.includeJavascript("uicommons", "handlebars/handlebars.min.js")
    ui.decorateWith("kenyaemr", "standardPage", [ patient: currentPatient ])
    def menuItems = [
            [ label: "Back to home",
              iconProvider: "kenyaui",
              icon: "buttons/back.png",
              href: ui.pageLink("initialpatientqueueapp", "patientCategory")
            ]
    ]
%>

<script>
    PAGE = {
        /** SUBMIT */
        submit: function () {
            console.log('..........ready to submit')
            jq("#SickOffForm").submit();
        }
    };
</script>
<div class="ke-page-content">
    <div >
        ${ ui.includeFragment("kenyaemr", "patient/patientSummary", [ patient: currentPatient ]) }
    </div>
</div>
<div class="ke-page-sidebar">
    ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ heading: "Tasks", items: menuItems ]) }
</div>

<div class="ke-page-content">
    <div class="ke-panel-frame">
        <div class="ke-panel-heading">Sickness Leave Form</div>
        <div class="ke-panel-content">
            <div class="container">
                <form id="SickOffForm" method="post" class="ng-pristine ng-valid">
                    <div class="ke-form-content">
                        <div class="onerow">
                            <div class="col4">
                            <label> Provider</label>
                            <select id="provider" name="Provider">
                                class="required form-combo1">
                                <option value="">Select Room</option>
                                <option value="1">Dr. Super Admin</option>
                                </select>
                            </div>
                            <div class="col4">
                                <label>Date of Onset</label><input type="date" id="date_of_onset_for_crrent_illnes" class="focused">
                            </div>
                        </div>
                        <div class="onerow">
                            <div class="col4">
                            <label>Provider/Facility Notes</label>
                            <field>
                                <textarea type="text" id="history" name="history" style="height: 80px; width: 700px;"></textarea>
                            </field>
                        </div>
                        </div>
                    </div>
                    <div class="onerow" style="margin-top: 100px">

                        <a class="button confirm" onclick="PAGE.submit();"
                           style="float:right; display:inline-block; margin-left: 5px;">
                            <span>FINISH</span>
                        </a>

                        <a class="button cancel" onclick="window.location.href = window.location.href"
                           style="float:right; display:inline-block;"/>
                        <span>RESET</span>
                    </a>
                    </div>
                </form>

            </div>
        </div>

    </div>
    <div>
        <section>
            <div>
                <table cellpadding="5" cellspacing="0" width="100%" id="queueList">
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

