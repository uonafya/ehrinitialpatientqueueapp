<%
    ui.decorateWith("kenyaemr", "standardPage", [ patient: currentPatient ])
    ui.includeCss("ehrconfigs", "referenceapplication.css")
%>
<script type="text/javascript">
    jq(document).ready(function () {
        jq("#metatabs").tabs();
    });
</script>
<div class="ke-page-content">
    <div id="metatabs">
        <ul>
            <li id="r1"><a href="#risk1">Risk 1</a></li>
            <li id="r2-history"><a href="#risk2">Risk 2</a></li>
            <li id="r3"><a href="#risk3">Risk 3</a></li>
            <li id="r4"><a href="#risk4">Risk 4</a></li>
        </ul>
        <div id="risk1">
          <iframe
              src=${url1}
              frameBorder="0"
              width="100%"
              style="border:none;"
              height="100%">
          </iframe>
        </div>
        <div id="risk2">
            <iframe
                src=${url2}
                frameBorder="0"
                width="100%"
                style="border:none;"
                height="100%">
            </iframe>
        </div>
        <div id="risk3">
            <iframe
                src=${url3}
                frameBorder="0"
                width="100%"
                style="border:none;"
                height="100%">
            </iframe>
        </div>
        <div id="risk4">
            <iframe
                src=${url4}
                frameBorder="0"
                width="100%"
                style="border:none;"
                height="100%">
            </iframe>
        </div>
    </div>
</div>