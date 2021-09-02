<h2>Legal Information</h2>
<table>
    <tr>
<div class="onerow">
    <td>
    <div class="col4">
        <span class="select-arrow" style="width: 100%">
            <select id="legal1" name="legal1" onchange="LoadLegalCases();">
                <option value="0">&nbsp;</option>
                <option value="1">AVAILABLE</option>
                <option value="2">NOT AVAILABLE</option>
            </select>
        </span>
    </div>
    </td>

    <td>
    <div class="col4">
        <span class="select-arrow" style="width: 100%">
            <select id="legal2" name="legal2">
            </select>
        </span>
    </div>
    </td>
 </div>
 </tr>
    <tr>
        <td>
            <div class="col4">
                <label for="legal1" style="margin:0px;">Medical Legal Case</label>
            </div>
        </td>
        <td>
            <div class="col4">
                <label for="legal2" style="margin:0px;">Description</label>
            </div>
        </td>
    </div>
        <div class="onerow">
    </tr>
</table>

<h2>Referral Information</h2>
<table cellpadding="0" cellspacing="0" border="0">
<tr>
<div class="onerow">
<td>
<div class="col4">
    <label for="refer1" style="margin:0px;">Patient Referred<span>*</span></label>
</div>
</td>
<td>
<div class="col4">
    <label id="forReferralType" for="referralType"
           style="margin:0px;">Referral Type<span></span></label>
</div>
</td>
<td>
<div class="col4 last">
    &nbsp;
</div>
</td>
</div>
</tr>
<tr>
<div class="onerow">
<td>
<div class="col4">
    <span class="select-arrow" style="width: 100%">
        <field>
            <select id="refer1" name="refer1" onchange="LoadReferralCases();">
                <option value="0">Select Option</option>
                <option value="1">YES</option>
                <option value="2">NO</option>
            </select>
        </field>
    </span>
</div>
</td>
<td>
<div class="col4">
    <span class="select-arrow" style="width: 100%">
        <field>
            <select id="referralType" name="patient.referred.reason">
            </select>
        </field>
    </span>
</div>

<div class="col4 last">
    &nbsp;
</div>
</td>
</div>

</tr>
<tr>
<div class="onerow referraldiv" style="margin-top:50px;">
<td>
<div class="col4">
    <label for="refer1" style="margin:0px;">Referred From</label>
</div></td>
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
    <span class="select-arrow" style="width: 100%">
        <field>
            <select id="referredCounty" name="patient.referred.county">
                <option value="0">Select County</option>

            </select>
        </field>
    </span>
</div>
</td>

<td>
<div class="col4">
    <span class="select-arrow" style="width: 100%">
        <field>
            <select id="referredFrom" name="patient.referred.from">
            </select>
        </field>
    </span>
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
<tr>
<td>
<div class="onerow referraldiv" id="referraldiv" style="padding-top:-5px; display:none;">
<label for="referralDescription" style="margin-top:20px;">Comments</label>
<field><textarea type="text" id="referralDescription" name="patient.referred.description"
                 value="N/A" placeholder="COMMENTS" readonly=""
                 style="height: 80px; width: 700px;"></textarea></field>
</div>
</td>
</tr>
</table>