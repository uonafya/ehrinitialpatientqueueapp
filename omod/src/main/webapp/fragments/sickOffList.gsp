<script type="text/javascript">
   var jq = jQuery.noConflict();
 </script>
 <script type="text/javascript">
     var jq = jQuery;
         jq(function () {
           var tbl = jq("#sickOffs").DataTable({{
                 searching: true,
                 lengthChange: false,
                 pageLength: 10,
                 jQueryUI: true,
                 pagingType: 'full_numbers',
                 sort: false,
                 dom: 't<"fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix datatables-info-and-pg"ip>',
                 language: {
                     zeroRecords: 'No Sick leave recorded.',
                     paginate: {
                         first: 'First',
                         previous: 'Previous',
                         next: 'Next',
                         last: 'Last'
                     }
                 }
             });

            jq('#sickOffs tbody').on( 'click', 'tr', function () {
                     var trData = tbl.row(this).data();
              ui.navigate('initialpatientqueueapp', 'sickOffDetailsForPatient', {sickOffId:trData[0]});
           });
         });

 </script>
<div class="ke-panel-frame">
    <div class="ke-panel-heading">Sick Off Listing for Patients</div>
            <div class="ke-panel-content">
                <table id="sickOffs">
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
                        <% if (sickOffsList) { %>
                            <% sickOffsList.each {%>
                                <tr>
                                    <td>${it.sickOffId}</td>
                                    <td>${it.patientIdentifier}</td>
                                    <td>${it.patientName}</td>
                                    <td>${it.provider}</td>
                                    <td>${it.createdOn}</td>
                                    <td>${it.user}</td>
                                    <td>${it.sickOffStartDate}</td>
                                    <td>${it.sickOffEndDate}</td>
                                    <td>${it.notes}</td>
                                    <td>
                                       <button id="printSickOff" class="task task">Print</button>
                                    </td>
                                </tr>
                            <%}%>
                        <%}%>
                    </tbody>

                </table>
            </div>
    </div>
</div>