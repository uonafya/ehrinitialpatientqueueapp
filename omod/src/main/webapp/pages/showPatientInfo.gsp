<%
    ui.decorateWith("kenyaemr", "standardPage")
    ui.includeCss("ehrconfigs", "referenceapplication.css")
%>
<script type="text/javascript">
    function printReceipt() {
        var printDiv = jQuery("#printDiv").html();
        var printWindow = window.open('height=500,width=400');
        //printWindow.document.write('<html><head><title>Patient Information</title>'); not needed
        printWindow.document.write('<body style="font-family: Dot Matrix Normal,Arial,Helvetica,sans-serif; font-size: 12px; font-style: normal;">');
        printWindow.document.write(printDiv);
        printWindow.document.write('</body>');
        printWindow.document.write('</html>');
        //hide print button
        printWindow.document.getElementById("printSlip").style.visibility = "hidden";
        printWindow.print();
        printWindow.close();

    }




</script>
<style>

/*@media printReceipt {
    .printSlip {
        visibility: hidden;
    }
}
*/
</style>
<html>
<body>

    <div id="printDiv">
        <center>
            <center>
                <img width="60" height="60" align="center"
                     src="${ui.resourceLink('ehrinventoryapp', 'images/kenya_logo.bmp')}">
            </center>
        </center>

        <h3><center><u><b>${location}</b></u></center></h3>
        <h4 style="font-size: 1.4em;"><center><b>Registration Receipt</b></center></h4>
        <div style="display: block;	margin-left: auto; margin-right: auto; width: 350px">
            <% if(previousVisit) { %>
            <div>
                <div class="col2" align="float" style="display:inline-block; width: 150px">
                    <b>Previous visit:</b>
                </div>

                <div class="col2" align="left" style="display: inline-block;">
                    <span>${previousVisit}</span>
                </div>
            </div>
            <%}%>
            <div>
            <div class="col2" align="float" style="display:inline-block; width: 150px">
                    <b>Payment category:</b>
                </div>

                <div class="col2" align="left" style="display: inline-block;">
                    <span>${selectedPaymentCategory}</span>
                </div>
            </div>
            <div>
                <div class="col2" align="float" style="display:inline-block; width: 150px">
                    <b>Receipt Date:</b>
                </div>

                <div class="col2" align="left" style="display: inline-block;">
                    <span>${receiptDate}</span>
                </div>
            </div>
            <div>
                <div class="col2" align="float" style="display:inline-block; width: 150px">
                    <b>Name:</b></div>
                    <div class="col2" align="left" style="display:inline-block;"><span id="patientName">${names}</span></div>
            </div>
            <div>
                <div class="col2" align="float" style="display:inline-block; width: 150px">
                    <b>Patient ID:</b></div>
                    <div class="col2" align="left" style="display:inline-block;"><span id="identifier">${patientId}</span></div>
             </div>
            <div>
                <div class="col2" align="float" style="display:inline-block; width: 150px">
                    <b>Age:</b></div>
                    <div class="col2" align="left" style="display:inline-block;"><span id="age"></span>${age}</div>
            </div>
        <div>
            <div class="col2" align="float" style="display:inline-block; width: 150px">
            <b>Gender:</b></div>
            <div class="col2" align="left" style="display:inline-block;"><span id="gender"></span>${gender}</div>
        </div>
        <div>
            <div class="col2" align="float" style="display:inline-block; width: 150px">
                <b>Room to visit:</b></div>
            <div class="col2" align="left" style="display:inline-block;"><span id="roomToVisit"></span>${roomToVisit}-${department}</div>
        </div>
        <% if(paying){ %>
            <div>
                <div class="col2" align="float" style="display:inline-block; width: 150px">
                    <b>What to be paid:</b></div>
                <div class="col2" align="left" style="display:inline-block;"><span id="payment"></span>${WhatToBePaid}</div>
            </div>
            <% if(specialClinicFees) { %>
            <div>
                <div class="col2" align="float" style="display:inline-block; width: 150px"></div>
                <div class="col2" align="left" style="display:inline-block;"><span id="specialClinicFees"></span>${specialClinicFees}</div>
            </div>
            <% } %>
        <% } %>
        <div>
            <div class="col2" align="float" style="display:inline-block; width: 150px"><b>Served by:</b></div>
                    <div class="col2" align="left" style="display:inline-block;"><span id="user"></span>${user}
                </div>
        </div>


        <div class="col2">
        <span class="button task" id="printSlip" onclick="printReceipt();window.location.href='patientQueueHome.page';" style="float:right; display:inline-block; margin-left: 5px;">
        <i class="icon-print small"></i>&nbsp; Print</a>
        </span>
</div>
</div>
</div>
</body>
</html>