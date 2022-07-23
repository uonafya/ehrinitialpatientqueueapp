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
    // ",Select Type|" + MODEL.TRIAGE;
    var TRIAGE = ",Select Type|${TRIAGE}"
    var OPDs = ",Select Type |${OPDs}";
    var SPECIALCLINIC = ",Select Type |${SPECIALCLINIC}";

    var jq = jQuery;
    var editroomDialog
    jq(function () {
        jq("#details").DataTable();
        editroomDialog = emr.setupConfirmationDialog({
            dialogOpts: {
                overlayClose: false,
                close: true
            },
            selector: '#new-room-dialog',
            actions: {
                confirm: function () {
                    if (validate() > 0) {
                        alert("Please complete highlighted fields")
                    } else {
                        //save client to selected room
                        //make an ajax call to initiate the posting
                        editroomDialog.close();
                    }
                },
                cancel: function () {
                    editroomDialog.close();
                }
            }
        });

    });
    function editQueue(queueId){
        LoadRoomsTypes() // load in the default room options
        editroomDialog.show();

    }

    function LoadRoomsTypes() {
        jq('#rooms2').empty(); //resets the selector
        if (jq("#rooms1").val() == 1) {
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
        } else if (jq("#rooms1").val() == 2) {
            fillOptions("#rooms2", {
                data: OPDs,
                delimiter: ",",
                optionDelimiter: "|"
            });
            jq("#rooms3").attr("readonly", true);
            jq("#rooms3").val("N/A");
            jq('#froom2').html('OPD Rooms<span>*</span>');
            jq("#triageRoom").attr('checked', false);
            jq("#opdRoom").attr('checked', 'checked');
            jq("#specialClinicRoom").attr('checked', false);
            jq('#rooms3').hide();
            jq('#froom3').hide();
            // Remove Maternity Triage when gender is male
            if (jq(".ke-patient-gender").text()[2] === "M") {
                jq("#rooms2 option[value='165418']").remove();
            }
        } else if (jq("#rooms1").val() == 3) {
            fillOptions("#rooms2", {
                data: SPECIALCLINIC,
                delimiter: ",",
                optionDelimiter: "|"
            });
            jq("#rooms3").attr("readonly", false);
            jq("#rooms3").val("");
            jq('#froom2').html('Special Clinic<span>*</span>');
            jq("#triageRoom").attr('checked', false);
            jq("#opdRoom").attr('checked', false);
            jq("#specialClinicRoom").attr('checked', 'checked');
            jq('#rooms3').show();
            jq('#froom3').show();

            // Remove maternity special clinic when gender is male
            if (jq(".ke-patient-gender").text()[2] === "M") {
                jq("#rooms2 option[value='159937']").remove();
            }
        } else {
            var myOptions = {0: 'N/A'};
            var mySelect = jq('#rooms2');
            jq.each(myOptions, function (val, text) {
                mySelect.append(
                    jq('<option></option>').val(val).html(text)
                );
            });
            jq("#rooms3").attr("readonly", true);
            jq("#rooms3").val("N/A");
            jq("#triageRoom").attr('checked', false);
            jq("#opdRoom").attr('checked', false);
            jq("#specialClinicRoom").attr('checked', false);
            jq('#rooms3').hide();
            jq('#froom3').hide();
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

    function validate() {
        var error = 0;
        if (jq("#rooms1").val() === "") {
            jq('#rooms1').addClass("red-border");
            error++;
        } else {
            jq('#rooms1').removeClass("red-border");
        }
        if (jq("#rooms2").val() === 0 || jq("#rooms2").val() === "" || jq("#rooms2").val() == null) {
            jq('#rooms2').addClass("red-border");
            error++;
        } else {
            jq('#rooms2').removeClass("red-border");
        }
        if (jq("#rooms1").val() === 3 && jq("#rooms3").val().trim() === "") {
            jq('#rooms3').addClass("red-border");
            error++;
        } else {
            jq('#rooms3').removeClass("red-border");
        }

        return error;
    }

</script>

<div class="ke-page-sidebar">
    ${ui.includeFragment("kenyaui", "widget/panelMenu", [items: menuItems])}
</div>

<div class="ke-page-content">
    ${ui.includeFragment("initialpatientqueueapp", "viewQueuedPatients")}
</div>