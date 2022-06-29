<%
    ui.decorateWith("kenyaemr", "standardPage", [layout: "sidebar"])
    ui.includeJavascript("ehrconfigs", "jquery.dataTables.min.js")
    ui.includeCss("ehrconfigs", "jquery.dataTables.min.css")
    ui.includeJavascript("ehrconfigs", "emr.js")
    ui.includeCss("ehrconfigs", "onepcssgrid.css")
    ui.includeCss("ehrconfigs", "custom.css")
    ui.includeCss("ehrconfigs", "referenceapplication.css")
    ui.includeJavascript("ehrconfigs", "jquery.simplemodal.1.4.4.min.js")

    def menuItems = [
            [label: "Back to home", iconProvider: "kenyaui", icon: "buttons/back.png", label: "Back to home", href: ui.pageLink("initialpatientqueueapp", "patientQueueHome")]
    ]
%>
<script type="text/javascript">
    var TRIAGE = ",Select Type |" + ${TRIAGE};
    var OPD = ",Select Type |" + ${OPDs};
    var jq = jQuery;
    jq(function () {
        jq("#details").DataTable();
        var editroomDialog = emr.setupConfirmationDialog({
            dialogOpts: {
                overlayClose: false,
                close: true
            },
            selector: '#new-room-dialog',
            actions: {
                confirm: function () {
                    editroomDialog.close();
                },
                cancel: function () {
                    editroomDialog.close();
                }
            }
        });

        jq("#editQueue").on("click", function (e) {
            e.preventDefault();
            editroomDialog.show();
        });
    });

    if (jq("#rooms1").val() === 1) {
        PAGE.fillOptions("#rooms2", {
            data: MODEL.TRIAGE,
            delimiter: ",",
            optionDelimiter: "|"
        });
        jq('#rooms2').empty(); // resent the selector
        if (jq("#rooms1").val() === 1) {
            fillOptions("#rooms2", {
                data: TRIAGE,
                delimiter: ",",
                optionDelimiter: "|"
            });
            jq("#rooms3").attr("readonly", true);
            jq("#rooms3").val("N/A");
            jq('#froom2').html('Triage Rooms<span>*</span>');
            jq("#triageRoom").attr('checked', 'checked');
            jq("#opdRoom").attr('checked', false);
            jq("#specialClinicRoom").attr('checked', false);
            jq('#rooms3').hide();
            jq('#froom3').hide();
            // Remove Maternity Triage and MCH when gender is male
            if (jq(".ke-patient-gender").text()[2] === "M") {
                jq("#rooms2 option[value='165417']").remove();

                jq("#rooms2 option[value='165418']").remove();
            }
        }


        /** FILL OPTIONS INTO SELECT
         * option = {
         * 		data: list of values or string
         *		index: list of corresponding indexes
         *		delimiter: seperator for value and label
         *		optionDelimiter: seperator for options
         * }
         */
        function fillOptions(divId, option) {
            jq(divId).empty();
            if (option.delimiter == undefined) {
                if (option.index == undefined) {
                    jq.each(option.data, function (index, value) {
                        if (value.length > 0) {
                            jq(divId).append(
                                "<option value='" + value + "'>" + value
                                + "</option>");
                        }
                    });
                } else {
                    jq.each(option.data, function (index, value) {
                        if (value.length > 0) {
                            jq(divId).append(
                                "<option value='" + option.index[index] + "'>"
                                + value + "</option>");
                        }
                    });
                }
            } else {
                options = option.data.split(option.optionDelimiter);
                jq.each(options, function (index, value) {
                    values = value.split(option.delimiter);
                    optionValue = values[0];
                    optionLabel = values[1];
                    if (optionLabel != undefined) {
                        if (optionLabel.length > 0) {
                            jq(divId).append(
                                "<option value='" + optionValue + "'>"
                                + optionLabel + "</option>");
                        }
                    }
                });
            }
        }
    }
</script>

<div class="ke-page-sidebar">
    ${ui.includeFragment("kenyaui", "widget/panelMenu", [items: menuItems])}
</div>

<div class="ke-page-content">
    ${ui.includeFragment("initialpatientqueueapp", "viewQueuedPatients")}
</div>