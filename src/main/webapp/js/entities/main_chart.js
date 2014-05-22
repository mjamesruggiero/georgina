Georgina.module("Entities", function(Entities, Georgina, Backbone, Marionette, $, _){
    Entities.MainChart = Backbone.Model.extend({});

    var initializeData = function() {
        chartData = new Entities.MainChart(getChartData());
    };

    var chartData;

    var getChartData = function() {
        var endpoint = "/reports/byday";
        var data = getData(endpoint);
        var values = _.map(data, function(d){ return Math.floor(d.total * -1); });
        return {debits: values};
    };

    // TODO - put this in a Utils
    var getData = function(endpoint) {
        var json = $.ajax({
            url: endpoint,
            async: false
        }).responseText;
        data = JSON.parse(json);
        return data;
    };

    var API = {
        getMainChartData : function() {
            if (chartData === undefined) {
                initializeData();
            }
            return chartData;
        }
    };

    Georgina.reqres.setHandler("main_chart:entities", function() {
        return API.getMainChartData();
    });
});
