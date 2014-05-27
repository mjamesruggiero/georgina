var Components = {
    mainChart: function(dataset, options) {
        options = options || {};
        options.w = options.w || 740;
        options.h = options.h || 250;
        options.barColor = options.barColor || "green";
        options.chartRegion = options.chartRegion || "#main-chart-region";

        var xScale = d3.scale.ordinal()
                        .domain(d3.range(dataset.length))
                        .rangeRoundBands([0, options.w], 0.05);

        var yScale = d3.scale.linear()
                        .domain([0, d3.max(dataset)])
                        .range([0, options.h]);

        var svg = d3.select(options.chartRegion)
                    .append("svg")
                    .attr("width", options.w)
                    .attr("height", options.h);

        svg.selectAll("rect")
            .data(dataset)
            .enter()
            .append("rect")
            .attr("x", function(d, i) {
                return xScale(i);
            })
            .attr("y", function(d) {
                return options.h - yScale(d);
            })
            .attr("width", xScale.rangeBand())
            .attr("height", function(d) {
                return yScale(d);
            })
            .attr("fill", options.barColor);

        svg.selectAll("text")
            .data(dataset)
            .enter()
            .append("text")
            .text(function(d) {
                return d;
            })
            .attr("text-anchor", "middle")
            .attr("x", function(d, i) {
                return xScale(i) + xScale.rangeBand() / 2;
            })
            .attr("y", function(d) {
                return options.h - yScale(d) + 14;
            })
            .attr("font-family", "sans-serif")
            .attr("font-size", "11px")
            .attr("fill", "white");
    },

    numberWithCommas: function(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },

    getDateSpans: function() {
        var startDate = $("#datepicker-start").val();
        var endDate = $("#datepicker-end").val();

        if(startDate == "" || "undefined" == typeof(startDate)) {
            startDate = "2013-01-01";
        }
        if(endDate == "" || "undefined" == typeof(endDate)) {
            endDate = this.getCurrentFormattedDate();
        }
        return { "startDate": startDate, "endDate": endDate };
    },

    getCurrentFormattedDate: function() {
        var rightNow = new Date();
        return rightNow.toISOString().slice(0, 10);
    }
};
