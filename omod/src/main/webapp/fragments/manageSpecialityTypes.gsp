<script type="text/javascript">
   var jq = jQuery.noConflict();
</script>
<script type="text/javascript">
  var jq = jQuery;
      jq(function () {
          jq('#confirm').on( 'click',function () {
              saveSpecialityType()
          });

          jq('#cancel').on( 'click',function () {
                      location.reload();
          });
          var tbl = jq("#specialityTb").DataTable(
          {
           searching: true,
           lengthChange: false,
           pageLength: 10,
           jQueryUI: true,
           pagingType: 'full_numbers',
           sort: false,
           dom: 't<"fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix datatables-info-and-pg"ip>',
           language: {
               zeroRecords: 'No speciality recorded.',
               paginate: {
                   first: 'First',
                   previous: 'Previous',
                   next: 'Next',
                   last: 'Last'
               }
           }
       });
      });

      function saveSpecialityType() {
                      jq.getJSON('${ ui.actionLink("initialpatientqueueapp", "manageAppointmentsTypes", "createSpecialityType") }', {
                          specialityName:jq("#speciality-type-name").val(),
                      }).success(function(data) {
                          jq().toastmessage('showSuccessToast', "Speciality type created successfully");
                          location.reload();
                      });
          }
</script>
<div class="ke-panel-frame">
    <div class="ke-panel-heading">Create Speciality Types</div>
        <div class="ke-panel-content" style="background-color: #F3F9FF">
            <div>
                <table border="0">
                    <tr>
                        <td>Name</td>
                        <td><input type="text" id="speciality-type-name" name="specialityName" /></td>
                    </tr>
                </table>
                <div class="onerow" style="margin-top:10px;">
                    <button class="button cancel" id="cancel">Cancel</button>
                    <button class="button confirm right" id="confirm">Confirm</button>
                </div>
            </div>
        <div>
    </div>
</div>

<br />

<table id="specialityTb">
    <thead>
        <tr>
            <th>Name</th>
            <th>Date Created</th>
            <th>Created By</th>
        </tr>
    </thead>
    <tbody>
        <% if (specialityTypes) { %>
            <% specialityTypes.each {%>
            <tr>
                <td>${it.name}</td>
                <td>${it.creator.username}</td>
                <td>${it.dateCreated}</td>
            </tr>
            <%}%>
        <%}%>
    </tbody>
</table>
