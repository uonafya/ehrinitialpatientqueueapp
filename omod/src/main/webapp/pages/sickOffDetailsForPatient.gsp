<%
    ui.decorateWith("kenyaemr", "standardPage", [patient: currentPatient])
    ui.includeCss("financials", "jquery.dataTables.min.css")
    ui.includeCss("financials", "bootstrap.min.css")
    ui.includeCss("financials", "bootstrap-print.css")
    ui.includeCss("ehrconfigs", "referenceapplication.css")


    ui.includeJavascript("ehrconfigs", "bootstrap.min.js")
    ui.includeJavascript("financials", "jquery.dataTables.min.js")
    ui.includeJavascript("patientdashboardapp", "jq.print.js")


%>
<script type="text/javascript">
    jq = jQuery
    jq(document).ready(function() {

    });

    function printSickOff() {
        jq("#sick-off-detail").print({
            globalStyles: false,
            mediaPrint: false,
            iframe: false,
            width: 600,
            height: 700
        });
    }
</script>
<style>
    body {
        width: 100%;
        height: 100%;
        margin: 0;
        padding: 0;
        background-color: #FAFAFA;
        font: 12pt "Tahoma";
    }
    * {
        box-sizing: border-box;
        -moz-box-sizing: border-box;
    }
    .page {
        width: 210mm;
        min-height: 297mm;
        padding: 20mm;
        margin: 10mm auto;
        border: 1px #D3D3D3 solid;
        border-radius: 5px;
        background: white;
        box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
    }
    .subpage {
        padding: 1cm;
        border: 5px red solid;
        height: 257mm;
        outline: 2cm #FFEAEA solid;
    }

    @page {
        size: A4;
        margin: 0;
    }
    @media print {
        html, body {
            width: 210mm;
            height: 297mm;
        }
        .page {
            margin: 0;
            border: initial;
            border-radius: initial;
            width: initial;
            min-height: initial;
            box-shadow: initial;
            background: initial;
            page-break-after: always;
        }
    }
</style>
<div class="ke-page-content">
    <div id="sick-off-detail">
        <div class="page">
            <div class="subpage">
               <div style="text-align: center;" id="header">
                   <center>
                       <img src="/openmrs/ms/uiframework/resource/ehrinventoryapp/images/kenya_logo.bmp" width="60" height="60" align="middle">
                   </center>
                   ${ui.includeFragment("patientdashboardapp", "printHeader")}
               </div>
               <div id="biodata">
                    <h3>PATIENT BIO DATA</h3>

                    <label>
                        <span class='status active'></span>
                        Identifier:
                    </label>
                    <span>${currentPatient.getPatientIdentifier()}</span>
                    <br/>

                    <label>
                        <span class='status active'></span>
                        Full Names:
                    </label>
                    <span>${currentPatient.givenName} ${currentPatient.familyName} ${currentPatient.middleName ? patient.middleName : ''}</span>
                    <br/>

                    <label>
                        <span class='status active'></span>
                        Age:
                    </label>
                    <span>${currentPatient.age} (${ui.formatDatePretty(currentPatient.birthdate)})</span>
                    <br/>

                    <label>
                        <span class='status active'></span>
                        Gender:
                    </label>
                    <span>${currentPatient.gender}</span>
               </div>
               <div id="sickOffInfo">
                    <table id="sickOffDetails">
                        <tr>
                            <td>Start Date</td>
                            <td>${sickOffStartDate}
                            <td>End Date</td>
                            <td>${sickOffEndDate}</td>
                        </tr>
                        <tr>
                            <td>Created On</td>
                            <td>${sickOffCreatedDate}</td>
                            <td>Created By</td>
                            <td>${sickOffCreator}</td>
                        </tr>
                        <tr>
                            <td colspan="2">Clinical notes</td>
                            <td colspan="2">${notes}</td>
                        </tr>
                        <tr>
                            <td colspan="2">Authorizing Doctor</td>
                            <td colspan="2">${sickOffProvider}</td>
                        </tr>
                    </table>
               <div>

            </div>
        </div>
    </div>
    <div class="onerow" style="margin-top:10px;">
        <button id="printSickOffBtn"  onclick="printSickOff()" class="button confirm right">
            <i class="btn-info"></i>
            Print Sick Off
        </button>
    </div>
</div>
