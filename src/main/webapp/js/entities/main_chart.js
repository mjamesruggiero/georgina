Georgina.module("Entities", function(Entities, Georgina, Backbone, Marionette, $, _){
    Entities.MainChart = Backbone.Model.extend({});

    var initializeData = function() {
        chartData = new Entities.MainChart(getChartData());
    };

    var chartData;

    var getChartData = function() {
        var endpoint = "/transactions";
        var data = getData(endpoint)["transactions"];
        var debits = _.filter(data, function(d){ return d.amount < 0; });
        var values = _.map(debits, function(d){ return d.amount * -1; });
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
