<script type="text/javascript">
   var jq = jQuery.noConflict();
 </script>
 <script type="text/javascript">
     var jq = jQuery;
         jq(function () {
            jq("#sickOffs").DataTable();
         });
 </script>
<div class="ke-panel-frame">
    <div class="ke-panel-heading">Sick Off Listing for Patients</div>
            <div class="ke-panel-content">
                <table border="1" cellpadding="0" cellspacing="0" id="sickOffs" width="75%">
                    <thead>
                        <tr>
                            <th>Sick off ID</th>
                            <th>Patient ID</th>
                            <th>Patient Name</th>
                            <th>Authorised Provider</th>
                            <th>Created on</th>
                            <th>Created By</th>
                            <th>Start Date</th>
                            <th>End Date</th>
                            <th style="width:200px">Notes</th>
                            <th style="width: 60px">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (sickOffsList.empty) { %>
                            <tr align="center">
                                <td colspan="10">
                                    No records found for specified period
                                </td>
                            </tr>
                        <% } %>
                        <% if (sickOffsList) { %>
                            <% sickOffsList.each {%>
                                <tr>
                                    <td>${it.sickOffId}</td>
                                    <td>${it.patientIdentifier}</td>
                                    <td>${it.patientName}</td>
                                    <td>${it.provider}</td>
                                    <td>${it.dateCreated}</td>
                                    <td>${it.user}</td>
                                    <td>${it.sickOffStartDate}</td>
                                    <td>${it.sickOffEndDate}</td>
                                    <td>${it.notes}</td>
                                    <td>Print | Edit</td>
                                </tr>
                            <%}%>
                        <%}%>
                    </tbody>

                </table>
            </div>
    </div>
</div>