<div>
    <div>
        <table class="table table-condensed table-hover">
            <thead>
            <tr>
                <th>
                    <i class="icon-calendar"></i>
                    <span>Date</span>
                </th>
                <th>
                    <i class="icon-stethoscope"></i>
                    <span>Visit Type</span>
                </th>
            </tr>
            </thead>
            <tbody>
            <% encounters.each { encounter -> %>
            <tr>
                <td>
                    <span class="menu-date">
                        <span id="date">
                            ${ui.formatDatetimePretty(encounter.visit.startDatetime)}
                        </span>
                    </span>
                </td>
                <td>
                    <span id="visit_name">
                        ${encounter.visit.visitType.name}
                    </span>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

<div class="main-content" style="border-top: 1px none #ccc;">
    <div id=""></div>
</div>
