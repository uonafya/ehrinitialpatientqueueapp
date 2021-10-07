<h2>Visit Information</h2>
<table cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td>
            <label>Medical Legal Case:<span>*</span></label>
            <div class=" onerow ">
                <label for="mlcCaseYes" class="checks">
                    <input type="radio" name="mlcCaseYes" id="mlcCaseYes"/> YES
                </label>
                 <label for="mlcCaseNo" class="checks">
                    <input type="radio" name="mlcCaseYes" id="mlcCaseNo" checked=""/> NO
                </label>
            </div>
        </td>
        <td>
            <select id="mlc" name="patient.mlc" style='width: 152px; display:inline-block;'></select>
        </td>

    </tr>
    <tr>
        <td>
            <label for="refer1" style="margin:0px;">Patient Referred<span>*</span></label>
        </td>
        <td>
            <label id="forReferralType" for="referralType"
                   style="margin:0px;">Referral Type<span></span></label>
        </td>

    </tr>
    <tr>
        <td>
            <div class="col4">
                <div class="select-arrow" style="width: 100%">
                    <field>
                        <select id="refer1" name="refer1" onchange="LoadReferralCases();">
                            <option value="0">Select Option</option>
                            <option value="1">YES</option>
                            <option value="2">NO</option>
                        </select>
                    </field>
                </div>
            </div>
        </td>
        <td>
            <div class="col4">
                <div class="select-arrow" style="width: 100%">
                    <field>
                        <select id="referralType" name="patient.referred.reason">
                        </select>
                    </field>
                </div>
            </div>
        </td>
    </tr>
</table>
<div class="onerow referraldiv" id="referraldiv" style="padding-top:-0px; display:none;">
<table cellpadding="0" cellspacing="0" border="0" >
    <tr>
        <div class="onerow referraldiv  ">
            <td>
                <div class="col4">
                    <label for="refer1" style="margin:0px;">Referred From</label>
                </div>
            </td>
            <td>
                <div class="col4">
                    <label id="forReferredFrom" for="referredFrom"
                           style="margin:0px;">Facility Type<span></span></label>
                </div>
            </td>
            <td>
                <div class="col4 last">
                    <label for="referralType" style="margin:0px;">Facility Name</label>
                </div>
            </td>
        </div>
    </tr>

    <tr>
        <div class="onerow referraldiv">
            <td>
                <div class="col4">
                    <div class="select-arrow" style="width: 100%">
                        <field>
                            <select id="referredCounty" name="patient.referred.county">
                                <option value="0">Select County</option>

                            </select>
                        </field>
                    </div>
                </div>
            </td>

            <td>
                <div class="col4">
                    <div class="select-arrow" style="width: 100%">
                        <field>
                            <select id="referredFrom" name="patient.referred.from">
                            </select>
                        </field>
                    </div>
                </div>
            </td>
            <td>
                <div class="col4 last">
                    <field>
                        <input id="referredInstitute" name="patient.referred.facility"
                               class="form-textbox1 focused" type="text" placeholder="Institution Name">
                    </field>
                </div>
            </td>

        </div>
    </tr>
</table>
<table  cellpadding="0" cellspacing="0" border="0">
    <tr>
        <div class="onerow referraldiv">
            <td>
                <div class="onerow referraldiv" >
                    <label for="referralDescription" style="margin-top:25px;">Comments</label>
                    <field>
                        <textarea type="text" id="referralDescription" name="patient.referred.description"
                                  value="N/A" placeholder="COMMENTS" readonly=""
                                  style="height: 80px; width: 700px;">
                        </textarea>
                    </field>
                </div>
            </td>
        </div>
    </tr>
</table>
</div>