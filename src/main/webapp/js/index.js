// the table
var rows = Components.templatedStrings(
    $("#transaction-item").html(),
    Components.getData("transactions/")
);
$("#main-table tbody").append(rows);

// the chart
var options = {
    w: 740, h: 250,
    barColor: "green",
    chartRegion: "#main-chart-region"
}
var munged = _.map(Components.getData("/reports/byday"), function(r) { return r.total; });
Components.lineChart(munged, options);
