Georgina.module("MainChartApp.Show", function(Show, Georgina, Backbone, Marionette, $, _){
    Show.Controller = {
        showMainChart: function() {
            var chartData = Georgina.request("main_chart:entities");
            console.log("data is ", chartData);
            var mainChartView = new Show.MainChart({
                model: chartData
            });
            Georgina.mainChartRegion.show(mainChartView);
        }
    }
});

