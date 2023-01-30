<%
    ui.decorateWith("kenyaemr", "standardPage", [ layout: "sidebar" ])
    ui.includeCss("ehrconfigs", "referenceapplication.css")
    ui.includeCss("ehrconfigs", "jquery.dataTables.min.css")
    ui.includeCss("ehrconfigs", "onepcssgrid.css")
//    ui.includeJavascript("ehrconfigs", "moment.js")
    ui.includeJavascript("ehrconfigs", "jquery.dataTables.min.js")
//    ui.includeJavascript("ehrconfigs", "jq.browser.select.js")
//    ui.includeJavascript("ehrconfigs", "knockout-3.4.0.js")
//    ui.includeJavascript("ehrconfigs", "jquery-ui-1.9.2.custom.min.js")
//    ui.includeJavascript("ehrconfigs", "underscore-min.js")
//    ui.includeJavascript("ehrconfigs", "emr.js")
//    ui.includeCss("ehrconfigs", "jquery-ui-1.9.2.custom.min.css")
//    ui.includeJavascript("ehrconfigs", "jquery.toastmessage.js")
//    ui.includeCss("ehrconfigs", "jquery.toastmessage.css")
//    ui.includeJavascript("ehrconfigs", "jquery.simplemodal.1.4.4.min.js")
    def props = ["identifier", "fullname", "age", "gender", "action"]
    def menuItems = [
            [ label: "Back to home",
              iconProvider: "kenyaui",
              icon: "buttons/back.png",
              href: ui.pageLink("initialpatientqueueapp", "patientCategory")
            ]
    ]
%>
<div class="ke-page-content">
        <div>
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
                <form class="ng-pristine ng-valid">
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

                    <div class="onerow" style="margin-top: 10px">

                        <button class="cancel confirm focused" style="float:left">CLEAR</button>

                        <button class="submitButton confirm focused" style="float:left">FINISH</button>
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
<script type="text/javascript">
    jQuery(function() {
        jQuery('input[name="query"]').focus();
        e.preventDefault();
        LoadProviders();
    });
    var OPDs = ",Select Type |${OPDs}";
    function LoadProviders() {
        jq('#rooms1').empty(); //resets the selector
        if (jq("#rooms1").val() == 1) {
            fillOptions("#rooms2", {
                data: TRIAGE,
                delimiter: ",",
                optionDelimiter: "|"
            });
        }}
    /**
     ** FORM
     **/
    PAGE = {
        // Print the slip
        print: function () {
            var printDiv = jQuery("#printDiv").html();
            var printWindow = window.open('', '', 'height=500,width=400');
            printWindow.document.write('<html><head><title>Patient Information</title>');
            printWindow.document.write('<body style="font-family: Dot Matrix Normal,Arial,Helvetica,sans-serif; font-size: 12px; font-style: normal;">');
            printWindow.document.write(printDiv);
            printWindow.document.write('</body>');
            printWindow.document.write('</html>');
            printWindow.print();
            printWindow.close();
        },
    /** SUBMIT */
    submit: function () {
        // Validate and submit
        if (this.validateRegisterForm()) {
            // print();
            jq("#patientRegistrationForm").submit();
        }
    },
</script>
