<%
	ui.decorateWith("kenyaui", "panel", [ heading: "Information", frameOnly: true ])
%>

<div class="ke-panel-content">
	<div class="ke-stack-item">
      <button type="button" class="ke-compact" onclick="ui.navigate('${ ui.pageLink("kenyaemr", "registration/editPatient", [ patientId: patient.id, returnUrl: ui.thisUrl() ]) }')">
        <img src="${ ui.resourceLink("kenyaui", "images/glyphs/edit.png") }" />
      </button>

  		<% attributes.each { %>
  		${ ui.includeFragment("kenyaui", "widget/dataPoint", [ label: ui.format(it.attributeType), value: it ]) }
  		<% } %>
	</div>
</div>
