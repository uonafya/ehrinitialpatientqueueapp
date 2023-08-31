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
          var tbl = jq("#specialityTb").DataTable();
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

<table border="0" cellpadding="0" cellspacing="0" id="specialityTb" width="100%">
    <thead>
        <tr>
            <th>Name</th>
            <th>Date Created</th>
            <th>Created By</th>
        </tr>
    </thead>
    <tbody>
        <% if (specialityTypes.empty) { %>
            <tr>
                <td colspan="5">
                    No records found for specified period
                </td>
            </tr>
        <% } %>
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
