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
        var printDiv = jq("#sick-off-detail").html();
        var printWindow = window.open('', '', 'height=400,width=800');
        printWindow.document.write('<html><head><title>Patient Sick Off Information</title>');
        printWindow.document.write(printDiv);
        printWindow.document.write('</body></html>');
        printWindow.document.close();
        printWindow.print();
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
    <div>
        <div class="page">
            <div class="subpage" id="sick-off-detail">
               <div style="text-align: center;" id="header">
                   ${ui.includeFragment("patientdashboardapp", "printHeader", [currentPatient: currentPatient])}
               </div>
               <div id="sickOffInfo">
                    <table id="sickOffDetails">
                        <tr>
                            <td colspan="2"><h3>Patient Sick Off Sheet</h3></td>
                        </tr>
                        <tr>
                            <td>Start Date</td>
                            <td>${sickOffStartDate}
                        </tr>
                        <tr>
                            <td>End Date</td>
                            <td>${sickOffEndDate}</td>
                        </tr>
                        <tr>
                            <td>Number of days</td>
                            <td>${numberOfDays}</td>
                        </tr>
                        <tr>
                            <td>Created On</td>
                            <td>${sickOffCreatedDate}</td>
                        </tr>
                        <tr>
                            <td>Created By</td>
                            <td>${sickOffCreator}</td>
                        </tr>
                        <tr>
                            <td colspan="2">Clinical notes</td>
                        </tr>
                        <tr>
                            <td colspan="2">${notes}</td>
                        </tr>
                        <tr>
                            <td>Authorizing Doctor</td>
                            <td>${sickOffProvider}</td>
                        </tr>
                        <tr>
                             <td>Date and timestamp</td>
                            <td>${timestamp}</td>
                        </tr>
                    </table>
               <div>
            </div>
        </div>
    </div>
    <div class="onerow" style="margin-top:10px;">
        <button id="printSickOffBtn"  onclick="printSickOff()" class="confirm right">
            <i class="btn-info"></i>
            Print Sick Off
        </button>
    </div>
</div>
