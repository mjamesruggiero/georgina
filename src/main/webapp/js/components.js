// TODO instantiate this with anonymous function
// so we can use underscore
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
        //TODO put selectors here
        var startDate = "",
            endDate = "";

        if(startDate === "" || "undefined" == typeof(startDate)) {
            startDate = "2013-01-01";
        }
        if(endDate === "" || "undefined" == typeof(endDate)) {
            endDate = this.getCurrentFormattedDate();
        }
        return { "startDate": startDate, "endDate": endDate };
    },

    getCurrentFormattedDate: function() {
        var rightNow = new Date();
        return rightNow.toISOString().slice(0, 10);
    },

    lineChart: function(data) {
        var m = [35, 60, 35, 40]; // margins
        var w = 740 - m[1] - m[3]; // width
        var h = 250 - m[0] - m[2]; // height

        var parseDate = d3.time.format('%Y-%m-%d').parse;
        var vals = _.map(data, function(r) { return r.total; });
        var minTotals = _.min(vals);
        var maxTotals = _.max(vals);

        var minDate = parseDate(data[0].date);
        var maxDate = parseDate(data[data.length - 1].date);

        var x = d3.time.scale().range([0, w]).domain([minDate, maxDate]);
        var y = d3.scale.linear().domain([minTotals, maxTotals]).range([h, 0]);

        var line = d3.svg.line()
            .x(function(d) {
                return x(parseDate(d.date));
            })
            .y(function(d) {
                return y(d.total);
            });

        var graph = d3.select("#main-chart-region").append("svg:svg")
              .attr("width", w + m[1] + m[3])
              .attr("height", h + m[0] + m[2])
              .append("svg:g")
              .attr("transform", "translate(" + m[3] + "," + m[0] + ")");

        var xAxis = d3.svg.axis().scale(x).orient("bottom");
        var yAxisLeft = d3.svg.axis().scale(y).orient("left");

        graph.append("svg:g")
              .attr("class", "x axis")
              .attr("transform", "translate(0," + h + ")")
              .call(xAxis);

        graph.append("svg:g")
              .attr("class", "y axis")
              .attr("transform", "translate(0,0)")
              .call(yAxisLeft);

        graph.append("svg:path")
            .datum(data)
            .attr("class", "line")
            .attr("d", line);
    },

    templatedStrings: function(templateString, data) {
        var templ = _.template(templateString);
        return _.map(data, function(o) { return templ(o); });
    },

    getData: function(endpoint) {
        var json = $.ajax({
            url: endpoint,
            async: false
            }).responseText;
        return JSON.parse(json);
    },

    existy: function(x) {
        return x != null;
    },

    truthy: function(x) {
        return (x !== false) && Components.existy(x);
    }

};
