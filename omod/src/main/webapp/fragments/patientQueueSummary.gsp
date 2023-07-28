<script>
    jq(function () {
        jq("#ul-left-menu").on("click", ".visit-summary", function () {
            jq("#visit-detail").html('<i class=\"icon-spinner icon-spin icon-2x pull-left\"></i> <span style="float: left; margin-top: 12px;">Loading...</span>');
            jq("#drugs-detail").html("");
            jq("#opdRecordsPrintButton").hide();

            var visitSummary = jq(this);
            jq(".visit-summary").removeClass("selected");
            jq(visitSummary).addClass("selected");
            jq.getJSON('${ ui.actionLink("initialpatientqueueapp", "patientQueueSummary" ,"getVisitSummaryDetails") }',
                {'encounterId': jq(visitSummary).find(".encounter-id").val()}
            ).success(function (data) {
                var visitDetailTemplate = _.template(jq("#visit-detail-template").html());
                jq("#visit-detail").html(visitDetailTemplate(data.notes));

                if (data.drugs.length > 0) {
                    var drugsTemplate = _.template(jq("#drugs-template").html());
                    jq("#drugs-detail").html(drugsTemplate(data));
                } else {
                    var drugsTemplate = _.template(jq("#empty-template").html());
                    jq("#drugs-detail").html(drugsTemplate(data));
                }

                jq("#opdRecordsPrintButton").show(100);
            })
        });

        jq('#opdRecordsPrintButton').click(function () {
            jq("#printSection").print({
                globalStyles: false,
                mediaPrint: false,
                stylesheet: '${ui.resourceLink("patientdashboardapp", "styles/printout.css")}',
                iframe: false,
                width: 600,
                height: 700
            });
        });
        jq('#opdRecordsSummaryFromSHRButton').click(function () {
            kenyaui.openPanelDialog({templateId: 'shr-visit-summary', width: 85, height: 70, scrolling: true});
        });

        var visitSummaries = jq(".visit-summary");

        if (visitSummaries.length > 0) {
            visitSummaries[0].click();
            jq('#cs').show();
        } else {
            jq('#cs').hide();
        }

        jq('#ul-left-menu').slimScroll({
            allowPageScroll: false,
            height: '426px',
            distance: '11px',
            color: '#363463'
        });

        jq('#ul-left-menu').scrollTop(0);
        jq('#slimScrollDiv').scrollTop(0);
    });
</script>

<style>
#ul-left-menu {
    border-top: medium none #fff;
    border-right: medium none #fff;
}

#ul-left-menu li:nth-child(1) {
    border-top: 1px solid #ccc;
}

#ul-left-menu li:last-child {
    border-bottom: 1px solid #ccc;
    border-right: 1px solid #ccc;
}

.visit-summary {

}
</style>

<div class="onerow">
    <div id="div-left-menu" style="padding-top: 15px;" class="col15 clear">
        <ul id="ul-left-menu" class="left-menu">
            <% encounters.each { encounter -> %>
            <li class="menu-item visit-summary"
                style="border-right:1px solid #ccc; margin-right: 15px; width: 168px; height: 18px;">
                <input type="hidden" class="encounter-id" value="${encounter.encounterId}">
                <span class="menu-date">
                    <i class="icon-time"></i>
                    <span id="vistdate">
                        ${ui.formatDatetimePretty(encounter.encounterDatetime)}
                    </span>
                </span>
                <span class="menu-title">
                    <i class="icon-stethoscope"></i>
                    <span id="visittype">
                        ${encounter.encounterType.name}
                    </span>
                </span>
                <span class="arrow-border"></span>
                <span class="arrow"></span>
            </li>
            <% } %>
        </ul>
    </div>

    <div class="col16 dashboard opdRecordsPrintDiv" style="min-width: 78%">
        <div id="printSection">
            <div id="person-detail">
                <center>
                    <img width="100" height="100" align="center" title="Integrated KenyaEMR"
                         alt="Integrated KenyaEMR"
                         src="${ui.resourceLink('ehrinventoryapp', 'images/kenya_logo.bmp')}">

                    <h2>${userLocation}</h2>
                </center>

                <h3>PATIENT SUMMARY INFORMATION</h3>

                <label>
                    <span class='status active'></span>
                    Identifier:
                </label>
                <span>${patient.getPatientIdentifier()}</span>
                <br/>

                <label>
                    <span class='status active'></span>
                    Full Names:
                </label>
                <span>${patient.givenName} ${patient.familyName} ${patient.middleName ? patient.middleName : ''}</span>
                <br/>

                <label>
                    <span class='status active'></span>
                    Age:
                </label>
                <span>${patient.age} (${ui.formatDatePretty(patient.birthdate)})</span>
                <br/>

                <label>
                    <span class='status active'></span>
                    Gender:
                </label>
                <span>${patient.gender}</span>
                <br/>
            </div>

            <div class="info-section" id="visit-detail">
            </div>

            <div class="info-sections" id="drugs-detail" style="margin: 0px 10px 0px 5px;">
            </div>
        </div>

        <button id="opdRecordsPrintButton" class="task" style="float: right; margin: 10px;">
            <i class="icon-print small"></i>
            Print
        </button>
    </div>
</div>

<div class="main-content" style="border-top: 1px none #ccc;">
    <div id=""></div>
</div>

<script id="visit-detail-template" type="text/template">
<div>
    <h4>Doctor Name:</h4><b>{{-providerName}}</b><br/>
    <h4>Date of Service:</h4><b>{{-dateOfService}}</b>
</div>
<br/>

<div class="info-header">
    <i class="icon-user-md"></i>

    <h3>PATIENT QUEUE SUMMARY INFORMATION</h3>
</div>

<div class="info-body">
    <label>
        <span class='status active'></span>
        Queue Number:
    </label>
    <span>12345</span>
    <br/>

    <label>
        <span class='status active'></span>
        Queue Status:
    </label>
    <span>Ended</span>
    <br/>

    <label>
        <span class='status active'></span>
        Queue Start Date:
    </label>
    <span>
        2015-01-01
    </span>
    <br/>

    <label>
        <span class='status active'></span>
        Queue End Date:
    </label>
    <span>
        2015-01-01
    </span>
    <br/>
</div>
</script>




