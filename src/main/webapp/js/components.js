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

        // X scale will fit all values from data[] within pixels 0-w
        var x = d3.scale.linear().domain([0, data.length]).range([0, w]);
        // Y scale will fit values from 0-10 within pixels h-0 (Note the inverted domain for the y-scale: bigger is up!)
        //var y = d3.scale.linear().domain([0, 10]).range([h, 0]);
        // automatically determining max range can work something like this
        var y = d3.scale.linear().domain([0, d3.max(data)]).range([h, 0]);

        // create a line function that can convert data[] into x and y points
        var line = d3.svg.line()
            // assign the X function to plot our line as we wish
            .x(function(d,i) {
                //console.log('Plotting X value for data point: ' + d + ' using index: ' + i + ' to be at: ' + x(i) + ' using our xScale.');
                return x(i);
            })
            .y(function(d) {
                //console.log('Plotting Y value for data point: ' + d + ' to be at: ' + y(d) + " using our yScale.");
                return y(d);
            })

            // Add an SVG element with the desired dimensions and margin.
            var graph = d3.select("#main-chart-region").append("svg:svg")
                  .attr("width", w + m[1] + m[3])
                  .attr("height", h + m[0] + m[2])
                  .append("svg:g")
                  .attr("transform", "translate(" + m[3] + "," + m[0] + ")");


            var xAxis = d3.svg.axis().scale(x).tickSize(-h).tickSubdivide(true);

            graph.append("svg:g")
                  .attr("class", "x axis")
                  .attr("transform", "translate(0," + h + ")")
                  .call(xAxis);

            // create left yAxis
            var yAxisLeft = d3.svg.axis().scale(y).ticks(4).orient("left");
            // Add the y-axis to the left
            graph.append("svg:g")
                  .attr("class", "y axis")
                  .attr("transform", "translate(0,0)")
                  .call(yAxisLeft);

            // Add the line by appending an svg:path element with the data line we created above
            // do this AFTER the axes above so that the line is above the tick-lines
            graph.append("svg:path").attr("d", line(data));
    }
};
